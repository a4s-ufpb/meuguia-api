package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.RegisterRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.TourismSegmentationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MockTouristSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.TourismSegmentationRequestUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.TourismSegmentationRequestUtil.PATH_TOURISM_SEGMENTATION;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TourismSegmentationControllerTest extends MeuguiaApiApplicationTests {
    private final MockTouristSegmentation mockTouristSegmentation = new MockTouristSegmentation();
    private final TourismSegmentationRequestUtil requestUtil = new TourismSegmentationRequestUtil();
    private String token;

    @BeforeEach
    void setup(){
        RegisterRequestData request = mockAuthentication.mockRequest(60);
        AuthenticationResponseData response = userRequestUtil.register(request);
        token = response.getToken();
    }

    @AfterEach
    void tearDown() {
        userRequestUtil.delete(token);
    }

    @Test
    void create_shouldReturn201_whenTourismSegmentationDataIsValidTest() {
        TourismSegmentationRequestData requestBody = mockTouristSegmentation.mockRequest(1);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_TOURISM_SEGMENTATION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            requestUtil.delete(response.as(TourismSegmentation.class), token);
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", equalTo(requestBody.getName()))
                .body("description", equalTo(requestBody.getDescription()))
                .extract()
                .as(TourismSegmentation.class);
    }

    @Test
    void create_shouldReturn403_whenUserIsNotAuthenticatedTest() {
        TourismSegmentationRequestData requestBody = mockTouristSegmentation.mockRequest(2);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_TOURISM_SEGMENTATION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            requestUtil.delete(response.as(TourismSegmentation.class), token);
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void create_shouldReturn401_whenTokenIsNotValidTest() {
        TourismSegmentationRequestData requestBody = mockTouristSegmentation.mockRequest(3);

        Response response = given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_TOURISM_SEGMENTATION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            requestUtil.delete(response.as(TourismSegmentation.class), token);
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void create_shouldReturn400_whenTourismSegmentationNameIsInvalidTest() {
        TourismSegmentationRequestData requestBody = mockTouristSegmentation.mockRequest(4);
        requestBody.setName("");

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_TOURISM_SEGMENTATION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            requestUtil.delete(response.as(TourismSegmentation.class), token);
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenTourismSegmentationNameIsNullTest() {
        TourismSegmentationRequestData requestBody = mockTouristSegmentation.mockRequest(5);
        requestBody.setName(null);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_TOURISM_SEGMENTATION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            requestUtil.delete(response.as(TourismSegmentation.class), token);
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenTourismSegmentationDescriptionNullTest() {
        TourismSegmentationRequestData requestBody = mockTouristSegmentation.mockRequest(7);
        requestBody.setDescription(null);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_TOURISM_SEGMENTATION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            requestUtil.delete(response.as(TourismSegmentation.class), token);
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void findById_shouldReturn200_whenTourismSegmentationExistsTest() {
        TourismSegmentationRequestData requestBody = mockTouristSegmentation.mockRequest(8);
        TourismSegmentation segmentation = requestUtil.post(requestBody, token);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_TOURISM_SEGMENTATION + "/" + segmentation.getId());

        requestUtil.delete(segmentation, token);

        response.then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(segmentation.getId().intValue()))
                .body("name", equalTo(segmentation.getName()))
                .body("description", equalTo(segmentation.getDescription()))
                .extract()
                .as(TourismSegmentation.class);
    }

    @Test
    void findById_shouldReturn404_whenTourismSegmentationDoesNotExistTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_TOURISM_SEGMENTATION + "/" + INVALID_ID)
                .then()
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void findAll_shouldReturn200and2Items_whenTourismSegmentationExistsTest() {
        TourismSegmentationRequestData request1 = mockTouristSegmentation.mockRequest(9);
        TourismSegmentationRequestData request2 = mockTouristSegmentation.mockRequest(10);

        TourismSegmentation segmentation1 = requestUtil.post(request1, token);
        TourismSegmentation segmentation2 = requestUtil.post(request2, token);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_TOURISM_SEGMENTATION);

        requestUtil.delete(segmentation1, token);
        requestUtil.delete(segmentation2, token);

        response.then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("id.flatten()", hasItems(segmentation1.getId().intValue(), segmentation2.getId().intValue()))
                .body("name.flatten()", hasItems(segmentation1.getName(), segmentation2.getName()))
                .body("description.flatten()", hasItems(segmentation1.getDescription(), segmentation2.getDescription()));
    }

    @Test
    void findAll_shouldReturn200AndEmptyList_whenTourismSegmentationNotExistsTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_TOURISM_SEGMENTATION)
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));

    }

    @Test
    void delete_shouldReturn204_whenTourismSegmentationExistsTest() {
        TourismSegmentationRequestData requestBody = mockTouristSegmentation.mockRequest(11);
        TourismSegmentation response = requestUtil.post(requestBody, token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + response.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void delete_shouldReturn403_whenUserIsNotAuthenticated() {
        TourismSegmentationRequestData requestBody = mockTouristSegmentation.mockRequest(12);
        TourismSegmentation response = requestUtil.post(requestBody, token);

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + response.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());

        requestUtil.delete(response, token);
    }

    @Test
    void delete_shouldReturn401_whenTokenIsInvalidTest() {
        TourismSegmentationRequestData requestBody = mockTouristSegmentation.mockRequest(13);
        TourismSegmentation response = requestUtil.post(requestBody, token);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + response.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

        requestUtil.delete(response, token);
    }

    @Test
    void delete_shouldReturn404_whenTourismSegmentationDoesNotExistTest() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + INVALID_ID)
                .then()
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
