package scooter;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();

        String uniqueLogin = "gor_" + System.currentTimeMillis();

        courier = new Courier(
                uniqueLogin,
                "123456",
                "Gor"
        );
    }

    @After
    public void cleanUp() {

        Response loginResponse =
                courierClient.login(CourierCredentials.from(courier));

        if (loginResponse.statusCode() == 200) {
            int courierId = loginResponse.jsonPath().getInt("id");
            courierClient.delete(courierId);
        }
    }

    @Test
    public void courierCanBeCreated() {

        Response response = courierClient.create(courier);

        assertEquals(201, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("ok"));
    }

    @Test
    public void cannotCreateTwoIdenticalCouriers() {

        courierClient.create(courier);

        Response secondResponse =
                courierClient.create(courier);

        assertEquals(409, secondResponse.statusCode());
        assertNotNull(
                secondResponse.jsonPath().getString("message")
        );
    }

    @Test
    public void cannotCreateCourierWithoutLogin() {

        Courier courierWithoutLogin =
                new Courier(null, "123456", "Gor");

        Response response =
                courierClient.create(courierWithoutLogin);

        assertEquals(400, response.statusCode());
        assertNotNull(
                response.jsonPath().getString("message")
        );
    }

    @Test
    public void cannotCreateCourierWithoutPassword() {

        Courier courierWithoutPassword =
                new Courier(courier.getLogin(), null, "Gor");

        Response response =
                courierClient.create(courierWithoutPassword);

        assertEquals(400, response.statusCode());
        assertNotNull(
                response.jsonPath().getString("message")
        );
    }
}