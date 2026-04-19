package tests;

import constants.Endpoints;
import io.restassured.RestAssured;
import models.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SpecBuilder;

import static io.restassured.RestAssured.given;

public class PetTests {

    @BeforeClass
    public void setup() {
        RestAssured.requestSpecification = SpecBuilder.getRequestSpec();
    }

    @Test(description = "Get pet by valid ID")
    public void getPetById() {
        Pet pet = given()
                .pathParam("petId", 1)
                .when()
                .get(Endpoints.PET_BY_ID)
                .then()
                .statusCode(200)
                .extract()
                .as(Pet.class);

        Assert.assertNotNull(pet.getId());
    }

    @Test(description = "Get pets by status - available")
    public void getPetsByStatus() {
        given()
                .queryParam("status", "available")
                .when()
                .get(Endpoints.PET_BY_STATUS)
                .then()
                .statusCode(200);
    }

    @Test(description = "Add a new pet")
    public void addNewPet() {
        Pet pet = new Pet();
        pet.setId(99999);
        pet.setName("TestDog");
        pet.setStatus("available");

        given()
                .body(pet)
                .when()
                .post(Endpoints.PET_ADD)
                .then()
                .statusCode(200);
    }

    @Test(description = "Delete a pet")
    public void deletePet() {
        given()
                .pathParam("petId", 99999)
                .when()
                .delete(Endpoints.PET_DELETE)
                .then()
                .statusCode(200);
    }
}