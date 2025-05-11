package brt;

/**
 * Интеграционные тесты для API сервиса CDR (Call Detail Record).
 * Проверяет основные функции обработки записей о звонках.
 */
public class CdrApiTest extends ApiTestBase {

    private static final String CDR_ENDPOINT = "/api/cdr";
    private static final String TEST_CDR_ID = "12345";

    /**
     * Тест успешной обработки записи о звонке.
     * Проверяет:
     * 1. Корректный статус ответа (201 Created)
     * 2. Наличие ID в ответе
     * 3. Соответствие переданных данных сохраненным
     */
    @Test
    void processCdr_ShouldSaveCdrData() {
        CdrRecord testRecord = TestData.validCdrRecord();
        
        CdrRecord response = given()
            .contentType(ContentType.JSON)
            .body(testRecord)
        .when()
            .post(CDR_ENDPOINT)
        .then()
            .statusCode(201)
            .extract().as(CdrRecord.class);
        
        // Проверка, что данные сохранены корректно
        CdrRecord savedRecord = given()
            .pathParam("id", response.getId())
        .when()
            .get(CDR_ENDPOINT + "/{id}")
        .then()
            .statusCode(200)
            .extract().as(CdrRecord.class);
            
        assertThat(savedRecord)
            .usingRecursiveComparison()
            .ignoringFields("id", "createdAt")
            .isEqualTo(testRecord);
    }

    /**
     * Тест обработки невалидной записи о звонке.
     * Проверяет:
     * 1. Корректный статус ошибки (400 Bad Request)
     * 2. Наличие сообщения об ошибке
     */
    @Test
    void processCdr_WithInvalidData_ShouldReturnBadRequest() {
        CdrRecord invalidRecord = TestData.invalidCdrRecord();
        
        given()
            .contentType(ContentType.JSON)
            .body(invalidRecord)
        .when()
            .post(CDR_ENDPOINT)
        .then()
            .statusCode(400)
            .body("message", notNullValue());
    }

    /**
     * Тест получения информации о существующей записи CDR.
     * Проверяет:
     * 1. Корректный статус ответа (200 OK)
     * 2. Наличие обязательных полей в ответе
     * 3. Корректность формата данных
     */
    @Test
    void getCdrInfo_ShouldReturnValidData() {
        given()
            .pathParam("id", TEST_CDR_ID)
        .when()
            .get(CDR_ENDPOINT + "/{id}")
        .then()
            .statusCode(200)
            .body("id", equalTo(TEST_CDR_ID))
            .body("caller", is(notNullValue()))
            .body("callee", is(notNullValue()))
            .body("duration", is(notNullValue()))
            .body("timestamp", is(notNullValue()));
    }

    /**
     * Тест получения информации о несуществующей записи CDR.
     * Проверяет:
     * 1. Корректный статус ошибки (404 Not Found)
     * 2. Наличие сообщения об ошибке
     */
    @Test
    void getCdrInfo_ForNonExistentCdr_ShouldReturnNotFound() {
        String nonExistentCdrId = "99999";
        
        given()
            .pathParam("id", nonExistentCdrId)
        .when()
            .get(CDR_ENDPOINT + "/{id}")
        .then()
            .statusCode(404)
            .body("message", containsString("not found"));
    }

    /**
     * Тест обновления записи CDR.
     * Проверяет:
     * 1. Корректный статус ответа (200 OK)
     * 2. Изменение данных в записи
     * 3. Корректность обновленных данных
     */
    @Test
    void updateCdr_ShouldUpdateCdrData() {
        CdrRecord initialRecord = given()
            .pathParam("id", TEST_CDR_ID)
        .when()
            .get(CDR_ENDPOINT + "/{id}")
        .then()
            .statusCode(200)
            .extract().as(CdrRecord.class);

        // Изменяем данные записи
        initialRecord.setDuration(300); // обновляем длительность
        
        given()
            .pathParam("id", TEST_CDR_ID)
            .contentType(ContentType.JSON)
            .body(initialRecord)
        .when()
            .put(CDR_ENDPOINT + "/{id}")
        .then()
            .statusCode(200)
            .body("duration", equalTo(300));
    }

    /**
     * Тест удаления записи CDR.
     * Проверяет:
     * 1. Корректный статус ответа (204 No Content)
     * 2. Убедиться, что запись была удалена
     */
    @Test
    void deleteCdr_ShouldDeleteCdrData() {
        given()
            .pathParam("id", TEST_CDR_ID)
        .when()
            .delete(CDR_ENDPOINT + "/{id}")
        .then()
            .statusCode(204);

        // Проверка, что запись удалена
        given()
            .pathParam("id", TEST_CDR_ID)
        .when()
            .get(CDR_ENDPOINT + "/{id}")
        .then()
            .statusCode(404);
    }
}
