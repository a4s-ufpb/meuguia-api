package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.repository.TourismSegmentationRepository;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.service.TourismSegmentationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.PaginationHelper.*;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class TourismSegmentationServiceTest {

    @Mock
    private TourismSegmentationRepository tourismSegmentationRepository;

    @InjectMocks
    private TourismSegmentationService tourismSegmentationService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void findTourismSegmentationByIdTest() {
        TourismSegmentation tourismSegmentation = createTourismSegmentation(1);
        when(tourismSegmentationRepository.findById(1L)).thenReturn(Optional.of(tourismSegmentation));

        TourismSegmentation result = tourismSegmentationService.findById(1L);

        verify(tourismSegmentationRepository).findById(1L);
        assertNotNull(result);
        assertEquals(tourismSegmentation.getId(), result.getId());
        assertEquals(tourismSegmentation.getName(), result.getName());
        assertEquals(tourismSegmentation.getDescription(), result.getDescription());
    }

    @Test
    void findTourismSegmentationByIdNotExistTest() {
        when(tourismSegmentationRepository.findById(1L)).thenReturn(Optional.empty());

        var thrown = assertThrows(ObjectNotFoundException.class, () -> {
            tourismSegmentationService.findById(1L);
        });

        assertEquals("Objeto não encontrado! id: 1, Tipo br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation", thrown.getMessage());
    }

    @Test
    void findAllTourismSegmentationTest() {
        List<TourismSegmentation> segmentations = getListOfTourismSegmentations();
        when(tourismSegmentationRepository.findAllDistinct(any(Pageable.class))).thenAnswer(invocation -> createPageWithContent(segmentations, invocation.getArgument(0, Pageable.class)));

        Page<TourismSegmentation> result = tourismSegmentationService.findAll(createDefaultPageable());

        verify(tourismSegmentationRepository).findAllDistinct(any(Pageable.class));
        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals(segmentations.size(), result.getContent().size());
    }

    @Test
    void findAllTourismSegmentation_EmptyTest() {
        when(tourismSegmentationRepository.findAllDistinct(any(Pageable.class))).thenAnswer(invocation -> createEmptyPage(invocation.getArgument(0, Pageable.class)));

        Page<TourismSegmentation> result = tourismSegmentationService.findAll(createDefaultPageable());

        verify(tourismSegmentationRepository).findAllDistinct(any(Pageable.class));
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void createTourismSegmentationTest() {
        TourismSegmentationRequestData requestData = createTourismSegmentationRequestData(1);
        when(tourismSegmentationRepository.save(any(TourismSegmentation.class))).thenAnswer(invocationOnMock -> {
            TourismSegmentation tourismSegmentation = invocationOnMock.getArgument(0);
            tourismSegmentation.setId(1L);
            return tourismSegmentation;
        });

        TourismSegmentation result = tourismSegmentationService.create(requestData);

        verify(tourismSegmentationRepository).save(any(TourismSegmentation.class));
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(requestData.getName(), result.getName());
        assertEquals(requestData.getDescription(), result.getDescription());
    }

    @Test
    void deleteTourismSegmentationTest() {
        when(tourismSegmentationRepository.findById(1L)).thenReturn(Optional.of(createTourismSegmentation(1)));

        tourismSegmentationService.delete(1L);

        verify(tourismSegmentationRepository).deleteById(1L);
    }

    @Test
    void deleteTourismSegmentation_NotExistTest() {
        when(tourismSegmentationRepository.findById(1L)).thenReturn(Optional.empty());

        var thrown = assertThrows(ObjectNotFoundException.class, () -> {
            tourismSegmentationService.delete(1L);
        });

        assertEquals("Objeto não encontrado! id: 1, Tipo br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation", thrown.getMessage());
    }

    @Test
    void existsTourismSegmentationByIdTest() {
        when(tourismSegmentationRepository.existsById(1L)).thenReturn(true);

        boolean result = tourismSegmentationService.existsById(1L);

        assertTrue(result);
    }

    @Test
    void existsTourismSegmentationById_NotExistTest() {
        when(tourismSegmentationRepository.existsById(1L)).thenReturn(false);

        boolean result = tourismSegmentationService.existsById(1L);

        assertFalse(result);
    }

    @Test
    void existsByNameTourismSegmentationTest() {
        when(tourismSegmentationRepository.existsByName("praias")).thenReturn(true);

        boolean result = tourismSegmentationService.existsByName("praias");

        assertTrue(result);
    }

    @Test
    void existsByNameTourismSegmentation_NotExistTest() {
        when(tourismSegmentationRepository.existsByName("praias")).thenReturn(false);

        boolean result = tourismSegmentationService.existsByName("praias");

        assertFalse(result);
    }
}