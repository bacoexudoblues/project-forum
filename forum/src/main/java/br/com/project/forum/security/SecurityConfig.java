package br.com.project.forum.security;

import br.com.project.forum.repository.RepositoryUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ServiceSecurity serviceSecurity;

    private final ServiceToken serviceToken;

    private final RepositoryUsuario repositoryUsuario;

    @Override
    @Bean // necessario para injetar a dependencia na controller auth
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //Config de autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/topicos").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/topicos/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .antMatchers(HttpMethod.GET, "/spring-boot-admin/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticacaoTokenFilter(serviceToken,repositoryUsuario), UsernamePasswordAuthenticationFilter.class);
    }

    //Config de autenticação
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(serviceSecurity).passwordEncoder(new BCryptPasswordEncoder());
    }

    //Config de recursos estaticos (imagem, csv, css, etc...)
    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring()
                .antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");

    }

    /*
    Gerar Token de teste
    public static void main(String[] args) {
      System.out.println(new BCryptPasswordEncoder().encode("123456"));
   }
 */
}
