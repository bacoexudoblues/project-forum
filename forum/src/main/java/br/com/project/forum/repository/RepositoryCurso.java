package br.com.project.forum.repository;

import br.com.project.forum.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryCurso extends JpaRepository<Curso, Long> {
    Curso findByNome(String nomeCurso);
}
