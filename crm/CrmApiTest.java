package crm;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CrmApiTest extends ApiTestBase {

    @Test
    void createCustomer_ShouldReturnCreated() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"name\": \"John Doe\", \"email\": \"john@example.com\" }")
        .when()
            .post("/api/crm/customers")
        .then()
            .statusCode(201)
            .body("name", equalTo("John Doe"))
            .body("email", equalTo("john@example.com"));
    }

    @Test
    void getCustomer_ShouldReturnValidCustomer() {
        given()
            .pathParam("id", "1")
        .when()
            .get("/api/crm/customers/{id}")
        .then()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("name", notNullValue())
            .body("email", notNullValue());
    }

    @Test
    void getNonExistingCustomer_ShouldReturnNotFound() {
        given()
            .pathParam("id", "999")
        .when()
            .get("/api/crm/customers/{id}")
        .then()
            .statusCode(404)
            .body("error", equalTo("Customer not found"));
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer() {
        given()
            .pathParam("id", "1")
            .contentType(ContentType.JSON)
            .body("{ \"name\": \"John Doe Updated\", \"email\": \"john_updated@example.com\" }")
        .when()
            .put("/api/crm/customers/{id}")
        .then()
            .statusCode(200)
            .body("name", equalTo("John Doe Updated"))
            .body("email", equalTo("john_updated@example.com"));
    }

    @Test
    void deleteCustomer_ShouldReturnNoContent() {
        given()
            .pathParam("id", "1")
        .when()
            .delete("/api/crm/customers/{id}")
        .then()
            .statusCode(204);
    }
}
