# Автоматические интеграционные тесты для сервиса BRT

Этот набор автотестов покрывает основные функции API микросервиса **BRT (Billing Real Time)**. Тесты написаны с использованием библиотеки **RestAssured** и проверяют корректность работы с записями звонков и данными абонентов.

## Структура тестов

Файл: `BrtApiTest.java`

### Проверка обработки CDR-записей (вызовы)

- ** `processCall_ShouldSaveCallData()`**
  - Проверяет, что при передаче валидной записи звонка:
    - API возвращает статус 201 Created
    - Ответ содержит ID
    - Данные корректно сохраняются (проверка по GET)

- ** `processCall_WithInvalidData_ShouldReturnBadRequest()`**
  - Отправка заведомо невалидных данных
  - Проверяется, что возвращается 400 Bad Request и сообщение об ошибке

### Проверка работы с абонентами

- ** `getSubscriberInfo_ShouldReturnValidData()`**
  - Проверяет:
    - что по существующему MSISDN возвращается 200 OK
    - в ответе есть все ключевые поля (`balance`, `tariffId`, `name`)

- ** `getSubscriberInfo_ForNonExistentSubscriber_ShouldReturnNotFound()`**
  - Запрос к несуществующему абоненту должен возвращать 404

- ** `updateBalance_ShouldChangeSubscriberBalance()`**
  - Проверяет:
    - успешное обновление баланса абонента
    - корректный расчет нового баланса

## Зависимости и окружение

- Java 17+
- RestAssured
- JUnit 5
- Запущенный сервис BRT с доступным API:
  - `/api/calls`
  - `/api/subscribers`

## Запуск тестов

Тесты можно запустить командой:

```bash
./gradlew test
