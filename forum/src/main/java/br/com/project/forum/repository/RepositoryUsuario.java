package br.com.project.forum.repository;

import br.com.project.forum.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryUsuario extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}
