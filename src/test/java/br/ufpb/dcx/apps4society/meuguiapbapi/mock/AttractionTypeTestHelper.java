package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionTypeRequestData;

public class AttractionTypeTestHelper {
    private static AttractionTypeTestHelper instance;

    public static AttractionTypeTestHelper getInstance() {
        if (instance == null) {
            instance = new AttractionTypeTestHelper();
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
