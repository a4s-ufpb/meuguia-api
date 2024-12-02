package br.ufpb.dcx.apps4society.meuguiapbapi.util;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AttractionRequestData;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class AttractionRequestUtil extends RequestUtil {
    public static final String PATH_ATTRACTION = "/tourists";
    public static final String PATH_CREATE_ATTRACTION = "/tourists/create";
    public static final String PATH_FIND_ATTRACTION_BY_NAME = "/tourists/byName";
    public static final String PATH_FIND_ATTRACTION_BY_CITY = "/tourists/byCity";
    public static final String PATH_FIND_ATTRACTION_BY_SEGMENTATION = "/tourists/bySegmentations";

    private static AttractionRequestUtil instance;

    public static AttractionRequestUtil getInstance() {
        if (instance == null) {
            instance = new AttractionRequestUtil();
        }
        return instance;
    }

    public void delete(Attraction attraction, String token) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_ATTRACTION + "/" + attraction.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    public Attraction post(AttractionRequestData requestBody, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(Attraction.class);
    }
}
