package brt;

/**
 * Интеграционные тесты для API сервиса BRT (Billing Real Time).
 * Проверяет основные функции обработки звонков и работы с абонентами.
 */
public class BrtApiTest extends ApiTestBase {

    private static final String CALLS_ENDPOINT = "/api/calls";
    private static final String SUBSCRIBERS_ENDPOINT = "/api/subscribers";
    private static final String TEST_MSISDN = "89991112233";

    /**
     * Тест успешной обработки записи о звонке.
     * Проверяет:
     * 1. Корректный статус ответа (201 Created)
     * 2. Наличие ID в ответе
     * 3. Соответствие переданных данных сохраненным
     */
    @Test
    void processCall_ShouldSaveCallData() {
        CallRecord testRecord = TestData.validCallRecord();
        
        CallRecord response = given()
            .contentType(ContentType.JSON)
            .body(testRecord)
        .when()
            .post(CALLS_ENDPOINT)
        .then()
            .statusCode(201)
            .extract().as(CallRecord.class);
        
        // Проверка, что данные сохранены корректно
        CallRecord savedRecord = given()
            .pathParam("id", response.getId())
        .when()
            .get(CALLS_ENDPOINT + "/{id}")
        .then()
            .statusCode(200)
            .extract().as(CallRecord.class);
            
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
    void processCall_WithInvalidData_ShouldReturnBadRequest() {
        CallRecord invalidRecord = TestData.invalidCallRecord();
        
        given()
            .contentType(ContentType.JSON)
            .body(invalidRecord)
        .when()
            .post(CALLS_ENDPOINT)
        .then()
            .statusCode(400)
            .body("message", notNullValue());
    }

    /**
     * Тест получения информации о существующем абоненте.
     * Проверяет:
     * 1. Корректный статус ответа (200 OK)
     * 2. Наличие обязательных полей в ответе
     * 3. Корректность формата данных
     */
    @Test
    void getSubscriberInfo_ShouldReturnValidData() {
        given()
            .pathParam("msisdn", TEST_MSISDN)
        .when()
            .get(SUBSCRIBERS_ENDPOINT + "/{msisdn}")
        .then()
            .statusCode(200)
            .body("msisdn", equalTo(TEST_MSISDN))
            .body("balance", is(notNullValue()))
            .body("tariffId", is(notNullValue()))
            .body("name", is(notNullValue()));
    }

    /**
     * Тест получения информации о несуществующем абоненте.
     * Проверяет:
     * 1. Корректный статус ошибки (404 Not Found)
     * 2. Наличие сообщения об ошибке
     */
    @Test
    void getSubscriberInfo_ForNonExistentSubscriber_ShouldReturnNotFound() {
        String nonExistentMsisdn = "00000000000";
        
        given()
            .pathParam("msisdn", nonExistentMsisdn)
        .when()
            .get(SUBSCRIBERS_ENDPOINT + "/{msisdn}")
        .then()
            .statusCode(404)
            .body("message", containsString("not found"));
    }

    /**
     * Тест обновления баланса абонента.
     * Проверяет:
     * 1. Корректный статус ответа (200 OK)
     * 2. Изменение баланса в БД
     * 3. Корректность расчета нового баланса
     */
    @Test
    void updateBalance_ShouldChangeSubscriberBalance() {
        BigDecimal initialBalance = getCurrentBalance(TEST_MSISDN);
        BigDecimal amountToAdd = new BigDecimal("50.00");
        
        given()
            .pathParam("msisdn", TEST_MSISDN)
            .contentType(ContentType.JSON)
            .body(new BalanceUpdateRequest(amountToAdd))
        .when()
            .patch(SUBSCRIBERS_ENDPOINT + "/{msisdn}/balance")
        .then()
            .statusCode(200)
            .body("balance", equalTo(initialBalance.add(amountToAdd).floatValue()));
    }

    private BigDecimal getCurrentBalance(String msisdn) {
        return new BigDecimal(
            given()
                .pathParam("msisdn", msisdn)
            .when()
                .get(SUBSCRIBERS_ENDPOINT + "/{msisdn}")
            .then()
                .extract().path("balance").toString()
        );
    }
}