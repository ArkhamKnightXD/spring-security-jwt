package arkham.knight.jwt.controllers;

import arkham.knight.jwt.models.AuthenticationRequest;
import arkham.knight.jwt.models.AuthenticationResponse;
import arkham.knight.jwt.services.MyUserDetailsService;
import arkham.knight.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    //Aqui el usuario me manda sus credenciales y si las credenciales son correctas, pues le retorno su jwt
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{

        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()));
        } catch (BadCredentialsException exception){

            throw new Exception("Incorrect username or password", exception);
        }

        // cargo los detalles de usuario mediante el metodo loadbyUsername
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        //genero un token con los userDetails obtenidos
        final String jwt = jwtUtil.generateToken(userDetails);

        // y finalmente retorno el objeto AuthenticationResponse que nos dara el jwt junto al status ok mediante el responseEntity
        //El jwt es los que nos permitira acceso a cada uno de los otros endpoints del api, este jwt se mandara en el header de donde
        // sera extraido en los demas endpoints y se validara
        return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
    }


    @GetMapping("/test")
    public String testSecurity(){

        return "security test";
    }
}
