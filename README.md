# Petstore API Tests

A modular, production-style REST API test automation framework built
with Java and RestAssured, validating the
[Swagger Petstore API](https://petstore.swagger.io).

Designed to demonstrate real-world SDET practices: separation of
concerns, environment-driven configuration, serialization/deserialization,
schema contract validation, and CI/CD integration.

---

## Why This Framework Exists

Most API test scripts are written as one-off files with hardcoded URLs,
repeated setup code, and no structure. This framework solves that by:

- **Centralising configuration** — base URL lives in one place, not scattered across tests
- **Reusable request specifications** — SpecBuilder sets headers and base URI once, every test inherits it
- **Typed response models** — Jackson deserializes JSON responses into Java POJOs, enabling strongly typed assertions
- **Schema contract validation** — validates API response structure, not just status codes — catches breaking changes early
- **Modular design** — config, constants, models, utils, and tests are fully separated. Change one without touching others

---

## Tech Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 11 | Core language |
| RestAssured | 5.4.0 | API testing DSL |
| TestNG | 7.9.0 | Test runner + assertions |
| Jackson | 2.17.0 | JSON serialization/deserialization |
| Extent Reports | 5.1.1 | HTML test reporting |
| Maven | 3.x | Build + dependency management |
| GitHub Actions | - | CI/CD pipeline |

---

## Framework Architecture

    src/test/java/
    ├── base/           # BaseTest — Extent Reports lifecycle management
    ├── config/         # ConfigReader — loads environment properties
    ├── constants/      # Endpoints — all API paths in one place
    ├── models/         # Pet, Order, User POJOs for deserialization
    ├── tests/          # PetTests, StoreTests, UserTests
    └── utilities/      # SpecBuilder, ExtentManager

    src/test/resources/
    ├── config.properties        # Base URL and environment config
    ├── pet-schema.json          # JSON schema for pet contract validation
    ├── order-schema.json        # JSON schema for order contract validation
    └── user-schema.json         # JSON schema for user contract validation

---

## How a Test Actually Flows

    1. ConfigReader loads base.url from config.properties
    2. SpecBuilder builds a reusable RequestSpecification
       (base URI + Content-Type: JSON)
    3. BaseTest initialises Extent Reports before the suite runs
    4. Each test method:
       a. Sends HTTP request using RestAssured given/when/then DSL
       b. Asserts status code
       c. Deserializes JSON response into POJO using Jackson
       d. Asserts business logic on the typed object
       e. Optionally validates response structure against JSON schema
    5. BaseTest logs pass/fail to Extent Report after each method
    6. Extent Report is flushed to HTML after the suite completes

---

## Test Coverage and Validation Strategy

Rather than basic CRUD testing, this framework implements a layered validation strategy:

| Layer | What it checks | Example |
|---|---|---|
| Status code | Did the server respond correctly? | 200 OK, 404 Not Found |
| Response body | Is the data correct? | Pet name matches what was sent |
| Schema validation | Is the structure correct? | Required fields present, correct types |
| Negative testing | Does the API handle bad input? | Invalid ID returns 404 |
| Chained requests | Does data flow correctly? | Created order ID used in GET request |

### Test Classes

| Class | Tests | Concepts covered |
|---|---|---|
| PetTests | Add pet, get by ID, get by status, delete, schema validation, negative test | POST to GET chaining, array schema |
| StoreTests | Get inventory, place order, get order by ID, schema validation, negative test | Dynamic ID capture, dependent tests |
| UserTests | Create user, get by username, login, schema validation, negative test | Object schema, auth flow |

---

## Example — Request and Response

**Request: Add a new pet**

    POST https://petstore.swagger.io/v2/pet
    Content-Type: application/json

    {
      "id": 0,
      "name": "TestDog",
      "status": "available"
    }

**Response: 200 OK**

    {
      "id": 9223372036854775807,
      "name": "TestDog",
      "status": "available"
    }

**What the test validates:**
- Status code is 200
- Returned ID is greater than 0
- ID is captured and reused in subsequent GET and DELETE calls

---

## How to Run

**Prerequisites:** Java 11+, Maven

    mvn test

Or right click testng.xml and select Run in IntelliJ.

---

## CI/CD Pipeline

**Trigger:** Every push and pull request to main branch

**Steps:**
1. Checkout code
2. Set up JDK 11 (Temurin distribution)
3. Run mvn test — executes full TestNG suite
4. Upload Extent Report HTML as build artifact

Every code change is automatically validated against the live API before merging.

---

## Test Report

After running, open reports/ExtentReport.html in a browser.

The report shows:
- Pass/fail status per test
- Execution timestamp and duration
- Full failure stack trace on failures

---

## Known Issues

| Test | Issue | Reason |
|---|---|---|
| getPetById | Intermittently fails with 404 | Petstore is a public shared server — data created by other users can overwrite or delete pets between test steps. Mitigation: test uses dynamically captured ID from addNewPet. |
```