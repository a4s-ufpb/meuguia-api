package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionTypeRequestData;

public class MockAttractionType {
    private static MockAttractionType instance;

    public static MockAttractionType getInstance() {
        if (instance == null) {
            instance = new MockAttractionType();
        }
        return instance;
    }

    public AttractionType mockEntity(Integer num) {
        return AttractionType.builder()
                .id(num.longValue())
                .name("mock Cultural"+num)
                .description("Turismo cultural, visando pontos históricos")
                .build();
    }

    public AttractionTypeRequestData mockRequest(Integer num) {
        return AttractionTypeRequestData.builder()
                .name("mock Cultural"+num)
                .description("Turismo cultural, visando pontos históricos")
                .build();
    }
}
