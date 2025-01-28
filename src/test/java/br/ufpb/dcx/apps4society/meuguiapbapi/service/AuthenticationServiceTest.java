package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AuthenticationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.RegisterUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.UserDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AuthenticationTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.UserRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AuthenticationServiceTest {
    private final AuthenticationTestHelper authenticationTestHelper = AuthenticationTestHelper.getInstance();

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
        AuthenticationRequestData requestData = authenticationTestHelper.createAuthenticationRequestData();
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
        AuthenticationRequestData requestData = authenticationTestHelper.createAuthenticationRequestData();
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(requestData));
    }

    @Test
    void registerUserTest() {
        RegisterUserRequestData requestData = authenticationTestHelper.getRegisterRequestData(1);
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
