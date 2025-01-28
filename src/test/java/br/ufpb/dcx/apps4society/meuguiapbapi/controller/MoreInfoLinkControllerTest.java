package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.RegisterUserRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.MoreInfoLinkRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MoreInfoLinkTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.MoreInfoLinkRequestUtil;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.MoreInfoLinkRequestUtil.PATH_MORE_INFO_LINK;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class MoreInfoLinkControllerTest extends MeuguiaApiApplicationTests {
    private final MoreInfoLinkTestHelper moreInfoLinkTestHelper = new MoreInfoLinkTestHelper();
    private final MoreInfoLinkRequestUtil moreInfoLinkRequestUtil = new MoreInfoLinkRequestUtil();

    @Autowired
    private Environment env;

    private String token;

    @BeforeAll
    void setUp() {
        RegisterUserRequestData request = authenticationTestHelper.getRegisterRequestData(70);
        AuthenticationResponseData response = userRequestUtil.registerAndAuthenticate(request);
        token = response.getToken();
    }

    @AfterAll
    void tearDown() {
        userRequestUtil.delete(token);
    }

    @Test
    void create_shouldReturn201_whenMoreInfoLinkDataIsValidTest() {
        MoreInfoLinkRequestData requestBody = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post(PATH_MORE_INFO_LINK);

        if (response.statusCode() == HttpStatus.CREATED.value()) {
            moreInfoLinkRequestUtil.delete(response.as(MoreInfoLink.class), token);
        }

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("link", equalTo(requestBody.getLink()))
                .body("description", equalTo(requestBody.getDescription()))
                .extract()
                .as(MoreInfoLink.class);
    }

    @Test
    void create_shouldReturn400_whenMoreInfoLinkLinkIsNullTest() {
        MoreInfoLinkRequestData requestBody = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(2);
        requestBody.setLink(null);

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post(PATH_MORE_INFO_LINK);

        if (response.statusCode() == HttpStatus.CREATED.value()) {
            moreInfoLinkRequestUtil.delete(response.as(MoreInfoLink.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenMoreInfoLinkDescriptionIsNullTest() {
        MoreInfoLinkRequestData requestBody = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(3);
        requestBody.setDescription(null);

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post(PATH_MORE_INFO_LINK);

        if (response.statusCode() == HttpStatus.CREATED.value()) {
            moreInfoLinkRequestUtil.delete(response.as(MoreInfoLink.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    void create_shouldReturn400_whenMoreInfoLinkLinkIsEmptyTest() {
        MoreInfoLinkRequestData requestBody = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(4);
        requestBody.setLink("");

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post(PATH_MORE_INFO_LINK);

        if (response.statusCode() == HttpStatus.CREATED.value()) {
            moreInfoLinkRequestUtil.delete(response.as(MoreInfoLink.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenMoreInfoLinkLinkIsInvalidTest() {
        MoreInfoLinkRequestData requestBody = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(5);
        requestBody.setLink("invalidLink");

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post(PATH_MORE_INFO_LINK);

        if (response.statusCode() == HttpStatus.CREATED.value()) {
            moreInfoLinkRequestUtil.delete(response.as(MoreInfoLink.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    void findById_shouldReturn200_whenMoreInfoLinkExistsTest() {
        MoreInfoLinkRequestData requestBody = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(8);
        MoreInfoLink infoLink = moreInfoLinkRequestUtil.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(PATH_MORE_INFO_LINK+"/"+infoLink.getId());

        moreInfoLinkRequestUtil.delete(infoLink, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(infoLink.getId().intValue()))
                .body("link", equalTo(infoLink.getLink()))
                .body("description", equalTo(infoLink.getDescription()));
    }

    @Test
    void findById_shouldReturn404_whenMoreInfoLinkDoesNotExistTest() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(PATH_MORE_INFO_LINK+"/"+INVALID_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void delete_shouldReturn204_whenTokenIsValidTest() {
        MoreInfoLinkRequestData requestBody = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(9);
        MoreInfoLink response = moreInfoLinkRequestUtil.post(requestBody, token);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_MORE_INFO_LINK+"/"+response.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void delete_shouldReturn401_whenTokenIsInvalidTest() {
       MoreInfoLinkRequestData requestBody = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(10);
       MoreInfoLink response = moreInfoLinkRequestUtil.post(requestBody, token);

       given()
               .contentType("application/json")
               .header("Authorization", "Bearer " + INVALID_TOKEN)
               .when()
               .delete(PATH_MORE_INFO_LINK+"/"+response.getId())
               .then()
               .statusCode(HttpStatus.UNAUTHORIZED.value());

       moreInfoLinkRequestUtil.delete(response, token);
    }

    @Test
    void delete_shouldReturn403_whenTokenIsMissingTest() {
        MoreInfoLinkRequestData requestBody = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(11);
        MoreInfoLink response = moreInfoLinkRequestUtil.post(requestBody, token);

        given()
                .contentType("application/json")
                .when()
                .delete(PATH_MORE_INFO_LINK+"/"+response.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        moreInfoLinkRequestUtil.delete(response, token);
    }

    @Test
    void delete_shouldReturn404_whenMoreInfoLinkDoesNotExistTest() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_MORE_INFO_LINK+"/"+INVALID_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}