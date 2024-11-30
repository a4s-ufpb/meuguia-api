package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AuthenticationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.RegisterRequestData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.UserRequestUtil.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthenticationControllerTest extends MeuguiaApiApplicationTests {

    @Test
    void register_shouldReturnStatus201_whenDataIsValidTest() {
        RegisterRequestData requestBody = mockAuthentication.mockRequest(1);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            userRequestUtil.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.CREATED.value())
                .body("token", notNullValue())
                .extract()
                .body()
                .as(AuthenticationResponseData.class);
    }

    @Test
    void register_shouldReturnStatus400_whenEmailAlreadyRegisteredTest() {
        RegisterRequestData requestBody = mockAuthentication.mockRequest(2);
        AuthenticationResponseData auth = userRequestUtil.register(requestBody);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER);

        userRequestUtil.delete(auth.getToken());

        response.then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenEmailIsInvalidTest() {
        RegisterRequestData mockRequest = mockAuthentication.mockRequest(3);
        mockRequest.setEmail("");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(mockRequest)
                .when()
                .post(PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            userRequestUtil.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenPasswordIsInvalidTest() {
        RegisterRequestData mockRequest = mockAuthentication.mockRequest(4);
        mockRequest.setPassword("");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(mockRequest)
                .when()
                .post(PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            userRequestUtil.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenFirstNameIsInvalidTest() {
        RegisterRequestData mockRequest = mockAuthentication.mockRequest(5);
        mockRequest.setFirstName("");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(mockRequest)
                .when()
                .post(PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            userRequestUtil.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenLastNameIsInvalidTest() {
        RegisterRequestData requestBody = mockAuthentication.mockRequest(6);
        requestBody.setLastName("");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            userRequestUtil.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenEmailIsMissingTest() {
        RegisterRequestData requestBody = mockAuthentication.mockRequest(7);
        requestBody.setEmail(null);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            userRequestUtil.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenPasswordIsMissingTest() {
        RegisterRequestData requestBody = mockAuthentication.mockRequest(8);
        requestBody.setPassword(null);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            userRequestUtil.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenFirstNameIsMissingTest() {
        RegisterRequestData requestBody = mockAuthentication.mockRequest(9);
        requestBody.setFirstName(null);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            userRequestUtil.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenLastNameIsMissingTest() {
        RegisterRequestData requestBody = mockAuthentication.mockRequest(10);
        requestBody.setLastName(null);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            userRequestUtil.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn200_whenUserExistTest() {
        RegisterRequestData mockRequest = mockAuthentication.mockRequest(11);
        AuthenticationResponseData auth = userRequestUtil.register(mockRequest);

        AuthenticationRequestData requestBody = mockAuthentication.mockAuthentication();
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE);

        userRequestUtil.delete(auth.getToken());

        response.then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("token", notNullValue());
    }

    @Test
    void authenticate_shouldReturn403_whenUserNotExistTest() {
        AuthenticationRequestData requestBody = mockAuthentication.mockAuthentication();

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void authenticate_shouldReturn400_whenEmailIsInvalidTest() {
        AuthenticationRequestData requestBody = mockAuthentication.mockAuthentication();
        requestBody.setEmail("");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn400_whenPasswordIsInvalidTest() {
        AuthenticationRequestData requestBody = mockAuthentication.mockAuthentication();
        requestBody.setPassword("");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn400_whenEmailIsMissingTest() {
        AuthenticationRequestData requestBody = mockAuthentication.mockAuthentication();
        requestBody.setEmail(null);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn400_whenPasswordIsMissingTest() {
        AuthenticationRequestData requestBody = mockAuthentication.mockAuthentication();
        requestBody.setPassword(null);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn400_whenPasswordLengthIsLessThan8Test() {
        AuthenticationRequestData requestBody = mockAuthentication.mockAuthentication();
        requestBody.setPassword("1234567");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    void delete_shouldReturnStatus204_whenTokenIsValidTest() {
        RegisterRequestData requestBody = mockAuthentication.mockRequest(12);
        AuthenticationResponseData auth = userRequestUtil.register(requestBody);

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