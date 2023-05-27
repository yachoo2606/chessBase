package pl.tiwpr.chessbase.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.tiwpr.chessbase.config.JwtService;
import pl.tiwpr.chessbase.model.auth.*;
import pl.tiwpr.chessbase.repositories.auth.UserRepository;
import pl.tiwpr.chessbase.services.AuthenticationService;

import java.util.List;
import java.util.Map;
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

    private final PasswordEncoder passwordEncoder;

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
            if(jwtService.extractUsername(token.substring(7)).equals(requestedUser.get().getUsername()) ||
                jwtService.extractRole(token.substring(7)).equals("[ADMIN]")){
                return ResponseEntity.ok().header("VERSION", requestedUser.get().getVersion().toString()).body(requestedUser.get());
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only requested your user information");
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested user not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestHeader("VERSION") Long requestVersion,
                                        @RequestHeader("Authorization") String token,
                                        @RequestBody UserView updateUser){
        Optional<User> exisingUser = userRepository.findOneById(id);
        if(exisingUser.isPresent()){
            if(jwtService.extractUsername(token.substring(7)).equals(exisingUser.get().getUsername()) ||
                jwtService.extractRole(token.substring(7)).equals("[ADMIN]")){

                if(!requestVersion.equals(exisingUser.get().getVersion())){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("You are trying to update out of date object get new user and try again.");
                }

                User userToUpdate= exisingUser.get();
                userToUpdate.setFirstName(updateUser.getFirstName());
                userToUpdate.setLastName(updateUser.getLastName());
                userToUpdate.setEmail(updateUser.getEmail());
                userToUpdate.setPassword(passwordEncoder.encode(updateUser.getPassword()));

                if(jwtService.extractRole(token.substring(7)).equals("[ADMIN]")){
                    userToUpdate.setRole(updateUser.getRole());
                }

                userRepository.save(userToUpdate);

                return ResponseEntity.status(HttpStatus.OK).body("user updated");

            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your user information");
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested user not found");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartUser(@PathVariable Long id,
                                            @RequestHeader("VERSION") Long requestVersion,
                                            @RequestHeader("Authorization") String token,
                                            @RequestBody Map<String, Object> updates){
        Optional<User> exisingUser = userRepository.findOneById(id);
        if(exisingUser.isPresent()){
            if(jwtService.extractUsername(token.substring(7)).equals(exisingUser.get().getUsername()) ||
                jwtService.extractRole(token.substring(7)).equals("[ADMIN]")){

                if(!requestVersion.equals(exisingUser.get().getVersion())){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("You are trying to update out of date object get new user and try again.");
                }

                User userToUpdate= exisingUser.get();

                for(Map.Entry<String, Object> entry: updates.entrySet()){
                    String field = entry.getKey();
                    String value = (String) entry.getValue();

                    switch (field) {
                        case "firstName" -> userToUpdate.setFirstName(value);
                        case "lastName" -> userToUpdate.setLastName(value);
                        case "email" -> userToUpdate.setEmail(value);
                        case "password" -> userToUpdate.setPassword(passwordEncoder.encode(value));
                        case "role" -> {
                            if (jwtService.extractRole(token.substring(7)).equals("[ADMIN]")) {
                                userToUpdate.setRole(Role.valueOf(value));
                            }
                        }
                    }
                }
                userRepository.save(userToUpdate);

                return ResponseEntity.status(HttpStatus.OK).body("user updated");

            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your user information");
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested user not found");
        }
    }

}
