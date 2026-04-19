# Petstore API Tests

Automated API test framework built with Java, RestAssured, and TestNG
against the [Swagger Petstore API](https://petstore.swagger.io).

## Tech Stack

| Tool | Purpose |
|---|---|
| Java 11 | Programming language |
| RestAssured 5.4.0 | API testing library |
| TestNG 7.9.0 | Test runner |
| Extent Reports 5.1.1 | HTML test reporting |
| Maven | Build and dependency management |
| GitHub Actions | CI/CD pipeline |

## Project Structure

    src/test/java/
    ├── base/          # BaseTest with Extent Reports setup
    ├── config/        # ConfigReader for properties
    ├── constants/     # API endpoint constants
    ├── models/        # POJOs - Pet, Order, User
    ├── tests/         # Test classes
    └── utilities/     # SpecBuilder, ExtentManager

## Test Coverage

| Class | Tests |
|---|---|
| PetTests | Add pet, get by ID, get by status, delete pet |
| StoreTests | Get inventory, place order, get order by ID |
| UserTests | Create user, get by username, user login |

## How to Run

    mvn test

## Reports

After running, open `reports/ExtentReport.html` in a browser to view
the full test report with pass/fail status and timestamps.

## CI

Tests run automatically on every push via GitHub Actions.