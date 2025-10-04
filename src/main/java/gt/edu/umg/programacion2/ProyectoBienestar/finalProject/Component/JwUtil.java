package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwUtil {
    // Clave secreta para firmar el token
    private String secret = "tu_clave_secreta_aqui";

    // 1. Genera un token JWT a partir de los detalles del usuario
    public String generateToken(UserDetails userDetails) {
        // Lógica que construye el token con fecha de expiración y firma
        return "TEMPORAL_JWT_TOKEN";
    }

    // 2. Valida la integridad y expiración del token
    public Boolean validateToken(String token, UserDetails userDetails) {
        // Lógica que verifica la firma y la fecha
        return false;
    }

    // 3. Extrae el nombre de usuario del token
    public String extractUsername(String token) {
        // Lógica que decodifica el token para obtener el 'subject' (username)
        return "usuario_temporal";
    }
}
