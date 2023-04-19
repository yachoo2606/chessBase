package pl.tiwpr.chessbase.api.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tiwpr.chessbase.model.auth.AuthenticationRequest;
import pl.tiwpr.chessbase.model.auth.AuthenticationResponse;
import pl.tiwpr.chessbase.model.auth.RegisterRequest;
import pl.tiwpr.chessbase.services.auth.AuthenticationService;


@Validated
@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class authController {

    @Autowired
    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws DataIntegrityViolationException{
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws AuthenticationException {
        return ResponseEntity.ok(authService.authenticate(request));
    }

}
