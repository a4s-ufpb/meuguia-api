package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dtos.TourismSegmentationForm;

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

    public TourismSegmentationForm mockRequest(Integer num) {
        return TourismSegmentationForm.builder()
                .name("mock Turismo de sol e mar"+num)
                .description("descrição")
                .build();
    }
}
