package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private final String secretKey = "mysecretkeymysecretkeymysecretkeymysecretkey";
    private final long expiration = 1000 * 60 * 60; // 1 hora

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "expiration", expiration);
    }

    @Test
    void getSigningKeyTest() {
        Key signingKey = jwtService.getSigningKey();
        assertNotNull(signingKey);
        assertEquals(Keys.hmacShaKeyFor(secretKey.getBytes()), signingKey);
    }

    @Test
    void buildTokenTest() {
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtService.buildToken(userDetails);

        assertNotNull(token);
        assertTrue(token.contains("."));
    }

    @Test
    void extractUsernameTest() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.buildToken(userDetails);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals("testuser", extractedUsername);
    }

    @Test
    void isTokenValid_ValidTokenTest() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.buildToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_InvalidTokenTest() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.buildToken(userDetails);

        assertThrows(SignatureException.class, () -> jwtService.isTokenValid(token + "tampered", userDetails));
    }

    @Test
    void isTokenValid_ExpiredTokenTest() throws InterruptedException {
        when(userDetails.getUsername()).thenReturn("testuser");
        ReflectionTestUtils.setField(jwtService, "expiration", 1);
        String token = jwtService.buildToken(userDetails);

        Thread.sleep(2);

        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, userDetails));
    }

}
