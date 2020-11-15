package arkham.knight.jwt.services;

import arkham.knight.jwt.models.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AuthenticationRequest authenticationRequest = userService.findByUsername(username);

        return new User(authenticationRequest.getUsername(),bCryptPasswordEncoder.encode(authenticationRequest.getPassword()) ,new ArrayList<>());
    }


    public void createAdminUser(){

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();

        authenticationRequest.setUsername("kar");
        authenticationRequest.setPassword(bCryptPasswordEncoder.encode("kar"));

        userService.saveUser(authenticationRequest);
    }
}
