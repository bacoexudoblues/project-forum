package br.com.project.forum.security;

import br.com.project.forum.model.Usuario;
import br.com.project.forum.repository.RepositoryUsuario;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 OncePerRequestFilter -> chamado pelo spring uma unica vez por request
 */
public class AutenticacaoTokenFilter extends OncePerRequestFilter {

    private ServiceToken serviceToken;
    private RepositoryUsuario repositoryUsuario;

    public AutenticacaoTokenFilter(ServiceToken serviceToken, RepositoryUsuario repositoryUsuario) {
        this.serviceToken = serviceToken;
        this.repositoryUsuario = repositoryUsuario;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        var token = recuperarToken(httpServletRequest);
        var valido = serviceToken.isTokenValido(token);
        if (valido) {
            autenticarCliente(token);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String recuperarToken(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }

        return token.substring(7, token.length());
    }

    private void autenticarCliente(String token) {

        var usuarioId = serviceToken.getIdUsuario(token);
        Usuario usuario = repositoryUsuario.findById(usuarioId).get();
        var autenticacao = new UsernamePasswordAuthenticationToken(usuarioId, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(autenticacao);
    }



}
