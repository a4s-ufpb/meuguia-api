package br.ufpb.dcx.apps4society.meuguiapbapi.unit.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import org.junit.jupiter.api.Test;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper.createAttractionType;
import static org.junit.jupiter.api.Assertions.*;

class AttractionTypeTest {

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
        AttractionType attractionType1 = createAttractionType(1);
        AttractionType attractionType2 = createAttractionType(1);

        assertTrue(attractionType1.equals(attractionType2) && attractionType2.equals(attractionType1));
    }

    @Test
    void testEqualsNull() {
        AttractionType attractionType1 = createAttractionType(1);
        Attraction attractionType2 = null;

        assertNotEquals(attractionType1, attractionType2);
    }

    @Test
    void testEqualsDifferentClass() {
        AttractionType attractionType1 = createAttractionType(1);
        var attractionType2 = new Object();

        assertNotEquals(attractionType1, attractionType2);
    }

    @Test
    void testEqualsDifferent() {
        AttractionType attractionType1 = createAttractionType(1);
        AttractionType attractionType2 = createAttractionType(2);

        assertFalse(attractionType1.equals(attractionType2) && attractionType2.equals(attractionType1));
    }

    @Test
    void testHashCode() {
        AttractionType attractionType1 = createAttractionType(1);
        AttractionType attractionType2 = createAttractionType(1);

        assertEquals(attractionType1.hashCode(), attractionType2.hashCode());
    }

    @Test
    void testToString() {
        AttractionType attractionType = createAttractionType(1);
        String expected = "AttractionType(id=1, name=mock Cultural1, description=Turismo cultural, visando pontos históricos)";
        assertEquals(expected, attractionType.toString());
    }
}