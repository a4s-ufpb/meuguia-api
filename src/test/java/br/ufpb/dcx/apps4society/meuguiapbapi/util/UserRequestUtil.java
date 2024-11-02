package br.ufpb.dcx.apps4society.meuguiapbapi.util;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationRequest;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterRequest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.*;

public class UserRequestUtil {
    public static final String PATH_USER_REGISTER = "/auth/register";
    public static final String PATH_USER_AUTHENTICATE = "/auth/authenticate";
    public static final String PATH_USER = "/user";

    @BeforeEach
    void setUp() {
        port = MeuguiaApiApplicationTests.port;
        baseURI = MeuguiaApiApplicationTests.baseURI;
        basePath = MeuguiaApiApplicationTests.basePath;
    }

    public AuthenticationResponse register(RegisterRequest bodyRequest) {

        return given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(AuthenticationResponse.class);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest bodyRequest) {

        return given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(AuthenticationResponse.class);
    }

    public void delete(String token) {
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .when()
            .delete(PATH_USER)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
