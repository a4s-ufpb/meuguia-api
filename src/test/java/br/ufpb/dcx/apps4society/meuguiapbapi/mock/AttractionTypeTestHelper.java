package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionTypeRequestData;

import java.util.List;

public class AttractionTypeTestHelper {
    private static AttractionTypeTestHelper instance;

    public static AttractionTypeTestHelper getInstance() {
        if (instance == null) {
            instance = new AttractionTypeTestHelper();
        }
        return instance;
    }

    public AttractionType createAttractionType(Integer num) {
        return AttractionType.builder()
                .id(num.longValue())
                .name("mock Cultural"+num)
                .description("Turismo cultural, visando pontos históricos")
                .build();
    }

    public AttractionTypeRequestData createAttractionTypeRequestData(Integer num) {
        return AttractionTypeRequestData.builder()
                .name("mock Cultural"+num)
                .description("Turismo cultural, visando pontos históricos")
                .build();
    }

    public List<AttractionType> createAttractionTypeList() {
        return List.of(
                createAttractionType(1),
                createAttractionType(2),
                createAttractionType(3)
        );
    }
}
