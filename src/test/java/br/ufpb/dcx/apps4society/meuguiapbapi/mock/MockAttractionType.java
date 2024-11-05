package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.dtos.AttractionTypeForm;

public class MockAttractionType {

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
