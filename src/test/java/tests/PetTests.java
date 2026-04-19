package tests;

import base.BaseTest;
import constants.Endpoints;
import io.restassured.RestAssured;
import models.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.SpecBuilder;

import static io.restassured.RestAssured.given;

public class PetTests extends BaseTest {

    private long createdPetId;

    @BeforeClass
    public void setup() {
        RestAssured.requestSpecification = SpecBuilder.getRequestSpec();
    }

    @Test(description = "Add a new pet")
    public void addNewPet() {
        Pet pet = new Pet();
        pet.setId(0);
        pet.setName("TestDog");
        pet.setStatus("available");

        Pet created = given()
                .body(pet)
                .when()
                .post(Endpoints.PET_ADD)
                .then()
                .statusCode(200)
                .extract()
                .as(Pet.class);

        createdPetId = created.getId();
        Assert.assertTrue(createdPetId > 0);
    }

    @Test(description = "Get pet by valid ID",
            dependsOnMethods = "addNewPet")
    public void getPetById() {
        Pet pet = given()
                .pathParam("petId", createdPetId)
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

    @Test(description = "Delete a pet",
            dependsOnMethods = "addNewPet")
    public void deletePet() {
        given()
                .pathParam("petId", createdPetId)
                .when()
                .delete(Endpoints.PET_DELETE)
                .then()
                .statusCode(200);
    }
}