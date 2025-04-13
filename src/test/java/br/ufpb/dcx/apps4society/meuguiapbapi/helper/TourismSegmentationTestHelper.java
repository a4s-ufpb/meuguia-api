package br.ufpb.dcx.apps4society.meuguiapbapi.helper;

import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationRequestData;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;

public class TourismSegmentationTestHelper {
    public final static String PATH_TOURISM_SEGMENTATION = "/segmentations";

    public static TourismSegmentation createTourismSegmentation(Integer id) {
        return TourismSegmentation.builder()
                .id(id.longValue())
                .name("mock Turismo de sol e mar" + id)
                .description("descrição")
                .build();
    }

    public static TourismSegmentationDTO createTourismSegmentationDTO(Integer id) {
        return TourismSegmentationDTO.builder()
                .id(id.longValue())
                .name("mock Turismo de sol e mar" + id)
                .description("descrição")
                .build();
    }


    public static TourismSegmentationRequestData createTourismSegmentationRequestData(Integer num) {
        return TourismSegmentationRequestData.builder()
                .name("mock Turismo de sol e mar" + num)
                .description("descrição")
                .build();
    }

    public static List<TourismSegmentation> getListOfTourismSegmentations() {
        return List.of(
                createTourismSegmentation(1),
                createTourismSegmentation(2),
                createTourismSegmentation(3)
        );
    }

    public static List<TourismSegmentationDTO> getListOfTourismSegmentationsDTO() {
        return List.of(
                createTourismSegmentationDTO(1),
                createTourismSegmentationDTO(2),
                createTourismSegmentationDTO(3)
        );
    }


    public static List<TourismSegmentation> getListOfDuplicatedTourismSegmentation() {
        return List.of(
                createTourismSegmentation(1),
                createTourismSegmentation(1),
                createTourismSegmentation(2),
                createTourismSegmentation(2)
        );
    }

    public static TourismSegmentation post(TourismSegmentationRequestData request, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(request)
                .when()
                .post(PATH_TOURISM_SEGMENTATION)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(TourismSegmentation.class);
    }

    public static void delete(TourismSegmentation request, String token) {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + request.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
