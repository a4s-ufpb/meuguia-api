package br.ufpb.dcx.apps4society.meuguiapbapi.helper;

import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkRequestData;
import org.springframework.http.HttpStatus;

import java.util.List;

import static br.ufpb.dcx.apps4society.meuguiapbapi.helper.AttractionTestHelper.PATH_ATTRACTION;
import static io.restassured.RestAssured.given;

public class MoreInfoLinkTestHelper {

    public static final String PATH_MORE_INFO_LINK = "/more-info-links";

    public static MoreInfoLink createMoreInfoLink(Integer num) {
        return MoreInfoLink.builder()
                .link("https://www.mock-link" + num + ".com")
                .description("description")
                .build();
    }

    public static MoreInfoLinkDTO createMoreInfoLinkDTO(Integer num) {
        return MoreInfoLinkDTO.builder()
                .link("https://www.mock-link" + num + ".com")
                .description("description")
                .build();
    }

    public static MoreInfoLinkRequestData createMoreInfoLinkRequestData(Integer num) {
        return MoreInfoLinkRequestData.builder()
                .link("https://www.mock-link" + num + ".com")
                .description("description")
                .build();

    }

    public static List<MoreInfoLink> getListOfMoreInfoLinks() {
        return List.of(
                createMoreInfoLink(1),
                createMoreInfoLink(2),
                createMoreInfoLink(3)
        );
    }

    public static List<MoreInfoLinkDTO> getListOfMoreInfoLinksDTO() {
        return List.of(
                createMoreInfoLinkDTO(1),
                createMoreInfoLinkDTO(2),
                createMoreInfoLinkDTO(3)
        );
    }

    public static List<MoreInfoLinkRequestData> getListOfMoreInfoLinksRequestData() {
        return List.of(
                createMoreInfoLinkRequestData(1),
                createMoreInfoLinkRequestData(2),
                createMoreInfoLinkRequestData(3)
        );
    }

    public static MoreInfoLinkDTO post(MoreInfoLinkRequestData request, Long attractionId, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(request)
                .when()
                .post(PATH_ATTRACTION + "/" + attractionId + PATH_MORE_INFO_LINK)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(MoreInfoLinkDTO.class);
    }

    public static void delete(MoreInfoLinkDTO request, Long attractionId, String token) {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(request)
                .when()
                .post(PATH_ATTRACTION + "/" + attractionId + PATH_MORE_INFO_LINK + "?link=" + request.getLink())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
