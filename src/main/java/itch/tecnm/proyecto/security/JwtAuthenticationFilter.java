package itch.tecnm.proyecto.security;

import java.io.IOException;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import itch.tecnm.proyecto.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        
        System.out.println("🔍 [Productos] Request: " + request.getMethod() + " " + request.getRequestURI());
        System.out.println("🔍 [Productos] Authorization Header: " + authHeader);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("⚠️ [Productos] No hay token Bearer");
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            final String jwt = authHeader.substring(7);
            
            // Extraer información del token
            final String userEmail = jwtService.extractUsername(jwt);
            final String role = jwtService.extractRole(jwt);
            final String nombre = jwtService.extractNombre(jwt);
            final Integer empleadoId = jwtService.extractEmpleadoId(jwt);
            
            System.out.println("✅ [Productos] Token decodificado:");
            System.out.println("   - Email: " + userEmail);
            System.out.println("   - Rol: " + role);
            System.out.println("   - Nombre: " + nombre);
            System.out.println("   - ID Empleado: " + empleadoId);
            
            if (userEmail != null && !jwtService.isTokenExpired(jwt)) {
                // Crear autenticación con el rol extraído del token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                System.out.println("✅ [Productos] Autenticación exitosa para: " + userEmail);
            } else {
                System.out.println("❌ [Productos] Token expirado o email nulo");
            }
        } catch (Exception e) {
            System.err.println("❌ [Productos] Error en JWT: " + e.getMessage());
            e.printStackTrace();
        }
        
        filterChain.doFilter(request, response);
    }
}