package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.TourismSegmentationRequestData;

public class MockTouristSegmentation {
    private static MockTouristSegmentation instance;

    public static MockTouristSegmentation getInstance() {
        if (instance == null) {
            instance = new MockTouristSegmentation();
        }
        return instance;
    }

    public TourismSegmentation mockEntity(Integer num) {
        return TourismSegmentation.builder()
                .id(num.longValue())
                .name("mock Turismo de sol e mar"+num)
                .description("descrição")
                .build();
    }

    public TourismSegmentationRequestData mockRequest(Integer num) {
        return TourismSegmentationRequestData.builder()
                .name("mock Turismo de sol e mar"+num)
                .description("descrição")
                .build();
    }
}
