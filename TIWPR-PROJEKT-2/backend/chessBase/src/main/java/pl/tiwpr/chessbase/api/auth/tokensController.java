package pl.tiwpr.chessbase.api.auth;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.tiwpr.chessbase.model.auth.AuthenticationRequest;
import pl.tiwpr.chessbase.services.AuthenticationService;
import pl.tiwpr.chessbase.services.tokens.TokenService;

@Slf4j
@Validated
@RestController
@RequestMapping("/tokens")
public class tokensController {

    private final AuthenticationService authService;

    private final TokenService tokenService;

    @Autowired
    public tokensController(AuthenticationService authService,
                          TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }


    @GetMapping
    public ResponseEntity<String> getTokenToPost(){
        return ResponseEntity.ok().body(tokenService.generateToken());
    }

    @PostMapping
    public ResponseEntity<?> authenticate(@Nullable @RequestBody AuthenticationRequest request, @Nullable @RequestParam String post) throws AuthenticationException {
        if(post != null){
            return ResponseEntity.ok().body(tokenService.generateToken());
        }else{
            assert request != null;
            return ResponseEntity.ok(authService.authenticate(request));
        }
    }

}
