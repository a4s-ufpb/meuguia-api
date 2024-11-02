package br.ufpb.dcx.apps4society.meuguiapbapi.util;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TouristSegmentation;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.basePath;

public class TourismSegmentationRequestUtil {
    public final static String PATH_TOURISM_SEGMENTATION = "/segmentations";

    @BeforeEach
    void setUP() {
        port = MeuguiaApiApplicationTests.port;
        baseURI = MeuguiaApiApplicationTests.baseURI;
        basePath = MeuguiaApiApplicationTests.basePath;
    }

    public TouristSegmentation post(TouristSegmentation request, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(request)
                .when()
                .post(PATH_TOURISM_SEGMENTATION)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(TouristSegmentation.class);
    }

    public void delete(TouristSegmentation request, String token) {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + request.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
