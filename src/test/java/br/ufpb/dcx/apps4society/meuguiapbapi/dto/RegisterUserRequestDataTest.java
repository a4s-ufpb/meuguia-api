package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AuthenticationTestHelper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserRequestDataTest {
    private final AuthenticationTestHelper testHelper = AuthenticationTestHelper.getInstance();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void builder() {
        var result = RegisterUserRequestData.builder()
                .email("test@test.com")
                .password("12345678")
                .firstName("test")
                .lastName("case")
                .build();

        assertEquals("test@test.com", result.getEmail());
        assertEquals("12345678", result.getPassword());
        assertEquals("test", result.getFirstName());
        assertEquals("case", result.getLastName());
    }

    @Test
    void testEquals() {
        var requestData1 = testHelper.getRegisterRequestData(1);
        var requestData2 = testHelper.getRegisterRequestData(1);

        assertTrue(requestData1.equals(requestData2) && requestData2.equals(requestData1));
    }

    @Test
    void testEqualsNull() {
        var requestData1 = testHelper.getRegisterRequestData(1);
        assertNotEquals(null, requestData1);
    }

    @Test
    void testEqualsDifferentClass() {
        var requestData1 = testHelper.getRegisterRequestData(1);
        assertNotEquals(new Object(), requestData1);
    }

    @Test
    void testEqualsDifferent() {
        var requestData1 = testHelper.getRegisterRequestData(1);
        var requestData2 = testHelper.getRegisterRequestData(2);

        assertFalse(requestData1.equals(requestData2) || requestData2.equals(requestData1));
    }

    @Test
    void testHashCode() {
        var requestData1 = testHelper.getRegisterRequestData(1);
        var requestData2 = testHelper.getRegisterRequestData(1);

        assertEquals(requestData1.hashCode(), requestData2.hashCode());
    }

    @Test
    void testToString() {
        var requestData = testHelper.getRegisterRequestData(1);
        String expected = "RegisterRequestData(email=" + requestData.getEmail() + ", password=" + requestData.getPassword() + ", firstName=" + requestData.getFirstName() + ", lastName=" + requestData.getLastName() + ")";
        assertEquals(expected, requestData.toString());
    }

    @Test
    void testValidation() {
        var requestData = testHelper.getRegisterRequestData(1);
        var violations = validator.validate(requestData);
        assertEquals(0, violations.size());
    }

    @Test
    void testValidationEmailNull() {
        var requestData = testHelper.getRegisterRequestData(1);
        requestData.setEmail(null);
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationEmailEmpty() {
        var requestData = testHelper.getRegisterRequestData(1);
        requestData.setEmail("");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationPasswordNull() {
        var requestData = testHelper.getRegisterRequestData(1);
        requestData.setPassword(null);
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationPasswordEmpty() {
        var requestData = testHelper.getRegisterRequestData(1);
        requestData.setPassword("");
        var violations = validator.validate(requestData);
        assertEquals(2, violations.size());
    }

    @Test
    void testValidationPasswordShort() {
        var requestData = testHelper.getRegisterRequestData(1);
        requestData.setPassword("1234567");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationFirstNameNull() {
        var requestData = testHelper.getRegisterRequestData(1);
        requestData.setFirstName(null);
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationFirstNameEmpty() {
        var requestData = testHelper.getRegisterRequestData(1);
        requestData.setFirstName("");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationLastNameNull() {
        var requestData = testHelper.getRegisterRequestData(1);
        requestData.setLastName(null);
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationLastNameEmpty() {
        var requestData = testHelper.getRegisterRequestData(1);
        requestData.setLastName("");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }
}