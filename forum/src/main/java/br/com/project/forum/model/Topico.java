package br.com.project.forum.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Topico {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titulo;

	private String mensagem;

	private LocalDateTime dataCriacao = LocalDateTime.now();

	@Enumerated(EnumType.STRING) //Gravar o valor dentro do enum
	private StatusTopico status = StatusTopico.NAO_RESPONDIDO;

	@ManyToOne // Multiplos topicos para um usuario
	private Usuario autor;

	@ManyToOne // multiplos topicos para um curso
	private Curso curso;

	@OneToMany(mappedBy = "topico") //Unico topico para multiplas respostas
	private List<Resposta> respostas = new ArrayList<>();

	public Topico() {
	}

	public Topico(String titulo, String mensagem, Curso curso) {
		this.titulo = titulo;
		this.mensagem = mensagem;
		this.curso = curso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Topico other = (Topico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
