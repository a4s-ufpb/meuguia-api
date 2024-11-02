package br.ufpb.dcx.apps4society.meuguiapbapi.util;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;

public class AttractionRequestUtil {
    public static final String PATH_ATTRACTION = "/tourists";
    public static final String PATH_CREATE_ATTRACTION = "/tourists/create";
    public static final String PATH_FIND_ATTRACTION_BY_NAME = "/tourists/byName";
    public static final String PATH_FIND_ATTRACTION_BY_CITY = "/tourists/byCity";
    public static final String PATH_FIND_ATTRACTION_BY_SEGMENTATION = "/tourists/bySegmentations";

    @BeforeEach
    void setup() {
        port = MeuguiaApiApplicationTests.port;
        baseURI = MeuguiaApiApplicationTests.baseURI;
        basePath = MeuguiaApiApplicationTests.basePath;
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

    public Attraction post(Attraction requestBody, String token) {
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
