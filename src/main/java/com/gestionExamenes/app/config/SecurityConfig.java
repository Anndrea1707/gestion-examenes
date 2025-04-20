package com.gestionExamenes.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactivar CSRF para pruebas
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/", "/index.html", "/cuentas.html", "/api/cuentas", "/login.html").permitAll()
                // Rutas compartidas para estudiantes y coordinadores (más específica, primero)
                .requestMatchers("/estudiantes/examenes/api/**").hasAnyRole("ESTUDIANTE", "COORDINADOR")
                // Rutas exclusivas para estudiantes
                .requestMatchers("/inicioEstudiante.html", "/estudiantes/estudiante/api/**").hasRole("ESTUDIANTE")
                // Rutas exclusivas para coordinadores
                .requestMatchers("/inicioCoordinador.html", "/registroEstudiante.html", "/estudiantes/**", "/estudiantes.html", "/editarEstudiante.html", "/registrarPrueba.html", "/verPrueba.html", "/api/coordinador/**", "/estudiantes/api/**", "/examenes/api/**").hasRole("COORDINADOR")
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    System.out.println("Autenticación exitosa para usuario: " + authentication.getName() + ", rol: " + authentication.getAuthorities());
                    String role = authentication.getAuthorities().iterator().next().getAuthority();
                    if (role.equals("ROLE_ESTUDIANTE")) {
                        System.out.println("Redirigiendo a /inicioEstudiante.html");
                        response.sendRedirect("/inicioEstudiante.html");
                    } else if (role.equals("ROLE_COORDINADOR")) {
                        System.out.println("Redirigiendo a /inicioCoordinador.html");
                        response.sendRedirect("/inicioCoordinador.html");
                    }
                })
                .failureHandler((request, response, exception) -> {
                    System.out.println("Error de autenticación: " + exception.getMessage());
                    response.sendRedirect("/login.html?error");
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString(); // No cifrar, devolver texto plano
            }
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword); // Comparar texto plano
            }
        };
    }
}