package pl.tiwpr.chessbase.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.tiwpr.chessbase.model.Gender;
import pl.tiwpr.chessbase.model.Title;
import pl.tiwpr.chessbase.model.auth.Role;
import pl.tiwpr.chessbase.model.auth.User;
import pl.tiwpr.chessbase.repositories.TitleRepository;
import pl.tiwpr.chessbase.repositories.auth.UserRepository;

@Configuration
@AllArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TitleRepository titleRepository;


    @PostConstruct
    public void init(){
        User user = new User();

        user.setEmail("janbialy@hotmail.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setFirstName("Jan");
        user.setLastName("Bialy");
        user.setRole(Role.ADMIN);

        userRepository.save(user);

        Title title = new Title();
        title.setCodeName("CM");
        title.setName("Candidate master");
        title.setDescription("The candidate master (CM) title is earned by any player who achieves a classical or standard FIDE rating of 2200 in international competition. No norms are required.");
        title.setGender(Gender.MALE);
        titleRepository.save(title);

        title = new Title();
        title.setCodeName("FM");
        title.setName("FIDE Master");
        title.setDescription("The FIDE master (FM) title is earned by any player who achieves a classical or standard FIDE rating of 2300 in international competition. No norms are required.");
        title.setGender(Gender.MALE);
        titleRepository.save(title);

        title = new Title();
        title.setCodeName("IM");
        title.setName("International Master");
        title.setDescription("The international master (IM) title is the second most difficult title to attain. To earn this title, a player must reach an established classical or standard FIDE rating of 2400 and achieve three international master norms in international competition.");
        title.setGender(Gender.MALE);
        titleRepository.save(title);

        title = new Title();
        title.setCodeName("GM");
        title.setName("Grandmaster");
        title.setDescription("The highest title awarded in chess (aside from the title of world champion) is the title of grandmaster. In order to achieve this title, a player must reach an established classical or standard FIDE rating of 2500 and earn three grandmaster norms in international competition.");
        title.setGender(Gender.MALE);
        titleRepository.save(title);

        title = new Title();
        title.setCodeName("WGM");
        title.setName("Woman Grandmaster");
        title.setDescription("The woman grandmaster (WGM) title is exclusive to women and is earned by achieving an established classical or standard FIDE rating of 2300 as well as three WGM norms in international competition.");
        title.setGender(Gender.FEMALE);
        titleRepository.save(title);

        title = new Title();
        title.setCodeName("WIM");
        title.setName("Woman International Master");
        title.setDescription("The woman international master title is exclusive to women and is earned by achieving an established classical or standard FIDE rating of 2200. Three norms are required for the WIM title.");
        title.setGender(Gender.FEMALE);
        titleRepository.save(title);

        title = new Title();
        title.setCodeName("WFM");
        title.setName("Woman FIDE Master");
        title.setDescription("The woman FIDE master (WFM) title is exclusive to women and is earned by achieving an established classical or standard FIDE rating of 2100. No norms are required.");
        title.setGender(Gender.FEMALE);
        titleRepository.save(title);

        title = new Title();
        title.setCodeName("WCM");
        title.setName("Woman Candidate Master");
        title.setDescription("The woman candidate master (WCM) title is exclusive to women and is earned by achieving an established classical or standard FIDE rating of 2000. No norms are required.");
        title.setGender(Gender.FEMALE);
        titleRepository.save(title);

    }

}
