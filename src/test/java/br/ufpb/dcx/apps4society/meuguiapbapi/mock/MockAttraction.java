package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TouristSegmentation;

import java.util.List;

public class MockAttraction {
    public Attraction mockEntity(Integer num) {
        return null;
    }

    public Attraction mockRequest(
            Integer num,
            TouristSegmentation segmentation,
            MoreInfoLink moreInfoLink,
            AttractionType attractionType
    ) {
        return Attraction.builder()
                .name("mock Teatro municipal " + num)
                .description("Teatro municipal de joão pessoa")
                .map_link("https://mapa.com")
                .city("João Pessoa")
                .state("Paraíba (PB)")
                .image_link("https://imagem.com")
                .fonte("Fonte: https://fonte.com")
                .segmentations(List.of(segmentation))
                .attractionTypes(attractionType)
                .moreInfoLinkList(List.of(moreInfoLink))
                .build();
    }
}