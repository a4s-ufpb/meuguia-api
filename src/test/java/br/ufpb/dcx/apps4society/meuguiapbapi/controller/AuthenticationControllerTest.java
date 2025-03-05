package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.RegisterUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.UserDTO;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.UserRequestUtil.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AuthenticationControllerTest extends MeuguiaApiApplicationTests {

    @Test
    void register_shouldReturnStatus201_whenDataIsValidTest() {
        RegisterUserRequestData requestBody = authenticationTestHelper.getRegisterRequestData(1);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            userRequestUtil.delete(response.as(UserDTO.class));
        }

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("email", equalTo(requestBody.getEmail()))
                .body("first_name", equalTo(requestBody.getFirstName()))
                .body("last_name", equalTo(requestBody.getLastName()))
                .body("id", notNullValue());
    }

    @Test
    void register_shouldReturnStatus409_whenEmailAlreadyRegisteredTest() {
        RegisterUserRequestData requestBody = authenticationTestHelper.getRegisterRequestData(2);
        AuthenticationResponseData auth = userRequestUtil.registerAndAuthenticate(requestBody);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_REGISTER);

        userRequestUtil.delete(auth.getToken());

        response.then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void register_shouldReturnStatus400_whenEmailIsInvalidTest() {
        RegisterUserRequestData mockRequest = authenticationTestHelper.getRegisterRequestData(3);
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
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenPasswordIsInvalidTest() {
        RegisterUserRequestData mockRequest = authenticationTestHelper.getRegisterRequestData(4);
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
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenFirstNameIsInvalidTest() {
        RegisterUserRequestData mockRequest = authenticationTestHelper.getRegisterRequestData(5);
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
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenLastNameIsInvalidTest() {
        RegisterUserRequestData requestBody = authenticationTestHelper.getRegisterRequestData(6);
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
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenEmailIsMissingTest() {
        RegisterUserRequestData requestBody = authenticationTestHelper.getRegisterRequestData(7);
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
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenPasswordIsMissingTest() {
        RegisterUserRequestData requestBody = authenticationTestHelper.getRegisterRequestData(8);
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
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenFirstNameIsMissingTest() {
        RegisterUserRequestData requestBody = authenticationTestHelper.getRegisterRequestData(9);
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
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenLastNameIsMissingTest() {
        RegisterUserRequestData requestBody = authenticationTestHelper.getRegisterRequestData(10);
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
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn200_whenUserExistTest() {
        RegisterUserRequestData mockRequest = authenticationTestHelper.getRegisterRequestData(11);
        AuthenticationResponseData auth = userRequestUtil.registerAndAuthenticate(mockRequest);

        AuthenticationRequestData requestBody = authenticationTestHelper.createAuthenticationRequestData(11);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE);

        userRequestUtil.delete(auth.getToken());

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("token", notNullValue());
    }

    @Test
    void authenticate_shouldReturn403_whenUserNotExistTest() {
        AuthenticationRequestData requestBody = authenticationTestHelper.createAuthenticationRequestData();

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void authenticate_shouldReturn400_whenEmailIsInvalidTest() {
        AuthenticationRequestData requestBody = authenticationTestHelper.createAuthenticationRequestData();
        requestBody.setEmail("");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn400_whenPasswordIsInvalidTest() {
        AuthenticationRequestData requestBody = authenticationTestHelper.createAuthenticationRequestData();
        requestBody.setPassword("");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn400_whenEmailIsMissingTest() {
        AuthenticationRequestData requestBody = authenticationTestHelper.createAuthenticationRequestData();
        requestBody.setEmail(null);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn400_whenPasswordIsMissingTest() {
        AuthenticationRequestData requestBody = authenticationTestHelper.createAuthenticationRequestData();
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
        AuthenticationRequestData requestBody = authenticationTestHelper.createAuthenticationRequestData();
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
        RegisterUserRequestData requestBody = authenticationTestHelper.getRegisterRequestData(12);
        AuthenticationResponseData auth = userRequestUtil.registerAndAuthenticate(requestBody);

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