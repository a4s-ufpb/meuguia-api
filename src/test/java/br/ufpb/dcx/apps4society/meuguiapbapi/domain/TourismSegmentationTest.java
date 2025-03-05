package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.mock.TourismSegmentationTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourismSegmentationTest {
    private final TourismSegmentationTestHelper helper = TourismSegmentationTestHelper.getInstance();

    @Test
    void builder() {
        var result = TourismSegmentation.builder()
                .id(1L)
                .name("Test")
                .description("Test")
                .build();

        assertEquals(1L, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("Test", result.getDescription());
    }

    @Test
    void testEquals() {
        var segmentation1 = helper.createTourismSegmentation(1);
        var segmentation2 = helper.createTourismSegmentation(1);

        assertTrue(segmentation1.equals(segmentation2) && segmentation2.equals(segmentation1));
    }

    @Test
    void testEqualsNulll() {
        var segmentation = helper.createTourismSegmentation(1);

        assertNotEquals(null, segmentation);
    }

    @Test
    void testEqualsDifferentClass() {
        var segmentation = helper.createTourismSegmentation(1);

        assertNotEquals(segmentation, new Object());
    }

    @Test
    void testDifferent() {
        var segmentation1 = helper.createTourismSegmentation(1);
        var segmentation2 = helper.createTourismSegmentation(2);

        assertFalse(segmentation1.equals(segmentation2) || segmentation2.equals(segmentation1));
    }

    @Test
    void testHashCode() {
        var segmentation1 = helper.createTourismSegmentation(1);
        var segmentation2 = helper.createTourismSegmentation(1);

        assertEquals(segmentation1.hashCode(), segmentation2.hashCode());
    }

    @Test
    void testToString() {
        var segmentation = helper.createTourismSegmentation(1);
        String expected = "TourismSegmentation(id=1, name=mock Turismo de sol e mar1, description=descrição)";

        assertEquals(expected, segmentation.toString());
    }
}