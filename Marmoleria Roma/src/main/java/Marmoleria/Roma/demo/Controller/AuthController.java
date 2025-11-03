package Marmoleria.Roma.demo.Controller;
import Marmoleria.Roma.demo.Modelos.Personas.Usuario;
import Marmoleria.Roma.demo.Modelos.Requests.LoginRequest;
import Marmoleria.Roma.demo.Service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Intentamos crear el token de autenticación
            UsernamePasswordAuthenticationToken authToken;
            try {
                authToken = new UsernamePasswordAuthenticationToken(
                        loginRequest.getCorreo(),
                        loginRequest.getPassword()
                );
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error creando token de autenticación: " + e.getMessage());
            }

            // Intentamos autenticar al usuario
            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate(authToken);
            } catch (BadCredentialsException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error en autenticación: " + e.getMessage());
            }

            // Intentamos obtener el empleado autenticado
            Usuario usuario;
            try {
                usuario = (Usuario) authentication.getPrincipal();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error obteniendo usuario autenticado: " + e.getMessage());
            }

            // Intentamos generar el token JWT
            String token;
            try {
                token = jwtService.generateToken(usuario);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error generando token JWT: " + e.getMessage());
            }

            // Si llegamos acá todo salió bien
            return ResponseEntity.ok().body(Map.of("token", token));

        } catch (Exception e) {
            // Cualquier otro error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado en login: " + e.getMessage());
        }
    }

    @GetMapping("/verificarToken")
    public ResponseEntity<?> verificarToken(Authentication aut) {
        Usuario usuario = (Usuario) aut.getPrincipal();
        return ResponseEntity.ok().body(Map.of("nombre", usuario.getUsername()));
    }
}
