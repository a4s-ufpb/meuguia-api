package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUserRequestDataTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void builder() {
        var result = UpdateUserRequestData.builder()
                .email("test@test.com")
                .firstName("test")
                .lastName("case")
                .build();

        assertEquals("test@test.com", result.getEmail());
        assertEquals("test", result.getFirstName());
        assertEquals("case", result.getLastName());
    }

    @Test
    void testEquals() {
        var requestData1 = new UpdateUserRequestData("test@test.com", "test", "case");
        var requestData2 = new UpdateUserRequestData("test@test.com", "test", "case");

        assertTrue(requestData1.equals(requestData2) && requestData2.equals(requestData1));
    }

    @Test
    void testEqualsNull() {
        var requestData = new UpdateUserRequestData("test@test.com", "test", "case");
        assertNotEquals(null, requestData);
    }

    @Test
    void testEqualsDifferentClass() {
        var requestData = new UpdateUserRequestData("test@test.com", "test", "case");
        assertNotEquals(new Object(), requestData);
    }

    @Test
    void testEqualsDifferent() {
        var requestData1 = new UpdateUserRequestData("test1@test.com", "test", "case");
        var requestData2 = new UpdateUserRequestData("test2@test.com", "test", "case");

        assertFalse(requestData1.equals(requestData2) || requestData2.equals(requestData1));
    }

    @Test
    void testHashCode() {
        var requestData1 = new UpdateUserRequestData("test@test.com", "test", "case");
        var requestData2 = new UpdateUserRequestData("test@test.com", "test", "case");
        assertEquals(requestData1.hashCode(), requestData2.hashCode());
    }

    @Test
    void testToString() {
        var requestData1 = new UpdateUserRequestData("test@test.com", "test", "case");
        String expected = "UserDTO(email=test@test.com, firstName=test, lastName=case)";
        assertEquals(expected, requestData1.toString());
    }

    @Test
    void testValidation() {
        var requestData = new UpdateUserRequestData("test@test.com", "test", "case");
        var violations = validator.validate(requestData);
        assertEquals(0, violations.size());
    }

    @Test
    void testValidationEmailNull() {
        var requestData = new UpdateUserRequestData(null, "test", "case");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationEmailEmpty() {
        var requestData = new UpdateUserRequestData("", "test", "case");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationFirstNameNull() {
        var requestData = new UpdateUserRequestData("test@test.com", null, "case");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationFirstNameEmpty() {
        var requestData = new UpdateUserRequestData("test@test.com", "", "case");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationLastNameNull() {
        var requestData = new UpdateUserRequestData("test@test.com", "test", null);
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationLastNameEmpty() {
        var requestData = new UpdateUserRequestData("test@test.com", "test", "");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }
}