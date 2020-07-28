package br.com.project.forum.repository;

import br.com.project.forum.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryTopico extends JpaRepository<Topico, Long> {

    Page<Topico> findByCursoNome(String cursoNome, Pageable page);
}
