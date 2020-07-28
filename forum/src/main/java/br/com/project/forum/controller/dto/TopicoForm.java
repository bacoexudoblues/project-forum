package br.com.project.forum.controller.dto;

import br.com.project.forum.model.Topico;
import br.com.project.forum.repository.RepositoryCurso;
import br.com.project.forum.repository.RepositoryTopico;
import com.sun.istack.NotNull;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TopicoForm {


    @NotNull @NotBlank
    private String titulo;

    @NotNull @NotBlank
    private String nomeCurso;

    @NotNull @NotBlank
    private String mensagem;

    public Topico convert(RepositoryCurso repo) {
        return new Topico(titulo, mensagem, repo.findByNome(nomeCurso));
    }

    public Topico atualizar(Long id, RepositoryTopico repositoryTopico) {
        Topico topico = repositoryTopico.getOne(id);

        topico.setTitulo(this.titulo);
        topico.setMensagem(this.mensagem);

        return topico;
    }
}
