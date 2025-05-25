package com.reservas.vehiculos.institucionales.security.config;

import com.reservas.vehiculos.institucionales.security.jwt.JwtAuthenticationEntryPoint;
import com.reservas.vehiculos.institucionales.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                // *** Mover estas reglas al principio para asegurar la precedencia ***
                                .requestMatchers(
                                        // Rutas de Swagger UI sin prefijo /api
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-resources/**",
                                        "/webjars/**",
                                        // Rutas de Swagger UI CON prefijo /api (ya que el error lo sugiere)
                                        "/api/swagger-ui/**",
                                        "/api/v3/api-docs/**",
                                        "/api/swagger-resources/**",
                                        "/api/webjars/**"
                                ).permitAll()
                                // Rutas para autenticación/registro (mantenerlas también permitidas)
                                .requestMatchers("/api/auth/**").permitAll()
                                // La regla general para /api/ (ahora evaluada después de las específicas)
                                .requestMatchers("/api/**").permitAll() // Esta regla es muy amplia, considera refinarla
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/api/docentes/**").hasAnyRole("ADMIN", "DOCENTE")
//                        .requestMatchers("/api/estudiantes/**").hasAnyRole("ADMIN", "DOCENTE", "ESTUDIANTE")
//                        .requestMatchers("/api/evaluaciones-docente").hasRole("ADMIN")
                                .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}