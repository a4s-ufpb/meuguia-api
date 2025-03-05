package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.AttractionTypeTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.MoreInfoLinkTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.mock.TourismSegmentationTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionRequestUtil;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionTypeRequestUtil;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.MoreInfoLinkRequestUtil;
import br.ufpb.dcx.apps4society.meuguiapbapi.util.TourismSegmentationRequestUtil;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionRequestUtil.PATH_ATTRACTION;
import static br.ufpb.dcx.apps4society.meuguiapbapi.util.MoreInfoLinkRequestUtil.PATH_MORE_INFO_LINK;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

class MoreInfoLinkControllerTest extends MeuguiaApiApplicationTests {
    AttractionRequestUtil attractionRequestUtil = AttractionRequestUtil.getInstance();
    AttractionTestHelper attractionTestHelper = AttractionTestHelper.getInstance();
    AttractionTypeRequestUtil attractionTypeRequestUtil = AttractionTypeRequestUtil.getInstance();
    AttractionTypeTestHelper attractionTypeTestHelper = AttractionTypeTestHelper.getInstance();
    TourismSegmentationRequestUtil tourismSegmentationRequestUtil = TourismSegmentationRequestUtil.getInstance();
    TourismSegmentationTestHelper tourismSegmentationTestHelper = TourismSegmentationTestHelper.getInstance();

    Attraction attraction;
    AttractionType attractionType;
    TourismSegmentation tourismSegmentation;
    MoreInfoLinkRequestData moreInfoLinkRequestData;

    MoreInfoLinkTestHelper moreInfoLinkTestHelper = MoreInfoLinkTestHelper.getInstance();
    MoreInfoLinkRequestUtil moreInfoLinkRequestUtil = MoreInfoLinkRequestUtil.getInstance();

    String token;

    @BeforeAll
    void setUp() {
        token = userRequestUtil.registerAndAuthenticate(
                authenticationTestHelper.getRegisterRequestData(1)
        ).getToken();
        log.info("Token: {}", token);
    }

    @BeforeEach
    void setUpEach() {
        moreInfoLinkRequestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        attractionType = attractionTypeRequestUtil.post(attractionTypeTestHelper.createAttractionTypeRequestData(1), token);
        tourismSegmentation = tourismSegmentationRequestUtil.post(tourismSegmentationTestHelper.createTourismSegmentationRequestData(1), token);

        AttractionRequestData attractionRequestData = attractionTestHelper.createAttractionRequestData(1,tourismSegmentation, moreInfoLinkRequestData, attractionType);
        attractionRequestData.setMoreInfoLinks(List.of());
        attraction = attractionRequestUtil.post(attractionRequestData, token);
    }

    @AfterEach
    void tearDown() {
        attractionRequestUtil.delete(attraction.getId(), token);
        attractionTypeRequestUtil.delete(attractionType, token);
        tourismSegmentationRequestUtil.delete(tourismSegmentation, token);
    }

