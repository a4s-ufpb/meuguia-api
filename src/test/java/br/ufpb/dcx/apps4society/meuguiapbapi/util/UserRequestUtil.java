package br.ufpb.dcx.apps4society.meuguiapbapi.util;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.User;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AuthenticationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.RegisterRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.UserDTO;
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

    public UserDTO register(RegisterRequestData bodyRequest) {

        return given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(UserDTO.class);
    }

    public AuthenticationResponseData authenticate(User user) {
        return given()
                .contentType(ContentType.JSON)
                .body(
                        AuthenticationRequestData.builder()
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .build()
                )
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(AuthenticationResponseData.class);
    }

    public AuthenticationResponseData registerAndAuthenticate(RegisterRequestData bodyRequest) {
        register(bodyRequest);
        return authenticate(User.builder()
                .email(bodyRequest.getEmail())
                .password(bodyRequest.getPassword())
                .build());
    }

    public void delete(UserDTO userDTO) {
        String token = authenticate(User.builder()
                .email(userDTO.getEmail())
                .password("12345678")
                .build()).getToken();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_USER)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
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
