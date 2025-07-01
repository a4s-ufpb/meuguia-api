package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.dto.AttractionTypeRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.CityTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationRequestData;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTestHelper.createAttractionRequestData;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper.createAttractionTypeRequestData;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper.createMoreInfoLinkRequestData;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper.createTourismSegmentationRequestData;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AttractionControllerTest extends MeuguiaApiApplicationTests {
    private final Logger log = LoggerFactory.getLogger(AttractionControllerTest.class);

    @Autowired
    private CityTestHelper cityTestHelper;

    private TourismSegmentation segmentation;
    private AttractionType attractionType;
    private MoreInfoLinkRequestData moreInfoLinkRequestData;


    @BeforeAll
    void setUp() {
        cityTestHelper.createCity();
    }

    @AfterAll
    void tearDown() {
        cityTestHelper.deleteLastCityCreated();
    }

    @BeforeEach
    void setUpEach() {
        TourismSegmentationRequestData segmentationRequest = createTourismSegmentationRequestData(90);
        AttractionTypeRequestData attractionTypeRequest = createAttractionTypeRequestData(90);
        this.moreInfoLinkRequestData = createMoreInfoLinkRequestData(90);

        segmentation = TourismSegmentationTestHelper.post(segmentationRequest, getDefaultToken());
        attractionType = AttractionTypeTestHelper.post(attractionTypeRequest, getDefaultToken());
    }

    @AfterEach
    void tearDownEach() {
        TourismSegmentationTestHelper.delete(segmentation, getAdminToken());
        AttractionTypeTestHelper.delete(attractionType, getAdminToken());
    }

    @Test
    void create_shouldReturn201_whenIsAValidAttractionTest() {
        AttractionRequestData requestBody = createAttractionRequestData(1, segmentation, moreInfoLinkRequestData, attractionType);

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        AttractionTestHelper.delete(response.getBody().jsonPath().getLong("id"), getAdminToken());

        response.then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(requestBody.getName()))
                .body("description", equalTo(requestBody.getDescription()))
                .body("mapLink", equalTo(requestBody.getMapLink()))
                .body("city.id", equalTo(requestBody.getCityId().intValue()))
                .body("imageLink", equalTo(requestBody.getImageLink()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attractionType.id", equalTo(attractionType.getId().intValue()))
                .body("moreInfoLinks[0].link", equalTo(moreInfoLinkRequestData.getLink()));
    }

    @Test
    void create_shouldReturn400_whenHasNullValuesTest() {
        Attraction requestBody = new Attraction();

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            AttractionTestHelper.delete(response.as(Attraction.class), getDefaultToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenImportantFieldsIsEmptyTest() {
        AttractionRequestData requestBody = createAttractionRequestData(2, segmentation, moreInfoLinkRequestData, attractionType);
        requestBody.setName("");

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            AttractionTestHelper.delete(response.as(Attraction.class), getDefaultToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn404_whenAttractionTypeNotExistsTest() {
        AttractionRequestData requestBody = createAttractionRequestData(3, segmentation, moreInfoLinkRequestData, attractionType);
        requestBody.setAttractionType(INVALID_ID);

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            AttractionTestHelper.delete(response.as(Attraction.class), getDefaultToken());
        }

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void create_shouldReturn404_whenSegmentationNotExistsTest() {
        AttractionRequestData requestBody = createAttractionRequestData(4, segmentation, moreInfoLinkRequestData, attractionType);
        requestBody.setSegmentations(List.of(INVALID_ID));

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            AttractionTestHelper.delete(response.as(Attraction.class), getDefaultToken());
        }

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void create_shouldReturn401_whenTokenInvalidTest() {
        AttractionRequestData requestBody = createAttractionRequestData(6, segmentation, moreInfoLinkRequestData, attractionType);

        Response response = given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            AttractionTestHelper.delete(response.as(Attraction.class), getDefaultToken());
        }

        response.then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void crete_shouldReturn403_whenUserNotAuthenticatedTest() {
        AttractionRequestData requestBody = createAttractionRequestData(7, segmentation, moreInfoLinkRequestData, attractionType);

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            AttractionTestHelper.delete(response.as(Attraction.class), getDefaultToken());
        }

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void create_shouldReturn409_whenAttractionAlreadyExistsTest() {
        AttractionRequestData requestBody = createAttractionRequestData(17, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void create_shouldReturn201_whenMoreThanOneAttractionIsCreatedTest() {
        AttractionRequestData requestBody1 = createAttractionRequestData(30, segmentation, moreInfoLinkRequestData, attractionType);
        AttractionRequestData requestBody2 = createAttractionRequestData(31, segmentation, moreInfoLinkRequestData, attractionType);

        Response response1 = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody1)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        Response response2 = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody2)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        AttractionTestHelper.delete(response1.getBody().jsonPath().getLong("id"), getAdminToken());
        AttractionTestHelper.delete(response2.getBody().jsonPath().getLong("id"), getAdminToken());

        response1.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(requestBody1.getName()))
                .body("description", equalTo(requestBody1.getDescription()))
                .body("mapLink", equalTo(requestBody1.getMapLink()))
                .body("city.id", equalTo(requestBody1.getCityId().intValue()))
                .body("imageLink", equalTo(requestBody1.getImageLink()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attractionType.id", equalTo(attractionType.getId().intValue()))
                .body("moreInfoLinks[0].link", equalTo(moreInfoLinkRequestData.getLink()));

        response2.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(requestBody2.getName()))
                .body("description", equalTo(requestBody2.getDescription()))
                .body("mapLink", equalTo(requestBody2.getMapLink()))
                .body("city.id", equalTo(requestBody2.getCityId().intValue()))
                .body("imageLink", equalTo(requestBody2.getImageLink()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attractionType.id", equalTo(attractionType.getId().intValue()))
                .body("moreInfoLinks[0].link", equalTo(moreInfoLinkRequestData.getLink()));
    }

    @Test
    void update_shouldReturn403_whenIsAValidAttractionTest() {
        AttractionRequestData requestBody = createAttractionRequestData(8, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        requestBody.setName("Teatro Municipal de João Pessoa");
        requestBody.setDescription("Teatro Municipal de João Pessoa, localizado no centro da cidade.");
        requestBody.setMapLink("https://mapa.com/teatro-municipal");
        requestBody.setImageLink("https://imagem.com/teatro-municipal");

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void update_shouldReturn200_whenIsAValidAttractionAdminUserTest() {
        AttractionRequestData requestBody = createAttractionRequestData(8, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        requestBody.setName("Teatro Municipal de João Pessoa");
        requestBody.setDescription("Teatro Municipal de João Pessoa, localizado no centro da cidade.");
        requestBody.setMapLink("https://mapa.com/teatro-municipal");
        requestBody.setImageLink("https://imagem.com/teatro-municipal");

        Response response = given()
                .header("Authorization", "Bearer " + getAdminToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(requestBody.getName()))
                .body("description", equalTo(requestBody.getDescription()))
                .body("mapLink", equalTo(requestBody.getMapLink()))
                .body("city.id", equalTo(requestBody.getCityId().intValue()))
                .body("imageLink", equalTo(requestBody.getImageLink()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attractionType.id", equalTo(attractionType.getId().intValue()))
                .body("moreInfoLinks[0].link", equalTo(moreInfoLinkRequestData.getLink()));
    }

    @Test
    void update_shouldReturn400_whenHasNullValuesTest() {
        Attraction requestBody = new Attraction();

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + INVALID_ID);

        if (response.getStatusCode() == HttpStatus.OK.value()) {
            AttractionTestHelper.delete(response.getBody().jsonPath().getLong("id"), getAdminToken());
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void update_shouldReturn400_whenImportantFieldsIsEmptyTest() {
        AttractionRequestData requestBody = createAttractionRequestData(9, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        requestBody.setName("");

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void update_shouldReturn404_whenAttractionTypeNotExistsAdminUserTest() {
        AttractionRequestData requestBody = createAttractionRequestData(10, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        requestBody.setAttractionType(INVALID_ID);

        Response response = given()
                .header("Authorization", "Bearer " + getAdminToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    void update_shouldReturn403_whenAttractionTypeNotExistsDefaultUserTest() {
        AttractionRequestData requestBody = createAttractionRequestData(10, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        requestBody.setAttractionType(INVALID_ID);

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());

    }


    @Test
    void update_shouldReturn403_whenSegmentationNotExistsDefaultUserTest() {
        AttractionRequestData requestBody = createAttractionRequestData(11, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        requestBody.setSegmentations(List.of(INVALID_ID));

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void update_shouldReturn404_whenSegmentationNotExistsAdminUserTest() {
        AttractionRequestData requestBody = createAttractionRequestData(11, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        requestBody.setSegmentations(List.of(INVALID_ID));

        Response response = given()
                .header("Authorization", "Bearer " + getAdminToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void update_shouldReturn404_whenAttractionNotExistsAdminUserTest() {
        AttractionRequestData requestBody = createAttractionRequestData(13, segmentation, moreInfoLinkRequestData, attractionType);

        Response response = given()
                .header("Authorization", "Bearer " + getAdminToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + INVALID_ID);

        if (response.getStatusCode() == HttpStatus.OK.value()) {
            AttractionTestHelper.delete(response.getBody().jsonPath().getLong("id"), getAdminToken());
        }

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void update_shouldReturn403_whenAttractionNotExistsDefaultUserTest() {
        AttractionRequestData requestBody = createAttractionRequestData(13, segmentation, moreInfoLinkRequestData, attractionType);

        Response response = given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + INVALID_ID);

        if (response.getStatusCode() == HttpStatus.OK.value()) {
            AttractionTestHelper.delete(response.getBody().jsonPath().getLong("id"), getAdminToken());
        }

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void update_shouldReturn401_whenTokenInvalidTest() {
        AttractionRequestData requestBody = createAttractionRequestData(14, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());
        savedAttraction.setName("Updated Name 1");

        Response response = given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

    }

    @Test
    void update_shouldReturn403_whenTokenIsMissing() {
        AttractionRequestData requestBody = createAttractionRequestData(15, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());
        savedAttraction.setName("Updated Name 2");

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        AttractionTestHelper.delete(savedAttraction, getAdminToken());

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());

    }

    @Test
    void delete_shouldReturn204_whenAuthenticatedAdminUserTest() {
        AttractionRequestData requestBody = createAttractionRequestData(16, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction response = AttractionTestHelper.post(requestBody, getDefaultToken());

        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .contentType("application/json")
                .when()
                .delete(AttractionTestHelper.PATH_ATTRACTION + "/" + response.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void delete_shouldReturn403_whenAuthenticatedDefaultUserTest() {
        AttractionRequestData requestBody = createAttractionRequestData(16, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .when()
                .delete(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());
    }

    @Test
    void delete_shouldReturn404_whenAttractionNotExistsAdminUserTest() {
        given()
                .header("Authorization", "Bearer " + getAdminToken())
                .contentType("application/json")
                .when()
                .delete(AttractionTestHelper.PATH_ATTRACTION + "/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void delete_shouldReturn403_whenAttractionNotExistsDefaultUserTest() {
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType("application/json")
                .when()
                .delete(AttractionTestHelper.PATH_ATTRACTION + "/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void delete_shouldReturn401_whenTokenInvalidTest() {
        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType("application/json")
                .when()
                .delete(AttractionTestHelper.PATH_ATTRACTION + "/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void delete_shouldReturn403_whenUserNotAuthenticatedTest() {
        given()
                .contentType("application/json")
                .when()
                .delete(AttractionTestHelper.PATH_ATTRACTION + "/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void findByName_shouldReturn200_whenAttractionWithNameExistsTest() {
        AttractionRequestData requestBody = createAttractionRequestData(18, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_ATTRACTION + "?name=" + savedAttraction.getName());

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("content.name", hasItem(savedAttraction.getName()))
                .body("content.description", hasItem(savedAttraction.getDescription()))
                .body("content.mapLink", hasItem(savedAttraction.getMapLink()))
                .body("content.city.id", hasItem(savedAttraction.getCity().getId().intValue()))
                .body("content.imageLink", hasItem(savedAttraction.getImageLink()))
                .body("content.segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("content.attractionType.id[0]", equalTo(attractionType.getId().intValue()))
                .body("content.moreInfoLinks[0].link[0]", equalTo(moreInfoLinkRequestData.getLink()));
    }

    @Test
    void findByName_shouldReturn200AndEmptyList_whenAttractionWithNameNoExistsTest() {
        AttractionRequestData requestBody = createAttractionRequestData(19, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_SEARCH_ATTRACTIONS + "?name=NotExist");

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content", empty())
                .body("totalElements", is(0));
    }

    @Test
    void findByName_shouldReturn200And2Items_whenExists2AttractionsWithNameTest() {

        AttractionRequestData requestBody = createAttractionRequestData(20, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction attraction1 = AttractionTestHelper.post(requestBody, getDefaultToken());

        TourismSegmentation segmentation2 = TourismSegmentationTestHelper.post(createTourismSegmentationRequestData(14), getDefaultToken());
        AttractionType attractionType2 = AttractionTypeTestHelper.post(createAttractionTypeRequestData(11), getDefaultToken());
        MoreInfoLinkRequestData moreInfoLink2 = createMoreInfoLinkRequestData(12);

        requestBody = createAttractionRequestData(21, segmentation2, moreInfoLink2, attractionType2);
        Attraction attraction2 = AttractionTestHelper.post(requestBody, getDefaultToken());

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_SEARCH_ATTRACTIONS + "?name=mock");

        AttractionTestHelper.delete(attraction1, getAdminToken());
        AttractionTestHelper.delete(attraction2, getAdminToken());

        TourismSegmentationTestHelper.delete(segmentation2, getAdminToken());
        AttractionTypeTestHelper.delete(attractionType2, getAdminToken());

        response.then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", is(2))
                .body("content.name", hasItems(attraction1.getName(), attraction2.getName()))
                .body("content.description", hasItems(attraction1.getDescription(), attraction2.getDescription()))
                .body("content.mapLink", hasItems(attraction1.getMapLink(), attraction2.getMapLink()))
                .body("content.city.id", hasItems(attraction1.getCity().getId().intValue(), attraction2.getCity().getId().intValue()))
                .body("content.imageLink", hasItems(attraction1.getImageLink(), attraction2.getImageLink()))
                .body("content.segmentations.id.flatten()", hasItems(segmentation.getId().intValue(), segmentation2.getId().intValue()))
                .body("content.attractionType.id.flatten()", hasItems(attractionType.getId().intValue(), attractionType2.getId().intValue()))
                .body("content.moreInfoLinks.link.flatten()", hasItems(moreInfoLinkRequestData.getLink(), moreInfoLink2.getLink()));
    }


    @Test
    void findByCity_shouldReturn200_whenAttractionExistsInCityTest() {
        AttractionRequestData request = createAttractionRequestData(22, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(request, getDefaultToken());

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_SEARCH_ATTRACTIONS + "?city=" + savedAttraction.getCity().getId());

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("content.name", hasItem(savedAttraction.getName()))
                .body("content.description", hasItem(savedAttraction.getDescription()))
                .body("content.mapLink", hasItem(savedAttraction.getMapLink()))
                .body("content.city.id", hasItem(savedAttraction.getCity().getId().intValue()))
                .body("content.imageLink", hasItem(savedAttraction.getImageLink()))
                .body("content.segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("content.attractionType.id[0]", equalTo(attractionType.getId().intValue()))
                .body("content.moreInfoLinks[0].link[0]", equalTo(moreInfoLinkRequestData.getLink()));
    }

    @Test
    void findByCity_shouldReturn200AndEmptyList_whenAttractionNoExistsInCityTest() {
        AttractionRequestData requestBody = createAttractionRequestData(23, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_SEARCH_ATTRACTIONS + "?city=" + INVALID_ID);

        AttractionTestHelper.delete(savedAttraction, getAdminToken());

        response.then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", is(0))
                .body("totalElements", is(0));
    }

    @Test
    void findByCity_shouldReturn200_whenCityNoExistsTest() {
        given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_SEARCH_ATTRACTIONS + "?city=" + INVALID_ID)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", is(0))
                .body("totalElements", is(0));
    }

    @Test
    void findByCity_shouldReturn200And2Items_when2AttractionsExistsInCityTest() {
        AttractionRequestData requestBody = createAttractionRequestData(24, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction attraction1 = AttractionTestHelper.post(requestBody, getDefaultToken());

        TourismSegmentation segmentation2 = TourismSegmentationTestHelper.post(createTourismSegmentationRequestData(15), getDefaultToken());
        AttractionType attractionType2 = AttractionTypeTestHelper.post(createAttractionTypeRequestData(12), getDefaultToken());
        MoreInfoLinkRequestData moreInfoLink2 = createMoreInfoLinkRequestData(13);

        requestBody = createAttractionRequestData(25, segmentation2, moreInfoLink2, attractionType2);
        Attraction attraction2 = AttractionTestHelper.post(requestBody, getDefaultToken());

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_SEARCH_ATTRACTIONS + "?city=" + attraction1.getCity().getId());

        AttractionTestHelper.delete(attraction1.getId(), getAdminToken());
        AttractionTestHelper.delete(attraction2.getId(), getAdminToken());

        TourismSegmentationTestHelper.delete(segmentation2, getAdminToken());
        AttractionTypeTestHelper.delete(attractionType2, getAdminToken());

        response.then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", is(2))
                .body("content.name", hasItems(attraction1.getName(), attraction2.getName()))
                .body("content.description", hasItems(attraction1.getDescription(), attraction2.getDescription()))
                .body("content.mapLink", hasItems(attraction1.getMapLink(), attraction2.getMapLink()))
                .body("content.city.id", hasItems(attraction1.getCity().getId().intValue(), attraction2.getCity().getId().intValue()))
                .body("content.imageLink", hasItems(attraction1.getImageLink(), attraction2.getImageLink()))
                .body("content.segmentations.id.flatten()", hasItems(segmentation.getId().intValue(), segmentation2.getId().intValue()))
                .body("content.attractionType.id.flatten()", hasItems(attractionType.getId().intValue(), attractionType2.getId().intValue()))
                .body("content.moreInfoLinks.link.flatten()", hasItems(moreInfoLinkRequestData.getLink(), moreInfoLink2.getLink()))
                .body("totalElements", is(2));
    }

    @Test
    void findBySegmentation_shouldReturn200_whenAttractionExistsInSegmentationTest() {
        AttractionRequestData request = createAttractionRequestData(26, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(request, getDefaultToken());

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_ATTRACTION + "?segmentation=" + segmentation.getId());

        AttractionTestHelper.delete(savedAttraction.getId(), getAdminToken());

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("content.name", hasItem(savedAttraction.getName()))
                .body("content.description", hasItem(savedAttraction.getDescription()))
                .body("content.mapLink", hasItem(savedAttraction.getMapLink()))
                .body("content.city.id", hasItem(savedAttraction.getCity().getId().intValue()))
                .body("content.imageLink", hasItem(savedAttraction.getImageLink()))
                .body("content.segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("content.attractionType.id[0]", equalTo(attractionType.getId().intValue()))
                .body("content.moreInfoLinks[0].link[0]", equalTo(moreInfoLinkRequestData.getLink()));
    }

    @Test
    void findBySegmentation_shouldReturn200AndEmptyList_whenAttractionNoExistsInSegmentationTest() {
        AttractionRequestData requestBody = createAttractionRequestData(27, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction attraction = AttractionTestHelper.post(requestBody, getDefaultToken());

        TourismSegmentationRequestData segmentationRequest = createTourismSegmentationRequestData(16);
        TourismSegmentation segmentation1 = TourismSegmentationTestHelper.post(segmentationRequest, getDefaultToken());

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_SEARCH_ATTRACTIONS + "?segmentation=" + segmentation1.getId());

        AttractionTestHelper.delete(attraction, getAdminToken());
        TourismSegmentationTestHelper.delete(segmentation1, getAdminToken());

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", is(0))
                .body("totalElements", is(0));
    }

    @Test
    void findBySegmentation_shouldReturn200And2Items_when2AttractionsExistsInSegmentationTest() {
        AttractionRequestData requestBody = createAttractionRequestData(28, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction attraction1 = AttractionTestHelper.post(requestBody, getDefaultToken());

        AttractionType attractionType2 = AttractionTypeTestHelper.post(createAttractionTypeRequestData(13), getDefaultToken());
        MoreInfoLinkRequestData moreInfoLink2 = createMoreInfoLinkRequestData(14);

        requestBody = createAttractionRequestData(29, segmentation, moreInfoLink2, attractionType2);
        Attraction attraction2 = AttractionTestHelper.post(requestBody, getDefaultToken());

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_SEARCH_ATTRACTIONS + "?segmentation=" + segmentation.getId());

        AttractionTestHelper.delete(attraction1.getId(), getAdminToken());
        AttractionTestHelper.delete(attraction2.getId(), getAdminToken());

        AttractionTypeTestHelper.delete(attractionType2, getAdminToken());

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", is(2))
                .body("content.name", hasItems(attraction1.getName(), attraction2.getName()))
                .body("content.description", hasItems(attraction1.getDescription(), attraction2.getDescription()))
                .body("content.mapLink", hasItems(attraction1.getMapLink(), attraction2.getMapLink()))
                .body("content.city.id", hasItems(attraction1.getCity().getId().intValue(), attraction2.getCity().getId().intValue()))
                .body("content.imageLink", hasItems(attraction1.getImageLink(), attraction2.getImageLink()))
                .body("content.segmentations.id.flatten()", hasItems(segmentation.getId().intValue(), segmentation.getId().intValue()))
                .body("content.attractionType.id.flatten()", hasItems(attractionType.getId().intValue(), attractionType2.getId().intValue()))
                .body("content.moreInfoLinks.link.flatten()", hasItems(moreInfoLinkRequestData.getLink(), moreInfoLink2.getLink()))
                .body("totalElements", is(2));
    }

    @Test
    void findBySegmentation_shouldReturn200_whenSegmentationNoExistsTest() {
        given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_SEARCH_ATTRACTIONS + "?segmentation=" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", is(0))
                .body("totalElements", is(0));
    }
}