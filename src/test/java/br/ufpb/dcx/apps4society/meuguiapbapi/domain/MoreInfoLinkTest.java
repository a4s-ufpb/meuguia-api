package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MoreInfoLinkTestHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoreInfoLinkTest {
    private final MoreInfoLinkTestHelper moreInfoLinkTestHelper = MoreInfoLinkTestHelper.getInstance();

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
        var moreInfoLink1 = moreInfoLinkTestHelper.createMoreInfoLink(1);
        var moreInfoLink2 = moreInfoLinkTestHelper.createMoreInfoLink(1);

        assertTrue(moreInfoLink1.equals(moreInfoLink2) && moreInfoLink2.equals(moreInfoLink1));
    }

    @Test
    void testEqualsNull() {
        var moreInfoLink1 = moreInfoLinkTestHelper.createMoreInfoLink(1);
        MoreInfoLink moreInfoLink2 = null;

        assertNotEquals(moreInfoLink1, moreInfoLink2);
    }

    @Test
    void testEqualsDifferentClass() {
        var moreInfoLink1 = moreInfoLinkTestHelper.createMoreInfoLink(1);
        var moreInfoLink2 = new Object();

        assertNotEquals(moreInfoLink1, moreInfoLink2);
    }

    @Test
    void testEqualDifferentId() {
        var moreInfoLink1 = moreInfoLinkTestHelper.createMoreInfoLink(1);
        var moreInfoLink2 = moreInfoLinkTestHelper.createMoreInfoLink(2);

        assertFalse(moreInfoLink1.equals(moreInfoLink2) || moreInfoLink2.equals(moreInfoLink1));
    }

    @Test
    void testHashCode() {
        var moreInfoLink1 = moreInfoLinkTestHelper.createMoreInfoLink(1);
        var moreInfoLink2 = moreInfoLinkTestHelper.createMoreInfoLink(1);

        assertEquals(moreInfoLink1.hashCode(), moreInfoLink2.hashCode());
    }

    @Test
    void testToString() {
        var moreInfoLink = moreInfoLinkTestHelper.createMoreInfoLink(1);
        String expected = "MoreInfoLink(link=https://www.mock-link1.com, description=description)";

        assertEquals(expected, moreInfoLink.toString());
    }
}