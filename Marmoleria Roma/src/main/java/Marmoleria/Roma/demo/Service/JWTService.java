package Marmoleria.Roma.demo.Service;
import Marmoleria.Roma.demo.Modelos.Personas.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Autowired
    UsuarioUserDetailsService userDetailsService;

    public UserDetails getUserDetailsFromToken(String token) {
        String username = extractUsername(token);
        return userDetailsService.loadUserByUsername(username);
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // email
                .claim("roles", userDetails.getAuthorities())
                .claim("tipoUsuario", ((Usuario) userDetails).getTipoUsuario().name()) // ← agregado
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    // Verifica si un token es válido para un usuario dado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Extrae el nombre de usuario desde el token (campo "sub")
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Verifica si el token está vencido
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Extrae todos los datos (claims) del token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Convierte la clave secreta en un objeto Key
    private Key getKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
