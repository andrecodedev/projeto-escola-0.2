package com.example.projeto_escola;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.projeto_escola.domains.Categoria;
import com.example.projeto_escola.domains.Curso;
import com.example.projeto_escola.repositories.CategoriaRepository;
import com.example.projeto_escola.repositories.CursoRepository;

@SpringBootApplication
public class ProjetoEscolaApplication {

	@Bean
	public CommandLineRunner init(
			@Autowired CursoRepository cursoRepository,
			@Autowired CategoriaRepository categoriaRepository) {
		return args -> {
			System.out.println("=== Criando categorias ===");
			Categoria c1 = new Categoria(null, "Engenharias", null);
			c1 = categoriaRepository.save(c1);

			System.out.println("=== Criando cursos ===");
			Curso curso1 = new Curso(null, "Engenharia de Software", 2000, c1);
			curso1 = cursoRepository.save(curso1);
			Curso curso2 = new Curso(null, "Engenharia de Produção", 2800, c1);
			curso2 = cursoRepository.save(curso2);

			System.out.println("=== Exibir categorias ===");

			categoriaRepository.obterCategoriasECursos().forEach(System.out::println);

		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ProjetoEscolaApplication.class, args);
	}

}
