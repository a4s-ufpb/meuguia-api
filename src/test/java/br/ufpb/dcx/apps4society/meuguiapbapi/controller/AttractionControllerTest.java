package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.*;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTypeTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MoreInfoLinkTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.TourismSegmentationTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionRequestUtil;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionTypeRequestUtil;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.MoreInfoLinkRequestUtil;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.TourismSegmentationRequestUtil;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.List;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionRequestUtil.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AttractionControllerTest extends MeuguiaApiApplicationTests {
    private final Logger log = LoggerFactory.getLogger(AttractionControllerTest.class);

    private final AttractionTypeTestHelper attractionTypeTestHelper = AttractionTypeTestHelper.getInstance();
    private final MoreInfoLinkTestHelper moreInfoLinkTestHelper = MoreInfoLinkTestHelper.getInstance();
    private final TourismSegmentationTestHelper tourismSegmentationTestHelper = TourismSegmentationTestHelper.getInstance();
    private final AttractionTestHelper attractionTestHelper = AttractionTestHelper.getInstance();

    private final TourismSegmentationRequestUtil tourismSegmentationRequestUtil = TourismSegmentationRequestUtil.getInstance();
    private final AttractionTypeRequestUtil attractionTypeRequestUtil = AttractionTypeRequestUtil.getInstance();
    private final MoreInfoLinkRequestUtil moreInfoLinkRequestUtil = MoreInfoLinkRequestUtil.getInstance();
    private final AttractionRequestUtil attractionRequestUtil = AttractionRequestUtil.getInstance();

    private TourismSegmentation segmentation;
    private AttractionType attractionType;
    private MoreInfoLink moreInfoLink;

    private String token;

    @BeforeAll
    void setUp() {
        RegisterUserRequestData registerUserRequestData = authenticationTestHelper.getRegisterRequestData(90);
        AuthenticationResponseData response = userRequestUtil.registerAndAuthenticate(registerUserRequestData);
        token = response.getToken();
    }

    @AfterAll
    void tearDown() {
        userRequestUtil.delete(token);
    }

    @BeforeEach
    void setUpEach() {
        TourismSegmentationRequestData segmentationRequest = tourismSegmentationTestHelper.createTourismSegmentationRequestData(90);
        AttractionTypeRequestData attractionTypeRequest = attractionTypeTestHelper.createAttractionTypeRequestData(90);
        MoreInfoLinkRequestData moreInfoLinkRequest = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(90);

        segmentation = tourismSegmentationRequestUtil.post(segmentationRequest, token);
        attractionType = attractionTypeRequestUtil.post(attractionTypeRequest, token);
        moreInfoLink = moreInfoLinkRequestUtil.post(moreInfoLinkRequest, token);

    }

    @AfterEach
    void tearDownEach() {
        tourismSegmentationRequestUtil.delete(segmentation, token);
        attractionTypeRequestUtil.delete(attractionType, token);
        moreInfoLinkRequestUtil.delete(moreInfoLink, token);
    }

    @Test
    void create_shouldReturn201_whenIsAValidAttractionTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(1, segmentation, moreInfoLink, attractionType);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        attractionRequestUtil.delete(response.as(Attraction.class), token);

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(requestBody.getName()))
                .body("description", equalTo(requestBody.getDescription()))
                .body("map_link", equalTo(requestBody.getMapLink()))
                .body("city", equalTo(requestBody.getCity()))
                .body("state", equalTo(requestBody.getState()))
                .body("image_link", equalTo(requestBody.getImageLink()))
                .body("info_source", equalTo(requestBody.getInfoSource()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id", equalTo(attractionType.getId().intValue()))
                .body("more_info_link_list[0].id", equalTo(moreInfoLink.getId().intValue()));
    }

    @Test
    void create_shouldReturn400_whenHasNullValuesTest() {
        Attraction requestBody = new Attraction();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            attractionRequestUtil.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenImportantFieldsIsEmptyTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(2, segmentation, moreInfoLink, attractionType);
        requestBody.setName("");
        requestBody.setCity("");
        requestBody.setState("");

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            attractionRequestUtil.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn404_whenAttractionTypeNotExistsTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(3, segmentation, moreInfoLink, attractionType);
        requestBody.setAttractionType(attractionTypeTestHelper.createAttractionType(INVALID_ID.intValue()));

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            attractionRequestUtil.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void create_shouldReturn404_whenSegmentationNotExistsTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(4, segmentation, moreInfoLink, attractionType);
        requestBody.setSegmentations(List.of(tourismSegmentationTestHelper.createTourismSegmentation(INVALID_ID.intValue())));

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            attractionRequestUtil.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void create_shouldReturn404_whenMoreInfoLinkNotExistsTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(5, segmentation, moreInfoLink, attractionType);
        requestBody.setMoreInfoLinks(List.of(moreInfoLinkTestHelper.createMoreInfoLink(INVALID_ID.intValue())));

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            attractionRequestUtil.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void create_shouldReturn401_whenTokenInvalidTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(6, segmentation, moreInfoLink, attractionType);

        Response response = given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            attractionRequestUtil.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void crete_shouldReturn403_whenUserNotAuthenticatedTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(7, segmentation, moreInfoLink, attractionType);

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            attractionRequestUtil.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void create_shouldReturn409_whenAttractionAlreadyExistsTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(17, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        attractionRequestUtil.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void create_shouldReturn201_whenMoreThanOneAttractionIsCreatedTest() {
        AttractionRequestData requestBody1 = attractionTestHelper.createAttractionRequestData(30, segmentation, moreInfoLink, attractionType);
        AttractionRequestData requestBody2 = attractionTestHelper.createAttractionRequestData(31, segmentation, moreInfoLink, attractionType);

        Response response1 = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody1)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        Response response2 = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody2)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        attractionRequestUtil.delete(response1.as(Attraction.class), token);
        attractionRequestUtil.delete(response2.as(Attraction.class), token);

        response1.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(requestBody1.getName()))
                .body("description", equalTo(requestBody1.getDescription()))
                .body("map_link", equalTo(requestBody1.getMapLink()))
                .body("city", equalTo(requestBody1.getCity()))
                .body("state", equalTo(requestBody1.getState()))
                .body("image_link", equalTo(requestBody1.getImageLink()))
                .body("info_source", equalTo(requestBody1.getInfoSource()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id", equalTo(attractionType.getId().intValue()))
                .body("more_info_link_list[0].id", equalTo(moreInfoLink.getId().intValue()));

        response2.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(requestBody2.getName()))
                .body("description", equalTo(requestBody2.getDescription()))
                .body("map_link", equalTo(requestBody2.getMapLink()))
                .body("city", equalTo(requestBody2.getCity()))
                .body("state", equalTo(requestBody2.getState()))
                .body("image_link", equalTo(requestBody2.getImageLink()))
                .body("info_source", equalTo(requestBody2.getInfoSource()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id", equalTo(attractionType.getId().intValue()))
                .body("more_info_link_list[0].id", equalTo(moreInfoLink.getId().intValue()));
    }

    @Test
    void update_shouldReturn200_whenIsAValidAttractionTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(8, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setName("Teatro Municipal de João Pessoa");
        requestBody.setDescription("Teatro Municipal de João Pessoa, localizado no centro da cidade.");
        requestBody.setMapLink("https://mapa.com/teatro-municipal");
        requestBody.setCity("João Pessoa");
        requestBody.setState("Paraíba (PB)");
        requestBody.setImageLink("https://imagem.com/teatro-municipal");
        requestBody.setInfoSource("Fonte: https://fonte.com/teatro-municipal");

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId());

        attractionRequestUtil.delete(response.as(Attraction.class), token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(requestBody.getName()))
                .body("description", equalTo(requestBody.getDescription()))
                .body("map_link", equalTo(requestBody.getMapLink()))
                .body("city", equalTo(requestBody.getCity()))
                .body("state", equalTo(requestBody.getState()))
                .body("image_link", equalTo(requestBody.getImageLink()))
                .body("info_source", equalTo(requestBody.getInfoSource()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id", equalTo(attractionType.getId().intValue()))
                .body("more_info_link_list[0].id", equalTo(moreInfoLink.getId().intValue()));
    }

    @Test
    void update_shouldReturn400_whenHasNullValuesTest() {
        Attraction requestBody = new Attraction();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + INVALID_ID);

        if (response.getStatusCode() == HttpStatus.OK.value()) {
            attractionRequestUtil.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void update_shouldReturn400_whenImportantFieldsIsEmptyTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(9, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setName("");
        requestBody.setCity("");

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId());

        attractionRequestUtil.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void update_shouldReturn404_whenAttractionTypeNotExistsTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(10, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setAttractionType(attractionTypeTestHelper.createAttractionType(INVALID_ID.intValue()));

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId());

        attractionRequestUtil.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    void update_shouldReturn404_whenSegmentationNotExistsTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(11, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setSegmentations(List.of(tourismSegmentationTestHelper.createTourismSegmentation(INVALID_ID.intValue())));

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId());

        attractionRequestUtil.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void update_shouldReturn404_whenMoreInfoLinkNotExistsTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(12, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setMoreInfoLinks(List.of(moreInfoLinkTestHelper.createMoreInfoLink(INVALID_ID.intValue())));

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId());

        attractionRequestUtil.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void update_shouldReturn404_whenAttractionNotExistsTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(13, segmentation, moreInfoLink, attractionType);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + INVALID_ID);

        if (response.getStatusCode() == HttpStatus.OK.value()) {
            attractionRequestUtil.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void update_shouldReturn401_whenTokenInvalidTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(14, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);
        attraction.setName("Updated Name 1");

        Response response = given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + attraction.getId());

        attractionRequestUtil.delete(attraction, token);

        response.then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

    }

    @Test
    void update_shouldReturn403_whenTokenIsMissing() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(15, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);
        attraction.setName("Updated Name 2");

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + attraction.getId());

        attractionRequestUtil.delete(attraction, token);

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());

    }

    @Test
    void delete_shouldReturn204_whenAuthenticatedTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(16, segmentation, moreInfoLink, attractionType);
        Attraction response = attractionRequestUtil.post(requestBody, token);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .delete(PATH_ATTRACTION + "/" + response.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void delete_shouldReturn404_whenAttractionNotExistsTest() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .delete(PATH_ATTRACTION + "/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void delete_shouldReturn401_whenTokenInvalidTest() {
        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType("application/json")
                .when()
                .delete(PATH_ATTRACTION + "/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void delete_shouldReturn403_whenUserNotAuthenticatedTest() {
        given()
                .contentType("application/json")
                .when()
                .delete(PATH_ATTRACTION + "/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void findByName_shouldReturn200_whenAttractionWithNameExistsTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(18, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_NAME + "?name=" + attraction.getName());

        attractionRequestUtil.delete(attraction, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("name", hasItem(attraction.getName()))
                .body("description", hasItem(attraction.getDescription()))
                .body("map_link", hasItem(attraction.getMapLink()))
                .body("city", hasItem(attraction.getCity()))
                .body("state", hasItem(attraction.getState()))
                .body("image_link", hasItem(attraction.getImageLink()))
                .body("info_source", hasItem(attraction.getInfoSource()))
                .body("segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id[0]", equalTo(attractionType.getId().intValue()))
                .body("more_info_link_list[0].id[0]", equalTo(moreInfoLink.getId().intValue()));
    }

    @Test
    void findByName_shouldReturn200AndEmptyList_whenAttractionWithNameNoExistsTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(19, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_NAME + "?name=NotExist");

        attractionRequestUtil.delete(attraction, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void findByName_shouldReturn200And2Items_whenExists2AttractionsWithNameTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(20, segmentation, moreInfoLink, attractionType);
        Attraction attraction1 = attractionRequestUtil.post(requestBody, token);

        TourismSegmentation segmentation2 = tourismSegmentationRequestUtil.post(tourismSegmentationTestHelper.createTourismSegmentationRequestData(14), token);
        AttractionType attractionType2 = attractionTypeRequestUtil.post(attractionTypeTestHelper.createAttractionTypeRequestData(11), token);
        MoreInfoLink moreInfoLink2 = moreInfoLinkRequestUtil.post(moreInfoLinkTestHelper.createMoreInfoLinkRequestData(12), token);

        requestBody = attractionTestHelper.createAttractionRequestData(21, segmentation2, moreInfoLink2, attractionType2);
        Attraction attraction2 = attractionRequestUtil.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_NAME + "?name=mock");

        attractionRequestUtil.delete(attraction1, token);
        attractionRequestUtil.delete(attraction2, token);

        tourismSegmentationRequestUtil.delete(segmentation2, token);
        attractionTypeRequestUtil.delete(attractionType2, token);
        moreInfoLinkRequestUtil.delete(moreInfoLink2, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("name", hasItems(attraction1.getName(), attraction2.getName()))
                .body("description", hasItems(attraction1.getDescription(), attraction2.getDescription()))
                .body("map_link", hasItems(attraction1.getMapLink(), attraction2.getMapLink()))
                .body("city", hasItems(attraction1.getCity(), attraction2.getCity()))
                .body("state", hasItems(attraction1.getState(), attraction2.getState()))
                .body("image_link", hasItems(attraction1.getImageLink(), attraction2.getImageLink()))
                .body("info_source", hasItems(attraction1.getInfoSource(), attraction2.getInfoSource()))
                .body("segmentations.id.flatten()", hasItems(segmentation.getId().intValue(), segmentation2.getId().intValue()))
                .body("attraction_type.id.flatten()", hasItems(attractionType.getId().intValue(), attractionType2.getId().intValue()))
                .body("more_info_link_list.id.flatten()", hasItems(moreInfoLink.getId().intValue(), moreInfoLink2.getId().intValue()));

    }

    @Test
    void findByCity_shouldReturn200_whenAttractionExistsInCityTest() {
        AttractionRequestData request = attractionTestHelper.createAttractionRequestData(22, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(request, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_CITY + "?city=" + request.getCity());

        attractionRequestUtil.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("name", hasItem(savedAttraction.getName()))
                .body("description", hasItem(savedAttraction.getDescription()))
                .body("map_link", hasItem(savedAttraction.getMapLink()))
                .body("city", hasItem(savedAttraction.getCity()))
                .body("state", hasItem(savedAttraction.getState()))
                .body("image_link", hasItem(savedAttraction.getImageLink()))
                .body("info_source", hasItem(savedAttraction.getInfoSource()))
                .body("segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id[0]", equalTo(attractionType.getId().intValue()))
                .body("more_info_link_list[0].id[0]", equalTo(moreInfoLink.getId().intValue()));
    }

    @Test
    void findByCity_shouldReturn200AndEmptyList_whenAttractionNoExistsInCityTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(23, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_CITY + "?city=Rio Tinto");

        attractionRequestUtil.delete(attraction, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void findByCity_shouldReturn404_whenCityNoExistsTest() {
        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_CITY + "?city=CidadeNãoExiste")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void findByCity_shouldReturn200And2Items_when2AttractionsExistsInCityTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(24, segmentation, moreInfoLink, attractionType);
        Attraction attraction1 = attractionRequestUtil.post(requestBody, token);

        TourismSegmentation segmentation2 = tourismSegmentationRequestUtil.post(tourismSegmentationTestHelper.createTourismSegmentationRequestData(15), token);
        AttractionType attractionType2 = attractionTypeRequestUtil.post(attractionTypeTestHelper.createAttractionTypeRequestData(12), token);
        MoreInfoLink moreInfoLink2 = moreInfoLinkRequestUtil.post(moreInfoLinkTestHelper.createMoreInfoLinkRequestData(13), token);

        requestBody = attractionTestHelper.createAttractionRequestData(25, segmentation2, moreInfoLink2, attractionType2);
        Attraction attraction2 = attractionRequestUtil.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_CITY + "?city=" + attraction1.getCity());

        attractionRequestUtil.delete(attraction1, token);
        attractionRequestUtil.delete(attraction2, token);

        tourismSegmentationRequestUtil.delete(segmentation2, token);
        attractionTypeRequestUtil.delete(attractionType2, token);
        moreInfoLinkRequestUtil.delete(moreInfoLink2, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("name", hasItems(attraction1.getName(), attraction2.getName()))
                .body("description", hasItems(attraction1.getDescription(), attraction2.getDescription()))
                .body("map_link", hasItems(attraction1.getMapLink(), attraction2.getMapLink()))
                .body("city", hasItems(attraction1.getCity(), attraction2.getCity()))
                .body("state", hasItems(attraction1.getState(), attraction2.getState()))
                .body("image_link", hasItems(attraction1.getImageLink(), attraction2.getImageLink()))
                .body("info_source", hasItems(attraction1.getInfoSource(), attraction2.getInfoSource()))
                .body("segmentations.id.flatten()", hasItems(segmentation.getId().intValue(), segmentation2.getId().intValue()))
                .body("attraction_type.id.flatten()", hasItems(attractionType.getId().intValue(), attractionType2.getId().intValue()))
                .body("more_info_link_list.id.flatten()", hasItems(moreInfoLink.getId().intValue(), moreInfoLink2.getId().intValue()));
    }

    @Test
    void findBySegmentation_shouldReturn200_whenAttractionExistsInSegmentationTest() {
        AttractionRequestData request = attractionTestHelper.createAttractionRequestData(26, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(request, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_SEGMENTATION + "?segmentation=" + segmentation.getName());

        attractionRequestUtil.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("name", hasItem(savedAttraction.getName()))
                .body("description", hasItem(savedAttraction.getDescription()))
                .body("map_link", hasItem(savedAttraction.getMapLink()))
                .body("city", hasItem(savedAttraction.getCity()))
                .body("state", hasItem(savedAttraction.getState()))
                .body("image_link", hasItem(savedAttraction.getImageLink()))
                .body("info_source", hasItem(savedAttraction.getInfoSource()))
                .body("segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id[0]", equalTo(attractionType.getId().intValue()))
                .body("more_info_link_list[0].id[0]", equalTo(moreInfoLink.getId().intValue()));
    }

    @Test
    void findBySegmentation_shouldReturn200AndEmptyList_whenAttractionNoExistsInSegmentationTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(27, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);

        TourismSegmentationRequestData segmentationRequest = tourismSegmentationTestHelper.createTourismSegmentationRequestData(16);
        TourismSegmentation segmentation1 = tourismSegmentationRequestUtil.post(segmentationRequest, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_SEGMENTATION + "?segmentation=" + segmentation1.getName());

        attractionRequestUtil.delete(attraction, token);
        tourismSegmentationRequestUtil.delete(segmentation1, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void findBySegmentation_shouldReturn200And2Items_when2AttractionsExistsInSegmentationTest() {
        AttractionRequestData requestBody = attractionTestHelper.createAttractionRequestData(28, segmentation, moreInfoLink, attractionType);
        Attraction attraction1 = attractionRequestUtil.post(requestBody, token);

        AttractionType attractionType2 = attractionTypeRequestUtil.post(attractionTypeTestHelper.createAttractionTypeRequestData(13), token);
        MoreInfoLink moreInfoLink2 = moreInfoLinkRequestUtil.post(moreInfoLinkTestHelper.createMoreInfoLinkRequestData(14), token);

        requestBody = attractionTestHelper.createAttractionRequestData(29, segmentation, moreInfoLink2, attractionType2);
        Attraction attraction2 = attractionRequestUtil.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_SEGMENTATION + "?segmentation=" + segmentation.getName());

        attractionRequestUtil.delete(attraction1, token);
        attractionRequestUtil.delete(attraction2, token);

        attractionTypeRequestUtil.delete(attractionType2, token);
        moreInfoLinkRequestUtil.delete(moreInfoLink2, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("name", hasItems(attraction1.getName(), attraction2.getName()))
                .body("description", hasItems(attraction1.getDescription(), attraction2.getDescription()))
                .body("map_link", hasItems(attraction1.getMapLink(), attraction2.getMapLink()))
                .body("city", hasItems(attraction1.getCity(), attraction2.getCity()))
                .body("state", hasItems(attraction1.getState(), attraction2.getState()))
                .body("image_link", hasItems(attraction1.getImageLink(), attraction2.getImageLink()))
                .body("info_source", hasItems(attraction1.getInfoSource(), attraction2.getInfoSource()))
                .body("segmentations.id.flatten()", hasItems(segmentation.getId().intValue(), segmentation.getId().intValue()))
                .body("attraction_type.id.flatten()", hasItems(attractionType.getId().intValue(), attractionType2.getId().intValue()))
                .body("more_info_link_list.id.flatten()", hasItems(moreInfoLink.getId().intValue(), moreInfoLink2.getId().intValue()));
    }

    @Test
    void findBySegmentation_shouldReturn404_whenSegmentationNoExistsTest() {
        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_SEGMENTATION + "?segmentation=segmentacao_que_nao_existe")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}