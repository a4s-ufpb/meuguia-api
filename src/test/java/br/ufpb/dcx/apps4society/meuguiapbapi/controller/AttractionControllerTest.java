package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.*;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MockAttraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MockAttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MockMoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MockTouristSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionRequestUtil;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionTypeRequestUtil;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.MoreInfoLinkRequestUtil;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.TourismSegmentationRequestUtil;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import java.util.List;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionRequestUtil.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AttractionControllerTest extends MeuguiaApiApplicationTests {
    private final Logger log = LoggerFactory.getLogger(AttractionControllerTest.class);

    private final MockAttractionType mockAttractionType = MockAttractionType.getInstance();
    private final MockMoreInfoLink mockMoreInfoLink = MockMoreInfoLink.getInstance();
    private final MockTouristSegmentation mockTouristSegmentation = MockTouristSegmentation.getInstance();
    private final MockAttraction mockAttraction = MockAttraction.getInstance();

    private final TourismSegmentationRequestUtil tourismSegmentationRequestUtil = TourismSegmentationRequestUtil.getInstance();
    private final AttractionTypeRequestUtil attractionTypeRequestUtil = AttractionTypeRequestUtil.getInstance();
    private final MoreInfoLinkRequestUtil moreInfoLinkRequestUtil = MoreInfoLinkRequestUtil.getInstance();
    private final AttractionRequestUtil attractionRequestUtil = AttractionRequestUtil.getInstance();

    private TourismSegmentation segmentation;
    private AttractionType attractionType;
    private MoreInfoLink moreInfoLink;

    @Autowired
    private Environment env;

    private String token;

    @BeforeAll
    void setUp() {
        RegisterRequestData registerRequestData = mockAuthentication.mockRequest(90);
        AuthenticationResponseData response = userRequestUtil.register(registerRequestData);
        token = response.getToken();
    }

    @AfterAll
    void tearDown() {
        userRequestUtil.delete(token);
    }

    @BeforeEach
    void setUpEach() {
        TourismSegmentationRequestData segmentationRequest = mockTouristSegmentation.mockRequest(90);
        AttractionTypeRequestData attractionTypeRequest = mockAttractionType.mockRequest(90);
        MoreInfoLinkRequestData moreInfoLinkRequest = mockMoreInfoLink.mockRequest(90);

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
        AttractionRequestData requestBody = mockAttraction.mockRequest(1, segmentation, moreInfoLink, attractionType);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        attractionRequestUtil.delete(response.as(Attraction.class), token);

        response.then()
                .log().body()
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
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenImportantFieldsIsEmptyTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(2, segmentation, moreInfoLink, attractionType);
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
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn404_whenAttractionTypeNotExistsTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(3, segmentation, moreInfoLink, attractionType);
        requestBody.setAttractionTypes(mockAttractionType.mockEntity(INVALID_ID.intValue()));

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
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void create_shouldReturn404_whenSegmentationNotExistsTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(4, segmentation, moreInfoLink, attractionType);
        requestBody.setSegmentations(List.of(mockTouristSegmentation.mockEntity(INVALID_ID.intValue())));

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
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void create_shouldReturn404_whenMoreInfoLinkNotExistsTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(5, segmentation, moreInfoLink, attractionType);
        requestBody.setMoreInfoLinkList(List.of(mockMoreInfoLink.mockEntity(INVALID_ID.intValue())));

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
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void create_shouldReturn401_whenTokenInvalidTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(6, segmentation, moreInfoLink, attractionType);

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
                .log().body()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void crete_shouldReturn403_whenUserNotAuthenticatedTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(7, segmentation, moreInfoLink, attractionType);

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            attractionRequestUtil.delete(response.as(Attraction.class), token);
        }

        response.then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void update_shouldReturn200_whenIsAValidAttractionTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(8, segmentation, moreInfoLink, attractionType);
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
                .log().body()
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
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void update_shouldReturn400_whenImportantFieldsIsEmptyTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(9, segmentation, moreInfoLink, attractionType);
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
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void update_shouldReturn404_whenAttractionTypeNotExistsTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(10, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setAttractionTypes(mockAttractionType.mockEntity(INVALID_ID.intValue()));

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId());

        attractionRequestUtil.delete(savedAttraction, token);

        response.then()
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    void update_shouldReturn404_whenSegmentationNotExistsTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(11, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setSegmentations(List.of(mockTouristSegmentation.mockEntity(INVALID_ID.intValue())));

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId());

        attractionRequestUtil.delete(savedAttraction, token);

        response.then()
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void update_shouldReturn404_whenMoreInfoLinkNotExistsTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(12, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setMoreInfoLinkList(List.of(mockMoreInfoLink.mockEntity(INVALID_ID.intValue())));

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId());

        attractionRequestUtil.delete(savedAttraction, token);

        response.then()
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void update_shouldReturn404_whenAttractionNotExistsTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(13, segmentation, moreInfoLink, attractionType);

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
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void update_shouldReturn401_whenTokenInvalidTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(14, segmentation, moreInfoLink, attractionType);
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
                .log().body()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

    }

    @Test
    void update_shouldReturn403_whenTokenIsMissing() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(15, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);
        attraction.setName("Updated Name 2");

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + attraction.getId());

        attractionRequestUtil.delete(attraction, token);

        response.then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());

    }

    @Test
    void delete_shouldReturn204_whenAuthenticatedTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(16, segmentation, moreInfoLink, attractionType);
        Attraction response = attractionRequestUtil.post(requestBody, token);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .delete(PATH_ATTRACTION + "/" + response.getId())
                .then()
                .log().body()
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
                .log().body()
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
                .log().body()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void delete_shouldReturn403_whenUserNotAuthenticatedTest() {
        given()
                .contentType("application/json")
                .when()
                .delete(PATH_ATTRACTION + "/" + INVALID_ID)
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void findByName_shouldReturn200_whenAttractionWithNameExistsTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(18, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_NAME + "?name=" + attraction.getName());

        attractionRequestUtil.delete(attraction, token);

        response.then()
                .log().body()
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
        AttractionRequestData requestBody = mockAttraction.mockRequest(19, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_NAME + "?name=NotExist");

        attractionRequestUtil.delete(attraction, token);

        response.then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void findByName_shouldReturn200And2Items_whenExists2AttractionsWithNameTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(20, segmentation, moreInfoLink, attractionType);
        Attraction attraction1 = attractionRequestUtil.post(requestBody, token);

        TourismSegmentation segmentation2 = tourismSegmentationRequestUtil.post(mockTouristSegmentation.mockRequest(14), token);
        AttractionType attractionType2 = attractionTypeRequestUtil.post(mockAttractionType.mockRequest(11), token);
        MoreInfoLink moreInfoLink2 = moreInfoLinkRequestUtil.post(mockMoreInfoLink.mockRequest(12), token);

        requestBody = mockAttraction.mockRequest(21, segmentation2, moreInfoLink2, attractionType2);
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
                .log().body()
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
        AttractionRequestData request = mockAttraction.mockRequest(22, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(request, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_CITY + "?city=" + request.getCity());

        attractionRequestUtil.delete(savedAttraction, token);

        response.then()
                .log().body()
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
        AttractionRequestData requestBody = mockAttraction.mockRequest(23, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_CITY + "?city=Rio Tinto");

        attractionRequestUtil.delete(attraction, token);

        response.then()
                .log().body()
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
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void findByCity_shouldReturn200And2Items_when2AttractionsExistsInCityTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(24, segmentation, moreInfoLink, attractionType);
        Attraction attraction1 = attractionRequestUtil.post(requestBody, token);

        TourismSegmentation segmentation2 = tourismSegmentationRequestUtil.post(mockTouristSegmentation.mockRequest(15), token);
        AttractionType attractionType2 = attractionTypeRequestUtil.post(mockAttractionType.mockRequest(12), token);
        MoreInfoLink moreInfoLink2 = moreInfoLinkRequestUtil.post(mockMoreInfoLink.mockRequest(13), token);

        requestBody = mockAttraction.mockRequest(25, segmentation2, moreInfoLink2, attractionType2);
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
                .log().body()
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
        AttractionRequestData request = mockAttraction.mockRequest(26, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(request, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_SEGMENTATION + "?segmentation=" + segmentation.getName());

        attractionRequestUtil.delete(savedAttraction, token);

        response.then()
                .log().body()
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
        AttractionRequestData requestBody = mockAttraction.mockRequest(27, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);

        TourismSegmentationRequestData segmentationRequest = mockTouristSegmentation.mockRequest(16);
        TourismSegmentation segmentation1 = tourismSegmentationRequestUtil.post(segmentationRequest, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_SEGMENTATION + "?segmentation=" + segmentation1.getName());

        attractionRequestUtil.delete(attraction, token);
        tourismSegmentationRequestUtil.delete(segmentation1, token);

        response.then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void findBySegmentation_shouldReturn200And2Items_when2AttractionsExistsInSegmentationTest() {
        AttractionRequestData requestBody = mockAttraction.mockRequest(28, segmentation, moreInfoLink, attractionType);
        Attraction attraction1 = attractionRequestUtil.post(requestBody, token);

        AttractionType attractionType2 = attractionTypeRequestUtil.post(mockAttractionType.mockRequest(13), token);
        MoreInfoLink moreInfoLink2 = moreInfoLinkRequestUtil.post(mockMoreInfoLink.mockRequest(14), token);

        requestBody = mockAttraction.mockRequest(29, segmentation, moreInfoLink2, attractionType2);
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
                .log().body()
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
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}