package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterRequest;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MockAttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionTypeRequestUtil;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionTypeRequestUtil.PATH_ATTRACTION_TYPE;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AttractionTypeControllerTest extends MeuguiaApiApplicationTests {
    private final MockAttractionType mockAttractionType = new MockAttractionType();
    private final AttractionTypeRequestUtil requestUtil = new AttractionTypeRequestUtil();

    private String token;

    @BeforeAll
    void setUP() {
        RegisterRequest registerRequest = mockAuthentication.mockRequest(80);
        AuthenticationResponse authenticationResponse = userRequestUtil.register(registerRequest);
        token = authenticationResponse.getToken();
    }

    @AfterAll
    void tearDown() {
        userRequestUtil.delete(token);
    }

    @Test
    void create_shouldReturn201_whenAttractionTypeIsValidAndUserIsAuthenticatedTest() {
        AttractionType requestBody = mockAttractionType.mockRequest(1);

        AttractionType response = given()
                .header("Authorization", "Bearer "+ token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_ATTRACTION_TYPE)
                .then()
                .log().body()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", equalTo(requestBody.getName()))
                .body("description", equalTo(requestBody.getDescription()))
                .extract().as(AttractionType.class);

        requestUtil.delete(response, token);
    }

    @Test
    void create_shouldReturn401_whenTokenInvalidTest() {
        AttractionType requestBody = mockAttractionType.mockRequest(2);

        given()
                .header("Authorization", "Bearer "+ INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_ATTRACTION_TYPE)
                .then()
                .log().body()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void create_shouldReturn401_whenUserNotAuthenticatedTest() {
        AttractionType requestBody = mockAttractionType.mockRequest(3);

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_ATTRACTION_TYPE)
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void create_shouldReturn400_whenAttractionTypeNameIsInvalidTest() {
        AttractionType requestBody = mockAttractionType.mockRequest(4);
        requestBody.setName(null);

        given()
                .header("Authorization", "Bearer "+token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_ATTRACTION_TYPE)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenAttractionTypeDescriptionIsInvalidTest() {
        AttractionType requestBody = mockAttractionType.mockRequest(5);
        requestBody.setDescription(null);

        given()
                .header("Authorization", "Bearer "+token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_ATTRACTION_TYPE)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void findAll_shouldReturn200AndListWith2Items_whenAttractionTypeExistsTest() {
        AttractionType requestBody1 = mockAttractionType.mockRequest(6);
        AttractionType requestBody2 = mockAttractionType.mockRequest(7);

        AttractionType attractionType1 = requestUtil.post(requestBody1, token);
        AttractionType attractionType2 = requestUtil.post(requestBody2, token);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_ATTRACTION_TYPE)
                .then()
                .log().body()
                .body("size()", is(2))
                .body("id.flatten()", hasItems(attractionType1.getId().intValue(), attractionType2.getId().intValue()))
                .body("name.flatten()", hasItems(attractionType1.getName(), attractionType2.getName()))
                .body("description.flatten()", hasItems(attractionType1.getDescription(), attractionType2.getDescription()))
                .statusCode(HttpStatus.OK.value());

        requestUtil.delete(attractionType1, token);
        requestUtil.delete(attractionType2, token);
    }

    @Test
    void findAll_shouldReturn200andEmptyList_whenThereNoAttractionTypeTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_ATTRACTION_TYPE)
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void delete_shouldReturn204_whenAttractionTypeIsDeletedTest() {
        AttractionType request = mockAttractionType.mockRequest(8);
        AttractionType response = requestUtil.post(request, token);

        given()
                .header("Authorization", "Bearer "+ token)
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_ATTRACTION_TYPE+"/"+response.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void delete_shouldReturn403_whenTokenIsMissingTest() {
        AttractionType request = mockAttractionType.mockRequest(9);
        AttractionType response = requestUtil.post(request, token);

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_ATTRACTION_TYPE+"/"+response.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());

        requestUtil.delete(response, token);
    }

    @Test
    void delete_shouldReturn401_whenTokenInvalidTest() {
        AttractionType request = mockAttractionType.mockRequest(10);
        AttractionType response = requestUtil.post(request, token);

        given()
                .header("Authorization", "Bearer "+ INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_ATTRACTION_TYPE+"/"+response.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

        requestUtil.delete(response, token);
    }

    @Test
    void delete_shouldReturn404_whenAttractionTypeDoesNotExistTest() {
        given()
                .header("Authorization", "Bearer "+ token)
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_ATTRACTION_TYPE+"/"+INVALID_ID)
                .then()
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

}
