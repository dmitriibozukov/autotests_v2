# Используемые технологии

- **RestAssured** — библиотека для тестирования REST API
- **JUnit 5** — тестовый фреймворк
- **Spring Boot** — контроллер с in-memory логикой


# 1 - CDR API – Интеграционные автотесты

## Назначение
Набор интеграционных автотестов для проверки REST API сервиса CDR (Call Detail Records). Тестируются основные функции генерации, получения и удаления CDR-файлов.

## Описание тестов

| №  | Название теста                             | Endpoint              | Описание |
|----|---------------------------------------------|-----------------------|----------|
| 1  | `generateCdrFile_ShouldReturnAccepted`      | `POST /api/cdr/generate` | Проверка успешного запуска генерации CDR-файла. Ожидается статус `202 Accepted`. |
| 2  | `getLastCdrFile_ShouldReturnValidStructure` | `GET /api/cdr/last`   | Проверка структуры ответа и статуса при запросе последнего CDR-файла. |
| 3  | `getCdrFileByDate_ShouldReturnValidData`    | `GET /api/cdr/by-date?date=YYYY-MM-DD` | Проверка фильтрации CDR-файлов по дате. |
| 4  | `getCdrFileByInvalidDate_ShouldReturnBadRequest` | `GET /api/cdr/by-date?date=invalid` | Проверка обработки некорректного параметра даты. Ожидается `400 Bad Request`. |
| 5  | `deleteCdrFiles_ShouldReturnSuccess`        | `DELETE /api/cdr`     | Проверка удаления всех CDR-файлов. Ожидается успешный ответ со статусом `200 OK`. |

# 2 - BRT API – Интеграционные автотесты

## Назначение
Набор автотестов предназначен для проверки корректной работы BRT (Billing and Routing Table) API, включая регистрацию абонентов, смену тарифа и получение информации.

## Описание тестов

| №  | Название теста                              | Endpoint              | Описание |
|----|----------------------------------------------|-----------------------|----------|
| 1  | `registerSubscriber_ShouldReturnCreated`     | `POST /api/brt/register` | Регистрирует нового абонента, ожидается статус `201 Created`. |
| 2  | `changeTariff_ShouldReturnSuccess`           | `PUT /api/brt/tariff` | Меняет тариф абонента. Ожидается статус `200 OK`. |
| 3  | `getSubscriberInfo_ShouldReturnCorrectData`  | `GET /api/brt/info?phone=...` | Возвращает информацию о тарифе по номеру телефона. |
| 4  | `changeTariff_InvalidPhone_ShouldReturnNotFound` | `PUT /api/brt/tariff` | Пытается сменить тариф несуществующему абоненту. Ожидается `404`. |
| 5  | `registerSubscriber_Duplicate_ShouldReturnConflict` | `POST /api/brt/register` | Пытается повторно зарегистрировать абонента. Ожидается `409 Conflict`. |

# 3 - HRS API – Интеграционные автотесты

## Назначение
Набор автотестов предназначен для проверки корректной работы сервиса HRS (Home Routing Service), ответственного за маршрутизацию вызовов между абонентами.

## Описание тестов

| №  | Название теста                              | Endpoint              | Описание |
|----|----------------------------------------------|-----------------------|----------|
| 1  | `routeCall_ShouldReturnSuccess`              | `POST /api/hrs/route` | Корректная маршрутизация звонка между двумя абонентами. |
| 2  | `routeCall_InvalidReceiver_ShouldReturnError`| `POST /api/hrs/route` | Ошибка при маршрутизации на несуществующего абонента. |
| 3  | `getRoutingTable_ShouldReturnList`           | `GET /api/hrs/routes` | Получение текущей таблицы маршрутов. |
| 4  | `clearRoutingTable_ShouldSucceed`            | `DELETE /api/hrs/routes` | Очистка всех маршрутов. |
| 5  | `routeCall_WithoutReceiver_ShouldReturnBadRequest` | `POST /api/hrs/route` | Ошибка при отсутствии параметра `receiver`. |

# 4 - CRM API – Интеграционные автотесты

## Назначение
Набор автотестов предназначен для проверки корректной работы сервиса CRM (Customer Relationship Management), включая создание, получение, обновление и удаление клиентов.

## Описание тестов

| №  | Название теста                              | Endpoint                     | Описание |
|----|----------------------------------------------|------------------------------|----------|
| 1  | `createCustomer_ShouldReturnCreated`         | `POST /api/crm/customers`     | Создание нового клиента с обязательными полями `name` и `email`. |
| 2  | `getCustomer_ShouldReturnValidCustomer`     | `GET /api/crm/customers/{id}` | Получение информации о клиенте по ID. |
| 3  | `getNonExistingCustomer_ShouldReturnNotFound` | `GET /api/crm/customers/{id}` | Попытка получить несуществующего клиента. |
| 4  | `updateCustomer_ShouldReturnUpdatedCustomer` | `PUT /api/crm/customers/{id}` | Обновление данных клиента. |
| 5  | `deleteCustomer_ShouldReturnNoContent`      | `DELETE /api/crm/customers/{id}` | Удаление клиента по ID. |










