package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.attraction.AttractionDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.attraction.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.moreinfolink.MoreInfoLinkRequestData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AttractionTestHelper {
    private static AttractionTestHelper instance;

    private final AttractionTypeTestHelper attractionTypeTestHelper = AttractionTypeTestHelper.getInstance();
    private final MoreInfoLinkTestHelper moreInfoLinkTestHelper = MoreInfoLinkTestHelper.getInstance();
    private final TourismSegmentationTestHelper tourismSegmentationTestHelper = TourismSegmentationTestHelper.getInstance();

    public static AttractionTestHelper getInstance() {
        if (instance == null) {
            instance = new AttractionTestHelper();
        }
        return instance;
    }

    public AttractionRequestData createAttractionRequestData(
            Integer num,
            TourismSegmentation segmentation,
            MoreInfoLinkRequestData moreInfoLinkRequestData,
            AttractionType attractionType
    ) {
        return AttractionRequestData.builder()
                .name("mock Teatro municipal " + num)
                .description("Teatro municipal de joão pessoa")
                .mapLink("https://mapa.com")
                .city("João Pessoa")
                .state("Paraíba (PB)")
                .imageLink("https://imagem.com")
                .segmentations(List.of(segmentation.getId()))
                .attractionType(attractionType.getId())
                .moreInfoLinks(List.of(moreInfoLinkRequestData))
                .build();
    }

    public AttractionRequestData createAttractionRequestData(Integer num) {
        return createAttractionRequestData(
                num,
                tourismSegmentationTestHelper.createTourismSegmentation(num),
                moreInfoLinkTestHelper.createMoreInfoLinkRequestData(num),
                attractionTypeTestHelper.createAttractionType(num)
        );
    }

    public Attraction createAttraction(
            Integer num,
            TourismSegmentation segmentation,
            MoreInfoLink moreInfoLink,
            AttractionType attractionType
    ) {
        return Attraction.builder()
                .id(num.longValue())
                .name("mock Teatro municipal " + num)
                .description("Teatro municipal de joão pessoa")
                .mapLink("https://mapa.com")
                .city("João Pessoa")
                .state("Paraíba (PB)")
                .imageLink("https://imagem.com")
                .segmentations(new ArrayList<>(List.of(segmentation)))
                .attractionType(attractionType)
                .moreInfoLinks(new ArrayList<>(List.of(moreInfoLink)))
                .build();

    }

    public Attraction createAttraction(Integer num) {
        return createAttraction(
                num,
                tourismSegmentationTestHelper.createTourismSegmentation(num),
                moreInfoLinkTestHelper.createMoreInfoLink(num),
                attractionTypeTestHelper.createAttractionType(num)
        );
    }

    public List<Attraction> createAttractionList() {
        return List.of(
                createAttraction(1, tourismSegmentationTestHelper.createTourismSegmentation(1), moreInfoLinkTestHelper.createMoreInfoLink(1), attractionTypeTestHelper.createAttractionType(1)),
                createAttraction(2, tourismSegmentationTestHelper.createTourismSegmentation(2), moreInfoLinkTestHelper.createMoreInfoLink(2), attractionTypeTestHelper.createAttractionType(2)),
                createAttraction(3, tourismSegmentationTestHelper.createTourismSegmentation(3), moreInfoLinkTestHelper.createMoreInfoLink(3), attractionTypeTestHelper.createAttractionType(3))
        );
    }

    public AttractionDTO createAttractionDTO(Integer id) {
        return AttractionDTO.builder()
                .id(id.longValue())
                .name("mock Teatro municipal " + id)
                .description("Teatro municipal de joão pessoa")
                .mapLink("https://mapa.com")
                .city("João Pessoa")
                .state("Paraíba (PB)")
                .imageLink("https://imagem.com")
                .segmentations(List.of(tourismSegmentationTestHelper.createTourismSegmentationDTO(id)))
                .attractionTypes(attractionTypeTestHelper.createAttractionTypeDTO(id))
                .moreInfoLinks(List.of(moreInfoLinkTestHelper.createMoreInfoLinkDTO(id)))
                .build();
    }

    public void assertAttractionEqualsToRequestData(Attraction attraction, AttractionRequestData attractionRequestData) {
        assertEquals(attractionRequestData.getName(), attraction.getName());
        assertEquals(attractionRequestData.getDescription(), attraction.getDescription());
        assertEquals(attractionRequestData.getMapLink(), attraction.getMapLink());
        assertEquals(attractionRequestData.getCity(), attraction.getCity());
        assertEquals(attractionRequestData.getState(), attraction.getState());
        assertEquals(attractionRequestData.getImageLink(), attraction.getImageLink());
    }
}
