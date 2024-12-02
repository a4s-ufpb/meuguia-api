package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionRequestData;

import java.util.List;

public class AttractionTestHelper {
    private static AttractionTestHelper instance;

    public static AttractionTestHelper getInstance() {
        if (instance == null) {
            instance = new AttractionTestHelper();
        }
        return instance;
    }

    public AttractionRequestData mockRequest(
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
}
