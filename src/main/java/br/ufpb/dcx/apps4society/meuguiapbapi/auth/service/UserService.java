package br.ufpb.dcx.apps4society.meuguiapbapi.auth.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterRequest;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.UserRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.buildToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
