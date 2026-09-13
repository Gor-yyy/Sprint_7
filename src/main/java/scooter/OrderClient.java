package scooter;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String BASE_URL =
            "https://qa-scooter.praktikum-services.ru";

    public Response create(Order order) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    public Response cancel(int track) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body("{\"track\":" + track + "}")
                .when()
                .put("/api/v1/orders/cancel");
    }

    public Response getOrders() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/v1/orders");
    }
}