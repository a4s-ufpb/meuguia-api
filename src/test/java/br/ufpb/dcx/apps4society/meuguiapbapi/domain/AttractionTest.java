package br.ufpb.dcx.apps4society.meuguiapbapi.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTypeTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MoreInfoLinkTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.TourismSegmentationTestHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttractionTest {
    private final AttractionTestHelper attractionTestHelper = AttractionTestHelper.getInstance();
    private final AttractionTypeTestHelper attractionTypeTestHelper = AttractionTypeTestHelper.getInstance();
    private final TourismSegmentationTestHelper tourismSegmentationTestHelper = TourismSegmentationTestHelper.getInstance();
    private final MoreInfoLinkTestHelper moreInfoLinkTestHelper = MoreInfoLinkTestHelper.getInstance();

    @Test
    void testEquals() {
        Attraction attraction1 = attractionTestHelper.createAttraction(1);
        Attraction attraction2 = attractionTestHelper.createAttraction(1);

        assertTrue(attraction1.equals(attraction2) && attraction2.equals(attraction1));
    }

    @Test
    void testEqualsNull() {
        Attraction attraction1 = attractionTestHelper.createAttraction(1);
        Attraction attraction2 = null;

        assertNotEquals(attraction1, attraction2);
    }

    @Test
    void testEqualsDifferentClass() {
        Attraction attraction1 = attractionTestHelper.createAttraction(1);
        Object attraction2 = new Object();

        assertFalse(attraction1.equals(attraction2) || attraction2.equals(attraction1));
    }

    @Test
    void testEqualsDifferent() {
        Attraction attraction1 = attractionTestHelper.createAttraction(1);
        Attraction attraction2 = attractionTestHelper.createAttraction(2);

        assertFalse(attraction1.equals(attraction2) || attraction2.equals(attraction1));
    }

    @Test
    void testBuilder() {
        var segmentations = tourismSegmentationTestHelper.getListOfTourismSegmentations();
        var attractionType = attractionTypeTestHelper.createAttractionType(1);
        var moreInfoLinks = moreInfoLinkTestHelper.getListOfMoreInfoLinks();
        var result = Attraction.builder()
                .id(1L)
                .name("Test")
                .description("Test")
                .mapLink("https://teste.com")
                .city("Test")
                .state("Test")
                .imageLink("https://teste.com/test.jpg")
                .infoSource("Test")
                .segmentations(segmentations)
                .attractionType(attractionType)
                .moreInfoLinkList(moreInfoLinks)
                .build();

        assertEquals(1L, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("Test", result.getDescription());
        assertEquals("https://teste.com", result.getMapLink());
        assertEquals("Test", result.getCity());
        assertEquals("Test", result.getState());
        assertEquals("https://teste.com/test.jpg", result.getImageLink());
        assertEquals("Test", result.getInfoSource());
        assertEquals(segmentations, result.getSegmentations());
        assertEquals(attractionType, result.getAttractionType());
        assertEquals(moreInfoLinks, result.getMoreInfoLinkList());
    }

    @Test
    void testHashCode() {
        Attraction attraction1 = attractionTestHelper.createAttraction(1);
        Attraction attraction2 = attractionTestHelper.createAttraction(1);

        assertEquals(attraction1.hashCode(), attraction2.hashCode());
    }

    @Test
    void testToString() {
        Attraction attraction = attractionTestHelper.createAttraction(1);
        String expected = "Attraction(id=null, name=mock Teatro municipal 1, description=Teatro municipal de joão pessoa, mapLink=https://mapa.com, city=João Pessoa, state=Paraíba (PB), imageLink=https://imagem.com, infoSource=Fonte: https://fonte.com, segmentations=[TourismSegmentation(id=1, name=mock Turismo de sol e mar1, description=descrição)], attractionType=AttractionType(id=1, name=mock Cultural1, description=Turismo cultural, visando pontos históricos), moreInfoLinkList=[MoreInfoLink(id=1, link=https://www.mock-link1.com, description=description)])";
        assertEquals(expected, attraction.toString());
    }
}