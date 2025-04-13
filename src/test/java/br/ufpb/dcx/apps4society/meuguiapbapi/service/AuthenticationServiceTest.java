package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.service.AuthenticationService;
import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.service.JwtService;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.RegisterUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.UserDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.UserTestsHelper.createAuthenticationRequestData;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.UserTestsHelper.createRegisterRequestData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AuthenticationServiceTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void authenticateUserTest() {
        AuthenticationRequestData requestData = createAuthenticationRequestData();
        when(authenticationManager.authenticate(any(Authentication.class))).thenAnswer(invocationOnMock -> {
            Authentication auth = invocationOnMock.getArgument(0);
            User user = User.builder().email((String) auth.getPrincipal()).password((String) auth.getCredentials()).build();

            return new TestingAuthenticationToken(user, user.getPassword());
        });
        when(jwtService.buildToken(any())).thenReturn("generatedToken");

        AuthenticationResponseData result = authenticationService.authenticate(requestData);

        assertEquals("generatedToken", result.getToken());
    }

    @Test
    void authenticateUser_InvalidCredentialsTest() {
        AuthenticationRequestData requestData = createAuthenticationRequestData();
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(requestData));
    }

    @Test
    void registerUserTest() {
        RegisterUserRequestData requestData = createRegisterRequestData(1);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocationOnMock -> {
            User user = invocationOnMock.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserDTO result = authenticationService.register(requestData);

        assertEquals(1, result.getId());
        assertEquals(requestData.getEmail(), result.getEmail());
        assertEquals(requestData.getFirstName(), result.getFirstName());
        assertEquals(requestData.getLastName(), result.getLastName());
    }
}
