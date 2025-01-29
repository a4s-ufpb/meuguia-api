package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTypeTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MoreInfoLinkTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.TourismSegmentationTestHelper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttractionRequestDataTest {
    private final AttractionTestHelper attractionTestHelper = AttractionTestHelper.getInstance();
    private final TourismSegmentationTestHelper tourismSegmentationTestHelper = TourismSegmentationTestHelper.getInstance();
    private final AttractionTypeTestHelper attractionTypeTestHelper = AttractionTypeTestHelper.getInstance();
    private final MoreInfoLinkTestHelper moreInfoLinkTestHelper = MoreInfoLinkTestHelper.getInstance();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void builder() {
        var segmentations = tourismSegmentationTestHelper.getListOfTourismSegmentations().stream().map(TourismSegmentation::getId).toList();
        var attractionType = attractionTypeTestHelper.createAttractionType(1).getId();
        var moreInfoLinks = moreInfoLinkTestHelper.getListOfMoreInfoLinks().stream().map(MoreInfoLink::getId).toList();

        var result = AttractionRequestData.builder()
                .name("Test")
                .description("Test")
                .mapLink("https://teste.com")
                .city("Test")
                .state("Test")
                .imageLink("https://teste.com")
                .infoSource("Test")
                .segmentations(segmentations)
                .attractionType(attractionType)
                .moreInfoLinks(moreInfoLinks)
                .build();

        assertNotNull(result);
        assertEquals("Test", result.getName());
        assertEquals("Test", result.getDescription());
        assertEquals("https://teste.com", result.getMapLink());
        assertEquals("Test", result.getCity());
        assertEquals("Test", result.getState());
        assertEquals("https://teste.com", result.getImageLink());
        assertEquals("Test", result.getInfoSource());
        assertEquals(segmentations, result.getSegmentations());
        assertEquals(attractionType, result.getAttractionType());
        assertEquals(moreInfoLinks, result.getMoreInfoLinks());
    }

    @Test
    void testEquals() {
        var attractionRequestData1 = attractionTestHelper.createAttraction(1);
        var attractionRequestData2 = attractionTestHelper.createAttraction(1);

        assertTrue(attractionRequestData1.equals(attractionRequestData2) && attractionRequestData2.equals(attractionRequestData1));
    }

    @Test
    void testEqualsNull() {
        var attractionRequestData1 = attractionTestHelper.createAttraction(1);
        assertNotEquals(null, attractionRequestData1);
    }

    @Test
    void testEqualsDifferentClass() {
        var attractionRequestData1 = attractionTestHelper.createAttraction(1);

        assertNotEquals(new Object(), attractionRequestData1);
    }

    @Test
    void testEqualsDifferent() {
        var attractionRequestData1 = attractionTestHelper.createAttraction(1);
        var attractionRequestData2 = attractionTestHelper.createAttraction(2);

        assertFalse(attractionRequestData1.equals(attractionRequestData2) || attractionRequestData2.equals(attractionRequestData1));
    }

    @Test
    void testHashCode() {
        var attractionRequestData1 = attractionTestHelper.createAttraction(1);
        var attractionRequestData2 = attractionTestHelper.createAttraction(1);

        assertEquals(attractionRequestData1.hashCode(), attractionRequestData2.hashCode());
    }

    @Test
    void testToString() {
        var attractionRequestData = attractionTestHelper.createAttraction(1);
        var expected = "Attraction(id=null, name=mock Teatro municipal 1, description=Teatro municipal de joão pessoa, mapLink=https://mapa.com, city=João Pessoa, state=Paraíba (PB), imageLink=https://imagem.com, infoSource=Fonte: https://fonte.com, segmentations=[TourismSegmentation(id=1, name=mock Turismo de sol e mar1, description=descrição)], attractionType=AttractionType(id=1, name=mock Cultural1, description=Turismo cultural, visando pontos históricos), moreInfoLinkList=[MoreInfoLink(id=1, link=https://www.mock-link1.com, description=description)])";
        assertEquals(expected, attractionRequestData.toString());
    }

    @Test
    void testValidation() {
        AttractionRequestData attractionRequestData = attractionTestHelper.createAttractionRequestData(1);
        var violations = validator.validate(attractionRequestData);
        assertEquals(0, violations.size());
    }

    @Test
    void testValidationEmptyName() {
        AttractionRequestData attractionRequestData = attractionTestHelper.createAttractionRequestData(1);
        attractionRequestData.setName("");
        var violations = validator.validate(attractionRequestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationNullName() {
        AttractionRequestData attractionRequestData = attractionTestHelper.createAttractionRequestData(1);
        attractionRequestData.setName(null);
        var violations = validator.validate(attractionRequestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationEmptyCity() {
        AttractionRequestData attractionRequestData = attractionTestHelper.createAttractionRequestData(1);
        attractionRequestData.setCity("");
        var violations = validator.validate(attractionRequestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationNullCity() {
        AttractionRequestData attractionRequestData = attractionTestHelper.createAttractionRequestData(1);
        attractionRequestData.setCity(null);
        var violations = validator.validate(attractionRequestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationEmptyState() {
        AttractionRequestData attractionRequestData = attractionTestHelper.createAttractionRequestData(1);
        attractionRequestData.setState("");
        var violations = validator.validate(attractionRequestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationNullState() {
        AttractionRequestData attractionRequestData = attractionTestHelper.createAttractionRequestData(1);
        attractionRequestData.setState(null);
        var violations = validator.validate(attractionRequestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationEmptyImageLink() {
        AttractionRequestData attractionRequestData = attractionTestHelper.createAttractionRequestData(1);
        attractionRequestData.setImageLink("");
        var violations = validator.validate(attractionRequestData);
        System.out.println(violations);
        assertEquals(2, violations.size());
    }

    @Test
    void testValidationNullImageLink() {
        AttractionRequestData attractionRequestData = attractionTestHelper.createAttractionRequestData(1);
        attractionRequestData.setImageLink(null);
        var violations = validator.validate(attractionRequestData);
        assertEquals(1, violations.size());
    }

    @Test
    void testValidationPatternImageLink() {
        AttractionRequestData attractionRequestData = attractionTestHelper.createAttractionRequestData(1);
        attractionRequestData.setImageLink("test");
        var violations = validator.validate(attractionRequestData);
        assertEquals(1, violations.size());
    }
}