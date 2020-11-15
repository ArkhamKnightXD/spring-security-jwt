package arkham.knight.jwt.services;

import arkham.knight.jwt.models.AuthenticationRequest;
import arkham.knight.jwt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public void saveUser(AuthenticationRequest authenticationRequest){

        userRepository.save(authenticationRequest);
    }


    public AuthenticationRequest findByUsername(String username){

        return userRepository.findByUsername(username);
    }
}
