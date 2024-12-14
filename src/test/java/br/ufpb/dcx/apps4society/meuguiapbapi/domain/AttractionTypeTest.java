package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTypeTestHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttractionTypeTest {
    private final AttractionTypeTestHelper attractionTypeTestHelper = AttractionTypeTestHelper.getInstance();

    @Test
    void builder() {
        var result = AttractionType.builder()
                .id(1L)
                .name("Test")
                .description("Test")
                .build();

        assertEquals(result.getId(), 1L);
        assertEquals(result.getName(), "Test");
        assertEquals(result.getDescription(), "Test");
    }

    @Test
    void testEquals() {
        var attractionType1 = attractionTypeTestHelper.createAttractionType(1);
        var attractionType2 = attractionTypeTestHelper.createAttractionType(1);

        assertTrue(attractionType1.equals(attractionType2) && attractionType2.equals(attractionType1));
    }

    @Test
    void testEqualsNull() {
        var attractionType1 = attractionTypeTestHelper.createAttractionType(1);
        Attraction attractionType2 = null;

        assertNotEquals(attractionType1, attractionType2);
    }

    @Test
    void testEqualsDifferentClass() {
        var attractionType1 = attractionTypeTestHelper.createAttractionType(1);
        var attractionType2 = new Object();

        assertNotEquals(attractionType1, attractionType2);
    }

    @Test
    void testEqualsDifferent() {
        var attractionType1 = attractionTypeTestHelper.createAttractionType(1);
        var attractionType2 = attractionTypeTestHelper.createAttractionType(2);

        assertFalse(attractionType1.equals(attractionType2) && attractionType2.equals(attractionType1));
    }

    @Test
    void testHashCode() {
        var attractionType1 = attractionTypeTestHelper.createAttractionType(1);
        var attractionType2 = attractionTypeTestHelper.createAttractionType(1);

        assertEquals(attractionType1.hashCode(), attractionType2.hashCode());
    }

    @Test
    void testToString() {
        var attractionType = attractionTypeTestHelper.createAttractionType(1);
        String expected = "AttractionType(id=1, name=mock Cultural1, description=Turismo cultural, visando pontos históricos)";
        assertEquals(expected, attractionType.toString());
    }
}