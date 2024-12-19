package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourismSegmentationRequestDataTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void builder() {
        var result = TourismSegmentationRequestData.builder()
                .name("name")
                .description("description")
                .build();

        assertEquals("name", result.getName());
        assertEquals("description", result.getDescription());
    }

    @Test
    void testEquals() {
        var requestData1 = new TourismSegmentationRequestData("name", "description");
        var requestData2 = new TourismSegmentationRequestData("name", "description");

        assertTrue(requestData1.equals(requestData2) && requestData2.equals(requestData1));
    }

    @Test
    void testEqualsNull() {
        var requestData1 = new TourismSegmentationRequestData("name", "description");
        assertNotEquals(null, requestData1);
    }

    @Test
    void testEqualsDifferentClass() {
        var requestData1 = new TourismSegmentationRequestData("name", "description");
        assertNotEquals(new Object(), requestData1);
    }

    @Test
    void testEqualsDifferent() {
        var requestData1 = new TourismSegmentationRequestData("name", "description");
        var requestData2 = new TourismSegmentationRequestData("name1", "description");

        assertFalse(requestData1.equals(requestData2) || requestData2.equals(requestData1));
    }

    @Test
    void testHashCode() {
        var requestData1 = new TourismSegmentationRequestData("name", "description");
        var requestData2 = new TourismSegmentationRequestData("name", "description");

        assertEquals(requestData1.hashCode(), requestData2.hashCode());
    }

    @Test
    void testToString() {
        var requestData = new TourismSegmentationRequestData("name", "description");
        String expected = "TourismSegmentationRequestData(name=name, description=description)";
        assertEquals(expected, requestData.toString());
    }

    @Test
    void testValidation() {
        var requestData = new TourismSegmentationRequestData("name", "description");
        var violations = validator.validate(requestData);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationNameNull() {
        var requestData = new TourismSegmentationRequestData(null, "description");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationNameEmpty() {
        var requestData = new TourismSegmentationRequestData("", "description");
        var violations = validator.validate(requestData);
        assertEquals(1, violations.size());
    }
}