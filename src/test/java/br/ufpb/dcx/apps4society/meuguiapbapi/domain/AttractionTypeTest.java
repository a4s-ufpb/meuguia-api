package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTypeTestHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttractionTypeTest {
    private final AttractionTypeTestHelper attractionTypeTestHelper = AttractionTypeTestHelper.getInstance();

    @Test
    void builder() {
        AttractionType result = AttractionType.builder()
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
        AttractionType attractionType1 = attractionTypeTestHelper.createAttractionType(1);
        AttractionType attractionType2 = attractionTypeTestHelper.createAttractionType(1);

        assertTrue(attractionType1.equals(attractionType2) && attractionType2.equals(attractionType1));
    }

    @Test
    void testEqualsNull() {
        AttractionType attractionType1 = attractionTypeTestHelper.createAttractionType(1);
        Attraction attractionType2 = null;

        assertNotEquals(attractionType1, attractionType2);
    }

    @Test
    void testEqualsDifferentClass() {
        AttractionType attractionType1 = attractionTypeTestHelper.createAttractionType(1);
        var attractionType2 = new Object();

        assertNotEquals(attractionType1, attractionType2);
    }

    @Test
    void testEqualsDifferent() {
        AttractionType attractionType1 = attractionTypeTestHelper.createAttractionType(1);
        AttractionType attractionType2 = attractionTypeTestHelper.createAttractionType(2);

        assertFalse(attractionType1.equals(attractionType2) && attractionType2.equals(attractionType1));
    }

    @Test
    void testHashCode() {
        AttractionType attractionType1 = attractionTypeTestHelper.createAttractionType(1);
        AttractionType attractionType2 = attractionTypeTestHelper.createAttractionType(1);

        assertEquals(attractionType1.hashCode(), attractionType2.hashCode());
    }

    @Test
    void testToString() {
        AttractionType attractionType = attractionTypeTestHelper.createAttractionType(1);
        String expected = "AttractionType(id=1, name=mock Cultural1, description=Turismo cultural, visando pontos históricos)";
        assertEquals(expected, attractionType.toString());
    }
}