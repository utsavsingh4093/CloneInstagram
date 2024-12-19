package com.java.main.restTemplates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Properly disable CSRF
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Allow all requests
                .formLogin(form -> form.disable()) // Disable default login form
                .httpBasic(httpBasic -> httpBasic.disable()); // Disable HTTP Basic authentication

        return http.build();
    }
}
