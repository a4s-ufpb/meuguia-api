package br.ufpb.dcx.apps4society.meuguiapbapi.unit.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.dto.AttractionTypeRequestData;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper.createAttractionTypeRequestData;
import static org.junit.jupiter.api.Assertions.*;

class AttractionTypeRequestDataTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testBuilder() {
        var result = AttractionTypeRequestData.builder()
                .name("Test name")
                .description("Test description")
                .build();

        assertNotNull(result);
        assertEquals("Test name", result.getName());
        assertEquals("Test description", result.getDescription());
    }

    @Test
    void testEquals() {
        var attractionTypeRequestData1 = createAttractionTypeRequestData(1);
        var attractionTypeRequestData2 = createAttractionTypeRequestData(1);

        assertTrue(attractionTypeRequestData1.equals(attractionTypeRequestData2) && attractionTypeRequestData2.equals(attractionTypeRequestData1));
    }

    @Test
    void testEqualsNull() {
        var attractionTypeRequestData = createAttractionTypeRequestData(1);

        assertNotEquals(null, attractionTypeRequestData);
    }

    @Test
    void testEqualsDifferentClass() {
        var attractionTypeRequestData = createAttractionTypeRequestData(1);

        assertNotEquals(new Object(), attractionTypeRequestData);
    }

    @Test
    void testEqualsDifferent() {
        var attractionTypeRequestData1 = createAttractionTypeRequestData(1);
        var attractionTypeRequestData2 = createAttractionTypeRequestData(2);

        assertFalse(attractionTypeRequestData1.equals(attractionTypeRequestData2) || attractionTypeRequestData2.equals(attractionTypeRequestData1));
    }

    @Test
    void testHashCode() {
        var attractionTypeRequestData1 = createAttractionTypeRequestData(1);
        var attractionTypeRequestData2 = createAttractionTypeRequestData(1);

        assertEquals(attractionTypeRequestData1.hashCode(), attractionTypeRequestData2.hashCode());
    }

    @Test
    void testToString() {
        var attractionTypeRequestData = createAttractionTypeRequestData(1);
        String expected = "AttractionTypeRequestData(name=mock Cultural1, description=Turismo cultural, visando pontos históricos)";

        assertEquals(expected, attractionTypeRequestData.toString());
    }

    @Test
    void testViolationsNullName() {
        var attractionTypeRequestData = createAttractionTypeRequestData(1);
        attractionTypeRequestData.setName(null);
        var violations = validator.validate(attractionTypeRequestData);

        assertEquals(1, violations.size());
    }

    @Test
    void testViolationsEmptyName() {
        AttractionTypeRequestData attractionTypeRequestData = createAttractionTypeRequestData(1);
        attractionTypeRequestData.setName("");
        var violations = validator.validate(attractionTypeRequestData);

        assertEquals(1, violations.size());
    }

    @Test
    void testViolationsDescription() {
        AttractionTypeRequestData attractionTypeRequestData = createAttractionTypeRequestData(1);
        var violations = validator.validate(attractionTypeRequestData);

        assertEquals(0, violations.size());
    }
}