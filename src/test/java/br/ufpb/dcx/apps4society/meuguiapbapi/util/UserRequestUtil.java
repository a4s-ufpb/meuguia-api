package br.ufpb.dcx.apps4society.meuguiapbapi.util;

import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationForm;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterForm;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.*;

public class UserRequestUtil extends RequestUtil {
    public static final String PATH_USER_REGISTER = "/auth/register";
    public static final String PATH_USER_AUTHENTICATE = "/auth/authenticate";
    public static final String PATH_USER = "/user";

    private static UserRequestUtil instance;

    public static UserRequestUtil getInstance() {
        if (instance == null) {
            instance = new UserRequestUtil();
        }
        return instance;
    }

    public AuthenticationResponse register(RegisterForm bodyRequest) {

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

    public AuthenticationResponse authenticate(AuthenticationForm bodyRequest) {

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
