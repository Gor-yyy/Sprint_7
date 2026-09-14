package scooter;

import io.restassured.response.Response;
import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;
import static scooter.Endpoints.COURIER;
import static scooter.Endpoints.COURIER_LOGIN;

public class CourierClient extends BaseClient {
    @Step("Создание курьера")
    public Response create(Courier courier) {
        return given()
                .spec(REQUEST_SPEC)
                .body(courier)
                .when()
                .post(COURIER);
    }
    @Step("Авторизация курьера")
    public Response login(CourierCredentials credentials) {
        return given()
                .spec(REQUEST_SPEC)
                .body(credentials)
                .when()
                .post(COURIER_LOGIN);
    }
    @Step("Удаление курьера")
    public Response delete(int courierId) {
        return given()
                .spec(REQUEST_SPEC)
                .when()
                .delete(COURIER + "/" + courierId);
    }
}