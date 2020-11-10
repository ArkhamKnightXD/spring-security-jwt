package arkham.knight.jwt.models;

//Esta clase se encargara de devolverle al usuario su respectivo jwt
public class AuthenticationResponse {

    private final String jwt;


    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }


    public String getJwt() {
        return jwt;
    }
}
