package brt;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BrtApiTest extends ApiTestBase {

    @Test
    void registerSubscriber_ShouldReturnCreated() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"phone\": \"1234567890\", \"tariff\": \"standard\" }")
        .when()
            .post("/api/brt/register")
        .then()
            .statusCode(201);
    }

    @Test
    void changeTariff_ShouldReturnSuccess() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"phone\": \"1234567890\", \"newTariff\": \"premium\" }")
        .when()
            .put("/api/brt/tariff")
        .then()
            .statusCode(200)
            .body("message", equalTo("Tariff updated"));
    }

    @Test
    void getSubscriberInfo_ShouldReturnCorrectData() {
        given()
            .queryParam("phone", "1234567890")
        .when()
            .get("/api/brt/info")
        .then()
            .statusCode(200)
            .body("phone", equalTo("1234567890"))
            .body("tariff", notNullValue());
    }

    @Test
    void changeTariff_InvalidPhone_ShouldReturnNotFound() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"phone\": \"0000000000\", \"newTariff\": \"premium\" }")
        .when()
            .put("/api/brt/tariff")
        .then()
            .statusCode(404);
    }

    @Test
    void registerSubscriber_Duplicate_ShouldReturnConflict() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"phone\": \"1234567890\", \"tariff\": \"standard\" }")
        .when()
            .post("/api/brt/register")
        .then()
            .statusCode(409);
    }
}
