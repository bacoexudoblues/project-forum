package br.com.project.forum.controller;

import br.com.project.forum.controller.dto.LoginForm;
import br.com.project.forum.controller.dto.TokenDTO;
import br.com.project.forum.security.ServiceToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class ControllerAutenticacao {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ServiceToken serviceToken;

    @PostMapping
    public ResponseEntity<TokenDTO> autenticar(@RequestBody @Valid LoginForm login) {

        UsernamePasswordAuthenticationToken dadosDeLogin = login.convert();

        try {
            Authentication authenticate = authenticationManager.authenticate(dadosDeLogin);
            var token = serviceToken.gerarToken(authenticate);
            return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
        }catch(AuthenticationException e){
            return ResponseEntity.badRequest().build();
        }

    }
}
