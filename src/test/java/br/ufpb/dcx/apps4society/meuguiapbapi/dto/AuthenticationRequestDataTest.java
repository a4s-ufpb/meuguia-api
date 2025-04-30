package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationRequestData;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.UserTestsHelper.createAuthenticationRequestData;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationRequestDataTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void builder() {
        var result = AuthenticationRequestData.builder()
                .email("test@test.com")
                .password("12345678")
                .build();

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
        assertEquals("12345678", result.getPassword());
    }

    @Test
    void testEquals() {
        var authenticationRequestData1 = createAuthenticationRequestData();
        var authenticationRequestData2 = createAuthenticationRequestData();

        assertEquals(authenticationRequestData1.equals(authenticationRequestData2), authenticationRequestData2.equals(authenticationRequestData1));
    }

    @Test
    void testEqualsNull() {
        var authenticationRequestData1 = createAuthenticationRequestData();

        assertNotEquals(null, authenticationRequestData1);
    }

    @Test
    void testEqualsDifferentClass() {
        var authenticationRequestData1 = createAuthenticationRequestData();

        assertNotEquals(new Object(), authenticationRequestData1);
    }

    @Test
    void testEqualsDifferent() {
        var authenticationRequestData1 = createAuthenticationRequestData(1);
        var authenticationRequestData2 = createAuthenticationRequestData(2);

        assertFalse(authenticationRequestData1.equals(authenticationRequestData2) || authenticationRequestData2.equals(authenticationRequestData1));
    }

    @Test
    void testHashCode() {
        var authenticationRequestData1 = createAuthenticationRequestData();
        var authenticationRequestData2 = createAuthenticationRequestData();

        assertEquals(authenticationRequestData1.hashCode(), authenticationRequestData2.hashCode());
    }

    @Test
    void testToString() {
        var authenticationRequestData = createAuthenticationRequestData();
        String expected = "AuthenticationRequestData(email=" + authenticationRequestData.getEmail() + ", password=" + authenticationRequestData.getPassword() + ")";

        assertEquals(expected, authenticationRequestData.toString());
    }

    @Test
    void testValidation() {
        var authenticationRequestData = createAuthenticationRequestData();
        var violations = validator.validate(authenticationRequestData);

        assertEquals(0, violations.size());
    }

    @Test
    void testValidationEmailNull() {
        var authenticationRequestData = createAuthenticationRequestData();
        authenticationRequestData.setEmail(null);
        var violations = validator.validate(authenticationRequestData);

        assertEquals(1, violations.size());
    }

    @Test
    void testValidationEmailEmpty() {
        var authenticationRequestData = createAuthenticationRequestData();
        authenticationRequestData.setEmail("");
        var violations = validator.validate(authenticationRequestData);

        assertEquals(1, violations.size());
    }

    @Test
    void testValidationInvalidEmail() {
        var authenticationRequestData = createAuthenticationRequestData();
        authenticationRequestData.setEmail("test");
        var violations = validator.validate(authenticationRequestData);

        assertEquals(1, violations.size());
    }

    @Test
    void testValidationPasswordNull() {
        var authenticationRequestData = createAuthenticationRequestData();
        authenticationRequestData.setPassword(null);
        var violations = validator.validate(authenticationRequestData);

        assertEquals(1, violations.size());
    }

    @Test
    void testValidationPasswordEmpty() {
        var authenticationRequestData = createAuthenticationRequestData();
        authenticationRequestData.setPassword("");
        var violations = validator.validate(authenticationRequestData);

        assertEquals(2, violations.size());
    }

    @Test
    void testValidationPasswordMinSize() {
        var authenticationRequestData = createAuthenticationRequestData();
        authenticationRequestData.setPassword("123");
        var violations = validator.validate(authenticationRequestData);

        assertEquals(1, violations.size());
    }
}