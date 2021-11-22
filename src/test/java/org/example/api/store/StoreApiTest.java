package org.example.api.store;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.example.model.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class StoreApiTest {
    @BeforeClass
    public void prepare() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/")
                .addHeader("api_key", "Nemirov")

                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.filters(new ResponseLoggingFilter());
    }
    @Test
    public void placeOrderTest() {
        Pet pet = new Pet();
        int id = 1;
        int petId = 1;
        pet.setId(id);
        pet.setPetId(petId);
        given()
                .body(pet)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);

        Pet actual =
                given()
                        .pathParam("orderId", petId)
                        .when()
                        .get("/store/order/{orderId}")
                        .then()
                        .statusCode(200)
                        .extract().body()
                        .as(Pet.class);

        Assert.assertEquals(actual.getId(), pet.getId());

    }

    @Test
    public void deleteOrderTest() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        given()
                    .pathParam("orderId", 1)
                .when()
                    .delete("/store/order/{orderId}")
                .then()
                    .statusCode(200);

        given()
                    .pathParam("orderId", 1)
                .when()
                    .get("/store/order/{orderId}")
                .then()
                    .statusCode(404);
    }
}
