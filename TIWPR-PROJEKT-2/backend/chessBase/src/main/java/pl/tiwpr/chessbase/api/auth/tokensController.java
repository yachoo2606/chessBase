package pl.tiwpr.chessbase.api.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.tiwpr.chessbase.model.auth.AuthenticationRequest;
import pl.tiwpr.chessbase.model.auth.AuthenticationResponse;
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
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws AuthenticationException {
        return ResponseEntity.ok(authService.authenticate(request));
    }

}
