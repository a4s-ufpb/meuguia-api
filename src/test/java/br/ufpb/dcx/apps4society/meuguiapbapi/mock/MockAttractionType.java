package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;

public class MockAttractionType {

    public AttractionType mockEntity(Integer num) {
        return AttractionType.builder()
                .id(num.longValue())
                .name("mock Cultural"+num)
                .description("Turismo cultural, visando pontos históricos")
                .build();
    }

    public AttractionType mockRequest(Integer num) {
        return AttractionType.builder()
                .name("mock Cultural"+num)
                .description("Turismo cultural, visando pontos históricos")
                .build();
    }
}
