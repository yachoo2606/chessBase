package pl.tiwpr.chessbase.api.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tiwpr.chessbase.model.auth.User;
import pl.tiwpr.chessbase.repositories.auth.UserRepository;

import java.util.List;


@Validated
@Slf4j
@RestController
@RequestMapping("/admin")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userRepository.findAll());
    }

}
