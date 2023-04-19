package pl.tiwpr.chessbase.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.tiwpr.chessbase.model.auth.Role;
import pl.tiwpr.chessbase.model.auth.User;
import pl.tiwpr.chessbase.repositories.auth.UserRepository;

@Configuration
@AllArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){
        User user = new User();

        user.setEmail("janbialy@hotmail.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setFirstName("Jan");
        user.setLastName("Bialy");
        user.setRole(Role.ADMIN);

        userRepository.save(user);

    }

}
