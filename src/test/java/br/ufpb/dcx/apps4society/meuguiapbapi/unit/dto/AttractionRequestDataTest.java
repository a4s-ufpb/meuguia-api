package br.ufpb.dcx.apps4society.meuguiapbapi.unit.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTestHelper.createAttraction;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTestHelper.createAttractionRequestData;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper.createAttractionType;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper.getListOfMoreInfoLinksRequestData;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper.getListOfTourismSegmentations;
import static org.junit.jupiter.api.Assertions.*;

class AttractionRequestDataTest {
    private static final Logger log = LoggerFactory.getLogger(AttractionRequestDataTest.class);

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void builder() {
        var segmentations = getListOfTourismSegmentations().stream().map(TourismSegmentation::getId).toList();
        var attractionType = createAttractionType(1).getId();
        var moreInfoLinks = getListOfMoreInfoLinksRequestData();

        var result = AttractionRequestData.builder()
                .name("Test")
                .description("Test")
                .mapLink("https://teste.com")
                .cityId(1L)
                .imageLink("https://teste.com")
                .segmentations(segmentations)
                .moreInfoLinks(moreInfoLinks)
                .attractionType(attractionType)
                .build();

        assertNotNull(result);
        assertEquals("Test", result.getName());
        assertEquals("Test", result.getDescription());
        assertEquals("https://teste.com", result.getMapLink());
        assertEquals(1L, result.getCityId());
        assertEquals("https://teste.com", result.getImageLink());
        assertEquals(segmentations, result.getSegmentations());
        assertEquals(attractionType, result.getAttractionType());
        assertEquals(moreInfoLinks, result.getMoreInfoLinks());
    }

    @Test
    void testEquals() {
        var attractionRequestData1 = createAttraction(1);
        var attractionRequestData2 = createAttraction(1);

        assertTrue(attractionRequestData1.equals(attractionRequestData2) && attractionRequestData2.equals(attractionRequestData1));
    }

    @Test
    void testEqualsNull() {
        var attractionRequestData1 = createAttraction(1);
        assertNotEquals(null, attractionRequestData1);
    }

    @Test
    void testEqualsDifferentClass() {
        var attractionRequestData1 = createAttraction(1);

        assertNotEquals(new Object(), attractionRequestData1);
    }

    @Test
    void testEqualsDifferent() {
        var attractionRequestData1 = createAttraction(1);
        var attractionRequestData2 = createAttraction(2);

        assertFalse(attractionRequestData1.equals(attractionRequestData2) || attractionRequestData2.equals(attractionRequestData1));
    }

    @Test
    void testHashCode() {
        var attractionRequestData1 = createAttraction(1);
        var attractionRequestData2 = createAttraction(1);

        assertEquals(attractionRequestData1.hashCode(), attractionRequestData2.hashCode());
    }

    @Test
    void testToString() {
        var attractionRequestData = createAttraction(1);
        var expected = "Attraction(id=1, name=mock Teatro municipal 1, description=Teatro municipal de joão pessoa, mapLink=https://mapa.com, city=City(id=1, country=MockCountry, stateAbbreviation=MC, state=MockState, name='MockCity), imageLink=https://imagem.com, segmentations=[TourismSegmentation(id=1, name=mock Turismo de sol e mar1, description=descrição)], attractionType=AttractionType(id=1, name=mock Cultural1, description=Turismo cultural, visando pontos históricos), moreInfoLinkList=[MoreInfoLink(link=https://www.mock-link1.com, description=description)])";
        assertEquals(expected, attractionRequestData.toString());
    }

    @Test
    void testValidation() {
        AttractionRequestData attractionRequestData = createAttractionRequestData(1);
        var violations = validator.validate(attractionRequestData);
        log.debug(violations.toString());
        assertEquals(0, violations.size());
    }

    @Test
    void testValidationEmptyName() {
        AttractionRequestData attractionRequestData = createAttractionRequestData(1);
        attractionRequestData.setName("");
        var violations = validator.validate(attractionRequestData);
        log.debug(violations.toString());
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationNullName() {
        AttractionRequestData attractionRequestData = createAttractionRequestData(1);
        attractionRequestData.setName(null);
        var violations = validator.validate(attractionRequestData);
        log.debug(violations.toString());
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationNullCityId() {
        AttractionRequestData attractionRequestData = createAttractionRequestData(1);
        attractionRequestData.setCityId(null);
        var violations = validator.validate(attractionRequestData);
        log.debug(violations.toString());
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationNegativeCityId() {
        AttractionRequestData attractionRequestData = createAttractionRequestData(1);
        attractionRequestData.setCityId(-1L);
        var violations = validator.validate(attractionRequestData);
        log.debug(violations.toString());
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationEmptyImageLink() {
        AttractionRequestData attractionRequestData = createAttractionRequestData(1);
        attractionRequestData.setImageLink("");
        var violations = validator.validate(attractionRequestData);
        log.debug(violations.toString());
        assertEquals(2, violations.size());
    }

    @Test
    void testValidationNullImageLink() {
        AttractionRequestData attractionRequestData = createAttractionRequestData(1);
        attractionRequestData.setImageLink(null);
        var violations = validator.validate(attractionRequestData);
        log.debug(violations.toString());
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationPatternImageLink() {
        AttractionRequestData attractionRequestData = createAttractionRequestData(1);
        attractionRequestData.setImageLink("test");
        var violations = validator.validate(attractionRequestData);
        log.debug(violations.toString());
        assertEquals(1, violations.size());
    }
}