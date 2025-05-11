package cdr;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CdrApiTest extends ApiTestBase {

    @Test
    void generateCdrFile_ShouldReturnAccepted() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .post("/api/cdr/generate")
        .then()
            .statusCode(202);
    }

    @Test
    void getLastCdrFile_ShouldReturnValidStructure() {
        when()
            .get("/api/cdr/last")
        .then()
            .statusCode(200)
            .body("records", notNullValue());
    }

    @Test
    void getCdrFileByDate_ShouldReturnValidData() {
        given()
            .queryParam("date", "2025-05-10")
        .when()
            .get("/api/cdr/by-date")
        .then()
            .statusCode(200)
            .body("records", notNullValue())
            .body("records.size()", greaterThanOrEqualTo(0));
    }

    @Test
    void getCdrFileByInvalidDate_ShouldReturnBadRequest() {
        given()
            .queryParam("date", "invalid-date")
        .when()
            .get("/api/cdr/by-date")
        .then()
            .statusCode(400);
    }

    @Test
    void deleteCdrFiles_ShouldReturnSuccess() {
        when()
            .delete("/api/cdr")
        .then()
            .statusCode(200)
            .body("message", equalTo("CDR files deleted"));
    }
}
