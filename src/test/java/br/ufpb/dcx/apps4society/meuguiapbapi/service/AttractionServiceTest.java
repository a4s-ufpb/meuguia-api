package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.repository.AttractionRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.service.AttractionService;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.repository.AttractionTypeService;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.service.CityService;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.service.TourismSegmentationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTestHelper.*;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper.createAttractionType;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.CityTestHelper.createDefaultCityObject;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper.createMoreInfoLink;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper.createMoreInfoLinkRequestData;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper.createTourismSegmentation;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;


// TODO: Verificar questão do MoreInfoLink
class AttractionServiceTest {

    @Mock
    private AttractionRepository attractionRepository;
    @Mock
    private TourismSegmentationService tourismSegmentationService;
    @Mock
    private AttractionTypeService attractionTypeService;
    @Mock
    private CityService cityService;

    @InjectMocks
    private AttractionService attractionService;

    private TourismSegmentation segmentation;
    private AttractionType attractionType;
    private MoreInfoLink moreInfoLink;
    private MoreInfoLinkRequestData moreInfoLinkRequestData;

    @BeforeEach
    void setUp() {
        openMocks(this);

        segmentation = createTourismSegmentation(1);
        attractionType = createAttractionType(1);
        moreInfoLink = createMoreInfoLink(1);
        moreInfoLinkRequestData = createMoreInfoLinkRequestData(1);
    }

    @Test
    void createAttractionTest() {
        AttractionRequestData requestData = createAttractionRequestData(1, segmentation, moreInfoLinkRequestData, attractionType);
        when(attractionRepository.save(any(Attraction.class))).thenAnswer(invocationOnMock -> {
            Attraction attraction = invocationOnMock.getArgument(0);
            attraction.setId(1L);
            return attraction;
        });
        when(attractionTypeService.existsById(anyLong())).thenReturn(true);
        when(tourismSegmentationService.existsById(anyLong())).thenReturn(true);
        when(cityService.existsById(1L)).thenReturn(true);

        Attraction result = attractionService.create(requestData);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(requestData.getName(), result.getName());
        assertEquals(requestData.getDescription(), result.getDescription());
    }

    @Test
    void createAttraction_InvalidAttractionTypeTest() {
        AttractionRequestData requestData = createAttractionRequestData(1, segmentation, moreInfoLinkRequestData, attractionType);
        when(attractionTypeService.existsById(anyLong())).thenReturn(false);
        when(tourismSegmentationService.existsById(anyLong())).thenReturn(true);

        Exception thrown = assertThrows(ObjectNotFoundException.class, () -> attractionService.create(requestData));

        assertEquals("Tipo de atração não encontrado! Id: 1",
                thrown.getMessage());
    }

    @Test
    void createAttraction_InvalidTourismSegmentationTest() {
        AttractionRequestData requestData = createAttractionRequestData(1, segmentation, moreInfoLinkRequestData, attractionType);
        when(attractionTypeService.existsById(anyLong())).thenReturn(true);
        when(cityService.existsById(1L)).thenReturn(true);
        when(tourismSegmentationService.existsById(anyLong())).thenReturn(false);

        Exception thrown = assertThrows(ObjectNotFoundException.class, () -> attractionService.create(requestData));

        assertEquals("Segmentação não encontrada! Id: 1",
                thrown.getMessage());
    }

    @Test
    void createAttraction_MoreInfoLinkDoesNotExist() {
        AttractionRequestData requestData = createAttractionRequestData(1, segmentation, moreInfoLinkRequestData, attractionType);
        when(attractionTypeService.existsById(anyLong())).thenReturn(true);
        when(tourismSegmentationService.existsById(anyLong())).thenReturn(true);
        when(cityService.existsById(1L)).thenReturn(true);
        when(cityService.findById(1L)).thenReturn(createDefaultCityObject());
        when(attractionRepository.save(any(Attraction.class))).thenAnswer(invocationOnMock -> {
            Attraction attraction = invocationOnMock.getArgument(0);
            attraction.setId(1L);
            return attraction;
        });

        var result = attractionService.create(requestData);

        assertAttractionEqualsToRequestData(result, requestData);
    }

