package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TouristSegmentation;

public class MockTouristSegmentation {

    public TouristSegmentation mockEntity(Integer num) {
        return TouristSegmentation.builder()
                .id(num.longValue())
                .name("mock Turismo de sol e mar"+num)
                .description("descrição")
                .build();
    }

    public TouristSegmentation mockRequest(Integer num) {
        return TouristSegmentation.builder()
                .name("mock Turismo de sol e mar"+num)
                .description("descrição")
                .build();
    }
}
