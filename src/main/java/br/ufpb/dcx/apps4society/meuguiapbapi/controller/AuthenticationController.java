package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.dto.authentication.AuthenticationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.user.RegisterUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.authentication.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.user.UserDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.service.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(
            @RequestBody @Valid RegisterUserRequestData request
    ) {
        log.debug("Trying to register a new user");
        UserDTO responseBody = authenticationService.register(request);
        log.debug("User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseData> authenticate(
            @RequestBody @Valid AuthenticationRequestData request
            ) {
        log.debug("Trying to authenticate user request");
        AuthenticationResponseData responseBody = authenticationService.authenticate(request);
        log.debug("User authenticated");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
