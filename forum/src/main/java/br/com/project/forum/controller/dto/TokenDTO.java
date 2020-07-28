package br.com.project.forum.controller.dto;

import lombok.Data;

@Data

public class TokenDTO {

    private final String token;
    private final String tipo;

    public TokenDTO(String token, String tipo) {
        this.token = token;
        this.tipo = tipo;
    }
}
