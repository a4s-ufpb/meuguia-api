package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoreInfoLinkRequestDataTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void builder() {
        var result = MoreInfoLinkRequestData.builder()
                .link("https://test.com")
                .description("test")
                .build();

        assertEquals("https://test.com", result.getLink());
        assertEquals("test", result.getDescription());
    }

    @Test
    void testEquals() {
        var requestData1 = new MoreInfoLinkRequestData("link", "description");
        var requestData2 = new MoreInfoLinkRequestData("link", "description");

        assertTrue(requestData1.equals(requestData2) && requestData2.equals(requestData1));
    }

    @Test
    void testEqualsNull() {
        var requestData1 = new MoreInfoLinkRequestData("link", "description");
        assertNotEquals(null, requestData1);
    }

    @Test
    void testEqualsDifferentClass() {
        var requestData1 = new MoreInfoLinkRequestData("link", "description");
        assertNotEquals(requestData1, new Object());
    }

    @Test
    void testEqualsDifferent() {
        var requestData1 = new MoreInfoLinkRequestData("link", "description");
        var requestData2 = new MoreInfoLinkRequestData("link1", "description");

        assertFalse(requestData1.equals(requestData2) || requestData2.equals(requestData1));
    }

    @Test
    void testHashCode() {
        var requestData1 = new MoreInfoLinkRequestData("link", "description");
        var requestData2 = new MoreInfoLinkRequestData("link", "description");

        assertEquals(requestData1.hashCode(), requestData2.hashCode());
    }

    @Test
    void testToString() {
        var requestData = new MoreInfoLinkRequestData("link", "description");
        assertEquals("MoreInfoLinkRequestData(link=link, description=description)", requestData.toString());
    }

    @Test
    void testValidation() {
        var requestData = new MoreInfoLinkRequestData("https://test.com/", "description");
        var violations = validator.validate(requestData);
        assertEquals(0, violations.size());
    }

    @Test
    void testValidationWWWLink() {
        var requestData = new MoreInfoLinkRequestData("www.test.com/", "description");
        var violations = validator.validate(requestData);
        assertEquals(0, violations.size());
    }

    @Test
    void testValidationHttpsWWWLink() {
        var requestData = new MoreInfoLinkRequestData("https://www.test.com/", "description");
        var violations = validator.validate(requestData);
        assertEquals(0, violations.size());
    }

    @Test
    void testValidationLinkNull() {
        var requestData = new MoreInfoLinkRequestData(null, "description");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationLinkEmpty() {
        var requestData = new MoreInfoLinkRequestData("", "description");
        var violations = validator.validate(requestData);
        assertEquals(2, violations.size());
    }

    @Test
    void testValidationLinkNotValid() {
        var requestData = new MoreInfoLinkRequestData("link", "description");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }
}