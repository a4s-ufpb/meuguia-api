package br.ufpb.dcx.apps4society.meuguiapbapi.integration;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTestHelper.PATH_ATTRACTION;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTestHelper.createAttractionRequestData;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper.createAttractionTypeRequestData;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper.PATH_MORE_INFO_LINK;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper.createMoreInfoLinkRequestData;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper.createTourismSegmentationRequestData;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Disabled
class MoreInfoLinkControllerTest extends MeuguiaApiApplicationTests {
    Attraction attraction;
    AttractionType attractionType;
    TourismSegmentation tourismSegmentation;
    MoreInfoLinkRequestData moreInfoLinkRequestData;


    @BeforeEach
    void setUpEach() {
        moreInfoLinkRequestData = createMoreInfoLinkRequestData(1);
        attractionType = AttractionTypeTestHelper.post(createAttractionTypeRequestData(1), getDefaultToken());
        tourismSegmentation = TourismSegmentationTestHelper.post(createTourismSegmentationRequestData(1), getDefaultToken());

        AttractionRequestData attractionRequestData = createAttractionRequestData(1, tourismSegmentation, moreInfoLinkRequestData, attractionType);
        attractionRequestData.setMoreInfoLinks(List.of());
        attraction = AttractionTestHelper.post(attractionRequestData, getDefaultToken());
    }

    @AfterEach
    void tearDown() {
        AttractionTestHelper.delete(attraction.getId(), getDefaultToken());
        AttractionTypeTestHelper.delete(attractionType, getDefaultToken());
        TourismSegmentationTestHelper.delete(tourismSegmentation, getDefaultToken());
    }

    @Test
    void addMoreInfoLinkToAttraction_ShouldReturn201() {
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .body(requestData)
                .post(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("link", equalTo(requestData.getLink()))
                .body("description", equalTo(requestData.getDescription()));
    }

    @Test
    void addMoreInfoLinkToAttraction_ShouldReturn404_WhenAttractionDoesNotExist() {
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .body(requestData)
                .post(PATH_ATTRACTION + "/" + 0 + PATH_MORE_INFO_LINK)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void addMoreInfoLinkToAttraction_ShouldReturn403_WhenUserIsNotAuthenticated() {
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);

        given()
                .contentType(ContentType.JSON)
                .body(requestData)
                .post(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void getMoreInfoLinkFromAttraction_ShouldReturn200() {
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        MoreInfoLinkTestHelper.post(requestData, attraction.getId(), getDefaultToken());

        String encodedLink = URLEncoder.encode(requestData.getLink(), StandardCharsets.UTF_8);
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .get(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=" + encodedLink)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("link", equalTo(requestData.getLink()))
                .body("description", equalTo(requestData.getDescription()));
    }

    @Test
    void getMoreInfoLinkShouldReturn200_WhenUserIsNotAuthenticated() {
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        MoreInfoLinkTestHelper.post(requestData, attraction.getId(), getDefaultToken());

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
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        MoreInfoLinkTestHelper.post(requestData, attraction.getId(), getDefaultToken());

        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .get(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=" + requestData.getLink())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("link", equalTo(requestData.getLink()))
                .body("description", equalTo(requestData.getDescription()));
    }

    @Test
    void getMoreInfoLinkShouldReturn404_WhenLinkDoesNotExist() {
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .get(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=link")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getMoreInfoLinkShouldReturn404_WhenAttractionDoesNotExist() {
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .get(PATH_ATTRACTION + "/" + 0 + PATH_MORE_INFO_LINK + "?link=link")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateMoreInfoLinkFromAttraction_ShouldReturn200() {
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        MoreInfoLinkTestHelper.post(requestData, attraction.getId(), getDefaultToken());

        MoreInfoLinkRequestData updatedRequestData = createMoreInfoLinkRequestData(2);
        updatedRequestData.setLink(requestData.getLink());
        String encodedLink = URLEncoder.encode(requestData.getLink(), StandardCharsets.UTF_8);
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
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
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        MoreInfoLinkTestHelper.post(requestData, attraction.getId(), getDefaultToken());

        MoreInfoLinkRequestData updatedRequestData = createMoreInfoLinkRequestData(2);
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .body(updatedRequestData)
                .put(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=link")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateMoreInfoLinkFromAttraction_ShouldReturn404_WhenAttractionDoesNotExist() {
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        MoreInfoLinkTestHelper.post(requestData, attraction.getId(), getDefaultToken());

        MoreInfoLinkRequestData updatedRequestData = createMoreInfoLinkRequestData(2);
        String encodedLink = URLEncoder.encode(updatedRequestData.getLink(), StandardCharsets.UTF_8);
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .body(updatedRequestData)
                .put(PATH_ATTRACTION + "/" + 0 + PATH_MORE_INFO_LINK + "?link=" + encodedLink)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateMoreInfoLinkFromAttraction_ShouldReturn400_WhenMoreInfoLinkIsInvalid() {
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        MoreInfoLinkTestHelper.post(requestData, attraction.getId(), getDefaultToken());

        MoreInfoLinkRequestData updatedRequestData = createMoreInfoLinkRequestData(3);
        updatedRequestData.setLink("invalidLink");
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .body(updatedRequestData)
                .put(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=" + requestData.getLink())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void removeMoreInfoLinkFromAttraction_ShouldReturn204() {
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        MoreInfoLinkTestHelper.post(requestData, attraction.getId(), getDefaultToken());

        String encodedLink = URLEncoder.encode(requestData.getLink(), StandardCharsets.UTF_8);
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .delete(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=" + encodedLink)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void removeMoreInfoLinkFromAttraction_ShouldReturn204_WhenLinkIsNotEncoded() {
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        MoreInfoLinkTestHelper.post(requestData, attraction.getId(), getDefaultToken());

        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .delete(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=" + requestData.getLink())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void removeMoreInfoLinkFromAttraction_ShouldReturn404_WhenLinkDoesNotExist() {
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .delete(PATH_ATTRACTION + "/" + attraction.getId() + PATH_MORE_INFO_LINK + "?link=link")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void removeMoreInfoLinkFromAttraction_ShouldReturn404_WhenAttractionDoesNotExist() {
        MoreInfoLinkRequestData requestData = createMoreInfoLinkRequestData(1);
        MoreInfoLinkTestHelper.post(requestData, attraction.getId(), getDefaultToken());

        String encodedLink = URLEncoder.encode(requestData.getLink(), StandardCharsets.UTF_8);
        given()
                .header("Authorization", "Bearer " + getDefaultToken())
                .contentType(ContentType.JSON)
                .delete(PATH_ATTRACTION + "/" + 0 + PATH_MORE_INFO_LINK + "?link=" + encodedLink)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}