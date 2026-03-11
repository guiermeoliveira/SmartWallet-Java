package com.Smartwallet.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

// 💡 @Configuration diz ao Spring que essa classe declara Beans (componentes gerenciados).
// @EnableWebSecurity ativa a configuração customizada do Spring Security —
// sem isso, o Spring usa a configuração padrão (tela de login automática).
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 💡 @Bean registra o retorno do método como um componente gerenciado pelo Spring.
    // Quando o RegistrationService declara 'PasswordEncoder' como dependência,
    // o Spring injeta exatamente esse BCryptPasswordEncoder aqui configurado.
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 💡 BCrypt com strength 12 (padrão é 10).
        // O 'strength' define o custo computacional do hash (2^strength iterações).
        // Strength 12 leva ~250ms por hash — lento o suficiente para dificultar
        // ataques de força bruta, rápido o suficiente para não prejudicar UX.
        return new BCryptPasswordEncoder(12);
    }

    // 💡 SecurityFilterChain define as regras de segurança HTTP da aplicação.
    // Aqui configuramos o mínimo para o cadastro funcionar no MVP.
    // Nas próximas sprints adicionamos JWT, roles, CORS etc.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 💡 CSRF (Cross-Site Request Forgery) desabilitado para APIs REST.
            // CSRF é necessário em aplicações com sessão/cookie (como formulários web).
            // APIs REST usam JWT no header — não são vulneráveis a CSRF.
            .csrf(AbstractHttpConfigurer::disable)

            // 💡 STATELESS = sem sessão no servidor.
            // Cada requisição é autenticada pelo JWT no header — o servidor
            // não guarda estado de sessão, o que é escalável e seguro.
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                // 💡 Endpoints públicos — não precisam de autenticação.
                // /auth/** cobre: POST /auth/register, POST /auth/login etc.
                .requestMatchers("/auth/**").permitAll()

                // 💡 Qualquer outro endpoint exige autenticação.
                // Quando implementarmos JWT, o token será validado aqui.
                .anyRequest().authenticated()
            );

        return http.build();
    }
}