package br.ufpb.dcx.apps4society.meuguiapbapi.dto;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.attraction.AttractionDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.attractiontype.AttractionTypeDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.moreinfolink.MoreInfoLinkDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.tourismsegmentation.TourismSegmentationDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTypeTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MoreInfoLinkTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.TourismSegmentationTestHelper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttractionDTOTest {
    private final AttractionTestHelper attractionTestHelper = AttractionTestHelper.getInstance();
    private final TourismSegmentationTestHelper tourismSegmentationTestHelper = TourismSegmentationTestHelper.getInstance();
    private final AttractionTypeTestHelper attractionTypeTestHelper = AttractionTypeTestHelper.getInstance();
    private final MoreInfoLinkTestHelper moreInfoLinkTestHelper = MoreInfoLinkTestHelper.getInstance();

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testBuilder() {
        List<TourismSegmentationDTO> segmentationsDTO = tourismSegmentationTestHelper.getListOfTourismSegmentationsDTO();
        AttractionTypeDTO attractionTypeDTO = attractionTypeTestHelper.createAttractionTypeDTO(1);
        List<MoreInfoLinkDTO> moreInfoLinksDTO = moreInfoLinkTestHelper.getListOfMoreInfoLinksDTO();

        var result = AttractionDTO.builder()
                .id(1L)
                .name("Test name")
                .description("Test description")
                .mapLink("https://www.google.com/maps")
                .city("Test city")
                .state("Test state")
                .imageLink("https://www.google.com/image")
                .segmentations(segmentationsDTO)
                .attractionTypes(attractionTypeDTO)
                .moreInfoLinks(moreInfoLinksDTO)
                .build();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test name", result.getName());
        assertEquals("Test description", result.getDescription());
        assertEquals("https://www.google.com/maps", result.getMapLink());
        assertEquals("Test city", result.getCity());
        assertEquals("Test state", result.getState());
        assertEquals("https://www.google.com/image", result.getImageLink());
        assertEquals(segmentationsDTO, result.getSegmentations());
        assertEquals(attractionTypeDTO, result.getAttractionType());
        assertEquals(moreInfoLinksDTO, result.getMoreInfoLinks());
    }

    @Test
    void testEquals() {
        var attractionDto1 = attractionTestHelper.createAttractionDTO(1);
        var attractionDto2 = attractionTestHelper.createAttractionDTO(1);

        assertTrue(attractionDto1.equals(attractionDto2) && attractionDto2.equals(attractionDto1));
    }

    @Test
    void testEqualsNulll() {
        var attractionDto1 = attractionTestHelper.createAttractionDTO(1);

        assertNotEquals(null, attractionDto1);
    }

    @Test
    void testEqualsDifferentClass() {
        var attractionDto1 = attractionTestHelper.createAttractionDTO(1);

        assertNotEquals(new Object(), attractionDto1);
    }

    @Test
    void testEqualsDifferent() {
        var attractionDto1 = attractionTestHelper.createAttractionDTO(1);
        var attractionDto2 = attractionTestHelper.createAttractionDTO(2);

        assertFalse(attractionDto1.equals(attractionDto2) || attractionDto2.equals(attractionDto1));
    }

    @Test
    void testHashCode() {
        var attractionDto1 = attractionTestHelper.createAttractionDTO(1);
        var attractionDto2 = attractionTestHelper.createAttractionDTO(1);

        assertEquals(attractionDto1.hashCode(), attractionDto2.hashCode());
    }

    @Test
    void testToString() {
        AttractionDTO attractionDto = attractionTestHelper.createAttractionDTO(1);
        String expected = "TouristAttractionDTO(id=1, name=mock Teatro municipal 1, description=Teatro municipal de joão pessoa, mapLink=https://mapa.com, city=João Pessoa, state=Paraíba (PB), imageLink=https://imagem.com, segmentations=[TourismSegmentationDTO(id=1, name=mock Turismo de sol e mar1, description=descrição)], attractionTypes=AttractionTypeDTO(id=1, name='mock Cultural1', description='Turismo cultural, visando pontos históricos'), moreInfoLinkList=[MoreInfoLinkDTO(link='https://www.mock-link1.com', description='description')])";

        assertEquals(expected, attractionDto.toString());
    }
}