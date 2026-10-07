# cards-api

Spring Boot REST API for managing bank cards, built for the Cards IT Java Backend assignment.

## Features

- CRUD for cards with **soft delete** (`deleted_date`)
- **Pagination** on the list endpoint (10 records per page)
- Request and response **logging to file** (card numbers are masked)
- **MSSQL** database (`TESTDB`) with `@Transactional` on all service methods
- Nested **3rd-party API call** (exchange rate): Client -> this API -> external API

## Tech stack

- Java 21
- Spring Boot, Spring Data JPA, Bean Validation
- Microsoft SQL Server
- Maven, Lombok

## Prerequisites

- JDK 21
- Maven
- Microsoft SQL Server running locally, with **TCP/IP enabled on port 1433** and **SQL Server Authentication** enabled
- Postman

## Setup
---
### 1. Create the database

Open SSMS and run:

1. `database/01_schema.sql` creates `TESTDB` and the `cards` table.
---
### 2. Configure the database connection

Create `src/main/resources/application-local.yaml` (this file is git-ignored, so credentials are not committed):

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=TESTDB;encrypt=true;trustServerCertificate=true
    username: <your SQL auth username>
    password: <your SQL auth password>
```
---

### 3. Run the application

Simply navigate to `src/main/java/com/yourpackage/CardsApiApplication.java` and click **Run**.

*(Spring Boot will automatically activate the `local` profile and load your `application-local.yaml` file).*

---

### 4. Test with Postman

1. Open Postman and click **Import**.
2. Import both files from the `postman/` folder:
    - `postman/CardsAPI.postman_collection.json`
3. Run the requests in this order:
    1. Create card (saves `cardId` automatically)
    2. Get card by id
    3. Get all cards (`?page=0`)
    4. Update card
    5. Delete card (soft delete)
    6. Get card by id again, which returns `404`
    7. Exchange rate (3rd-party call)
---
## API endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/cards` | Create a card |
| GET | `/api/cards/{id}` | Get a card by id |
| GET | `/api/cards?page=0` | List cards, 10 per page |
| PUT | `/api/cards/{id}` | Update a card |
| DELETE | `/api/cards/{id}` | Soft delete a card |
| GET | `/api/exchange-rate?from=USD&to=MYR` | Exchange rate from the 3rd-party API |

Card ids are UUIDs.
---
### Sample request body (create)

```json
{
  "cardNumber": "1234567812345678",
  "cardHolder": "Ahmad Ali",
  "cardType": "CREDIT",
  "creditLimit": 5000.00,
  "cardStatus": "ACTIVE"
}
```

Allowed values:

- `cardType`: `CREDIT`, `DEBIT`, `PREPAID`
- `cardStatus`: `ACTIVE`, `INACTIVE`, `BLOCKED`, `CLOSED`

---
## Logging

Every request and response under `/api` is logged with method, URL, status, duration and body. Card numbers are masked in the logs.

- Log file: `logs/cards-api.log` in the project root
- The file is created when the application starts and receives its first request
- Each request has a `correlationId` so all log lines of one call can be traced together
---
## Project structure

```
cards-api/
├── database/        SQL scripts (schema, seed data)
├── postman/         Postman collection and environment
├── logs/            Generated log files (git-ignored)
├── src/main/java/CardsAPI/
│   ├── Controllers/
│   ├── Dtos/
│   ├── Entities/
│   ├── Enums/
│   ├── Filter/      Request/response logging filter
│   ├── Repositories/
│   └── Services/
└── src/main/resources/
    ├── application.yaml
    ├── application-local.yaml   (create yourself, git-ignored)
    └── logback-spring.xml
```
---
## Troubleshooting

- **Cannot connect to the database:** check that SQL Server TCP/IP is enabled on port 1433 and that the SQL auth username and password are correct.
- **`Validation failed` (400):** check the JSON key spelling and the enum values.
- **No `logs/` folder:** start the app and send at least one request, then refresh the project view.
---