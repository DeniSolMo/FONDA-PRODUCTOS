package itch.tecnm.proyecto.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    // NO necesitamos generateToken() aquí, solo validar
    
    // Extrae el email del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    // Extrae el ID del empleado del token
    public Integer extractEmpleadoId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("idEmpleado", Integer.class);
    }
    
    // Extrae el rol del token
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }
    
    // Extrae el puesto del token
    public String extractPuesto(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("puesto", String.class);
    }
    
    // Extrae el nombre del token
    public String extractNombre(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("nombre", String.class);
    }
    
    // Verifica si el token ha expirado
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    // Extrae la fecha de expiración
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    // Extrae un claim específico del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    // Extrae todos los claims del token (método público para usar en el filtro)
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    // Obtiene la clave de firma
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}