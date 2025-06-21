package br.ufpb.dcx.apps4society.meuguiapbapi.controller;

import br.ufpb.dcx.apps4society.meuguiapbapi.MeuguiaApiApplicationTests;
import br.ufpb.dcx.apps4society.meuguiapbapi.city.dto.CityRequestData;
import br.ufpb.dcx.apps4society.meuguiapbapi.helper.CityTestHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

class CityControllerTest extends MeuguiaApiApplicationTests {
    private static final String CITY_PATH = "/cities";

    @Autowired
    private CityTestHelper cityTestHelper;
    private CityRequestData cityRequestData;

    @BeforeAll
    public void setUp() {
        this.cityRequestData = new CityRequestData("MockCity", "MockState", "MockCountry", "MC");
    }

    @Test
    public void createCity_shouldReturn200_whenUserHasPermission() {
        // request to create city
        try {
            given()
                    .header("Authorization", "Bearer " + getAdminToken())
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
    public void createCity_shouldReturn403_whenUserHasNoPermission() {
        try {
            given()
                    .header("Authorization", "Bearer " + getDefaultToken())
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
                    .header("Authorization", "Bearer " + getAdminToken())
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
    public void createCity_shouldReturn500_whenCityRequestDataIsNull() {
        try {
            given()
                    .header("Authorization", "Bearer " + getAdminToken())
                    .contentType("application/json")
                    .when()
                    .post(CITY_PATH)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Test
    public void createCity_shouldReturn400_whenCityRequestDataIsEmpty() {
        try {
            given()
                    .header("Authorization", "Bearer " + getAdminToken())
                    .contentType("application/json")
                    .body("{}")
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
    public void updateCity_shouldReturn200_whenUserHasPermission() {
        var createdCity = cityTestHelper.createCity();

        var updatedCityRequestData = new CityRequestData("UpdatedCity", "UpdatedState", "UpdatedCountry", "UC");

        try {
            given()
                    .header("Authorization", "Bearer " + getAdminToken())
                    .contentType("application/json")
                    .body(updatedCityRequestData)
                    .when()
                    .put(CITY_PATH + "/" + createdCity.getId())
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(createdCity.getId().intValue()))
                    .body("name", equalTo(updatedCityRequestData.getName()))
                    .body("state", equalTo(updatedCityRequestData.getState()))
                    .body("country", equalTo(updatedCityRequestData.getCountry()))
                    .body("state_abbreviation", equalTo(updatedCityRequestData.getStateAbbreviation()));
        } finally {
            cityTestHelper.deleteLastCityCreated();
        }
    }

    @Test
    public void updateCity_shouldReturn403_whenUserHasNoPermission() {
        var createdCity = cityTestHelper.createCity();

        var updatedCityRequestData = new CityRequestData("UpdatedCity", "UpdatedState", "UpdatedCountry", "UC");

        try {
            given()
                    .header("Authorization", "Bearer " + getDefaultToken())
                    .contentType("application/json")
                    .body(updatedCityRequestData)
                    .when()
                    .put(CITY_PATH + "/" + createdCity.getId())
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.FORBIDDEN.value());
        } finally {
            cityTestHelper.deleteLastCityCreated();
        }
    }

    @Test
    public void updateCity_shouldReturn403_whenAuthorizationHeaderIsNotPresent() {
        var createdCity = cityTestHelper.createCity();

        var updatedCityRequestData = new CityRequestData("UpdatedCity", "UpdatedState", "UpdatedCountry", "UC");

        try {
            given()
                    .contentType("application/json")
                    .body(updatedCityRequestData)
                    .when()
                    .put(CITY_PATH + "/" + createdCity.getId())
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.FORBIDDEN.value());
        } finally {
            cityTestHelper.deleteLastCityCreated();
        }
    }

    @Test
    public void deleteCity_shouldReturn204_whenUserHasPermission() {
        var createdCity = cityTestHelper.createCity();

        try {
            given()
                    .header("Authorization", "Bearer " + getAdminToken())
                    .when()
                    .delete(CITY_PATH + "/" + createdCity.getId())
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        } catch (Exception e) {
            log.error(e.getMessage());
            cityTestHelper.deleteLastCityCreated();
            fail();
        }
    }

    @Test
    public void deleteCity_shouldReturn403_whenUserHasNoPermission() {
        var createdCity = cityTestHelper.createCity();

        try {
            given()
                    .header("Authorization", "Bearer " + getDefaultToken())
                    .when()
                    .delete(CITY_PATH + "/" + createdCity.getId())
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.FORBIDDEN.value());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        } finally {
            cityTestHelper.deleteLastCityCreated();
        }
    }

    @Test
    public void getCity_shouldReturn200_whenCityExists() {
        var createdCity = cityTestHelper.createCity();

        try {
            given()
                    .when()
                    .get(CITY_PATH + "/" + createdCity.getId())
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(createdCity.getId().intValue()))
                    .body("name", equalTo(createdCity.getName()))
                    .body("state", equalTo(createdCity.getState()))
                    .body("country", equalTo(createdCity.getCountry()))
                    .body("state_abbreviation", equalTo(createdCity.getStateAbbreviation()));
        } finally {
            cityTestHelper.deleteLastCityCreated();
        }
    }

    @Test
    public void getCity_shouldReturn404_whenCityDoesNotExist() {
        try {
            given()
                    .when()
                    .get(CITY_PATH + "/" + INVALID_ID)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Test
    public void getCities_shouldReturn200_whenCitiesExist() {
        cityTestHelper.createCity();

        try {
            given()
                    .when()
                    .get(CITY_PATH)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("content", notNullValue())
                    .body("content", hasSize(1))
                    .body("total_elements", is(1));
        } finally {
            cityTestHelper.deleteLastCityCreated();
        }
    }

    @Test
    public void getCities_shouldReturn200_whenNoCitiesExist() {
        try {
            given()
                    .when()
                    .get(CITY_PATH)
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("content", notNullValue())
                    .body("content", hasSize(0))
                    .body("total_elements", is(0));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

}
