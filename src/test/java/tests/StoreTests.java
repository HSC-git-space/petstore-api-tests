package tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import java.io.File;
import base.BaseTest;
import constants.Endpoints;
import io.restassured.RestAssured;
import models.Order;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.SpecBuilder;

import static io.restassured.RestAssured.given;

public class StoreTests extends BaseTest {

    private long createdOrderId;

    @BeforeClass
    public void setup() {
        RestAssured.requestSpecification = SpecBuilder.getRequestSpec();
    }

    @Test(description = "Get store inventory")
    public void getInventory() {
        given()
                .when()
                .get(Endpoints.STORE_INVENTORY)
                .then()
                .statusCode(200);
    }

    @Test(description = "Place a new order")
    public void placeOrder() {
        Order order = new Order();
        order.setId(0);
        order.setPetId(1);
        order.setQuantity(1);
        order.setStatus("placed");
        order.setComplete(true);

        Order created = given()
                .body(order)
                .when()
                .post(Endpoints.ORDER_PLACE)
                .then()
                .statusCode(200)
                .extract()
                .as(Order.class);

        createdOrderId = created.getId();
        Assert.assertTrue(createdOrderId > 0);
    }

    @Test(description = "Get order by ID",
            dependsOnMethods = "placeOrder")
    public void getOrderById() {
        Order order = given()
                .pathParam("orderId", createdOrderId)
                .when()
                .get(Endpoints.ORDER_BY_ID)
                .then()
                .statusCode(200)
                .extract()
                .as(Order.class);

        Assert.assertEquals(order.getId(), createdOrderId);
    }
    @Test(description = "Get order with invalid ID - negative test")
    public void getOrderByInvalidId() {
        given()
                .pathParam("orderId", 999999999)
                .when()
                .get(Endpoints.ORDER_BY_ID)
                .then()
                .statusCode(404);
    }
    @Test(description = "Validate order response schema",
            dependsOnMethods = "placeOrder")
    public void validateOrderSchema() {
        given()
                .pathParam("orderId", createdOrderId)
                .when()
                .get(Endpoints.ORDER_BY_ID)
                .then()
                .statusCode(200)
                .body(matchesJsonSchema(new File("src/test/resources/order-schema.json")));
    }
}