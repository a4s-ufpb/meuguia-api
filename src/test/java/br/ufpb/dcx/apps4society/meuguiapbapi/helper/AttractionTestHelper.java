package br.ufpb.dcx.apps4society.meuguiapbapi.helper;

import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.domain.Attraction;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.attraction.dto.AttractionRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.tourismsegmentation.domain.TourismSegmentation;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper.createAttractionType;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTypeTestHelper.createAttractionTypeDTO;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.CityTestHelper.createDefaultCityDTO;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.CityTestHelper.createDefaultCityObject;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.MoreInfoLinkTestHelper.*;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper.createTourismSegmentation;
import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.TourismSegmentationTestHelper.createTourismSegmentationDTO;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttractionTestHelper {
    public static final String PATH_ATTRACTION = "/attractions";
    public static final String PATH_CREATE_ATTRACTION = PATH_ATTRACTION;
    public static final String PATH_SEARCH_ATTRACTIONS = PATH_ATTRACTION + "/search";

    public static AttractionRequestData createAttractionRequestData(
            Integer num,
            TourismSegmentation segmentation,
            MoreInfoLinkRequestData moreInfoLinkRequestData,
            AttractionType attractionType
    ) {
        return AttractionRequestData.builder()
                .name("mock Teatro municipal " + num)
                .description("Teatro municipal de joão pessoa")
                .mapLink("https://mapa.com")
                .imageLink("https://imagem.com")
                .cityId(1L)
                .segmentations(List.of(segmentation.getId()))
                .attractionType(attractionType.getId())
                .moreInfoLinks(List.of(moreInfoLinkRequestData))
                .build();
    }

    public static AttractionRequestData createAttractionRequestData(Integer num) {
        return createAttractionRequestData(
                num,
                createTourismSegmentation(num),
                createMoreInfoLinkRequestData(num),
                createAttractionType(num)
        );
    }

    // TODO: Corrigir a cidade
    public static Attraction createAttraction(
            Integer num,
            TourismSegmentation segmentation,
            MoreInfoLink moreInfoLink,
            AttractionType attractionType
    ) {
        return Attraction.builder()
                .id(num.longValue())
                .name("mock Teatro municipal " + num)
                .description("Teatro municipal de joão pessoa")
                .mapLink("https://mapa.com")
                .city(createDefaultCityObject())
                .imageLink("https://imagem.com")
                .segmentations(new ArrayList<>(List.of(segmentation)))
                .attractionType(attractionType)
                .moreInfoLinks(new ArrayList<>(List.of(moreInfoLink)))
                .build();

    }

    public static Attraction createAttraction(Integer num) {
        return createAttraction(
                num,
                createTourismSegmentation(num),
                createMoreInfoLink(num),
                createAttractionType(num)
        );
    }

    public static List<Attraction> createAttractionList() {
        return List.of(
                createAttraction(1, createTourismSegmentation(1), createMoreInfoLink(1), createAttractionType(1)),
                createAttraction(2, createTourismSegmentation(2), createMoreInfoLink(2), createAttractionType(2)),
                createAttraction(3, createTourismSegmentation(3), createMoreInfoLink(3), createAttractionType(3))
        );
    }

    public static AttractionDTO createAttractionDTO(Integer id) {
        return AttractionDTO.builder()
                .id(id.longValue())
                .name("mock Teatro municipal " + id)
                .description("Teatro municipal de joão pessoa")
                .mapLink("https://mapa.com")
                .imageLink("https://imagem.com")
                .city(createDefaultCityDTO())
                .segmentations(List.of(createTourismSegmentationDTO(id)))
                .attractionTypes(createAttractionTypeDTO(id))
                .moreInfoLinks(List.of(createMoreInfoLinkDTO(id)))
                .build();
    }

    public static void delete(Long attractionId, String token) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_ATTRACTION + "/" + attractionId)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    public static void delete(Attraction attraction, String token) {
        delete(attraction.getId(), token);
    }

    public static Attraction post(AttractionRequestData requestBody, String token) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post(PATH_CREATE_ATTRACTION)
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(Attraction.class);
    }

    public static void assertAttractionEqualsToRequestData(Attraction attraction, AttractionRequestData attractionRequestData) {
        assertEquals(attractionRequestData.getName(), attraction.getName());
        assertEquals(attractionRequestData.getDescription(), attraction.getDescription());
        assertEquals(attractionRequestData.getMapLink(), attraction.getMapLink());
        assertEquals(attractionRequestData.getCityId(), attraction.getCity().getId());
        assertEquals(attractionRequestData.getImageLink(), attraction.getImageLink());
    }
}
