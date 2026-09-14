package scooter;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

            if (response.statusCode() == SC_OK) {
                courierId = response.jsonPath().getInt("id");
            }
        }

        if (courierId != null) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @Description("Проверяем, что существующий курьер может авторизоваться и получает id")
    public void courierCanLoginTest() {
        Response response =
                courierClient.login(CourierCredentials.from(courier));

        assertEquals(SC_OK, response.statusCode());

        courierId = response.jsonPath().getInt("id");

        assertTrue(courierId > 0);
    }

    @Test
    @Description("Проверяем ошибку при авторизации без обязательного поля login")
    public void cannotLoginWithoutLoginTest() {
        CourierCredentials credentials =
                new CourierCredentials(
                        null,
                        courier.getPassword()
                );

        Response response = courierClient.login(credentials);

        assertEquals(SC_BAD_REQUEST, response.statusCode());

        assertEquals(
                "Недостаточно данных для входа",
                response.jsonPath().getString("message")
        );
    }

    @Test
    @Description("Проверяем ошибку при авторизации без обязательного поля password")
    public void cannotLoginWithoutPasswordTest() {
        CourierCredentials credentials =
                new CourierCredentials(
                        courier.getLogin(),
                        null
                );

        Response response = courierClient.login(credentials);

        System.out.println("STATUS CODE: " + response.statusCode());
        System.out.println("RESPONSE BODY: " + response.asString());

        assertEquals(SC_BAD_REQUEST, response.statusCode());

        assertEquals(
                "Недостаточно данных для входа",
                response.jsonPath().getString("message")
        );
    }

    @Test
    @Description("Проверяем ошибку при авторизации курьера с несуществующим логином")
    public void cannotLoginWithWrongLoginTest() {
        CourierCredentials credentials =
                new CourierCredentials(
                        "wrong_" + System.currentTimeMillis(),
                        courier.getPassword()
                );

        Response response = courierClient.login(credentials);

        assertEquals(SC_NOT_FOUND, response.statusCode());

        assertEquals(
                "Учетная запись не найдена",
                response.jsonPath().getString("message")
        );
    }

    @Test
    @Description("Проверяем ошибку при авторизации курьера с неверным паролем")
    public void cannotLoginWithWrongPasswordTest() {
        CourierCredentials credentials =
                new CourierCredentials(
                        courier.getLogin(),
                        "wrongPassword"
                );

        Response response = courierClient.login(credentials);

        assertEquals(SC_NOT_FOUND, response.statusCode());

        assertEquals(
                "Учетная запись не найдена",
                response.jsonPath().getString("message")
        );
    }
}