package br.ufpb.dcx.apps4society.meuguiapbapi.util;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.MoreInfoLink;
import br.ufpb.dcx.apps4society.meuguiapbapi.dto.moreinfolink.MoreInfoLinkRequestData;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class MoreInfoLinkRequestUtil extends RequestUtil {
    public static final String PATH_MORE_INFO_LINK = "/more-info";

    private static MoreInfoLinkRequestUtil instance;

    public static MoreInfoLinkRequestUtil getInstance() {
        if (instance == null) {
            instance = new MoreInfoLinkRequestUtil();
        }
        return instance;
    }

    public MoreInfoLink post(MoreInfoLinkRequestData request, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(request)
                .when()
                .post(PATH_MORE_INFO_LINK)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(MoreInfoLink.class);
    }

    public void delete(MoreInfoLink request, String token) {
//        given()
//                .header("Authorization", "Bearer " + token)
//                .contentType("application/json")
//                .body(request)
//                .when()
//                .delete(PATH_MORE_INFO_LINK + "/" + request.getId())
//                .then()
//                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
