package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MoreInfoLinkTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.repository.MoreInfoLinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.MockitoAnnotations.openMocks;

class MoreInfoLinkServiceTest {
    private final MoreInfoLinkTestHelper moreInfoLinkTestHelper = MoreInfoLinkTestHelper.getInstance();

    @Mock
    private MoreInfoLinkRepository moreInfoLinkRepository;

    @InjectMocks
    private MoreInfoLinkService moreInfoLinkService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void createMoreInfoLinkTest() {
        var requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        when(moreInfoLinkRepository.save(any(MoreInfoLink.class))).thenAnswer(invocationOnMock -> {
            MoreInfoLink moreInfoLink = invocationOnMock.getArgument(0);
            moreInfoLink.setId(1L);
            return moreInfoLink;
        });

        MoreInfoLink result = moreInfoLinkService.create(requestData);

        verify(moreInfoLinkRepository).save(any(MoreInfoLink.class));
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(requestData.getLink(), result.getLink());
        assertEquals(requestData.getDescription(), result.getDescription());
    }

    @Test
    void deleteMoreInfoLinkTest() {
        when(moreInfoLinkRepository.findById(1L)).thenReturn(Optional.of(moreInfoLinkTestHelper.createMoreInfoLink(1)));

        moreInfoLinkService.delete(1L);

        verify(moreInfoLinkRepository).deleteById(1L);
    }

    @Test
    void deleteMoreInfoLink_NotExistTest() {
        when(moreInfoLinkRepository.findById(1L)).thenReturn(Optional.empty());

        var thrown = assertThrows(ObjectNotFoundException.class, () -> {
            moreInfoLinkService.delete(1L);
        });

        assertEquals("Objeto não encontrado! id: 1, Tipo br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink", thrown.getMessage());
    }

    @Test
    void findMoreInfoLinkByIdTest() {
        MoreInfoLink moreInfoLink = moreInfoLinkTestHelper.createMoreInfoLink(1);
        when(moreInfoLinkRepository.findById(moreInfoLink.getId())).thenReturn(Optional.of(moreInfoLink));

        MoreInfoLink result = moreInfoLinkService.findById(1L);

        verify(moreInfoLinkRepository).findById(1L);
        assertNotNull(result);
        assertEquals(moreInfoLink.getId(), result.getId());
        assertEquals(moreInfoLink.getLink(), result.getLink());
        assertEquals(moreInfoLink.getDescription(), result.getDescription());
    }

    @Test
    void findMoreInfoLinkById_NotExistTest() {
        when(moreInfoLinkRepository.findById(1L)).thenReturn(Optional.empty());

        var thrown = assertThrows(ObjectNotFoundException.class, () -> {
            moreInfoLinkService.findById(1L);
        });

        assertEquals("Objeto não encontrado! id: 1, Tipo br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink", thrown.getMessage());
    }

    @Test
    void findAllMoreInfoLinkTest() {
        when(moreInfoLinkRepository.findAll()).thenReturn(moreInfoLinkTestHelper.getListOfMoreInfoLinks());

        var result = moreInfoLinkService.findAll();

        verify(moreInfoLinkRepository).findAll();
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void findAllMoreInfoLink_EmptyDatabaseTest() {
        when(moreInfoLinkRepository.findAll()).thenReturn(null);

        var result = moreInfoLinkService.findAll();

        verify(moreInfoLinkRepository).findAll();
        assertNull(result);
    }

    @Test
    void exitsMoreInfoLinkByIdTest() {
        when(moreInfoLinkRepository.existsById(1L)).thenReturn(true);

        var result = moreInfoLinkService.existsById(1L);

        verify(moreInfoLinkRepository).existsById(1L);
        assertTrue(result);
    }

    @Test
    void existsMoreInfoLinkById_NotExistTest() {
        when(moreInfoLinkRepository.existsById(1L)).thenReturn(false);

        var result = moreInfoLinkService.existsById(1L);

        verify(moreInfoLinkRepository).existsById(1L);
        assertFalse(result);
    }
}
