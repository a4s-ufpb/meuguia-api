package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.dto.AttractionTypeRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.dto.AuthenticationResponseData;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.*;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.dto.TourismSegmentationRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.user.dto.RegisterUserRequestData;
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
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.UserTestsHelper.createRegisterRequestData;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AttractionControllerTest extends MeuguiaApiApplicationTests {
    private final Logger log = LoggerFactory.getLogger(AttractionControllerTest.class);

    @Autowired
    private CityTestHelper cityTestHelper;

    private TourismSegmentation segmentation;
    private AttractionType attractionType;
    private MoreInfoLinkRequestData moreInfoLinkRequestData;

    private String token;

    @BeforeAll
    void setUp() {
        cityTestHelper.createCity();

        RegisterUserRequestData registerUserRequestData = createRegisterRequestData(90);
        AuthenticationResponseData response = UserTestsHelper.registerAndAuthenticate(registerUserRequestData);
        token = response.getToken();
    }

    @AfterAll
    void tearDown() {
        cityTestHelper.deleteLastCityCreated();
        UserTestsHelper.delete(token);
    }

    @BeforeEach
    void setUpEach() {
        TourismSegmentationRequestData segmentationRequest = createTourismSegmentationRequestData(90);
        AttractionTypeRequestData attractionTypeRequest = createAttractionTypeRequestData(90);
        this.moreInfoLinkRequestData = createMoreInfoLinkRequestData(90);

        segmentation = TourismSegmentationTestHelper.post(segmentationRequest, token);
        attractionType = AttractionTypeTestHelper.post(attractionTypeRequest, token);
    }

    @AfterEach
    void tearDownEach() {
        TourismSegmentationTestHelper.delete(segmentation, token);
        AttractionTypeTestHelper.delete(attractionType, token);
    }

    @Test
    void create_shouldReturn201_whenIsAValidAttractionTest() {
        AttractionRequestData requestBody = createAttractionRequestData(1, segmentation, moreInfoLinkRequestData, attractionType);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        AttractionTestHelper.delete(response.as(Attraction.class), token);

        response.then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(requestBody.getName()))
                .body("description", equalTo(requestBody.getDescription()))
                .body("map_link", equalTo(requestBody.getMapLink()))
                .body("city.id", equalTo(requestBody.getCityId().intValue()))
                .body("image_link", equalTo(requestBody.getImageLink()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id", equalTo(attractionType.getId().intValue()))
                .body("more_info_links[0].link", equalTo(moreInfoLinkRequestData.getLink()));
    }

    @Test
    void create_shouldReturn400_whenHasNullValuesTest() {
        Attraction requestBody = new Attraction();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            AttractionTestHelper.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenImportantFieldsIsEmptyTest() {
        AttractionRequestData requestBody = createAttractionRequestData(2, segmentation, moreInfoLinkRequestData, attractionType);
        requestBody.setName("");

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            AttractionTestHelper.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn404_whenAttractionTypeNotExistsTest() {
        AttractionRequestData requestBody = createAttractionRequestData(3, segmentation, moreInfoLinkRequestData, attractionType);
        requestBody.setAttractionType(INVALID_ID);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            AttractionTestHelper.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void create_shouldReturn404_whenSegmentationNotExistsTest() {
        AttractionRequestData requestBody = createAttractionRequestData(4, segmentation, moreInfoLinkRequestData, attractionType);
        requestBody.setSegmentations(List.of(INVALID_ID));

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            AttractionTestHelper.delete(response.as(Attraction.class), token);
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
            AttractionTestHelper.delete(response.as(Attraction.class), token);
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
            AttractionTestHelper.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void create_shouldReturn409_whenAttractionAlreadyExistsTest() {
        AttractionRequestData requestBody = createAttractionRequestData(17, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, token);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        AttractionTestHelper.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void create_shouldReturn201_whenMoreThanOneAttractionIsCreatedTest() {
        AttractionRequestData requestBody1 = createAttractionRequestData(30, segmentation, moreInfoLinkRequestData, attractionType);
        AttractionRequestData requestBody2 = createAttractionRequestData(31, segmentation, moreInfoLinkRequestData, attractionType);

        Response response1 = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody1)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        Response response2 = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody2)
                .when()
                .post(AttractionTestHelper.PATH_CREATE_ATTRACTION);

        AttractionTestHelper.delete(response1.as(Attraction.class), token);
        AttractionTestHelper.delete(response2.as(Attraction.class), token);

        response1.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(requestBody1.getName()))
                .body("description", equalTo(requestBody1.getDescription()))
                .body("map_link", equalTo(requestBody1.getMapLink()))
                .body("city.id", equalTo(requestBody1.getCityId().intValue()))
                .body("image_link", equalTo(requestBody1.getImageLink()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id", equalTo(attractionType.getId().intValue()))
                .body("more_info_links[0].link", equalTo(moreInfoLinkRequestData.getLink()));

        response2.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(requestBody2.getName()))
                .body("description", equalTo(requestBody2.getDescription()))
                .body("map_link", equalTo(requestBody2.getMapLink()))
                .body("city.id", equalTo(requestBody2.getCityId().intValue()))
                .body("image_link", equalTo(requestBody2.getImageLink()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id", equalTo(attractionType.getId().intValue()))
                .body("more_info_links[0].link", equalTo(moreInfoLinkRequestData.getLink()));
    }

    @Test
    void update_shouldReturn200_whenIsAValidAttractionTest() {
        AttractionRequestData requestBody = createAttractionRequestData(8, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, token);

        requestBody.setName("Teatro Municipal de João Pessoa");
        requestBody.setDescription("Teatro Municipal de João Pessoa, localizado no centro da cidade.");
        requestBody.setMapLink("https://mapa.com/teatro-municipal");
        requestBody.setImageLink("https://imagem.com/teatro-municipal");

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        Integer attractionId = response.path("id");
        AttractionTestHelper.delete(attractionId.longValue(), token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(requestBody.getName()))
                .body("description", equalTo(requestBody.getDescription()))
                .body("map_link", equalTo(requestBody.getMapLink()))
                .body("city.id", equalTo(requestBody.getCityId().intValue()))
                .body("image_link", equalTo(requestBody.getImageLink()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id", equalTo(attractionType.getId().intValue()))
                .body("more_info_links[0].link", equalTo(moreInfoLinkRequestData.getLink()));
    }

    @Test
    void update_shouldReturn400_whenHasNullValuesTest() {
        Attraction requestBody = new Attraction();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + INVALID_ID);

        if (response.getStatusCode() == HttpStatus.OK.value()) {
            AttractionTestHelper.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void update_shouldReturn400_whenImportantFieldsIsEmptyTest() {
        AttractionRequestData requestBody = createAttractionRequestData(9, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, token);

        requestBody.setName("");

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        AttractionTestHelper.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void update_shouldReturn404_whenAttractionTypeNotExistsTest() {
        AttractionRequestData requestBody = createAttractionRequestData(10, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, token);

        requestBody.setAttractionType(INVALID_ID);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        AttractionTestHelper.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    void update_shouldReturn404_whenSegmentationNotExistsTest() {
        AttractionRequestData requestBody = createAttractionRequestData(11, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(requestBody, token);

        requestBody.setSegmentations(List.of(INVALID_ID));

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + savedAttraction.getId());

        AttractionTestHelper.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void update_shouldReturn404_whenAttractionNotExistsTest() {
        AttractionRequestData requestBody = createAttractionRequestData(13, segmentation, moreInfoLinkRequestData, attractionType);

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + INVALID_ID);

        if (response.getStatusCode() == HttpStatus.OK.value()) {
            AttractionTestHelper.delete(response.as(Attraction.class), token);
        }

        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void update_shouldReturn401_whenTokenInvalidTest() {
        AttractionRequestData requestBody = createAttractionRequestData(14, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction attraction = AttractionTestHelper.post(requestBody, token);
        attraction.setName("Updated Name 1");

        Response response = given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + attraction.getId());

        AttractionTestHelper.delete(attraction, token);

        response.then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

    }

    @Test
    void update_shouldReturn403_whenTokenIsMissing() {
        AttractionRequestData requestBody = createAttractionRequestData(15, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction attraction = AttractionTestHelper.post(requestBody, token);
        attraction.setName("Updated Name 2");

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(AttractionTestHelper.PATH_ATTRACTION + "/" + attraction.getId());

        AttractionTestHelper.delete(attraction, token);

        response.then()
                .statusCode(HttpStatus.FORBIDDEN.value());

    }

    @Test
    void delete_shouldReturn204_whenAuthenticatedTest() {
        AttractionRequestData requestBody = createAttractionRequestData(16, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction response = AttractionTestHelper.post(requestBody, token);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .delete(AttractionTestHelper.PATH_ATTRACTION + "/" + response.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void delete_shouldReturn404_whenAttractionNotExistsTest() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .delete(AttractionTestHelper.PATH_ATTRACTION + "/" + INVALID_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
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
        Attraction attraction = AttractionTestHelper.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_FIND_ATTRACTION_BY_NAME + "?name=" + attraction.getName());

        AttractionTestHelper.delete(attraction, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("name", hasItem(attraction.getName()))
                .body("description", hasItem(attraction.getDescription()))
                .body("map_link", hasItem(attraction.getMapLink()))
                .body("city.id", hasItem(attraction.getCity().getId().intValue()))
                .body("image_link", hasItem(attraction.getImageLink()))
                .body("segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id[0]", equalTo(attractionType.getId().intValue()))
                .body("more_info_links[0].link[0]", equalTo(moreInfoLinkRequestData.getLink()));
    }

    @Test
    void findByName_shouldReturn200AndEmptyList_whenAttractionWithNameNoExistsTest() {
        AttractionRequestData requestBody = createAttractionRequestData(19, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction attraction = AttractionTestHelper.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_FIND_ATTRACTION_BY_NAME + "?name=NotExist");

        AttractionTestHelper.delete(attraction, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void findByName_shouldReturn200And2Items_whenExists2AttractionsWithNameTest() {

        AttractionRequestData requestBody = createAttractionRequestData(20, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction attraction1 = AttractionTestHelper.post(requestBody, token);

        TourismSegmentation segmentation2 = TourismSegmentationTestHelper.post(createTourismSegmentationRequestData(14), token);
        AttractionType attractionType2 = AttractionTypeTestHelper.post(createAttractionTypeRequestData(11), token);
        MoreInfoLinkRequestData moreInfoLink2 = createMoreInfoLinkRequestData(12);

        requestBody = createAttractionRequestData(21, segmentation2, moreInfoLink2, attractionType2);
        Attraction attraction2 = AttractionTestHelper.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_FIND_ATTRACTION_BY_NAME + "?name=mock");

        AttractionTestHelper.delete(attraction1, token);
        AttractionTestHelper.delete(attraction2, token);

        TourismSegmentationTestHelper.delete(segmentation2, token);
        AttractionTypeTestHelper.delete(attractionType2, token);

        response.then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("name", hasItems(attraction1.getName(), attraction2.getName()))
                .body("description", hasItems(attraction1.getDescription(), attraction2.getDescription()))
                .body("map_link", hasItems(attraction1.getMapLink(), attraction2.getMapLink()))
                .body("city.id", hasItems(attraction1.getCity().getId().intValue(), attraction2.getCity().getId().intValue()))
                .body("image_link", hasItems(attraction1.getImageLink(), attraction2.getImageLink()))
                .body("segmentations.id.flatten()", hasItems(segmentation.getId().intValue(), segmentation2.getId().intValue()))
                .body("attraction_type.id.flatten()", hasItems(attractionType.getId().intValue(), attractionType2.getId().intValue()))
                .body("more_info_links.link.flatten()", hasItems(moreInfoLinkRequestData.getLink(), moreInfoLink2.getLink()));

    }


    // TODO: Adicionar criação da cidade aqui
    @Test
    void findByCity_shouldReturn200_whenAttractionExistsInCityTest() {
        AttractionRequestData request = createAttractionRequestData(22, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(request, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_FIND_ATTRACTION_BY_CITY + "?city=" + savedAttraction.getCity().getName());

        AttractionTestHelper.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("name", hasItem(savedAttraction.getName()))
                .body("description", hasItem(savedAttraction.getDescription()))
                .body("map_link", hasItem(savedAttraction.getMapLink()))
                .body("city.id", hasItem(savedAttraction.getCity().getId().intValue()))
                .body("image_link", hasItem(savedAttraction.getImageLink()))
                .body("segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id[0]", equalTo(attractionType.getId().intValue()))
                .body("more_info_links[0].link[0]", equalTo(moreInfoLinkRequestData.getLink()));
    }

    @Test
    void findByCity_shouldReturn200AndEmptyList_whenAttractionNoExistsInCityTest() {
        AttractionRequestData requestBody = createAttractionRequestData(23, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction attraction = AttractionTestHelper.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_FIND_ATTRACTION_BY_CITY + "?city=Cidade%20que%20existe");

        AttractionTestHelper.delete(attraction, token);

        response.then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void findByCity_shouldReturn404_whenCityNoExistsTest() {
        given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_FIND_ATTRACTION_BY_CITY + "?city=CidadeNãoExiste")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void findByCity_shouldReturn200And2Items_when2AttractionsExistsInCityTest() {
        AttractionRequestData requestBody = createAttractionRequestData(24, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction attraction1 = AttractionTestHelper.post(requestBody, token);

        TourismSegmentation segmentation2 = TourismSegmentationTestHelper.post(createTourismSegmentationRequestData(15), token);
        AttractionType attractionType2 = AttractionTypeTestHelper.post(createAttractionTypeRequestData(12), token);
        MoreInfoLinkRequestData moreInfoLink2 = createMoreInfoLinkRequestData(13);

        requestBody = createAttractionRequestData(25, segmentation2, moreInfoLink2, attractionType2);
        Attraction attraction2 = AttractionTestHelper.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_FIND_ATTRACTION_BY_CITY + "?city=" + attraction1.getCity().getName());

        AttractionTestHelper.delete(attraction1, token);
        AttractionTestHelper.delete(attraction2, token);

        TourismSegmentationTestHelper.delete(segmentation2, token);
        AttractionTypeTestHelper.delete(attractionType2, token);

        response.then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("name", hasItems(attraction1.getName(), attraction2.getName()))
                .body("description", hasItems(attraction1.getDescription(), attraction2.getDescription()))
                .body("map_link", hasItems(attraction1.getMapLink(), attraction2.getMapLink()))
                .body("city.id", hasItems(attraction1.getCity().getId().intValue(), attraction2.getCity().getId().intValue()))
                .body("image_link", hasItems(attraction1.getImageLink(), attraction2.getImageLink()))
                .body("segmentations.id.flatten()", hasItems(segmentation.getId().intValue(), segmentation2.getId().intValue()))
                .body("attraction_type.id.flatten()", hasItems(attractionType.getId().intValue(), attractionType2.getId().intValue()))
                .body("more_info_links.link.flatten()", hasItems(moreInfoLinkRequestData.getLink(), moreInfoLink2.getLink()));
    }

    @Test
    void findBySegmentation_shouldReturn200_whenAttractionExistsInSegmentationTest() {
        AttractionRequestData request = createAttractionRequestData(26, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction savedAttraction = AttractionTestHelper.post(request, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_FIND_ATTRACTION_BY_SEGMENTATION + "?segmentation=" + segmentation.getName());

        AttractionTestHelper.delete(savedAttraction, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("name", hasItem(savedAttraction.getName()))
                .body("description", hasItem(savedAttraction.getDescription()))
                .body("map_link", hasItem(savedAttraction.getMapLink()))
                .body("city.id", hasItem(savedAttraction.getCity().getId().intValue()))
                .body("image_link", hasItem(savedAttraction.getImageLink()))
                .body("segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("attraction_type.id[0]", equalTo(attractionType.getId().intValue()))
                .body("more_info_links[0].link[0]", equalTo(moreInfoLinkRequestData.getLink()));
    }

    @Test
    void findBySegmentation_shouldReturn200AndEmptyList_whenAttractionNoExistsInSegmentationTest() {
        AttractionRequestData requestBody = createAttractionRequestData(27, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction attraction = AttractionTestHelper.post(requestBody, token);

        TourismSegmentationRequestData segmentationRequest = createTourismSegmentationRequestData(16);
        TourismSegmentation segmentation1 = TourismSegmentationTestHelper.post(segmentationRequest, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_FIND_ATTRACTION_BY_SEGMENTATION + "?segmentation=" + segmentation1.getName());

        AttractionTestHelper.delete(attraction, token);
        TourismSegmentationTestHelper.delete(segmentation1, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void findBySegmentation_shouldReturn200And2Items_when2AttractionsExistsInSegmentationTest() {
        AttractionRequestData requestBody = createAttractionRequestData(28, segmentation, moreInfoLinkRequestData, attractionType);
        Attraction attraction1 = AttractionTestHelper.post(requestBody, token);

        AttractionType attractionType2 = AttractionTypeTestHelper.post(createAttractionTypeRequestData(13), token);
        MoreInfoLinkRequestData moreInfoLink2 = createMoreInfoLinkRequestData(14);

        requestBody = createAttractionRequestData(29, segmentation, moreInfoLink2, attractionType2);
        Attraction attraction2 = AttractionTestHelper.post(requestBody, token);

        Response response = given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_FIND_ATTRACTION_BY_SEGMENTATION + "?segmentation=" + segmentation.getName());

        AttractionTestHelper.delete(attraction1, token);
        AttractionTestHelper.delete(attraction2, token);

        AttractionTypeTestHelper.delete(attractionType2, token);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("name", hasItems(attraction1.getName(), attraction2.getName()))
                .body("description", hasItems(attraction1.getDescription(), attraction2.getDescription()))
                .body("map_link", hasItems(attraction1.getMapLink(), attraction2.getMapLink()))
                .body("city.id", hasItems(attraction1.getCity().getId().intValue(), attraction2.getCity().getId().intValue()))
                .body("image_link", hasItems(attraction1.getImageLink(), attraction2.getImageLink()))
                .body("segmentations.id.flatten()", hasItems(segmentation.getId().intValue(), segmentation.getId().intValue()))
                .body("attraction_type.id.flatten()", hasItems(attractionType.getId().intValue(), attractionType2.getId().intValue()))
                .body("more_info_links.link.flatten()", hasItems(moreInfoLinkRequestData.getLink(), moreInfoLink2.getLink()));
    }

    @Test
    void findBySegmentation_shouldReturn404_whenSegmentationNoExistsTest() {
        given()
                .contentType("application/json")
                .when()
                .get(AttractionTestHelper.PATH_FIND_ATTRACTION_BY_SEGMENTATION + "?segmentation=segmentacao_que_nao_existe")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}