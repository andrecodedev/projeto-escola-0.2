# Aula 5 - Relacionamentos com Spring Data JPA

## Objetivo

Nesta etapa, o projeto passa a representar uma escola com categorias e cursos. A categoria possui vários cursos, e cada curso pertence a uma categoria.

Ao final da aula, a aplicação deve ser capaz de:

- mapear as entidades `Categoria` e `Curso`;
- criar o relacionamento entre as tabelas;
- salvar categorias e cursos com Spring Data JPA;
- consultar categorias junto com seus cursos;
- executar a aplicação usando o banco H2 em memória.

## Dependências

O projeto utiliza:

- Spring Boot;
- Spring Data JPA;
- H2 Database;
- Lombok;
- Java 21.

As dependências ficam no arquivo `pom.xml`. O Spring Data JPA fornece os repositórios, o H2 oferece um banco local em memória e o Lombok reduz o código repetitivo das entidades.

## Entidade Categoria

A categoria é uma entidade persistida no banco:

```java
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(mappedBy = "categoria")
    private List<Curso> cursos;
}
```

### Anotações principais

- `@Entity`: informa que a classe representa uma tabela;
- `@Id`: identifica a chave primária;
- `@GeneratedValue`: permite que o banco gere o identificador;
- `@OneToMany`: indica que uma categoria pode possuir vários cursos;
- `mappedBy = "categoria"`: informa que o relacionamento é controlado pelo atributo `categoria` da entidade `Curso`.

## Entidade Curso

O curso possui uma referência para sua categoria:

```java
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private Integer cargaHoraria;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
```

O relacionamento entre as tabelas é criado pela coluna `categoria_id` na tabela de cursos.

- `@ManyToOne`: vários cursos podem pertencer à mesma categoria;
- `@JoinColumn(name = "categoria_id")`: define o nome da chave estrangeira;
- `@Column(nullable = false)`: exige que o nome do curso seja informado.

## Cardinalidade do relacionamento

O relacionamento é bidirecional:

```text
Categoria 1 ---- N Curso
```

A entidade `Categoria` acessa sua lista de cursos através de `cursos`. A entidade `Curso` acessa sua categoria através de `categoria`.

O lado `Curso` é o lado dono do relacionamento porque possui `@JoinColumn`. Por isso, `mappedBy` é usado em `Categoria` para indicar que a coluna pertence ao outro lado.

## Repositórios

Os repositórios herdam operações prontas do Spring Data JPA:

```java
public interface CategoriaRepository
        extends JpaRepository<Categoria, Long> {
}
```

```java
public interface CursoRepository extends JpaRepository<Curso, Long> {
}
```

Entre as operações disponíveis estão:

- `save(entidade)`: salva ou atualiza uma entidade;
- `findById(id)`: busca uma entidade pelo identificador;
- `findAll()`: retorna todas as entidades;
- `deleteById(id)`: remove uma entidade pelo identificador.

## Consulta com `JOIN FETCH`

Para buscar as categorias já acompanhadas de seus cursos, foi criada uma consulta personalizada:

```java
@Query("SELECT c FROM Categoria c LEFT JOIN FETCH c.cursos")
List<Categoria> obterCategoriasECursos();
```

O `LEFT JOIN FETCH` faz duas coisas:

1. mantém categorias mesmo quando elas ainda não possuem cursos;
2. carrega os cursos na mesma consulta.

Essa abordagem evita que a lista de cursos dependa de uma sessão do Hibernate ainda aberta e deixa explícito que a consulta precisa trazer os dois lados do relacionamento.

## Carga inicial dos dados

A classe principal usa um `CommandLineRunner` para inserir dados quando a aplicação inicia:

```java
Categoria categoria = new Categoria(null, "Engenharias", null);
categoria = categoriaRepository.save(categoria);

Curso curso = new Curso(
        null,
        "Engenharia de Software",
        2000,
        categoria
);
cursoRepository.save(curso);
```

A ordem é importante: primeiro a categoria é salva; depois os cursos são salvos apontando para a categoria persistida.

A consulta final é executada assim:

```java
categoriaRepository.obterCategoriasECursos()
        .forEach(System.out::println);
```

## Banco H2

As configurações do banco ficam em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:escola
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Como o banco usa `jdbc:h2:mem`, os dados existem apenas enquanto a aplicação está em execução.

A console pode ser acessada em:

```text
http://localhost:8081/h2-console
```

Na tela de login, use:

- JDBC URL: `jdbc:h2:mem:escola`;
- User Name: `sa`;
- Password: `password`.

## Problema comum: recursão no `toString()`

O `@Data` do Lombok gera automaticamente o método `toString()`. Em um relacionamento bidirecional, isso pode gerar uma chamada infinita:

```text
Categoria.toString()
 -> cursos
 -> Curso.toString()
 -> categoria
 -> Categoria.toString()
```

O resultado pode ser um `StackOverflowError` ao executar `System.out.println(categoria)`.

Para evitar isso, não inclua o relacionamento no `toString()`:

```java
@ToString.Exclude
private Categoria categoria;
```

E, se necessário, também exclua a coleção:

```java
@ToString.Exclude
private List<Curso> cursos;
```

Outra alternativa é substituir `@Data` por `@Getter` e `@Setter` e escrever manualmente um `toString()` que mostre apenas `id`, `nome` e outros campos simples.

## Como executar

No Windows, usando o Maven Wrapper:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
.\mvnw.cmd spring-boot:run
```

Para executar os testes:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
.\mvnw.cmd test
```

O `JAVA_HOME` precisa apontar para um JDK compatível com a propriedade `<java.version>21</java.version>` do projeto.

## Resultado esperado

Durante a inicialização, a aplicação cria uma categoria e dois cursos. Em seguida, a consulta retorna a categoria com seus cursos carregados. O Hibernate também exibe no console os comandos SQL porque `hibernate.show_sql` está habilitado.

## Exercícios

1. Crie uma segunda categoria, como `Tecnologia`.
2. Adicione um curso associado à nova categoria.
3. Crie uma consulta para buscar cursos por nome.
4. Altere a consulta de categorias para ordenar pelo nome.
5. Troque o banco H2 em memória por H2 em arquivo e observe a diferença no ciclo de vida dos dados.
