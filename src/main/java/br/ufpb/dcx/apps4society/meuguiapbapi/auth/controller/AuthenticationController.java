package br.ufpb.dcx.apps4society.meuguiapbapi.auth.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationRequest;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterRequest;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.service.AuthenticationService;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.service.UserService;
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
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        log.debug("Trying to register a new user");
        AuthenticationResponse responseBody = userService.register(request);
        log.debug("User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        log.debug("Trying to authenticate user request");
        AuthenticationResponse responseBody = authenticationService.authenticate(request);
        log.debug("User authenticated");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
