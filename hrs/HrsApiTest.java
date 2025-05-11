package hrs;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class HrsApiTest extends ApiTestBase {

    @Test
    void routeCall_ShouldReturnSuccess() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"caller\": \"1001\", \"receiver\": \"1002\" }")
        .when()
            .post("/api/hrs/route")
        .then()
            .statusCode(200)
            .body("status", equalTo("routed"));
    }

    @Test
    void routeCall_InvalidReceiver_ShouldReturnError() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"caller\": \"1001\", \"receiver\": \"9999\" }")
        .when()
            .post("/api/hrs/route")
        .then()
            .statusCode(404)
            .body("error", equalTo("Receiver not found"));
    }

    @Test
    void getRoutingTable_ShouldReturnList() {
        when()
            .get("/api/hrs/routes")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    void clearRoutingTable_ShouldSucceed() {
        when()
            .delete("/api/hrs/routes")
        .then()
            .statusCode(204);
    }

    @Test
    void routeCall_WithoutReceiver_ShouldReturnBadRequest() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"caller\": \"1001\" }")
        .when()
            .post("/api/hrs/route")
        .then()
            .statusCode(400);
    }
}
