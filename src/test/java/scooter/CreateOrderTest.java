package scooter;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final List<String> color;
    private Integer track;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Color: {0}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {Collections.singletonList("BLACK")},
                {Collections.singletonList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    @After
    public void cleanUp() {
        if (track != null) {
            new OrderClient().cancel(track);
        }
    }

    @Test
    public void orderCanBeCreatedWithDifferentColors() {

        Order order = new Order(
                "Gor",
                "Test",
                "Moscow Street 10",
                4,
                "+79999999999",
                3,
                "2026-09-20",
                "Test order",
                color
        );

        Response response =
                new OrderClient().create(order);

        assertEquals(201, response.statusCode());

        track = response.jsonPath().getInt("track");

        assertTrue(track > 0);
    }
}