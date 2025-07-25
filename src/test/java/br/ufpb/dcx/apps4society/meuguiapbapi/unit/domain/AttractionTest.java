package br.ufpb.dcx.apps4society.meuguiapbapi.unit.domain;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTestHelper.createAttraction;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper.createAttractionType;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.CityTestHelper.createDefaultCityObject;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper.getListOfMoreInfoLinks;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper.getListOfTourismSegmentations;
import static org.junit.jupiter.api.Assertions.*;

class AttractionTest {

    @Test
    void testEquals() {
        Attraction attraction1 = createAttraction(1);
        Attraction attraction2 = createAttraction(1);

        assertTrue(attraction1.equals(attraction2) && attraction2.equals(attraction1));
    }

    @Test
    void testEqualsNull() {
        Attraction attraction1 = createAttraction(1);
        Attraction attraction2 = null;

        assertNotEquals(attraction2, attraction1);
    }

    @Test
    void testEqualsDifferentClass() {
        Attraction attraction1 = createAttraction(1);
        Object attraction2 = new Object();

        assertFalse(attraction1.equals(attraction2) || attraction2.equals(attraction1));
    }

    @Test
    void testEqualsDifferent() {
        Attraction attraction1 = createAttraction(1);
        Attraction attraction2 = createAttraction(2);

        assertFalse(attraction1.equals(attraction2) || attraction2.equals(attraction1));
    }

    @Test
    void testBuilder() {
        List<TourismSegmentation> segmentations = getListOfTourismSegmentations();
        AttractionType attractionType = createAttractionType(1);
        var moreInfoLinks = getListOfMoreInfoLinks();
        var city = createDefaultCityObject();

        var result = Attraction.builder()
                .id(1L)
                .name("Test")
                .description("Test")
                .mapLink("https://teste.com")
                .imageLink("https://teste.com/test.jpg")
                .city(city)
                .segmentations(segmentations)
                .attractionType(attractionType)
                .moreInfoLinks(moreInfoLinks)
                .build();

        assertEquals(1L, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("Test", result.getDescription());
        assertEquals("https://teste.com", result.getMapLink());
        assertEquals("https://teste.com/test.jpg", result.getImageLink());
        assertEquals(city.getName(), result.getCity().getName());
        assertEquals(segmentations, result.getSegmentations());
        assertEquals(attractionType, result.getAttractionType());
        assertEquals(moreInfoLinks, result.getMoreInfoLinks());
    }

    @Test
    void testHashCode() {
        Attraction attraction1 = createAttraction(1);
        Attraction attraction2 = createAttraction(1);

        assertEquals(attraction1.hashCode(), attraction2.hashCode());
    }

    @Test
    void testToString() {
        Attraction attraction = createAttraction(1);
        String expected = "Attraction(id=1, name=mock Teatro municipal 1, description=Teatro municipal de joão pessoa, mapLink=https://mapa.com, city=City(id=1, country=MockCountry, stateAbbreviation=MC, state=MockState, name='MockCity), imageLink=https://imagem.com, segmentations=[TourismSegmentation(id=1, name=mock Turismo de sol e mar1, description=descrição)], attractionType=AttractionType(id=1, name=mock Cultural1, description=Turismo cultural, visando pontos históricos), moreInfoLinkList=[MoreInfoLink(link=https://www.mock-link1.com, description=description)])";
        assertEquals(expected, attraction.toString());
    }
}