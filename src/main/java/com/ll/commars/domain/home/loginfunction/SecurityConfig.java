package com.ll.commars.domain.home.loginfunction;
/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // CSRF 비활성화 (테스트 용도)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/users/signup", "/api/users/login").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin().disable()
                .logout().logoutUrl("/api/users/logout").logoutSuccessUrl("http://localhost:5173");

        return http.build();
    }
}
*/