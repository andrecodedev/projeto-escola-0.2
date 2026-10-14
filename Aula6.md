# Aula 6 - Arquitetura em camadas com Spring Boot

## Objetivo

Nesta aula, o projeto foi organizado usando o padrão de **arquitetura em camadas** (*layered architecture*). Também foi criada uma API REST para realizar o CRUD de cursos.

A separação das responsabilidades deixa o código mais organizado, facilita a manutenção e permite que cada parte da aplicação evolua sem concentrar toda a lógica em uma única classe.

## O que foi feito

- criada a camada de controle com `CursoController`;
- criada a camada de serviço com `CursoService` e `CursoServiceImpl`;
- adicionada a regra de negócio para validar cursos e categorias;
- criados endpoints REST para inserir, editar, consultar e excluir cursos;
- criado o `CursoRequestDTO` para transportar os dados da API;
- utilizados os repositórios do Spring Data JPA para acessar o banco;
- criada a exceção `RegraNegocioException` para representar erros de negócio.

## Conceito de arquitetura em camadas

A arquitetura em camadas divide a aplicação em partes com responsabilidades bem definidas. Cada camada possui um papel específico e normalmente se comunica com a camada imediatamente abaixo dela.

Neste projeto, o fluxo principal é:

```text
Cliente HTTP
    |
    v
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
Banco de dados
```

A resposta percorre o caminho inverso: o repositório retorna os dados para o serviço, o serviço prepara o resultado e o controller devolve a resposta HTTP ao cliente.

### Vantagens

- **organização:** cada classe possui uma responsabilidade principal;
- **manutenção:** alterações ficam concentradas na camada responsável;
- **reutilização:** a mesma regra de negócio pode ser usada por diferentes controllers ou outros consumidores;
- **testabilidade:** cada camada pode ser testada separadamente;
- **baixo acoplamento:** o controller não precisa conhecer os detalhes do banco de dados.

## Camada Controller

A camada Controller é responsável pela comunicação HTTP. Ela recebe requisições, obtém os parâmetros da URL ou do corpo da requisição e encaminha a operação para o serviço.

O controller não deve conter regras de negócio nem comandos diretamente relacionados ao banco de dados.

```java
@RestController
@RequestMapping("/api/curso")
public class CursoController {
    private CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }
}
```

O `CursoController` disponibiliza os seguintes endpoints:

| Método | Endpoint | Operação |
|---|---|---|
| `POST` | `/api/curso` | cadastra um curso |
| `PUT` | `/api/curso/{id}` | edita um curso |
| `GET` | `/api/curso` | lista todos os cursos |
| `GET` | `/api/curso/{id}` | consulta um curso pelo id |
| `DELETE` | `/api/curso/{id}` | exclui um curso |

Os códigos HTTP utilizados são:

- `201 CREATED` após a criação de um curso;
- `200 OK` nas consultas e edições;
- `204 NO CONTENT` após a exclusão.

## Camada Service

A camada Service concentra as regras de negócio da aplicação. Ela fica entre o controller e os repositórios.

A interface `CursoService` define as operações disponíveis:

```java
public interface CursoService {
    CursoRequestDTO inserir(CursoRequestDTO cursoRequestDTO);
    CursoRequestDTO editar(Long id, CursoRequestDTO cursoRequestDTO);
    List<CursoRequestDTO> obterTodos();
    CursoRequestDTO obterPorId(Long id);
    void excluir(Long id);
}
```

A classe `CursoServiceImpl` implementa essas operações. Por exemplo, antes de inserir um curso, ela verifica se a categoria informada existe:

```java
Categoria categ = categoriaRepository
        .findById(dto.getCategoriaId())
        .orElseThrow(() -> new RegraNegocioException(
                "Categoria não encontrada"));
```

Essa validação pertence ao serviço porque representa uma regra da aplicação: um curso não deve ser criado apontando para uma categoria inexistente.

A anotação `@Service` registra a classe como um componente do Spring:

