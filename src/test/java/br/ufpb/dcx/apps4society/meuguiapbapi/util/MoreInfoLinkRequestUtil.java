package br.ufpb.dcx.apps4society.meuguiapbapi.util;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;

public class MoreInfoLinkRequestUtil {
    public static final String PATH_MORE_INFO_LINK = "/more-info";

    @BeforeEach
    void setUp() {
        port = MeuguiaApiApplicationTests.port;
        baseURI = MeuguiaApiApplicationTests.baseURI;
        basePath = MeuguiaApiApplicationTests.basePath;
    }

    public MoreInfoLink post(MoreInfoLink request, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(request)
                .when()
                .post(PATH_MORE_INFO_LINK)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(MoreInfoLink.class);
    }

    public void delete(MoreInfoLink request, String token) {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(request)
                .when()
                .delete(PATH_MORE_INFO_LINK + "/" + request.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
