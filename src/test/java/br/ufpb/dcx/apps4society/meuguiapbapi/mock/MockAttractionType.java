package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.dtos.AttractionTypeForm;

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

    public AttractionTypeForm mockRequest(Integer num) {
        return AttractionTypeForm.builder()
                .name("mock Cultural"+num)
                .description("Turismo cultural, visando pontos históricos")
                .build();
    }
}
