package br.ufpb.dcx.apps4society.meuguiapbapi.mock;

import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationRequestData;

import java.util.List;

public class TourismSegmentationTestHelper {
    private static TourismSegmentationTestHelper instance;

    public static TourismSegmentationTestHelper getInstance() {
        if (instance == null) {
            instance = new TourismSegmentationTestHelper();
        }
        return instance;
    }

    public TourismSegmentation createTourismSegmentation(Integer id) {
        return TourismSegmentation.builder()
                .id(id.longValue())
                .name("mock Turismo de sol e mar"+id)
                .description("descrição")
                .build();
    }

    public TourismSegmentationDTO createTourismSegmentationDTO(Integer id) {
        return TourismSegmentationDTO.builder()
                .id(id.longValue())
                .name("mock Turismo de sol e mar"+id)
                .description("descrição")
                .build();
    }


    public TourismSegmentationRequestData createTourismSegmentationRequestData(Integer num) {
        return TourismSegmentationRequestData.builder()
                .name("mock Turismo de sol e mar"+num)
                .description("descrição")
                .build();
    }

    public List<TourismSegmentation> getListOfTourismSegmentations() {
        return List.of(
                createTourismSegmentation(1),
                createTourismSegmentation(2),
                createTourismSegmentation(3)
        );
    }

    public List<TourismSegmentationDTO> getListOfTourismSegmentationsDTO() {
        return List.of(
                createTourismSegmentationDTO(1),
                createTourismSegmentationDTO(2),
                createTourismSegmentationDTO(3)
        );
    }


    public List<TourismSegmentation> getListOfDuplicatedTourismSegmentation() {
        return List.of(
                createTourismSegmentation(1),
                createTourismSegmentation(1),
                createTourismSegmentation(2),
                createTourismSegmentation(2)
        );
    }
}
