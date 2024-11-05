package br.ufpb.dcx.apps4society.meuguiapbapi.util;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.dtos.AttractionTypeForm;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.port;

public class AttractionTypeRequestUtil {
    public static final String PATH_ATTRACTION_TYPE = "/types";

    @BeforeEach
    void setUP() {
        port = MeuguiaApiApplicationTests.port;
        baseURI = MeuguiaApiApplicationTests.baseURI;
        basePath = MeuguiaApiApplicationTests.basePath;
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
