package br.ufpb.dcx.apps4society.meuguiapbapi.util;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.TourismSegmentationRequestData;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class TourismSegmentationRequestUtil extends RequestUtil {
    public final static String PATH_TOURISM_SEGMENTATION = "/segmentations";

    private static TourismSegmentationRequestUtil instance;

    public static TourismSegmentationRequestUtil getInstance() {
        if (instance == null) {
            instance = new TourismSegmentationRequestUtil();
        }
        return instance;
    }

    public TourismSegmentation post(TourismSegmentationRequestData request, String token) {
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

    public void delete(TourismSegmentation request, String token) {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + request.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
