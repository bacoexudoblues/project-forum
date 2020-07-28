package br.com.project.forum.exception;

import lombok.Data;

@Data
public class ResponseError {

    private String codigo;
    private String message;

    public ResponseError(String codigo, String message) {
        this.codigo = codigo;
        this.message = message;
    }
}
