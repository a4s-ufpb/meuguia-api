package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTypeTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MoreInfoLinkTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.TouristSegmentationTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.AttractionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;


class AttractionServiceTest {
    private final AttractionTestHelper attractionTestHelper = AttractionTestHelper.getInstance();
    private final TouristSegmentationTestHelper touristSegmentationTestHelper = TouristSegmentationTestHelper.getInstance();
    private final AttractionTypeTestHelper attractionTypeTestHelper = AttractionTypeTestHelper.getInstance();
    private final MoreInfoLinkTestHelper moreInfoLinkTestHelper = MoreInfoLinkTestHelper.getInstance();

    @Mock
    private AttractionRepository attractionRepository;
    @Mock
    private TourismSegmentationService tourismSegmentationService;
    @Mock
    private AttractionTypeService attractionTypeService;
    @Mock
    private MoreInfoLinkService moreInfoLinkService;

    @InjectMocks
    private AttractionService attractionService;

    private TourismSegmentation segmentation;
    private AttractionType attractionType;
    private MoreInfoLink moreInfoLink;

    @BeforeEach
    void setUp() {
        openMocks(this);

        segmentation = touristSegmentationTestHelper.createTourismSegmentation(1);
        attractionType = attractionTypeTestHelper.createAttractionType(1);
        moreInfoLink = moreInfoLinkTestHelper.createMoreInfoLink(1);
    }

    @Test
    void createAttraction() {
        AttractionRequestData requestData = attractionTestHelper.createAttractionRequestData(1, segmentation, moreInfoLink, attractionType);
        when(attractionRepository.save(any(Attraction.class))).thenAnswer(invocationOnMock -> {
            Attraction attraction = invocationOnMock.getArgument(0);
            attraction.setId(1L);
            return attraction;
        });
        when(attractionTypeService.existsById(anyLong())).thenReturn(true);
        when(tourismSegmentationService.existsById(anyLong())).thenReturn(true);
        when(moreInfoLinkService.existsById(anyLong())).thenReturn(true);

        Attraction result = attractionService.create(requestData);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(requestData.getName(), result.getName());
        assertEquals(requestData.getDescription(), result.getDescription());
    }

    @Test
    void createAttraction_InvalidAttractionType() {
        AttractionRequestData requestData = attractionTestHelper.createAttractionRequestData(1, segmentation, moreInfoLink, attractionType);
        when(attractionTypeService.existsById(anyLong())).thenReturn(false);
        when(tourismSegmentationService.existsById(anyLong())).thenReturn(true);
        when(moreInfoLinkService.existsById(anyLong())).thenReturn(true);

        Exception thrown = assertThrows(ObjectNotFoundException.class, () -> attractionService.create(requestData));

        assertEquals("Tipo de atração não encontrado! Id: 1",
                thrown.getMessage());
    }

    @Test
    void createAttraction_InvalidTourismSegmentation() {
        AttractionRequestData requestData = attractionTestHelper.createAttractionRequestData(1, segmentation, moreInfoLink, attractionType);
        when(attractionTypeService.existsById(anyLong())).thenReturn(true);
        when(tourismSegmentationService.existsById(anyLong())).thenReturn(false);
        when(moreInfoLinkService.existsById(anyLong())).thenReturn(true);

        Exception thrown = assertThrows(ObjectNotFoundException.class, () -> attractionService.create(requestData));

        assertEquals("Segmentação não encontrada! Id: 1",
                thrown.getMessage());
    }

    @Test
    void createAttraction_InvalidMoreInfoLink() {
        AttractionRequestData requestData = attractionTestHelper.createAttractionRequestData(1, segmentation, moreInfoLink, attractionType);
        when(attractionTypeService.existsById(anyLong())).thenReturn(true);
        when(tourismSegmentationService.existsById(anyLong())).thenReturn(true);
        when(moreInfoLinkService.existsById(anyLong())).thenReturn(false);

        Exception thrown = assertThrows(ObjectNotFoundException.class, () -> attractionService.create(requestData));

        assertEquals("Link de mais informações não encontrado! Id: 1",
                thrown.getMessage());
    }

    @Test
    void deleteAttractionTest() {
        Attraction attraction = attractionTestHelper.createAttraction(1, segmentation, moreInfoLink, attractionType);
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
        Attraction attraction = attractionTestHelper.createAttraction(1, segmentation, moreInfoLink, attractionType);
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
        when(attractionRepository.findAll()).thenReturn(attractionTestHelper.createAttractionList());

        assertEquals(3, attractionService.findAll().size());
    }

    @Test
    void findAllAttractions_AttractionNotExistTest() {
        when(attractionRepository.findAll()).thenReturn(List.of());

        assertEquals(0, attractionService.findAll().size());
    }

    @Test
    void updateAttractionTest() {
        Attraction attraction = attractionTestHelper.createAttraction(1, segmentation, moreInfoLink, attractionType);
        AttractionRequestData requestData = attractionTestHelper.createAttractionRequestData(1, segmentation, moreInfoLink, attractionType);
        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));
        when(attractionRepository.save(any(Attraction.class))).thenAnswer(invocationOnMock -> {
            Attraction att = invocationOnMock.getArgument(0);
            att.setId(1L);
            return att;
        });
        when(attractionTypeService.existsById(anyLong())).thenReturn(true);
        when(tourismSegmentationService.existsById(anyLong())).thenReturn(true);
        when(moreInfoLinkService.existsById(anyLong())).thenReturn(true);

        Attraction result = attractionService.update(1L, requestData);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(requestData.getName(), result.getName());
        assertEquals(requestData.getDescription(), result.getDescription());
    }

    @Test
    void updateAttraction_AttractionNotExistTest() {
        AttractionRequestData requestData = attractionTestHelper.createAttractionRequestData(1, segmentation, moreInfoLink, attractionType);
        when(attractionRepository.findById(1L)).thenReturn(Optional.empty());

        Exception thrown = assertThrows(ObjectNotFoundException.class, () -> attractionService.update(1L, requestData));

        assertEquals("Tipo de atração não encontrado! Id: 1", thrown.getMessage());
    }

    @Test
    void findAttractionByNameTest() {
        Attraction attraction = attractionTestHelper.createAttraction(1, segmentation, moreInfoLink, attractionType);
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
        Attraction attraction = attractionTestHelper.createAttraction(1, segmentation, moreInfoLink, attractionType);
        when(attractionRepository.findAllByCity("João Pessoa")).thenReturn(List.of(attraction));

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
        Attraction attraction = attractionTestHelper.createAttraction(1, segmentation, moreInfoLink, attractionType);
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
        Attraction attraction = attractionTestHelper.createAttraction(1, segmentation, moreInfoLink, attractionType);
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