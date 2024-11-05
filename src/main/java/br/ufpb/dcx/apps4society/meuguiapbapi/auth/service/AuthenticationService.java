package br.ufpb.dcx.apps4society.meuguiapbapi.auth.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationForm;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterForm;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.UserRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService {
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

    public AuthenticationResponse authenticate(AuthenticationForm request) {
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

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponse register(RegisterForm registerForm) {
        User user = User.builder()
                .email(registerForm.getEmail())
                .password(passwordEncoder.encode(registerForm.getPassword()))
                .firstName(registerForm.getFirstName())
                .lastName(registerForm.getLastName())
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.buildToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
