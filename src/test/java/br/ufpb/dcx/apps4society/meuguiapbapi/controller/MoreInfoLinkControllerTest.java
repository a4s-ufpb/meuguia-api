package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterForm;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.dtos.MoreInfoLinkForm;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MockMoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.MoreInfoLinkRequestUtil;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.MoreInfoLinkRequestUtil.PATH_MORE_INFO_LINK;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MoreInfoLinkControllerTest extends MeuguiaApiApplicationTests {
    private final MockMoreInfoLink mockMoreInfoLink = new MockMoreInfoLink();
    private final MoreInfoLinkRequestUtil moreInfoLinkRequestUtil = new MoreInfoLinkRequestUtil();

    @Autowired
    private Environment env;

    private String token;

    @BeforeAll
    void setUp() {
        RegisterForm request = mockAuthentication.mockRequest(70);
        AuthenticationResponse response = userRequestUtil.register(request);
        token = response.getToken();
    }

    @AfterAll
    void tearDown() {
        userRequestUtil.delete(token);
    }

    @Test
    void create_shouldReturn201_whenMoreInfoLinkDataIsValidTest() {
        MoreInfoLinkForm requestBody = mockMoreInfoLink.mockRequest(1);

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
                .log().body()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("link", equalTo(requestBody.getLink()))
                .body("description", equalTo(requestBody.getDescription()))
                .extract()
                .as(MoreInfoLink.class);
    }

    @Test
    void create_shouldReturn400_whenMoreInfoLinkLinkIsNullTest() {
        MoreInfoLinkForm requestBody = mockMoreInfoLink.mockRequest(2);
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
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenMoreInfoLinkDescriptionIsNullTest() {
        MoreInfoLinkForm requestBody = mockMoreInfoLink.mockRequest(3);
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
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    void create_shouldReturn400_whenMoreInfoLinkLinkIsEmptyTest() {
        MoreInfoLinkForm requestBody = mockMoreInfoLink.mockRequest(4);
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
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenMoreInfoLinkLinkIsInvalidTest() {
        MoreInfoLinkForm requestBody = mockMoreInfoLink.mockRequest(5);
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
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void findAll_shouldReturn200_whenMoreInfoLinkExistsTest() {
        Assumptions.assumeTrue(env.matchesProfiles("test-dev"),
                "Test only runs when 'test-dev' profile is active");

        MoreInfoLinkForm requestBody1 = mockMoreInfoLink.mockRequest(6);
        MoreInfoLinkForm requestBody2 = mockMoreInfoLink.mockRequest(7);
        MoreInfoLink infoLink1 = moreInfoLinkRequestUtil.post(requestBody1, token);
        MoreInfoLink infoLink2 = moreInfoLinkRequestUtil.post(requestBody2, token);

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(PATH_MORE_INFO_LINK);

        moreInfoLinkRequestUtil.delete(infoLink1, token);
        moreInfoLinkRequestUtil.delete(infoLink2, token);

        response.then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("id.flatten()", hasItems(infoLink1.getId().intValue(), infoLink2.getId().intValue()))
                .body("link.flatten()", hasItems(infoLink1.getLink(), infoLink1.getLink()))
                .body("description.flatten()", hasItems(infoLink1.getDescription(), infoLink1.getDescription()));
}

    @Test
    void findById_shouldReturn200_whenMoreInfoLinkExistsTest() {
        MoreInfoLinkForm requestBody = mockMoreInfoLink.mockRequest(8);
        MoreInfoLink infoLink = moreInfoLinkRequestUtil.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get(PATH_MORE_INFO_LINK+"/"+infoLink.getId());

        moreInfoLinkRequestUtil.delete(infoLink, token);

        response.then()
                .log().body()
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
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void delete_shouldReturn204_whenTokenIsValidTest() {
        MoreInfoLinkForm requestBody = mockMoreInfoLink.mockRequest(9);
        MoreInfoLink response = moreInfoLinkRequestUtil.post(requestBody, token);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_MORE_INFO_LINK+"/"+response.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void delete_shouldReturn401_whenTokenIsInvalidTest() {
       MoreInfoLinkForm requestBody = mockMoreInfoLink.mockRequest(10);
       MoreInfoLink response = moreInfoLinkRequestUtil.post(requestBody, token);

       given()
               .contentType("application/json")
               .header("Authorization", "Bearer " + INVALID_TOKEN)
               .when()
               .delete(PATH_MORE_INFO_LINK+"/"+response.getId())
               .then()
               .log().body()
               .statusCode(HttpStatus.UNAUTHORIZED.value());

       moreInfoLinkRequestUtil.delete(response, token);
    }

    @Test
    void delete_shouldReturn403_whenTokenIsMissingTest() {
        MoreInfoLinkForm requestBody = mockMoreInfoLink.mockRequest(11);
        MoreInfoLink response = moreInfoLinkRequestUtil.post(requestBody, token);

        given()
                .contentType("application/json")
                .when()
                .delete(PATH_MORE_INFO_LINK+"/"+response.getId())
                .then()
                .log().body()
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
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}