```java
@Service
public class CursoServiceImpl implements CursoService {
}
```

## Camada Repository

A camada Repository é responsável pelo acesso aos dados. Neste projeto, os repositórios herdam operações prontas do `JpaRepository`:

```java
public interface CursoRepository extends JpaRepository<Curso, Long> {
}
```

Com isso, a aplicação pode utilizar métodos como:

- `save(entidade)`: salva ou atualiza uma entidade;
- `findById(id)`: busca uma entidade pelo identificador;
- `findAll()`: lista as entidades;
- `delete(entidade)`: remove uma entidade.

O serviço utiliza o repositório sem precisar conhecer os detalhes de SQL ou do Hibernate.

## DTO - Data Transfer Object

O `CursoRequestDTO` é um objeto utilizado para transportar dados entre a API e a aplicação:

```java
public class CursoRequestDTO {
    private Long id;
    private String nome;
    private Integer cargaHoraria;
    private Long categoriaId;
}
```

O DTO não é a entidade do banco. Ele representa o formato dos dados que a API recebe e devolve.

Neste caso, a API recebe `categoriaId` em vez de receber um objeto `Categoria` inteiro. O serviço usa esse id para buscar a categoria no repositório e associá-la ao curso.

O uso de DTOs ajuda a:

- evitar expor diretamente as entidades JPA;
- controlar quais campos entram e saem da API;
- evitar o envio de relacionamentos completos e desnecessários;
- adaptar o formato da API sem alterar o modelo do banco.

## Tratamento de erros de negócio

Foi criada a exceção:

```java
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String message) {
        super(message);
    }
}
```

Ela é lançada quando uma operação viola uma regra da aplicação, por exemplo:

- categoria não encontrada;
- curso não encontrado.

Exemplo:

```java
cursoRepository.findById(id)
        .orElseThrow(() -> new RegraNegocioException(
                "Curso não encontrado"));
```

Assim, o serviço interrompe a operação no momento em que identifica o problema, em vez de continuar com dados inválidos.

## Fluxo de uma inclusão

Quando o cliente envia um `POST /api/curso`, o fluxo é:

1. o `CursoController` recebe o JSON e transforma o corpo em `CursoRequestDTO`;
2. o controller chama `cursoService.inserir(dto)`;
3. o serviço busca a categoria pelo `categoriaId`;
4. se a categoria não existir, uma `RegraNegocioException` é lançada;
5. se a categoria existir, o serviço cria a entidade `Curso`;
6. o `CursoRepository` salva o curso no banco;
7. o serviço transforma a entidade salva em `CursoRequestDTO`;
8. o controller devolve o resultado ao cliente com status `201 CREATED`.

Exemplo de requisição:

```json
{
  "nome": "Engenharia de Software",
  "cargaHoraria": 2000,
  "categoriaId": 1
}
```

## Responsabilidade de cada camada

| Camada | Responsabilidade | Exemplo no projeto |
|---|---|---|
| Controller | receber e responder requisições HTTP | `CursoController` |
| Service | executar regras de negócio e coordenar operações | `CursoServiceImpl` |
| Repository | consultar e persistir dados | `CursoRepository` |
| Domain | representar as entidades do sistema | `Curso` e `Categoria` |
| DTO | transportar dados entre as camadas e a API | `CursoRequestDTO` |
| Exception | representar erros de regra de negócio | `RegraNegocioException` |

## Regra prática

Uma forma simples de decidir onde colocar cada código é perguntar:

- trata de HTTP, rota, parâmetro ou status? Deve ficar no **Controller**;
- representa uma validação ou decisão da aplicação? Deve ficar no **Service**;
- consulta ou altera dados persistidos? Deve ficar no **Repository**;
- representa dados do banco? Deve ficar no **Domain**;
- define o formato de entrada ou saída da API? Deve ficar no **DTO**.

O objetivo não é criar camadas apenas por convenção, mas manter cada responsabilidade no lugar mais adequado.

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
