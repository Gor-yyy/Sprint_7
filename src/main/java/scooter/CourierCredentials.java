package scooter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourierCredentials {

    private String login;
    private String password;

    public static CourierCredentials from(Courier courier) {
        return new CourierCredentials(
                courier.getLogin(),
                courier.getPassword()
        );
    }
}