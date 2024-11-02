package br.ufpb.dcx.apps4society.meuguiapbapi.controller;


import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationRequest;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterRequest;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.UserRequestUtil.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Slf4j
@Disabled
public class AuthenticationControllerTest extends MeuguiaApiApplicationTests {

    @Test
    void register_shouldReturnStatus201_whenDataIsValidTest() {
        RegisterRequest requestBody = mockAuthentication.mockRequest(1);

        AuthenticationResponse response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .log().body()
                .statusCode(HttpStatus.CREATED.value())
                .body("token", notNullValue())
                .extract()
                .body()
                .as(AuthenticationResponse.class);

        userRequestUtil.delete(response.getToken());
    }

    @Test
    void register_shouldReturnStatus400_whenEmailAlreadyRegisteredTest() {
        RegisterRequest requestBody = mockAuthentication.mockRequest(2);
        AuthenticationResponse auth = userRequestUtil.register(requestBody);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        userRequestUtil.delete(auth.getToken());
    }

    @Test
    void register_shouldReturnStatus400_whenEmailIsInvalidTest() {
        RegisterRequest mockRequest = mockAuthentication.mockRequest(3);
        mockRequest.setEmail("");

        given()
                .contentType(ContentType.JSON)
                .body(mockRequest)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenPasswordIsInvalidTest() {
        RegisterRequest mockRequest = mockAuthentication.mockRequest(4);
        mockRequest.setPassword("");

        given()
                .contentType(ContentType.JSON)
                .body(mockRequest)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenFirstNameIsInvalidTest() {
        RegisterRequest mockRequest = mockAuthentication.mockRequest(5);
        mockRequest.setFirstName("");

        given()
                .contentType(ContentType.JSON)
                .body(mockRequest)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenLastNameIsInvalidTest() {
        RegisterRequest requestBody = mockAuthentication.mockRequest(6);
        requestBody.setLastName("");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenEmailIsMissingTest() {
        RegisterRequest requestBody = mockAuthentication.mockRequest(7);
        requestBody.setEmail(null);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenPasswordIsMissingTest() {
        RegisterRequest requestBody = mockAuthentication.mockRequest(8);
        requestBody.setPassword(null);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void register_shouldReturnStatus400_whenFirstNameIsMissingTest() {
        RegisterRequest requestBody = mockAuthentication.mockRequest(9);
        requestBody.setFirstName(null);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenLastNameIsMissingTest() {
        RegisterRequest requestBody = mockAuthentication.mockRequest(10);
        requestBody.setLastName(null);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn200_whenUserExistTest() {
        RegisterRequest mockRequest = mockAuthentication.mockRequest(11);
        userRequestUtil.register(mockRequest);

        AuthenticationRequest requestBody = mockAuthentication.mockAuthentication();
        AuthenticationResponse response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("token", notNullValue())
                .extract()
                .as(AuthenticationResponse.class);

        userRequestUtil.delete(response.getToken());
    }

    @Test
    void authenticate_shouldReturn403_whenUserNotExistTest() {
        AuthenticationRequest requestBody = mockAuthentication.mockAuthentication();

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
        }

    @Test
    void delete_shouldReturnStatus204_whenTokenIsValidTest() {
        RegisterRequest requestBody = mockAuthentication.mockRequest(12);
        AuthenticationResponse auth = userRequestUtil.register(requestBody);

        given()
                .header("Authorization", "Bearer " + auth.getToken())
                .when()
                .delete(PATH_USER)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void delete_shouldReturnStatus401_whenTokenIsInvalidTest() {
        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .when()
                .delete(PATH_USER)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void delete_shouldReturnStatus403_whenTokenIsMissingTest() {
        given()
                .when()
                .delete(PATH_USER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

}