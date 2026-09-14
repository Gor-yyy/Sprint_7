package scooter;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderListTest {

    @Test
    @Description("Проверяем, что запрос списка заказов возвращает массив orders")
    public void ordersListIsReturnedTest() {

        Response response =
                new OrderClient().getOrders();

        assertEquals(SC_OK, response.statusCode());

        List<?> orders =
                response.jsonPath().getList("orders");

        assertNotNull(orders);
    }
}