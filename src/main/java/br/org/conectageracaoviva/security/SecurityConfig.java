package br.org.conectageracaoviva.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Para facilitar testes no Postman durante o aprendizado.
                // Depois podemos separar CSRF para telas e API.
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/login", "/voluntarios/novo", "/doacoes/nova", "/visitas/nova", "/api/eventos", "/api/visitas/**").permitAll()
                        .requestMatchers("/admin", "/admin/**", "/api/voluntarios").hasRole("ADMINISTRADOR")
                        .requestMatchers("/voluntario", "/voluntario/**").hasAnyRole("VOLUNTARIO", "ADMINISTRADOR")
                        .requestMatchers("/comunidade", "/comunidade/**", "/beneficiarios/novo").hasAnyRole("COMUNIDADE", "ADMINISTRADOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandlerPorPerfil())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt salva senhas de forma criptografada, nunca em texto puro.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler successHandlerPorPerfil() {
        return (request, response, authentication) -> {
            String perfilSelecionado = request.getParameter("perfil");

            if (perfilSelecionado != null && !perfilSelecionado.isBlank() && !usuarioPossuiPerfilSelecionado(authentication, perfilSelecionado)) {
                SecurityContextHolder.clearContext();
                request.getSession().invalidate();
                response.sendRedirect("/login?perfil=" + perfilSelecionado + "&perfilError=true");
                return;
            }

            String destino = definirDestino(authentication, perfilSelecionado);
            response.sendRedirect(destino);
        };
    }

    private String definirDestino(Authentication authentication, String perfilSelecionado) {
        if ("administrador".equals(perfilSelecionado)) {
            return "/admin";
        }

        if ("voluntario".equals(perfilSelecionado)) {
            return "/voluntario";
        }

        if ("comunidade".equals(perfilSelecionado)) {
            return "/comunidade";
        }

        boolean isAdmin = possuiPerfil(authentication, "ROLE_ADMINISTRADOR");
        boolean isVoluntario = possuiPerfil(authentication, "ROLE_VOLUNTARIO");
        boolean isComunidade = possuiPerfil(authentication, "ROLE_COMUNIDADE");

        if (isAdmin) {
            return "/admin";
        }

        if (isVoluntario) {
            return "/voluntario";
        }

        if (isComunidade) {
            return "/comunidade";
        }

        return "/";
    }

    private boolean possuiPerfil(Authentication authentication, String perfil) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(perfil::equals);
    }

    private boolean usuarioPossuiPerfilSelecionado(Authentication authentication, String perfilSelecionado) {
        if (possuiPerfil(authentication, "ROLE_ADMINISTRADOR")) {
            return true;
        }

        return switch (perfilSelecionado) {
            case "administrador" -> possuiPerfil(authentication, "ROLE_ADMINISTRADOR");
            case "voluntario" -> possuiPerfil(authentication, "ROLE_VOLUNTARIO");
            case "comunidade" -> possuiPerfil(authentication, "ROLE_COMUNIDADE");
            default -> true;
        };
    }
}
