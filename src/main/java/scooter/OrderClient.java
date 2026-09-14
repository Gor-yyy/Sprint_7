package scooter;

import io.restassured.response.Response;
import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;
import static scooter.Endpoints.ORDERS;
import static scooter.Endpoints.ORDER_CANCEL;

public class OrderClient extends BaseClient {
    @Step("Создание заказа")
    public Response create(Order order) {
        return given()
                .spec(REQUEST_SPEC)
                .body(order)
                .when()
                .post(ORDERS);
    }
    @Step("Отмена заказа")
    public Response cancel(int track) {
        return given()
                .spec(REQUEST_SPEC)
                .body("{\"track\":" + track + "}")
                .when()
                .put(ORDER_CANCEL);
    }
    @Step("Получение списка заказов")
    public Response getOrders() {
        return given()
                .spec(REQUEST_SPEC)
                .when()
                .get(ORDERS);
    }
}