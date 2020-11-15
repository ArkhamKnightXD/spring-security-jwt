package arkham.knight.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    //no es recomendable guardar la clave secreta de esta forma ni que sea tan simple tampoco
    private final String secretKey = "secret";


    public String extractUsername(String token){

        return extractClaim(token, Claims::getSubject);
    }


    public Date extractExpirationDate(String token){

        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token){

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


    private Boolean isTokenExpired(String token){

        return extractExpirationDate(token).before(new Date());
    }

    //Este es el metodo mas importante, con este metodo creo un jwt basado en los userDetails que le mando
    //En este caso mando el username a la hora de crear el jwt
    public String generateToken(UserDetails userDetails){

        Map<String, Object> claims = new HashMap<>();

        return createToken(claims, userDetails.getUsername());
    }

    //Aqui creo el jwt basicamente en serSubjet le indico la persona que ya ha sido autentificada mandando el username
    //Ademas agrego la fecha exacta en que se crea el token y una fecha de expiracion que son 10 horas extra para que expire el jwt
    //finalmente firmo el token mediante el signatureAlgorith y mi clave secreta
    private String createToken(Map<String, Object> claims, String username) {

        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    //Este metodo valida que el username del token sea igual al username recibido del userDetails y que tambien
    // el token no haya expirado para finalmente retornar true o false
    public Boolean validateToken(String token, UserDetails userDetails){

        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
