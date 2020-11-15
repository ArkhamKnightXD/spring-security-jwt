package arkham.knight.jwt;

import arkham.knight.jwt.services.MyUserDetailsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class, args);
    }


    @Bean
    CommandLineRunner runner(MyUserDetailsService myUserDetailsService){
        return args -> {

            myUserDetailsService.createAdminUser();
        };
    }
}