    @Test
    void addMoreInfoLinkToAttraction_ShouldReturn201() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestData)
                .post(PATH_ATTRACTION  + "/" + attraction.getId() + PATH_MORE_INFO_LINK)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("link", equalTo(requestData.getLink()))
                .body("description", equalTo(requestData.getDescription()));
    }

    @Test
    void addMoreInfoLinkToAttraction_ShouldReturn404_WhenAttractionDoesNotExist() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestData)
                .post(PATH_ATTRACTION + "/" + 0 + PATH_MORE_INFO_LINK)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void addMoreInfoLinkToAttraction_ShouldReturn403_WhenUserIsNotAuthenticated() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);

        given()
                .contentType(ContentType.JSON)
                .body(requestData)
                .post(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void getMoreInfoLinkFromAttraction_ShouldReturn200() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        moreInfoLinkRequestUtil.post(requestData, attraction.getId(), token);

        String encodedLink = URLEncoder.encode(requestData.getLink(), StandardCharsets.UTF_8);
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .get(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=" + encodedLink)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("link", equalTo(requestData.getLink()))
                .body("description", equalTo(requestData.getDescription()));
    }

    @Test
    void getMoreInfoLinkShouldReturn200_WhenUserIsNotAuthenticated() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        moreInfoLinkRequestUtil.post(requestData, attraction.getId(), token);

        String encodedLink = URLEncoder.encode(requestData.getLink(), StandardCharsets.UTF_8);
        given()
                .contentType(ContentType.JSON)
                .get(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=" + requestData.getLink())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("link", equalTo(requestData.getLink()))
                .body("description", equalTo(requestData.getDescription()));
    }

    @Test
    void getMoreInfoLink_ShouldReturn200_WhenLinkIsNotEncoded() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        moreInfoLinkRequestUtil.post(requestData, attraction.getId(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .get(PATH_ATTRACTION + "/"+ attraction.getId() + PATH_MORE_INFO_LINK + "?link=" + requestData.getLink())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("link", equalTo(requestData.getLink()))
                .body("description", equalTo(requestData.getDescription()));
    }

    @Test
    void getMoreInfoLinkShouldReturn404_WhenLinkDoesNotExist() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .get(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=link")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getMoreInfoLinkShouldReturn404_WhenAttractionDoesNotExist() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .get(PATH_ATTRACTION + "/" + 0 + PATH_MORE_INFO_LINK + "?link=link")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateMoreInfoLinkFromAttraction_ShouldReturn200() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        moreInfoLinkRequestUtil.post(requestData, attraction.getId(), token);

        MoreInfoLinkRequestData updatedRequestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(2);
        updatedRequestData.setLink(requestData.getLink());
        String encodedLink = URLEncoder.encode(requestData.getLink(), StandardCharsets.UTF_8);
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(updatedRequestData)
                .put(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=" + requestData.getLink())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("link", equalTo(updatedRequestData.getLink()))
                .body("description", equalTo(updatedRequestData.getDescription()));
    }

    @Test
    void updateMoreInfoLinkFromAttraction_ShouldReturn404_WhenLinkDoesNotExist() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        moreInfoLinkRequestUtil.post(requestData, attraction.getId(), token);

        MoreInfoLinkRequestData updatedRequestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(2);
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(updatedRequestData)
                .put(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=link")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateMoreInfoLinkFromAttraction_ShouldReturn404_WhenAttractionDoesNotExist() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        moreInfoLinkRequestUtil.post(requestData, attraction.getId(), token);

        MoreInfoLinkRequestData updatedRequestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(2);
        String encodedLink = URLEncoder.encode(updatedRequestData.getLink(), StandardCharsets.UTF_8);
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(updatedRequestData)
                .put(PATH_ATTRACTION + "/" + 0 + PATH_MORE_INFO_LINK + "?link=" + encodedLink)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateMoreInfoLinkFromAttraction_ShouldReturn400_WhenMoreInfoLinkIsInvalid() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        moreInfoLinkRequestUtil.post(requestData, attraction.getId(), token);

        MoreInfoLinkRequestData updatedRequestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(3);
        updatedRequestData.setLink("invalidLink");
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(updatedRequestData)
                .put(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=" + requestData.getLink())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void removeMoreInfoLinkFromAttraction_ShouldReturn204() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        moreInfoLinkRequestUtil.post(requestData, attraction.getId(), token);

        String encodedLink = URLEncoder.encode(requestData.getLink(), StandardCharsets.UTF_8);
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .delete(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=" + encodedLink)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void removeMoreInfoLinkFromAttraction_ShouldReturn204_WhenLinkIsNotEncoded() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        moreInfoLinkRequestUtil.post(requestData, attraction.getId(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .delete(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=" + requestData.getLink())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void removeMoreInfoLinkFromAttraction_ShouldReturn404_WhenLinkDoesNotExist() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .delete(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=link")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void removeMoreInfoLinkFromAttraction_ShouldReturn404_WhenAttractionDoesNotExist() {
        MoreInfoLinkRequestData requestData = moreInfoLinkTestHelper.createMoreInfoLinkRequestData(1);
        moreInfoLinkRequestUtil.post(requestData, attraction.getId(), token);

        String encodedLink = URLEncoder.encode(requestData.getLink(), StandardCharsets.UTF_8);
        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .delete(PATH_ATTRACTION + "/" + 0 + PATH_MORE_INFO_LINK + "?link=" + encodedLink)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}