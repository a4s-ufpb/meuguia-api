package br.ufpb.dcx.apps4society.meuguiapbapi.integration;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationRequestData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper.PATH_TOURISM_SEGMENTATION;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper.createTourismSegmentationRequestData;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class TourismSegmentationControllerTest extends MeuguiaApiApplicationTests {

    @Test
    void create_shouldReturn201_whenTourismSegmentationDataIsValidTest() {
        TourismSegmentationRequestData requestBody = createTourismSegmentationRequestData(1);

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_TOURISM_SEGMENTATION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            TourismSegmentationTestHelper.delete(response.as(TourismSegmentation.class), getAdminToken());
        }

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", equalTo(requestBody.getName()))
                .body("description", equalTo(requestBody.getDescription()))
                .extract()
                .as(TourismSegmentation.class);
    }

    @Test
    void create_shouldReturn403_whenUserIsNotAuthenticatedTest() {
        TourismSegmentationRequestData requestBody = createTourismSegmentationRequestData(2);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_TOURISM_SEGMENTATION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            TourismSegmentationTestHelper.delete(response.as(TourismSegmentation.class), getDefaultToken());
        }

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void create_shouldReturn401_whenTokenIsNotValidTest() {
        TourismSegmentationRequestData requestBody = createTourismSegmentationRequestData(3);

        Response response = given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_TOURISM_SEGMENTATION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            TourismSegmentationTestHelper.delete(response.as(TourismSegmentation.class), getDefaultToken());
        }

        response.then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void create_shouldReturn400_whenTourismSegmentationNameIsInvalidTest() {
        TourismSegmentationRequestData requestBody = createTourismSegmentationRequestData(4);
        requestBody.setName("");

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_TOURISM_SEGMENTATION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            TourismSegmentationTestHelper.delete(response.as(TourismSegmentation.class), getDefaultToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenTourismSegmentationNameIsNullTest() {
        TourismSegmentationRequestData requestBody = createTourismSegmentationRequestData(5);
        requestBody.setName(null);

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_TOURISM_SEGMENTATION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            TourismSegmentationTestHelper.delete(response.as(TourismSegmentation.class), getDefaultToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenTourismSegmentationDescriptionNullTest() {
        TourismSegmentationRequestData requestBody = createTourismSegmentationRequestData(7);
        requestBody.setDescription(null);

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PATH_TOURISM_SEGMENTATION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            TourismSegmentationTestHelper.delete(response.as(TourismSegmentation.class), getDefaultToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void findById_shouldReturn200_whenTourismSegmentationExistsTest() {
        TourismSegmentationRequestData requestBody = createTourismSegmentationRequestData(8);
        TourismSegmentation segmentation = TourismSegmentationTestHelper.post(requestBody, getDefaultToken());

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_TOURISM_SEGMENTATION + "/" + segmentation.getId());

        TourismSegmentationTestHelper.delete(segmentation, getAdminToken());

        response.then()
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
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void findAll_shouldReturn200and2Items_whenTourismSegmentationExistsTest() {
        TourismSegmentationRequestData request1 = createTourismSegmentationRequestData(9);
        TourismSegmentationRequestData request2 = createTourismSegmentationRequestData(10);

        TourismSegmentation segmentation1 = TourismSegmentationTestHelper.post(request1, getDefaultToken());
        TourismSegmentation segmentation2 = TourismSegmentationTestHelper.post(request2, getDefaultToken());

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_TOURISM_SEGMENTATION);

        TourismSegmentationTestHelper.delete(segmentation1, getAdminToken());
        TourismSegmentationTestHelper.delete(segmentation2, getAdminToken());

        response.then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", is(2))
                .body("content.id.flatten()", hasItems(segmentation1.getId().intValue(), segmentation2.getId().intValue()))
                .body("content.name.flatten()", hasItems(segmentation1.getName(), segmentation2.getName()))
                .body("content.description.flatten()", hasItems(segmentation1.getDescription(), segmentation2.getDescription()));
    }

    @Test
    void findAll_shouldReturn200AndEmptyList_whenTourismSegmentationNotExistsTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_TOURISM_SEGMENTATION)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content", empty())
                .body("totalElements", is(0));

    }

    @Test
    void delete_shouldReturn204_whenTourismSegmentationExistsAdminUserTest() {
        TourismSegmentationRequestData requestBody = createTourismSegmentationRequestData(11);
        TourismSegmentation response = TourismSegmentationTestHelper.post(requestBody, getDefaultToken());

        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + response.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void delete_shouldReturn403_whenTourismSegmentationExistsDefaultUserTest() {
        TourismSegmentationRequestData requestBody = createTourismSegmentationRequestData(11);
        TourismSegmentation savedTourismSegmentation = TourismSegmentationTestHelper.post(requestBody, getDefaultToken());

        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + savedTourismSegmentation.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        TourismSegmentationTestHelper.delete(savedTourismSegmentation, getAdminToken());
    }

    @Test
    void delete_shouldReturn403_whenUserIsNotAuthenticated() {
        TourismSegmentationRequestData requestBody = createTourismSegmentationRequestData(12);
        TourismSegmentation response = TourismSegmentationTestHelper.post(requestBody, getDefaultToken());

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + response.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        TourismSegmentationTestHelper.delete(response, getAdminToken());
    }

    @Test
    void delete_shouldReturn401_whenTokenIsInvalidTest() {
        TourismSegmentationRequestData requestBody = createTourismSegmentationRequestData(13);
        TourismSegmentation response = TourismSegmentationTestHelper.post(requestBody, getDefaultToken());

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + response.getId())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

        TourismSegmentationTestHelper.delete(response, getAdminToken());
    }

    @Test
    void delete_shouldReturn403_whenTourismSegmentationDoesNotExistDefaultUserTest() {
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void delete_shouldReturn404_whenTourismSegmentationDoesNotExistAdminUserTest() {
        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .when()
                .delete(PATH_TOURISM_SEGMENTATION + "/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
