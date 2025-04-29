package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.dto.CityRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.CityTestHelper;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.UserTestsHelper;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class CityControllerTest extends MeuguiaApiApplicationTests {
    private static final String CITY_PATH = "/cities";

    @Autowired
    private UserTestsHelper userTestsHelper;
    @Autowired
    private CityTestHelper cityTestHelper;

    private String adminApiToken;
    private String defaultApiToken;

    private CityRequestData cityRequestData;

    @BeforeAll
    public void setUp() {
        this.cityRequestData = new CityRequestData("MockCity", "MockState", "MockCountry", "MC");
        this.adminApiToken = userTestsHelper.createAndAuthenticateUserWithAdminPermission();
        this.defaultApiToken = userTestsHelper.createAndAuthenticateUserWithDefaultPermissions();
    }

    @AfterAll
    public void tearDown() {
        this.userTestsHelper.deleteLastUserCreated();
    }

    @Test
    public void createCity_shouldReturn200_whenUserHasPermission() {
        // request to create city
        try {
            given()
                    .header("Authorization", "Bearer " + this.adminApiToken)
                    .contentType("application/json")
                    .body(cityRequestData)
                    .when()
                    .post(CITY_PATH)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", notNullValue())
                    .body("name", equalTo(cityRequestData.getName()))
                    .body("state", equalTo(cityRequestData.getState()))
                    .body("country", equalTo(cityRequestData.getCountry()))
                    .body("state_abbreviation", equalTo(cityRequestData.getStateAbbreviation()));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        } finally {
            this.cityTestHelper.deleteCityWithNameAndCountry(cityRequestData.getName(), cityRequestData.getCountry());
        }
    }

    @Test
    @Disabled
    public void createCity_shouldReturn403_whenUserHasNoPermission() {
        try {
            given()
                    .header("Authorization", "Bearer " + this.defaultApiToken)
                    .contentType("application/json")
                    .body(cityRequestData)
                    .when()
                    .post(CITY_PATH)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.FORBIDDEN.value());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Test
    public void createCity_shouldReturn403_whenAuthorizationHeaderIsNotPresent() {
        try {
            given()
                    .contentType("application/json")
                    .body(cityRequestData)
                    .when()
                    .post(CITY_PATH)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.FORBIDDEN.value());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Test
    public void createCity_shouldReturn400_whenCityRequestDataIsInvalid() {
        var invalidRequestData = new CityRequestData("", "", "", "");
        try {
            given()
                    .header("Authorization", "Bearer " + this.adminApiToken)
                    .contentType("application/json")
                    .body(invalidRequestData)
                    .when()
                    .post(CITY_PATH)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Test
    @Disabled
    public void createCity_shouldReturn400_whenCityRequestDataIsNull() {
        try {
            given()
                    .header("Authorization", "Bearer " + this.adminApiToken)
                    .contentType("application/json")
                    .when()
                    .post(CITY_PATH)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }
}
