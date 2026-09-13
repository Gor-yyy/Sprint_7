package scooter;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourierLoginTest {

    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();

        String uniqueLogin = "gor_" + System.currentTimeMillis();

        courier = new Courier(
                uniqueLogin,
                "123456",
                "Gor"
        );

        courierClient.create(courier);
    }

    @After
    public void cleanUp() {
        if (courierId == null) {
            Response response =
                    courierClient.login(CourierCredentials.from(courier));

            if (response.statusCode() == 200) {
                courierId = response.jsonPath().getInt("id");
            }
        }

        if (courierId != null) {
            courierClient.delete(courierId);
        }
    }

    @Test
    public void courierCanLogin() {
        Response response =
                courierClient.login(CourierCredentials.from(courier));

        assertEquals(200, response.statusCode());

        courierId = response.jsonPath().getInt("id");
        assertTrue(courierId > 0);
    }

    @Test
    public void cannotLoginWithoutLogin() {
        CourierCredentials credentials =
                new CourierCredentials(null, courier.getPassword());

        Response response = courierClient.login(credentials);

        assertEquals(400, response.statusCode());
        assertNotNull(response.jsonPath().getString("message"));
    }

    @Test
    public void cannotLoginWithoutPassword() {
        CourierCredentials credentials =
                new CourierCredentials(courier.getLogin(), null);

        Response response = courierClient.login(credentials);

        assertEquals(400, response.statusCode());
        assertNotNull(response.jsonPath().getString("message"));
    }

    @Test
    public void cannotLoginWithWrongLogin() {
        CourierCredentials credentials =
                new CourierCredentials(
                        "wrong_" + System.currentTimeMillis(),
                        courier.getPassword()
                );

        Response response = courierClient.login(credentials);

        assertEquals(404, response.statusCode());
        assertNotNull(response.jsonPath().getString("message"));
    }

    @Test
    public void cannotLoginWithWrongPassword() {
        CourierCredentials credentials =
                new CourierCredentials(
                        courier.getLogin(),
                        "wrongPassword"
                );

        Response response = courierClient.login(credentials);

        assertEquals(404, response.statusCode());
        assertNotNull(response.jsonPath().getString("message"));
    }
}