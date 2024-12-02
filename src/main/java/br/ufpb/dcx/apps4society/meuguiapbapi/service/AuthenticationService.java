package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AuthenticationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.RegisterRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(JwtService jwtService,
                                 AuthenticationManager authenticationManager,
                                 UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponseData authenticate(AuthenticationRequestData request) {
        log.debug("Authenticating user");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        log.debug("Authenticated");
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var token = jwtService.buildToken(user);
        log.debug("Token generated");

        return AuthenticationResponseData.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponseData register(RegisterRequestData registerRequestData) {
        User user = User.builder()
                .email(registerRequestData.getEmail())
                .password(passwordEncoder.encode(registerRequestData.getPassword()))
                .firstName(registerRequestData.getFirstName())
                .lastName(registerRequestData.getLastName())
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.buildToken(user);
        return AuthenticationResponseData.builder()
                .token(jwtToken)
                .build();
    }
}
