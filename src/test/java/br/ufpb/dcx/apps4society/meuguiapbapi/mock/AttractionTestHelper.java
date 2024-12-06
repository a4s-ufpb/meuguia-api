package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionRequestData;

import java.util.List;

public class AttractionTestHelper {
    private static AttractionTestHelper instance;

    private final AttractionTypeTestHelper attractionTypeTestHelper = AttractionTypeTestHelper.getInstance();
    private final MoreInfoLinkTestHelper moreInfoLinkTestHelper = MoreInfoLinkTestHelper.getInstance();
    private final TouristSegmentationTestHelper touristSegmentationTestHelper = TouristSegmentationTestHelper.getInstance();

    public static AttractionTestHelper getInstance() {
        if (instance == null) {
            instance = new AttractionTestHelper();
        }
        return instance;
    }

    public AttractionRequestData createAttractionRequestData(
            Integer num,
            TourismSegmentation segmentation,
            MoreInfoLink moreInfoLink,
            AttractionType attractionType
    ) {
        return AttractionRequestData.builder()
                .name("mock Teatro municipal " + num)
                .description("Teatro municipal de joão pessoa")
                .mapLink("https://mapa.com")
                .city("João Pessoa")
                .state("Paraíba (PB)")
                .imageLink("https://imagem.com")
                .infoSource("Fonte: https://fonte.com")
                .segmentations(List.of(segmentation))
                .attractionTypes(attractionType)
                .moreInfoLinkList(List.of(moreInfoLink))
                .build();
    }

    public Attraction createAttraction(
            Integer num,
            TourismSegmentation segmentation,
            MoreInfoLink moreInfoLink,
            AttractionType attractionType
    ) {
        return Attraction.builder()
                .name("mock Teatro municipal " + num)
                .description("Teatro municipal de joão pessoa")
                .mapLink("https://mapa.com")
                .city("João Pessoa")
                .state("Paraíba (PB)")
                .imageLink("https://imagem.com")
                .infoSource("Fonte: https://fonte.com")
                .segmentations(List.of(segmentation))
                .attractionType(attractionType)
                .moreInfoLinkList(List.of(moreInfoLink))
                .build();

    }

    public List<Attraction> createAttractionList() {
        return List.of(
                createAttraction(1, touristSegmentationTestHelper.createTourismSegmentation(1), moreInfoLinkTestHelper.createMoreInfoLink(1), attractionTypeTestHelper.createAttractionType(1)),
                createAttraction(2, touristSegmentationTestHelper.createTourismSegmentation(2), moreInfoLinkTestHelper.createMoreInfoLink(2), attractionTypeTestHelper.createAttractionType(2)),
                createAttraction(3, touristSegmentationTestHelper.createTourismSegmentation(3), moreInfoLinkTestHelper.createMoreInfoLink(3), attractionTypeTestHelper.createAttractionType(3))
        );
    }
}
