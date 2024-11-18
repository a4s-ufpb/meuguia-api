package br.ufpb.dcx.apps4society.meuguiapbapi.util;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.dtos.AttractionTypeForm;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class AttractionTypeRequestUtil extends RequestUtil {
    public static final String PATH_ATTRACTION_TYPE = "/types";

    private static AttractionTypeRequestUtil instance;

    public static AttractionTypeRequestUtil getInstance() {
        if (instance == null) {
            instance = new AttractionTypeRequestUtil();
        }
        return instance;
    }

    public AttractionType post(AttractionTypeForm request, String token) {
        return given()
            .header("Authorization", "Bearer "+ token)
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post(PATH_ATTRACTION_TYPE)
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .as(AttractionType.class);
    }

    public void delete(AttractionType request, String token) {
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .when()
            .delete(PATH_ATTRACTION_TYPE+"/"+request.getId())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
