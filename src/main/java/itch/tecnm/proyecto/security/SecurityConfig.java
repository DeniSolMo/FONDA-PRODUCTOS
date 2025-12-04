package itch.tecnm.proyecto.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import itch.tecnm.proyecto.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                // ====== ARCHIVOS PÚBLICOS (IMÁGENES) ======
                .requestMatchers("/api/files/**").permitAll()
                
                // Productos
                .requestMatchers(HttpMethod.GET, "/api/producto/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/producto").hasAnyAuthority("ADMIN", "SUPERVISOR")
                .requestMatchers(HttpMethod.PUT, "/api/producto/**").hasAnyAuthority("ADMIN", "SUPERVISOR")
                .requestMatchers(HttpMethod.DELETE, "/api/producto/**").hasAnyAuthority("ADMIN", "SUPERVISOR")
                
                // Tipos
                .requestMatchers(HttpMethod.GET, "/api/tipo/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/tipo").hasAnyAuthority("ADMIN", "SUPERVISOR")
                .requestMatchers(HttpMethod.PUT, "/api/tipo/**").hasAnyAuthority("ADMIN", "SUPERVISOR")
                .requestMatchers(HttpMethod.DELETE, "/api/tipo/**").hasAnyAuthority("ADMIN", "SUPERVISOR")
                
                // Ventas
                .requestMatchers(HttpMethod.GET, "/api/venta/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/venta").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/venta/**").hasAnyAuthority("ADMIN", "SUPERVISOR")
                .requestMatchers(HttpMethod.DELETE, "/api/venta/**").hasAnyAuthority("ADMIN", "SUPERVISOR")
                
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
