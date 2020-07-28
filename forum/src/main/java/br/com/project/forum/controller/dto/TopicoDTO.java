package br.com.project.forum.controller.dto;

import br.com.project.forum.model.Topico;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
public class TopicoDTO {

    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataCriacao;

    public TopicoDTO(Topico topico) {
        this.id = topico.getId();
        this.titulo = topico.getTitulo();
        this.mensagem = topico.getMensagem();
        this.dataCriacao = topico.getDataCriacao();
    }

    public static Page<TopicoDTO> convert(Page<Topico> topicos) {
        //Map -> ira chamar o constructor que recebe o proprio topico como parametro
        return topicos.map(TopicoDTO::new);
    }
}
