package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.dto.AttractionTypeDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.dto.CityDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTestHelper.createAttractionDTO;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper.createAttractionTypeDTO;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.CityTestHelper.createDefaultCityDTO;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper.getListOfMoreInfoLinksDTO;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper.getListOfTourismSegmentationsDTO;
import static org.junit.jupiter.api.Assertions.*;

class AttractionDTOTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testBuilder() {
        List<TourismSegmentationDTO> segmentationsDTO = getListOfTourismSegmentationsDTO();
        AttractionTypeDTO attractionTypeDTO = createAttractionTypeDTO(1);
        List<MoreInfoLinkDTO> moreInfoLinksDTO = getListOfMoreInfoLinksDTO();
        CityDTO cityDTO = createDefaultCityDTO();

        var result = AttractionDTO.builder()
                .id(1L)
                .name("Test name")
                .description("Test description")
                .mapLink("https://www.google.com/maps")
                .imageLink("https://www.google.com/image")
                .city(cityDTO)
                .segmentations(segmentationsDTO)
                .attractionTypes(attractionTypeDTO)
                .moreInfoLinks(moreInfoLinksDTO)
                .build();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test name", result.getName());
        assertEquals("Test description", result.getDescription());
        assertEquals("https://www.google.com/maps", result.getMapLink());
        assertEquals("https://www.google.com/image", result.getImageLink());
        assertEquals(cityDTO, result.getCity());
        assertEquals(segmentationsDTO, result.getSegmentations());
        assertEquals(attractionTypeDTO, result.getAttractionType());
        assertEquals(moreInfoLinksDTO, result.getMoreInfoLinks());
    }

    @Test
    void testEquals() {
        var attractionDto1 = createAttractionDTO(1);
        var attractionDto2 = createAttractionDTO(1);

        assertTrue(attractionDto1.equals(attractionDto2) && attractionDto2.equals(attractionDto1));
    }

    @Test
    void testEqualsNull() {
        var attractionDto1 = createAttractionDTO(1);

        assertNotEquals(null, attractionDto1);
    }

    @Test
    void testEqualsDifferentClass() {
        var attractionDto1 = createAttractionDTO(1);

        assertNotEquals(new Object(), attractionDto1);
    }

    @Test
    void testEqualsDifferent() {
        var attractionDto1 = createAttractionDTO(1);
        var attractionDto2 = createAttractionDTO(2);

        assertFalse(attractionDto1.equals(attractionDto2) || attractionDto2.equals(attractionDto1));
    }

    @Test
    void testHashCode() {
        var attractionDto1 = createAttractionDTO(1);
        var attractionDto2 = createAttractionDTO(1);

        assertEquals(attractionDto1.hashCode(), attractionDto2.hashCode());
    }

    @Test
    void testToString() {
        AttractionDTO attractionDto = createAttractionDTO(1);
        String expected = "TouristAttractionDTO(id=1, name=mock Teatro municipal 1, description=Teatro municipal de joão pessoa, mapLink=https://mapa.com, city=CityDTO(id=1, name=MockCity, state=MockState, country=MockCountry, stateAbbreviation=MC), imageLink=https://imagem.com, segmentations=[TourismSegmentationDTO(id=1, name=mock Turismo de sol e mar1, description=descrição)], attractionTypes=AttractionTypeDTO(id=1, name='mock Cultural1', description='Turismo cultural, visando pontos históricos'), moreInfoLinkList=[MoreInfoLinkDTO(link='https://www.mock-link1.com', description='description')])";

        assertEquals(expected, attractionDto.toString());
    }

    // TODO: Test Validation
}