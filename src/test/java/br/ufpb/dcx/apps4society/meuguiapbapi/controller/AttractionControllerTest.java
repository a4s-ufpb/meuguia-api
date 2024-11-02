package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.AuthenticationResponse;
import br.ufpb.dcx.apps4society.meuguiapbapi.auth.dto.RegisterRequest;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.domain.TouristSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MockAttraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MockAttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MockMoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MockTouristSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionRequestUtil;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionTypeRequestUtil;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.MoreInfoLinkRequestUtil;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.TourismSegmentationRequestUtil;
import org.springframework.core.env.Environment;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionRequestUtil.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class AttractionControllerTest extends MeuguiaApiApplicationTests {
    private final MockAttractionType mockAttractionType = new MockAttractionType();
    private final MockMoreInfoLink mockMoreInfoLink = new MockMoreInfoLink();
    private final MockTouristSegmentation mockTouristSegmentation = new MockTouristSegmentation();
    private final MockAttraction mockAttraction = new MockAttraction();

    private final TourismSegmentationRequestUtil tourismSegmentationRequestUtil = new TourismSegmentationRequestUtil();
    private final AttractionTypeRequestUtil attractionTypeRequestUtil = new AttractionTypeRequestUtil();
    private final MoreInfoLinkRequestUtil moreInfoLinkRequestUtil = new MoreInfoLinkRequestUtil();
    private final AttractionRequestUtil attractionRequestUtil = new AttractionRequestUtil();

    private TouristSegmentation segmentation;
    private AttractionType attractionType;
    private MoreInfoLink moreInfoLink;

    @Autowired
    private Environment env;

    private String token;

    @BeforeAll
    void setUp() {
        RegisterRequest registerRequest = mockAuthentication.mockRequest(90);
        AuthenticationResponse response = userRequestUtil.register(registerRequest);
        token = response.getToken();
    }

    @AfterAll
    void tearDown() {
        userRequestUtil.delete(token);
    }

    @BeforeEach
    void setUpEach() {
        TouristSegmentation segmentationRequest = mockTouristSegmentation.mockRequest(90);
        AttractionType attractionTypeRequest = mockAttractionType.mockRequest(90);
        MoreInfoLink moreInfoLinkRequest = mockMoreInfoLink.mockRequest(90);

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

        Attraction requestBody = mockAttraction.mockRequest(1, segmentation, moreInfoLink, attractionType);

        Attraction response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION)
                .then()
                .log().body()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo(requestBody.getName()))
                .body("description", equalTo(requestBody.getDescription()))
                .body("map_link", equalTo(requestBody.getMap_link()))
                .body("city", equalTo(requestBody.getCity()))
                .body("state", equalTo(requestBody.getState()))
                .body("image_link", equalTo(requestBody.getImage_link()))
                .body("fonte", equalTo(requestBody.getFonte()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attractionTypes.id", equalTo(attractionType.getId().intValue()))
                .body("moreInfoLinkList[0].id", equalTo(moreInfoLink.getId().intValue()))
                .extract()
                .as(Attraction.class);

        attractionRequestUtil.delete(response, token);
    }

    @Test
    void create_shouldReturn400_whenHasNullValuesTest() {
        Attraction requestBody = new Attraction();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenImportantFieldsIsEmptyTest(){
        Attraction requestBody = mockAttraction.mockRequest(2, segmentation, moreInfoLink, attractionType);
        requestBody.setName("");
        requestBody.setCity("");

        given()
                .header("Authorization", "Bearer" + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenAttractionTypeNotExistsTest() {
        Attraction requestBody = mockAttraction.mockRequest(3, segmentation, moreInfoLink, attractionType);
        requestBody.setAttractionTypes(mockAttractionType.mockEntity(INVALID_ID.intValue()));

        given()
                .header("Authorization", "Bearer" + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenSegmentationNotExistsTest() {
        Attraction requestBody = mockAttraction.mockRequest(4, segmentation, moreInfoLink, attractionType);
        requestBody.setSegmentations(List.of(mockTouristSegmentation.mockEntity(INVALID_ID.intValue())));

        given()
                .header("Authorization", "Bearer" + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn400_whenMoreInfoLinkNotExistsTest() {
        Attraction requestBody = mockAttraction.mockRequest(5, segmentation, moreInfoLink, attractionType);
        requestBody.setMoreInfoLinkList(List.of(mockMoreInfoLink.mockEntity(INVALID_ID.intValue())));

        given()
                .header("Authorization", "Bearer" + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void create_shouldReturn401_whenTokenInvalidTest() {
        Attraction requestBody = mockAttraction.mockRequest(6, segmentation, moreInfoLink, attractionType);

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION)
                .then()
                .log().body()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void crete_shouldReturn403_whenUserNotAuthenticatedTest() {
        Attraction requestBody = mockAttraction.mockRequest(7, segmentation, moreInfoLink, attractionType);

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION)
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void update_shouldReturn200_whenIsAValidAttractionTest() {
        Attraction requestBody = mockAttraction.mockRequest(8, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setName("Teatro Municipal de João Pessoa");
        requestBody.setDescription("Teatro Municipal de João Pessoa, localizado no centro da cidade.");
        requestBody.setMap_link("https://mapa.com/teatro-municipal");
        requestBody.setCity("João Pessoa");
        requestBody.setState("Paraíba (PB)");
        requestBody.setImage_link("https://imagem.com/teatro-municipal");
        requestBody.setFonte("Fonte: https://fonte.com/teatro-municipal");

        Attraction response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(requestBody.getName()))
                .body("description", equalTo(requestBody.getDescription()))
                .body("map_link", equalTo(requestBody.getMap_link()))
                .body("city", equalTo(requestBody.getCity()))
                .body("state", equalTo(requestBody.getState()))
                .body("image_link", equalTo(requestBody.getImage_link()))
                .body("fonte", equalTo(requestBody.getFonte()))
                .body("segmentations[0].id", equalTo(segmentation.getId().intValue()))
                .body("attractionTypes.id", equalTo(attractionType.getId().intValue()))
                .body("moreInfoLinkList[0].id", equalTo(moreInfoLink.getId().intValue()))
                .extract()
                .as(Attraction.class);

        attractionRequestUtil.delete(response, token);
    }

    @Test
    void update_shouldReturn400_whenHasNullValuesTest() {
        Attraction requestBody = new Attraction();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + INVALID_ID)
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void update_shouldReturn400_whenImportantFieldsIsEmptyTest() {
        Attraction requestBody = mockAttraction.mockRequest(9, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setName("");
        requestBody.setCity("");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        attractionRequestUtil.delete(savedAttraction, token);
    }

    @Test
    void update_shouldReturn400_whenAttractionTypeNotExistsTest() {
        Attraction requestBody = mockAttraction.mockRequest(10, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setAttractionTypes(mockAttractionType.mockEntity(INVALID_ID.intValue()));

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        attractionRequestUtil.delete(savedAttraction, token);
    }

    @Test
    void update_shouldReturn400_whenSegmentationNotExistsTest() {
        Attraction requestBody = mockAttraction.mockRequest(11, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setSegmentations(List.of(mockTouristSegmentation.mockEntity(INVALID_ID.intValue())));

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        attractionRequestUtil.delete(savedAttraction, token);
    }

    @Test
    void update_shouldReturn400_whenMoreInfoLinkNotExistsTest() {
        Attraction requestBody = mockAttraction.mockRequest(12, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(requestBody, token);

        requestBody.setMoreInfoLinkList(List.of(mockMoreInfoLink.mockEntity(INVALID_ID.intValue())));

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + savedAttraction.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        attractionRequestUtil.delete(savedAttraction, token);
    }

    @Test
    void update_shouldReturn404_whenAttractionNotExistsTest() {
        Attraction requestBody = mockAttraction.mockRequest(13, segmentation, moreInfoLink, attractionType);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + INVALID_ID)
                .then()
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void update_shouldReturn401_whenTokenInvalidTest() {
        Attraction requestBody = mockAttraction.mockRequest(14, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);
        attraction.setName("Updated Name 1");

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + attraction.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

        attractionRequestUtil.delete(attraction, token);
    }

    @Test
    void update_shouldReturn403_whenTokenIsMissing() {
        Attraction requestBody = mockAttraction.mockRequest(15, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);
        attraction.setName("Updated Name 2");

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(PATH_ATTRACTION + "/" + attraction.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.FORBIDDEN.value());

        attractionRequestUtil.delete(attraction, token);
    }

    @Test
    void delete_shouldReturn204_whenAuthenticatedTest() {
        Attraction requestBody = mockAttraction.mockRequest(16, segmentation, moreInfoLink, attractionType);
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
    void findAll_shouldReturn200_whenHasAttractionsTest() {
        Assumptions.assumeTrue(env.matchesProfiles("test-dev"),
                "Test only runs when 'test-dev' profile is active");

        Attraction attraction = attractionRequestUtil.post(mockAttraction.mockRequest(17, segmentation, moreInfoLink, attractionType), token);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .get(PATH_ATTRACTION)
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1))
                .body("name", hasItem(attraction.getName()))
                .body("description", hasItem(attraction.getDescription()))
                .body("map_link", hasItem(attraction.getMap_link()))
                .body("city", hasItem(attraction.getCity()))
                .body("state", hasItem(attraction.getState()))
                .body("image_link", hasItem(attraction.getImage_link()))
                .body("fonte", hasItem(attraction.getFonte()))
                .body("segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("attractionTypes.id[0]", equalTo(attractionType.getId().intValue()))
                .body("moreInfoLinkList[0].id[0]", equalTo(moreInfoLink.getId().intValue()));

        attractionRequestUtil.delete(attraction, token);
    }

    @Test
    void findAll_shouldReturn200AndEmptyList_whenHasNoAttractionsTest() {
        Assumptions.assumeTrue(env.matchesProfiles("test-dev"),
                "Test only runs when 'test-dev' profile is active");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .when()
                .get(PATH_ATTRACTION)
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0))
                .extract()
                .jsonPath()
                .getList(".", Attraction.class);
    }

    @Test
    void findByName_shouldReturn200_whenAttractionWithNameExistsTest() {
        Attraction requestBody = mockAttraction.mockRequest(18, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);

        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_NAME+"?name=" + attraction.getName())
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("name", hasItem(attraction.getName()))
                .body("description", hasItem(attraction.getDescription()))
                .body("map_link", hasItem(attraction.getMap_link()))
                .body("city", hasItem(attraction.getCity()))
                .body("state", hasItem(attraction.getState()))
                .body("image_link", hasItem(attraction.getImage_link()))
                .body("fonte", hasItem(attraction.getFonte()))
                .body("segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("attractionTypes.id[0]", equalTo(attractionType.getId().intValue()))
                .body("moreInfoLinkList[0].id[0]", equalTo(moreInfoLink.getId().intValue()));

        attractionRequestUtil.delete(attraction, token);
    }

    @Test
    void findByName_shouldReturn200AndEmptyList_whenAttractionWithNameNoExistsTest() {
        Attraction requestBody = mockAttraction.mockRequest(19, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);

        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_NAME+"?name=NotExist")
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));

        attractionRequestUtil.delete(attraction, token);
    }

    @Test
    void findByName_shouldReturn200And2Items_whenExists2AttractionsWithNameTest() {
        Attraction requestBody = mockAttraction.mockRequest(20, segmentation, moreInfoLink, attractionType);
        Attraction attraction1 = attractionRequestUtil.post(requestBody, token);

        TouristSegmentation segmentation2 = tourismSegmentationRequestUtil.post(mockTouristSegmentation.mockRequest(14), token);
        AttractionType attractionType2 = attractionTypeRequestUtil.post(mockAttractionType.mockRequest(11), token);
        MoreInfoLink moreInfoLink2 = moreInfoLinkRequestUtil.post(mockMoreInfoLink.mockRequest(12), token);

        requestBody = mockAttraction.mockRequest(21, segmentation2, moreInfoLink2, attractionType2);
        Attraction attraction2 = attractionRequestUtil.post(requestBody, token);

        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_NAME+"?name="+attraction1.getName())
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("name", hasItems(attraction1.getName(), attraction2.getName()))
                .body("description", hasItems(attraction1.getDescription(), attraction2.getDescription()))
                .body("map_link", hasItems(attraction1.getMap_link(), attraction2.getMap_link()))
                .body("city", hasItems(attraction1.getCity(), attraction2.getCity()))
                .body("state", hasItems(attraction1.getState(), attraction2.getState()))
                .body("image_link", hasItems(attraction1.getImage_link(), attraction2.getImage_link()))
                .body("fonte", hasItems(attraction1.getFonte(), attraction2.getFonte()))
                .body("segmentations.id.flatten()", hasItems(segmentation.getId().intValue(), segmentation2.getId().intValue()))
                .body("attractionTypes.id.flatten()", hasItems(attractionType.getId().intValue(), attractionType2.getId().intValue()))
                .body("moreInfoLinkList.id.flatten()", hasItems(moreInfoLink.getId().intValue(), moreInfoLink2.getId().intValue()));

        attractionRequestUtil.delete(attraction1, token);
        attractionRequestUtil.delete(attraction2, token);

        tourismSegmentationRequestUtil.delete(segmentation2, token);
        attractionTypeRequestUtil.delete(attractionType2, token);
        moreInfoLinkRequestUtil.delete(moreInfoLink2, token);
    }

    @Test
    void findByCity_shouldReturn200_whenAttractionExistsInCityTest() {
        Attraction request = mockAttraction.mockRequest(22, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(request, token);

        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_CITY+ "?city=" + request.getCity())
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("name", hasItem(savedAttraction.getName()))
                .body("description", hasItem(savedAttraction.getDescription()))
                .body("map_link", hasItem(savedAttraction.getMap_link()))
                .body("city", hasItem(savedAttraction.getCity()))
                .body("state", hasItem(savedAttraction.getState()))
                .body("image_link", hasItem(savedAttraction.getImage_link()))
                .body("fonte", hasItem(savedAttraction.getFonte()))
                .body("segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("attractionTypes.id[0]", equalTo(attractionType.getId().intValue()))
                .body("moreInfoLinkList[0].id[0]", equalTo(moreInfoLink.getId().intValue()))
                .extract()
                .jsonPath()
                .getList(".", Attraction.class).get(0);

        attractionRequestUtil.delete(savedAttraction, token);
    }

    @Test
    void findByCity_shouldReturn200AndEmptyList_whenAttractionNoExistsInCityTest() {
        Attraction requestBody = mockAttraction.mockRequest(23, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);

        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_CITY+"?city=Rio Tinto")
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));

        attractionRequestUtil.delete(attraction, token);
    }

    @Test
    void findByCity_shouldReturn404_whenCityNoExistsTest() {
        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_CITY+"?city=CidadeNãoExiste")
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    void findByCity_shouldReturn200And2Items_when2AttractionsExistsInCityTest() {
        Attraction requestBody = mockAttraction.mockRequest(24, segmentation, moreInfoLink, attractionType);
        Attraction attraction1 = attractionRequestUtil.post(requestBody, token);

        TouristSegmentation segmentation2 = tourismSegmentationRequestUtil.post(mockTouristSegmentation.mockRequest(15), token);
        AttractionType attractionType2 = attractionTypeRequestUtil.post(mockAttractionType.mockRequest(12), token);
        MoreInfoLink moreInfoLink2 = moreInfoLinkRequestUtil.post(mockMoreInfoLink.mockRequest(13), token);

        requestBody = mockAttraction.mockRequest(25, segmentation2, moreInfoLink2, attractionType2);
        Attraction attraction2 = attractionRequestUtil.post(requestBody, token);

        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_CITY+"?city="+attraction1.getCity())
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("name", hasItems(attraction1.getName(), attraction2.getName()))
                .body("description", hasItems(attraction1.getDescription(), attraction2.getDescription()))
                .body("map_link", hasItems(attraction1.getMap_link(), attraction2.getMap_link()))
                .body("city", hasItems(attraction1.getCity(), attraction2.getCity()))
                .body("state", hasItems(attraction1.getState(), attraction2.getState()))
                .body("image_link", hasItems(attraction1.getImage_link(), attraction2.getImage_link()))
                .body("fonte", hasItems(attraction1.getFonte(), attraction2.getFonte()))
                .body("segmentations.id.flatten()", hasItems(segmentation.getId().intValue(), segmentation2.getId().intValue()))
                .body("attractionTypes.id.flatten()", hasItems(attractionType.getId().intValue(), attractionType2.getId().intValue()))
                .body("moreInfoLinkList.id.flatten()", hasItems(moreInfoLink.getId().intValue(), moreInfoLink2.getId().intValue()));

        attractionRequestUtil.delete(attraction1, token);
        attractionRequestUtil.delete(attraction2, token);

        tourismSegmentationRequestUtil.delete(segmentation2, token);
        attractionTypeRequestUtil.delete(attractionType2, token);
        moreInfoLinkRequestUtil.delete(moreInfoLink2, token);
    }

    @Test
    void findBySegmentation_shouldReturn200_whenAttractionExistsInSegmentationTest() {
        Attraction request = mockAttraction.mockRequest(26, segmentation, moreInfoLink, attractionType);
        Attraction savedAttraction = attractionRequestUtil.post(request, token);

        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_SEGMENTATION+ "?segmentations=" + segmentation.getName())
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("name", hasItem(savedAttraction.getName()))
                .body("description", hasItem(savedAttraction.getDescription()))
                .body("map_link", hasItem(savedAttraction.getMap_link()))
                .body("city", hasItem(savedAttraction.getCity()))
                .body("state", hasItem(savedAttraction.getState()))
                .body("image_link", hasItem(savedAttraction.getImage_link()))
                .body("fonte", hasItem(savedAttraction.getFonte()))
                .body("segmentations[0].id[0]", equalTo(segmentation.getId().intValue()))
                .body("attractionTypes.id[0]", equalTo(attractionType.getId().intValue()))
                .body("moreInfoLinkList[0].id[0]", equalTo(moreInfoLink.getId().intValue()));

        attractionRequestUtil.delete(savedAttraction, token);
    }

    @Test
    void findBySegmentation_shouldReturn200AndEmptyList_whenAttractionNoExistsInSegmentationTest() {
        Attraction requestBody = mockAttraction.mockRequest(27, segmentation, moreInfoLink, attractionType);
        Attraction attraction = attractionRequestUtil.post(requestBody, token);

        TouristSegmentation segmentationRequest = mockTouristSegmentation.mockRequest(16);
        TouristSegmentation segmentation1 = tourismSegmentationRequestUtil.post(segmentationRequest, token);

        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_SEGMENTATION+"?segmentations="+segmentation1.getName())
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));

        attractionRequestUtil.delete(attraction, token);
        tourismSegmentationRequestUtil.delete(segmentation1, token);
    }

    @Test
    void findBySegmentation_shouldReturn200And2Items_when2AttractionsExistsInSegmentationTest() {
        Attraction requestBody = mockAttraction.mockRequest(28, segmentation, moreInfoLink, attractionType);
        Attraction attraction1 = attractionRequestUtil.post(requestBody, token);

        TouristSegmentation segmentation2 = tourismSegmentationRequestUtil.post(mockTouristSegmentation.mockRequest(17), token);
        AttractionType attractionType2 = attractionTypeRequestUtil.post(mockAttractionType.mockRequest(13), token);
        MoreInfoLink moreInfoLink2 = moreInfoLinkRequestUtil.post(mockMoreInfoLink.mockRequest(14), token);

        requestBody = mockAttraction.mockRequest(29, segmentation2, moreInfoLink2, attractionType2);
        Attraction attraction2 = attractionRequestUtil.post(requestBody, token);

        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_SEGMENTATION+"?segmentations="+segmentation.getName())
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("name", hasItems(attraction1.getName(), attraction2.getName()))
                .body("description", hasItems(attraction1.getDescription(), attraction2.getDescription()))
                .body("map_link", hasItems(attraction1.getMap_link(), attraction2.getMap_link()))
                .body("city", hasItems(attraction1.getCity(), attraction2.getCity()))
                .body("state", hasItems(attraction1.getState(), attraction2.getState()))
                .body("image_link", hasItems(attraction1.getImage_link(), attraction2.getImage_link()))
                .body("fonte", hasItems(attraction1.getFonte(), attraction2.getFonte()))
                .body("segmentations.id.flatten()", hasItems(segmentation.getId().intValue(), segmentation2.getId().intValue()))
                .body("attractionTypes.id.flatten()", hasItems(attractionType.getId().intValue(), attractionType2.getId().intValue()))
                .body("moreInfoLinkList.id.flatten()", hasItems(moreInfoLink.getId().intValue(), moreInfoLink2.getId().intValue()));


        attractionRequestUtil.delete(attraction1, token);
        attractionRequestUtil.delete(attraction2, token);

        tourismSegmentationRequestUtil.delete(segmentation2, token);
        attractionTypeRequestUtil.delete(attractionType2, token);
        moreInfoLinkRequestUtil.delete(moreInfoLink2, token);
    }

    @Test
    void findBySegmentation_shouldReturn404_whenSegmentationNoExistsTest() {
        given()
                .contentType("application/json")
                .when()
                .get(PATH_FIND_ATTRACTION_BY_SEGMENTATION+"?segmentations=segmentacao_que_nao_existe")
                .then()
                .log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}