package scooter;

import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class OrderListTest {

    @Test
    public void ordersListIsReturned() {

        Response response =
                new OrderClient().getOrders();

        assertEquals(200, response.statusCode());

        List<?> orders =
                response.jsonPath().getList("orders");

        assertNotNull(orders);
    }
}