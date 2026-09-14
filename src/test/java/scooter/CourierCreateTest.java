package scooter;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        if (loginResponse.statusCode() == SC_OK) {
            int courierId = loginResponse.jsonPath().getInt("id");
            courierClient.delete(courierId);
        }
    }

    @Test
    @Description("Проверяем успешное создание курьера со всеми обязательными полями")
    public void courierCanBeCreatedTest() {
        Response response = courierClient.create(courier);

        assertEquals(SC_CREATED, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("ok"));
    }

    @Test
    @Description("Проверяем, что нельзя создать двух одинаковых курьеров")
    public void cannotCreateTwoIdenticalCouriersTest() {
        courierClient.create(courier);

        Response secondResponse = courierClient.create(courier);

        assertEquals(SC_CONFLICT, secondResponse.statusCode());

        assertEquals(
                "Этот логин уже используется. Попробуйте другой.",
                secondResponse.jsonPath().getString("message")
        );
    }

    @Test
    @Description("Проверяем ошибку при создании курьера без обязательного поля login")
    public void cannotCreateCourierWithoutLoginTest() {
        Courier courierWithoutLogin =
                new Courier(null, "123456", "Gor");

        Response response = courierClient.create(courierWithoutLogin);

        assertEquals(SC_BAD_REQUEST, response.statusCode());

        assertEquals(
                "Недостаточно данных для создания учетной записи",
                response.jsonPath().getString("message")
        );
    }

    @Test
    @Description("Проверяем ошибку при создании курьера без обязательного поля password")
    public void cannotCreateCourierWithoutPasswordTest() {
        Courier courierWithoutPassword =
                new Courier(courier.getLogin(), null, "Gor");

        Response response = courierClient.create(courierWithoutPassword);

        assertEquals(SC_BAD_REQUEST, response.statusCode());

        assertEquals(
                "Недостаточно данных для создания учетной записи",
                response.jsonPath().getString("message")
        );
    }
}