package tests;

import constants.Endpoints;
import io.restassured.RestAssured;
import models.Order;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SpecBuilder;

import static io.restassured.RestAssured.given;

public class StoreTests {

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
        order.setId(1);
        order.setPetId(1);
        order.setQuantity(1);
        order.setStatus("placed");
        order.setComplete(true);

        given()
                .body(order)
                .when()
                .post(Endpoints.ORDER_PLACE)
                .then()
                .statusCode(200);
    }

    @Test(description = "Get order by ID")
    public void getOrderById() {
        Order order = given()
                .pathParam("orderId", 1)
                .when()
                .get(Endpoints.ORDER_BY_ID)
                .then()
                .statusCode(200)
                .extract()
                .as(Order.class);

        Assert.assertNotNull(order.getId());
    }
}