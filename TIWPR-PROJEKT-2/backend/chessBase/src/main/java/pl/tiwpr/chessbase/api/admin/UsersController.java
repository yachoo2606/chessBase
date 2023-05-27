package pl.tiwpr.chessbase.api.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.tiwpr.chessbase.config.JwtService;
import pl.tiwpr.chessbase.model.auth.AuthenticationResponse;
import pl.tiwpr.chessbase.model.auth.RegisterRequest;
import pl.tiwpr.chessbase.model.auth.User;
import pl.tiwpr.chessbase.repositories.auth.UserRepository;
import pl.tiwpr.chessbase.services.AuthenticationService;

import java.util.List;
import java.util.Optional;


@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final AuthenticationService authService;

    @Autowired
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws DataIntegrityViolationException {
        Optional<User> tempUser = userRepository.findByEmail(request.getEmail());
        if(tempUser.isPresent()){
            throw new DataIntegrityViolationException("User with provided email already exists");
        }
        log.info("New User Registered: "+request.getEmail());
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        log.info("Requested list of users");
        return ResponseEntity.ok().body(userRepository.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String token ,@PathVariable Long id){
        log.info("Requested user with id: "+id);
        Optional<User> requestedUser = userRepository.findOneById(id);
        if(requestedUser.isPresent()){
             if(jwtService.extractRole(token.substring(7)).equals("[ADMIN]")){
                 return ResponseEntity.ok().body(requestedUser.get());
             }else{
                 if(jwtService.extractUsername(token.substring(7)).equals(requestedUser.get().getUsername())){
                     return ResponseEntity.ok().body(requestedUser.get());
                 }else{
                     return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only requested your user information");
                 }
             }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested user not found");
        }
    }



}
