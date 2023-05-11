package pl.tiwpr.chessbase.api.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.tiwpr.chessbase.model.auth.AuthenticationResponse;
import pl.tiwpr.chessbase.model.auth.RegisterRequest;
import pl.tiwpr.chessbase.model.auth.User;
import pl.tiwpr.chessbase.repositories.auth.UserRepository;
import pl.tiwpr.chessbase.services.AuthenticationService;

import java.util.List;


@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final AuthenticationService authService;

    @PostMapping("/users")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws DataIntegrityViolationException {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userRepository.findAll());
    }

}
