package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionTypeRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTypeTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.AttractionTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AttractionTypeServiceTest {
    private final AttractionTypeTestHelper attractionTypeTestHelper = AttractionTypeTestHelper.getInstance();

    @Mock
    private AttractionTypeRepository attractionTypeRepository;

    @InjectMocks
    private AttractionTypeService attractionTypeService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void createAttractionTypeTest() {
        AttractionTypeRequestData requestData = attractionTypeTestHelper.createAttractionTypeRequestData(1);
        when(attractionTypeRepository.save(any(AttractionType.class))).thenAnswer(invocationOnMock -> {
            AttractionType attractionType = invocationOnMock.getArgument(0);
            attractionType.setId(1L);
            return attractionType;
        });

        AttractionType result = attractionTypeService.create(requestData);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(requestData.getName(), result.getName());
        assertEquals(requestData.getDescription(), result.getDescription());
    }

    @Test
    void findAttractionTypeByIdTest() {
        AttractionType attractionType = attractionTypeTestHelper.createAttractionType(1);
        when(attractionTypeRepository.findById(anyLong())).thenReturn(Optional.of(attractionType));

        AttractionType result = attractionTypeService.findById(1L);

        verify(attractionTypeRepository).findById(anyLong());
        assertNotNull(result);
        assertEquals(attractionType, result);
    }

    @Test
    void findAttractionById_NotExistsTest() {
        when(attractionTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception thrown = assertThrows(ObjectNotFoundException.class,
                () -> attractionTypeService.findById(1L));

        assertEquals("Objeto não encontrado! Id: 1, Tipo: br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType", thrown.getMessage());
    }

    @Test
    void findAllAttractionTypesTest() {
        when(attractionTypeRepository.findAll()).thenReturn(attractionTypeTestHelper.createAttractionTypeList());

        List<AttractionType> result = attractionTypeService.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void findAllAttractionTypes_EmptyDatabaseTest() {
        when(attractionTypeRepository.findAll()).thenReturn(List.of());

        List<AttractionType> result = attractionTypeService.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void deleteAttraction() {
        when(attractionTypeRepository.findById(1L)).thenReturn(Optional.of(attractionTypeTestHelper.createAttractionType(1)));

        attractionTypeService.delete(1L);

        verify(attractionTypeRepository).deleteById(1L);
    }

    @Test
    void deleteAttraction_AttractionNotExists() {
        when(attractionTypeRepository.findById(1L)).thenReturn(Optional.empty());

        Exception thrown = assertThrows(ObjectNotFoundException.class,
                () -> attractionTypeService.delete(1L));

        assertEquals(thrown.getMessage(), "Objeto não encontrado! Id: 1, Tipo: br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType");
    }

    @Test
    void existsAttractionTypeByIdTest() {
        when(attractionTypeRepository.existsById(1L)).thenReturn(true);

        boolean result = attractionTypeService.existsById(1L);

        assertTrue(result);
    }

    @Test
    void existsAttractionTypeById_NotExistsTest() {
        when(attractionTypeRepository.existsById(1L)).thenReturn(false);

        boolean result = attractionTypeService.existsById(1L);

        assertFalse(result);
    }
}