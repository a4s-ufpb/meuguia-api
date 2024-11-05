package br.ufpb.dcx.apps4society.meuguiapbapi.auth.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationForm;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterForm;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterForm request
    ) {
        log.debug("Trying to register a new user");
        AuthenticationResponse responseBody = authenticationService.register(request);
        log.debug("User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationForm request
    ) {
        log.debug("Trying to authenticate user request");
        AuthenticationResponse responseBody = authenticationService.authenticate(request);
        log.debug("User authenticated");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
