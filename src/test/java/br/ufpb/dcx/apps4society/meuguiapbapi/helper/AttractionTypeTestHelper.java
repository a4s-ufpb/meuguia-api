package br.ufpb.dcx.apps4society.meuguiapbapi.helper;

import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.domain.AttractionType;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.dto.AttractionTypeDTO;
import br.ufpb.dcx.apps4society.meuguiapbapi.attractiontype.dto.AttractionTypeRequestData;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;

public class AttractionTypeTestHelper {

    public static final String PATH_ATTRACTION_TYPE = "/types";

    public static AttractionType createAttractionType(Integer num) {
        return AttractionType.builder()
                .id(num.longValue())
                .name("mock Cultural" + num)
                .description("Turismo cultural, visando pontos históricos")
                .build();
    }

    public static AttractionTypeDTO createAttractionTypeDTO(Integer num) {
        return AttractionTypeDTO.builder()
                .id(num.longValue())
                .name("mock Cultural" + num)
                .description("Turismo cultural, visando pontos históricos")
                .build();
    }

    public static AttractionTypeRequestData createAttractionTypeRequestData(Integer num) {
        return AttractionTypeRequestData.builder()
                .name("mock Cultural" + num)
                .description("Turismo cultural, visando pontos históricos")
                .build();
    }

    public static List<AttractionType> createAttractionTypeList() {
        return List.of(
                createAttractionType(1),
                createAttractionType(2),
                createAttractionType(3)
        );
    }

    public static AttractionType post(AttractionTypeRequestData request, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(PATH_ATTRACTION_TYPE)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(AttractionType.class);
    }

    public static void delete(AttractionType request, String token) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PATH_ATTRACTION_TYPE + "/" + request.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
