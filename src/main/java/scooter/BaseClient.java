package scooter;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseClient {

    protected static final RequestSpecification REQUEST_SPEC =
            new RequestSpecBuilder()
                    .setBaseUri("https://qa-scooter.praktikum-services.ru")
                    .setContentType("application/json")
                    .build();
}