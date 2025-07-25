package br.ufpb.dcx.apps4society.meuguiapbapi.unit.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.domain.MoreInfoLink;
import org.junit.jupiter.api.Test;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper.createMoreInfoLink;
import static org.junit.jupiter.api.Assertions.*;

class MoreInfoLinkTest {

    @Test
    void builder() {
        var result = MoreInfoLink.builder()
                .link("https://www.google.com")
                .description("description")
                .build();

        assertEquals("https://www.google.com", result.getLink());
        assertEquals("description", result.getDescription());
    }

    @Test
    void testEquals() {
        var moreInfoLink1 = createMoreInfoLink(1);
        var moreInfoLink2 = createMoreInfoLink(1);

        assertTrue(moreInfoLink1.equals(moreInfoLink2) && moreInfoLink2.equals(moreInfoLink1));
    }

    @Test
    void testEqualsNull() {
        var moreInfoLink1 = createMoreInfoLink(1);
        MoreInfoLink moreInfoLink2 = null;

        assertNotEquals(moreInfoLink2, moreInfoLink1);
    }

    @Test
    void testEqualsDifferentClass() {
        var moreInfoLink1 = createMoreInfoLink(1);
        var moreInfoLink2 = new Object();

        assertNotEquals(moreInfoLink1, moreInfoLink2);
    }

    @Test
    void testEqualDifferentId() {
        var moreInfoLink1 = createMoreInfoLink(1);
        var moreInfoLink2 = createMoreInfoLink(2);

        assertFalse(moreInfoLink1.equals(moreInfoLink2) || moreInfoLink2.equals(moreInfoLink1));
    }

    @Test
    void testHashCode() {
        var moreInfoLink1 = createMoreInfoLink(1);
        var moreInfoLink2 = createMoreInfoLink(1);

        assertEquals(moreInfoLink1.hashCode(), moreInfoLink2.hashCode());
    }

    @Test
    void testToString() {
        var moreInfoLink = createMoreInfoLink(1);
        String expected = "MoreInfoLink(link=https://www.mock-link1.com, description=description)";

        assertEquals(expected, moreInfoLink.toString());
    }
}