package br.ufpb.dcx.apps4society.meuguiapbapi.util;

import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.moreinfolink.dto.MoreInfoLinkRequestData;
import org.springframework.http.HttpStatus;

import static br.ufpb.dcx.apps4society.meuguiapbapi.util.AttractionRequestUtil.PATH_ATTRACTION;
import static io.restassured.RestAssured.given;

public class MoreInfoLinkRequestUtil extends RequestUtil {
    public static final String PATH_MORE_INFO_LINK = "/more-info-links";

    private static MoreInfoLinkRequestUtil instance;

    public static MoreInfoLinkRequestUtil getInstance() {
        if (instance == null) {
            instance = new MoreInfoLinkRequestUtil();
        }
        return instance;
    }

    public MoreInfoLinkDTO post(MoreInfoLinkRequestData request, Long attractionId, String token) {
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

    public void delete(MoreInfoLinkDTO request, Long attractionId, String token) {
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
