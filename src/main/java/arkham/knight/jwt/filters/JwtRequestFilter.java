package arkham.knight.jwt.filters;

import arkham.knight.jwt.services.MyUserDetailsService;
import arkham.knight.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//El proposito de esta clase sera el de interceptar todos los request y examinar el header solo una vez
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    //Este metodo es el encargado de hacer el filtrado, basicamente revisa el header del request en busca del jwt
    //y si el jwt es valido va a entonces conseguir el userdetails de userDetailServvice y lo va a guardar
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Este variable almacenara el contenido del header llamado authorization que es el que contiene bearer+el jwt
        final String authorizeRequestHeader = request.getHeader("Authorization");

        String jwt = null;
        String username = null;

        if (authorizeRequestHeader != null && authorizeRequestHeader.startsWith("Bearer ")){

            //Le pongo 7 en el substring pues el jwt empieza en la posicion 7 del string de authorizeRequest
            jwt = authorizeRequestHeader.substring(7);

            //Extraigo el username del jwt
            username = jwtUtil.extractUsername(jwt);
        }

        if (username!= null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

            //si el token es valido
            if (jwtUtil.validateToken(jwt, userDetails)){

                //instancio esta nueva clase que es la especifica de springsecurity para manejar la verificacion de token
                //mediante username y password, esto es basicamente el proceso final de la comprobacion
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        //Al final de todas esas comprobaciones al filterChain le agrego el requesy y response
        filterChain.doFilter(request, response);
    }
}
