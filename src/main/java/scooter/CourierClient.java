package scooter;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String BASE_URL =
            "https://qa-scooter.praktikum-services.ru";

    public Response create(Courier courier) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    public Response login(CourierCredentials credentials) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post("/api/v1/courier/login");
    }

    public Response delete(int courierId) {
        return given()
                .baseUri(BASE_URL)
                .when()
                .delete("/api/v1/courier/" + courierId);
    }
}