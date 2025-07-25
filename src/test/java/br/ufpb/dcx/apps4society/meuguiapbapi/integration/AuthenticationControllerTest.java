package br.ufpb.dcx.apps4society.meuguiapbapi.integration;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.UserTestsHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.RegisterUserRequestData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class AuthenticationControllerTest extends MeuguiaApiApplicationTests {

    @Test
    void register_shouldReturnStatus201_whenDataIsValidTest() {
        RegisterUserRequestData requestBody = UserTestsHelper.createRegisterRequestData(1);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            UserTestsHelper.delete(response.getBody().jsonPath().getLong("id"), getAdminToken());
        }

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("email", equalTo(requestBody.getEmail()))
                .body("firstName", equalTo(requestBody.getFirstName()))
                .body("lastName", equalTo(requestBody.getLastName()))
                .body("id", notNullValue());
    }

    @Test
    void register_shouldReturnStatus409_whenEmailAlreadyRegisteredTest() {
        RegisterUserRequestData requestBody = UserTestsHelper.createRegisterRequestData(2);
        AuthenticationResponseData auth = UserTestsHelper.registerAndAuthenticate(requestBody);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_REGISTER);

        UserTestsHelper.delete(auth.getToken());

        response.then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void register_shouldReturnStatus400_whenEmailIsInvalidTest() {
        RegisterUserRequestData mockRequest = UserTestsHelper.createRegisterRequestData(3);
        mockRequest.setEmail("");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(mockRequest)
                .when()
                .post(UserTestsHelper.PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            UserTestsHelper.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenPasswordIsInvalidTest() {
        RegisterUserRequestData mockRequest = UserTestsHelper.createRegisterRequestData(4);
        mockRequest.setPassword("");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(mockRequest)
                .when()
                .post(UserTestsHelper.PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            UserTestsHelper.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenFirstNameIsInvalidTest() {
        RegisterUserRequestData mockRequest = UserTestsHelper.createRegisterRequestData(5);
        mockRequest.setFirstName("");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(mockRequest)
                .when()
                .post(UserTestsHelper.PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            UserTestsHelper.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenLastNameIsInvalidTest() {
        RegisterUserRequestData requestBody = UserTestsHelper.createRegisterRequestData(6);
        requestBody.setLastName("");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            UserTestsHelper.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenEmailIsMissingTest() {
        RegisterUserRequestData requestBody = UserTestsHelper.createRegisterRequestData(7);
        requestBody.setEmail(null);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            UserTestsHelper.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenPasswordIsMissingTest() {
        RegisterUserRequestData requestBody = UserTestsHelper.createRegisterRequestData(8);
        requestBody.setPassword(null);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            UserTestsHelper.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenFirstNameIsMissingTest() {
        RegisterUserRequestData requestBody = UserTestsHelper.createRegisterRequestData(9);
        requestBody.setFirstName(null);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            UserTestsHelper.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void register_shouldReturnStatus400_whenLastNameIsMissingTest() {
        RegisterUserRequestData requestBody = UserTestsHelper.createRegisterRequestData(10);
        requestBody.setLastName(null);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_REGISTER);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            UserTestsHelper.delete(response.as(AuthenticationResponseData.class).getToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn200_whenUserExistTest() {
        RegisterUserRequestData mockRequest = UserTestsHelper.createRegisterRequestData(11);
        AuthenticationResponseData auth = UserTestsHelper.registerAndAuthenticate(mockRequest);

        AuthenticationRequestData requestBody = UserTestsHelper.createAuthenticationRequestData(11);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_AUTHENTICATE);

        UserTestsHelper.delete(auth.getToken());

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("token", notNullValue());
    }

    @Test
    void authenticate_shouldReturn401_whenUserNotExistTest() {
        AuthenticationRequestData requestBody = UserTestsHelper.createAuthenticationRequestData();
        requestBody.setEmail("dontExist@test.com");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void authenticate_shouldReturn400_whenEmailIsInvalidTest() {
        AuthenticationRequestData requestBody = UserTestsHelper.createAuthenticationRequestData();
        requestBody.setEmail("");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn400_whenPasswordIsInvalidTest() {
        AuthenticationRequestData requestBody = UserTestsHelper.createAuthenticationRequestData();
        requestBody.setPassword("");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn400_whenEmailIsMissingTest() {
        AuthenticationRequestData requestBody = UserTestsHelper.createAuthenticationRequestData();
        requestBody.setEmail(null);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn400_whenPasswordIsMissingTest() {
        AuthenticationRequestData requestBody = UserTestsHelper.createAuthenticationRequestData();
        requestBody.setPassword(null);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void authenticate_shouldReturn400_whenPasswordLengthIsLessThan8Test() {
        AuthenticationRequestData requestBody = UserTestsHelper.createAuthenticationRequestData();
        requestBody.setPassword("1234567");

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(UserTestsHelper.PATH_USER_AUTHENTICATE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    void delete_shouldReturnStatus204_whenTokenIsValidTest() {
        RegisterUserRequestData requestBody = UserTestsHelper.createRegisterRequestData(12);
        AuthenticationResponseData auth = UserTestsHelper.registerAndAuthenticate(requestBody);

        given()
                .header("Authorization", "Bearer " + auth.getToken())
                .when()
                .delete(UserTestsHelper.PATH_USER)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void delete_shouldReturnStatus401_whenTokenIsInvalidTest() {
        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .when()
                .delete(UserTestsHelper.PATH_USER)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void delete_shouldReturnStatus403_whenTokenIsMissingTest() {
        given()
                .when()
                .delete(UserTestsHelper.PATH_USER)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

}