    @Test
    void deleteAttractionTest() {
        Attraction attraction = createAttraction(1, segmentation, moreInfoLink, attractionType);
        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));

        attractionService.delete(1L);

        verify(attractionRepository).deleteById(1L);
    }

    @Test
    void deleteAttraction_AttractionNotExistTest() {
        when(attractionRepository.existsById(1L)).thenReturn(false);

        Exception thrown = assertThrows(ObjectNotFoundException.class, () -> attractionService.delete(1L));

        assertEquals("Objeto não encontrado! Id: 1, Tipo: " + Attraction.class.getName(), thrown.getMessage());
    }

    @Test
    void findAttractionByIdTest() {
        Attraction attraction = createAttraction(1, segmentation, moreInfoLink, attractionType);
        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));

        Attraction result = attractionService.findById(1L);

        assertNotNull(result);
        assertEquals(attraction, result);
    }

    @Test
    void findAttractionById_AttractionNotExistTest() {
        when(attractionRepository.findById(1L)).thenReturn(Optional.empty());

        Exception thrown = assertThrows(ObjectNotFoundException.class, () -> attractionService.findById(1L));

        assertEquals("Objeto não encontrado! Id: 1, Tipo: " + Attraction.class.getName(), thrown.getMessage());
    }

    @Test
    void findAllAttractionsTest() {
        when(attractionRepository.findAll()).thenReturn(createAttractionList());

        assertEquals(3, attractionService.findAll().size());
    }

    @Test
    void findAllAttractions_AttractionNotExistTest() {
        when(attractionRepository.findAll()).thenReturn(List.of());

        assertEquals(0, attractionService.findAll().size());
    }

    @Test
    void updateAttractionTest() {
        Attraction attraction = createAttraction(1, segmentation, moreInfoLink, attractionType);
        AttractionRequestData requestData = createAttractionRequestData(1, segmentation, moreInfoLinkRequestData, attractionType);
        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));
        when(attractionRepository.save(any(Attraction.class))).thenAnswer(invocationOnMock -> {
            Attraction att = invocationOnMock.getArgument(0);
            att.setId(1L);
            return att;
        });
        when(attractionTypeService.existsById(anyLong())).thenReturn(true);
        when(tourismSegmentationService.existsById(anyLong())).thenReturn(true);
        when(cityService.existsById(1L)).thenReturn(true);

        Attraction result = attractionService.update(1L, requestData);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(requestData.getName(), result.getName());
        assertEquals(requestData.getDescription(), result.getDescription());
    }

    @Test
    void updateAttraction_AttractionNotExistTest() {
        AttractionRequestData requestData = createAttractionRequestData(1, segmentation, moreInfoLinkRequestData, attractionType);
        when(attractionRepository.findById(1L)).thenReturn(Optional.empty());

        Exception thrown = assertThrows(ObjectNotFoundException.class, () -> attractionService.update(1L, requestData));

        assertEquals("Tipo de atração não encontrado! Id: 1", thrown.getMessage());
    }

    @Test
    void findAttractionByNameTest() {
        Attraction attraction = createAttraction(1, segmentation, moreInfoLink, attractionType);
        when(attractionRepository.findByNameContainingIgnoreCase("mock Teatro municipal 1")).thenReturn(List.of(attraction));

        List<Attraction> result = attractionService.findByName("mock Teatro municipal 1");

        assertNotNull(result);
        assertEquals(attraction, result.getFirst());
    }

    @Test
    void findAttractionByName_AttractionNotExistTest() {
        when(attractionRepository.findByNameContainingIgnoreCase("mock Teatro municipal 1")).thenReturn(List.of());

        assertEquals(0, attractionService.findByName("mock Teatro municipal 1").size());
    }

    @Test
    void findAttractionByCityTest() {
        Attraction attraction = createAttraction(1, segmentation, moreInfoLink, attractionType);
        when(attractionRepository.findAllByCityName("João Pessoa")).thenReturn(List.of(attraction));

        List<Attraction> result = attractionService.findByCity("João Pessoa");

        assertNotNull(result);
        assertEquals(attraction, result.getFirst());
    }

    @Test
    void findAttractionByCity_AttractionNotExistTest() {
        when(attractionRepository.findAllByCity("João Pessoa")).thenReturn(List.of());

        assertEquals(0, attractionService.findByCity("João Pessoa").size());
    }

    @Test
    void findAttractionBySegmentationTest() {
        Attraction attraction = createAttraction(1, segmentation, moreInfoLink, attractionType);
        when(tourismSegmentationService.existsByName("mock Turismo de sol e mar1")).thenReturn(true);
        when(attractionRepository.findAllBySegmentationName("mock Turismo de sol e mar1")).thenReturn(List.of(attraction));

        List<Attraction> result = attractionService.findBySegmentation("mock Turismo de sol e mar1");

        assertNotNull(result);
        assertEquals(attraction, result.getFirst());
    }

    @Test
    void findAttractionBySegmentation_SegmentationNotExistsTest() {
        when(tourismSegmentationService.existsByName("mock Turismo de sol e mar1")).thenReturn(false);

        Exception thrown = assertThrows(ObjectNotFoundException.class,
                () -> attractionService.findBySegmentation("mock Turismo de sol e mar1"));

        assertEquals("Segmentação com nome 'mock Turismo de sol e mar1' não encontrada!", thrown.getMessage());
    }

    @Test
    void findAttractionBySegmentation_AttractionNotExistTest() {
        when(tourismSegmentationService.existsByName("mock Turismo de sol e mar1")).thenReturn(true);
        when(attractionRepository.findAllBySegmentationName("mock Turismo de sol e mar1")).thenReturn(List.of());

        assertEquals(0, attractionService.findBySegmentation("mock Turismo de sol e mar1").size());
    }

    @Test
    void findAttractionByTypeTest() {
        Attraction attraction = createAttraction(1, segmentation, moreInfoLink, attractionType);
        when(attractionRepository.findAllByType("mock Cultural1")).thenReturn(List.of(attraction));

        List<Attraction> result = attractionService.findByType("mock Cultural1");

        assertNotNull(result);
        assertEquals(attraction, result.getFirst());
    }

    @Test
    void findAttractionByType_AttractionNotExistTest() {
        when(attractionRepository.findAllByType("mock Cultural1")).thenReturn(List.of());

        assertEquals(0, attractionService.findByType("mock Cultural1").size());
    }
}