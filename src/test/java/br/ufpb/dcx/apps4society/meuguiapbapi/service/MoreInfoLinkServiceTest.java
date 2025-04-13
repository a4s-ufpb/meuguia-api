package br.ufpb.dcx.apps4society.meuguiapbapi.service;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.service.AttractionService;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.ObjectNotFoundException;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.service.MoreInfoLinkService;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTestHelper.createAttraction;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper.createAttractionType;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper.createMoreInfoLink;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper.createMoreInfoLinkRequestData;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper.createTourismSegmentation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class MoreInfoLinkServiceTest {
    Attraction attraction;
    AttractionType attractionType;
    MoreInfoLink moreInfoLink;
    TourismSegmentation tourismSegmentation;

    @Mock
    AttractionService attractionService;

    @InjectMocks
    MoreInfoLinkService moreInfoLinkService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        moreInfoLink = createMoreInfoLink(1);
        tourismSegmentation = createTourismSegmentation(1);
        attractionType = createAttractionType(1);
        attraction = createAttraction(1, tourismSegmentation, moreInfoLink, attractionType);
    }

    @Test
    void getMoreInfoLinkFromAttraction() {
        when(attractionService.findById(1L)).thenReturn(this.attraction);

        MoreInfoLinkDTO moreInfoLink = moreInfoLinkService.getMoreInfoLinkFromAttraction(1L, this.moreInfoLink.getLink());

        assertEquals(this.moreInfoLink.getLink(), moreInfoLink.getLink());
        assertEquals(this.moreInfoLink.getDescription(), moreInfoLink.getDescription());
    }

    @Test
    void getMoreInfoLinkFromAttractionNotFound() {
        when(attractionService.findById(1L)).thenThrow(ObjectNotFoundException.class);

        assertThrows(ObjectNotFoundException.class, () -> moreInfoLinkService.getMoreInfoLinkFromAttraction(1L, moreInfoLink.getLink()));
    }

    @Test
    void addMoreInfoLinkToAttraction() {
        when(attractionService.findById(1L)).thenReturn(this.attraction);
        when(attractionService.save(any(Attraction.class))).thenAnswer(i -> i.getArgument(0));

        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        MoreInfoLinkDTO moreInfoLinkDTO = moreInfoLinkService.addMoreInfoLinkToAttraction(1L, requestData);

        assertEquals(requestData.getLink(), moreInfoLinkDTO.getLink());
        assertEquals(requestData.getDescription(), moreInfoLinkDTO.getDescription());
    }

    @Test
    void addMoreInfoLinkToAttractionNotFound() {
        when(attractionService.findById(1L)).thenThrow(ObjectNotFoundException.class);

        assertThrows(ObjectNotFoundException.class, () -> moreInfoLinkService.addMoreInfoLinkToAttraction(1L, createMoreInfoLinkRequestData(1)));
    }

    @Test
    void updateMoreInfoLinkFromAttraction() {
        when(attractionService.findById(1L)).thenReturn(this.attraction);
        when(attractionService.save(any(Attraction.class))).thenAnswer(i -> i.getArgument(0));

        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        MoreInfoLinkDTO moreInfoLinkDTO = moreInfoLinkService.updateMoreInfoLinkFromAttraction(1L, moreInfoLink.getLink(), requestData);

        assertEquals(moreInfoLink.getLink(), moreInfoLinkDTO.getLink());
        assertEquals(requestData.getDescription(), moreInfoLinkDTO.getDescription());
    }

    @Test
    void updateMoreInfoLinkFromAttractionNotFound() {
        when(attractionService.findById(1L)).thenThrow(ObjectNotFoundException.class);

        assertThrows(ObjectNotFoundException.class, () -> moreInfoLinkService.updateMoreInfoLinkFromAttraction(1L, moreInfoLink.getLink(), createMoreInfoLinkRequestData(1)));
    }

    @Test
    void updateMoreInfoLink_LinkNotFound() {
        when(attractionService.findById(1L)).thenReturn(this.attraction);

        assertThrows(ObjectNotFoundException.class, () -> moreInfoLinkService.updateMoreInfoLinkFromAttraction(1L, "link", createMoreInfoLinkRequestData(1)));
    }

    @Test
    void deleteMoreInfoLinkFromAttraction() {
        when(attractionService.findById(1L)).thenReturn(this.attraction);
        when(attractionService.save(any(Attraction.class))).thenAnswer(i -> i.getArgument(0));

        moreInfoLinkService.deleteMoreInfoLinkFromAttraction(1L, moreInfoLink.getLink());

        verify(attractionService, times(1)).save(any(Attraction.class));
    }

    @Test
    void deleteMoreInfoLinkFromAttractionNotFound() {
        when(attractionService.findById(1L)).thenThrow(ObjectNotFoundException.class);

        assertThrows(ObjectNotFoundException.class, () -> moreInfoLinkService.deleteMoreInfoLinkFromAttraction(1L, moreInfoLink.getLink()));
    }

    @Test
    void deleteMoreInfoLinkFromAttraction_LinkNotFound() {
        when(attractionService.findById(1L)).thenReturn(this.attraction);

        assertThrows(ObjectNotFoundException.class, () -> moreInfoLinkService.deleteMoreInfoLinkFromAttraction(1L, "link"));
    }
}