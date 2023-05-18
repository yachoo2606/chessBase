package pl.tiwpr.chessbase.api.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tiwpr.chessbase.model.auth.AuthenticationRequest;
import pl.tiwpr.chessbase.model.auth.AuthenticationResponse;
import pl.tiwpr.chessbase.services.AuthenticationService;


@Validated
@RestController
@RequestMapping("/tokens")
@Slf4j
@RequiredArgsConstructor
public class authController {

    @Autowired
    private final AuthenticationService authService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws AuthenticationException {
        System.out.println(request);
        return ResponseEntity.ok(authService.authenticate(request));
    }

}
