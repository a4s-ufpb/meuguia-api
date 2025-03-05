package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.RegisterUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.dto.AttractionTypeRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTypeTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionTypeRequestUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionTypeRequestUtil.PATH_ATTRACTION_TYPE;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class AttractionTypeControllerTest extends MeuguiaApiApplicationTests {
    private final AttractionTypeTestHelper attractionTypeTestHelper = new AttractionTypeTestHelper();
    private final AttractionTypeRequestUtil requestUtil = new AttractionTypeRequestUtil();

    private String token;

    @BeforeAll
    void setUP() {
        RegisterUserRequestData registerUserRequestData = authenticationTestHelper.getRegisterRequestData(80);
        AuthenticationResponseData authenticationResponseData = userRequestUtil.registerAndAuthenticate(registerUserRequestData);
        token = authenticationResponseData.getToken();
    }

    @AfterAll
    void tearDown() {
        userRequestUtil.delete(token);
    }

    @Test
    void create_shouldReturn201_whenAttractionTypeIsValidAndUserIsAuthenticatedTest() {
        AttractionTypeRequestData requestBody = attractionTypeTestHelper.createAttractionTypeRequestData(1);

        Response response = given()
                .header("Authorization", "Bearer "+ token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_ATTRACTION_TYPE);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            requestUtil.delete(response.as(AttractionType.class), token);
        }

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", equalTo(requestBody.getName()))
                .body("description", equalTo(requestBody.getDescription()))
                .extract().as(AttractionType.class);
    }

    @Test
    void create_shouldReturn401_whenTokenInvalidTest() {
        AttractionTypeRequestData requestBody = attractionTypeTestHelper.createAttractionTypeRequestData(2);

        Response response = given()
                .header("Authorization", "Bearer "+ INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_ATTRACTION_TYPE);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            requestUtil.delete(response.as(AttractionType.class), token);
        }

        response.then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void create_shouldReturn401_whenUserNotAuthenticatedTest() {
        AttractionTypeRequestData requestBody = attractionTypeTestHelper.createAttractionTypeRequestData(3);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_ATTRACTION_TYPE);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            requestUtil.delete(response.as(AttractionType.class), token);
        }

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void create_shouldReturn400_whenAttractionTypeNameIsMissingTest() {
        AttractionTypeRequestData requestBody = attractionTypeTestHelper.createAttractionTypeRequestData(4);
        requestBody.setName(null);

        Response response = given()
                .header("Authorization", "Bearer "+token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_ATTRACTION_TYPE);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            requestUtil.delete(response.as(AttractionType.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenAttractionTypeDescriptionIsMissingTest() {
        AttractionTypeRequestData requestBody = attractionTypeTestHelper.createAttractionTypeRequestData(5);
        requestBody.setDescription(null);

        Response response = given()
                .header("Authorization", "Bearer "+token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_ATTRACTION_TYPE);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            requestUtil.delete(response.as(AttractionType.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    void create_shouldReturn400_whenAttractionTypeNameIsInvalidTest() {
        AttractionTypeRequestData requestBody = attractionTypeTestHelper.createAttractionTypeRequestData(4);
        requestBody.setName("");

        Response response = given()
                .header("Authorization", "Bearer "+token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_ATTRACTION_TYPE);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            requestUtil.delete(response.as(AttractionType.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void findAll_shouldReturn200AndListWith2Items_whenAttractionTypeExistsTest() {
        AttractionTypeRequestData requestBody1 = attractionTypeTestHelper.createAttractionTypeRequestData(6);
        AttractionTypeRequestData requestBody2 = attractionTypeTestHelper.createAttractionTypeRequestData(7);

        AttractionType attractionType1 = requestUtil.post(requestBody1, token);
        AttractionType attractionType2 = requestUtil.post(requestBody2, token);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_ATTRACTION_TYPE);

        requestUtil.delete(attractionType1, token);
        requestUtil.delete(attractionType2, token);

        response.then()
                .body("size()", is(2))
                .body("id.flatten()", hasItems(attractionType1.getId().intValue(), attractionType2.getId().intValue()))
                .body("name.flatten()", hasItems(attractionType1.getName(), attractionType2.getName()))
                .body("description.flatten()", hasItems(attractionType1.getDescription(), attractionType2.getDescription()))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void findAll_shouldReturn200andEmptyList_whenThereNoAttractionTypeTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_ATTRACTION_TYPE)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void delete_shouldReturn204_whenAttractionTypeIsDeletedTest() {
        AttractionTypeRequestData request = attractionTypeTestHelper.createAttractionTypeRequestData(8);
        AttractionType response = requestUtil.post(request, token);

        given()
                .header("Authorization", "Bearer "+ token)
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_ATTRACTION_TYPE+"/"+response.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void delete_shouldReturn403_whenTokenIsMissingTest() {
        AttractionTypeRequestData request = attractionTypeTestHelper.createAttractionTypeRequestData(9);
        AttractionType attractionType = requestUtil.post(request, token);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_ATTRACTION_TYPE+"/"+attractionType.getId());

        requestUtil.delete(attractionType, token);

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void delete_shouldReturn401_whenTokenInvalidTest() {
        AttractionTypeRequestData request = attractionTypeTestHelper.createAttractionTypeRequestData(10);
        AttractionType attractionType = requestUtil.post(request, token);

        Response response = given()
                .header("Authorization", "Bearer "+ INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_ATTRACTION_TYPE+"/"+attractionType.getId());

        requestUtil.delete(attractionType, token);

        response.then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void delete_shouldReturn404_whenAttractionTypeDoesNotExistTest() {
        given()
                .header("Authorization", "Bearer "+ token)
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_ATTRACTION_TYPE+"/"+INVALID_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
