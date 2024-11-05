package br.ufpb.dcx.apps4society.meuguiapbapi.controller;


import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationForm;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterForm;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.UserRequestUtil.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Slf4j
public class AuthenticationControllerTest extends MeuguiaApiApplicationTests {

    @Test
    void register_shouldReturnStatus201_whenDataIsValidTest() {
        RegisterForm requestBody = mockAuthentication.mockRequest(1);

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
        RegisterForm requestBody = mockAuthentication.mockRequest(2);
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
        RegisterForm mockRequest = mockAuthentication.mockRequest(3);
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
        RegisterForm mockRequest = mockAuthentication.mockRequest(4);
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
        RegisterForm mockRequest = mockAuthentication.mockRequest(5);
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
        RegisterForm requestBody = mockAuthentication.mockRequest(6);
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
        RegisterForm requestBody = mockAuthentication.mockRequest(7);
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
        RegisterForm requestBody = mockAuthentication.mockRequest(8);
        requestBody.setPassword(null);

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
    void register_shouldReturnStatus400_whenFirstNameIsMissingTest() {
        RegisterForm requestBody = mockAuthentication.mockRequest(9);
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
        RegisterForm requestBody = mockAuthentication.mockRequest(10);
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
        RegisterForm mockRequest = mockAuthentication.mockRequest(11);
        userRequestUtil.register(mockRequest);

        AuthenticationForm requestBody = mockAuthentication.mockAuthentication();
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
        AuthenticationForm requestBody = mockAuthentication.mockAuthentication();

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
        AuthenticationForm requestBody = mockAuthentication.mockAuthentication();
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
        AuthenticationForm requestBody = mockAuthentication.mockAuthentication();
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
        AuthenticationForm requestBody = mockAuthentication.mockAuthentication();
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
        AuthenticationForm requestBody = mockAuthentication.mockAuthentication();
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
        AuthenticationForm requestBody = mockAuthentication.mockAuthentication();
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
        RegisterForm requestBody = mockAuthentication.mockRequest(12);
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