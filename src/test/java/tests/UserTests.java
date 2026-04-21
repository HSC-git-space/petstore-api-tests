package tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import java.io.File;
import base.BaseTest;
import constants.Endpoints;
import io.restassured.RestAssured;
import models.User;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.SpecBuilder;

import static io.restassured.RestAssured.given;

public class UserTests extends BaseTest {

    @BeforeClass
    public void setup() {
        RestAssured.requestSpecification = SpecBuilder.getRequestSpec();
    }

    @Test(description = "Create a new user")
    public void createUser() {
        User user = new User();
        user.setId(99999);
        user.setUsername("testuser");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@test.com");
        user.setPassword("password123");
        user.setPhone("1234567890");

        given()
                .body(user)
                .when()
                .post(Endpoints.USER_CREATE)
                .then()
                .statusCode(200);
    }

    @Test(description = "Get user by username")
    public void getUserByUsername() {
        User user = given()
                .pathParam("username", "testuser")
                .when()
                .get(Endpoints.USER_BY_NAME)
                .then()
                .statusCode(200)
                .extract()
                .as(User.class);

        Assert.assertNotNull(user.getUsername());
    }

    @Test(description = "User login")
    public void userLogin() {
        given()
                .queryParam("username", "testuser")
                .queryParam("password", "password123")
                .when()
                .get(Endpoints.USER_LOGIN)
                .then()
                .statusCode(200);
    }
    @Test(description = "Get user that does not exist - negative test")
    public void getUserThatDoesNotExist() {
        given()
                .pathParam("username", "thisuserdoesnotexist12345")
                .when()
                .get(Endpoints.USER_BY_NAME)
                .then()
                .statusCode(404);
    }
    @Test(description = "Validate user response schema",
            dependsOnMethods = "createUser")
    public void validateUserSchema() {
        given()
                .pathParam("username", "testuser")
                .when()
                .get(Endpoints.USER_BY_NAME)
                .then()
                .statusCode(200)
                .body(matchesJsonSchema(new File("src/test/resources/user-schema.json")));
    }
}