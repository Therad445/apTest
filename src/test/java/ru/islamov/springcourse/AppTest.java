package ru.islamov.springcourse;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AppTest {

    private static final String BASE_URL = "https://api.ok.ru/";
    private static final String METHOD = "group.getCounters";
    private static final String APPLICATION_KEY = "YOUR_APPLICATION_KEY";
    private static final String APPLICATION_SECRET_KEY = "YOUR_APPLICATION_SECRET_KEY";
    private static final String ACCESS_TOKEN = "YOUR_ACCESS_TOKEN";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    private String calculateSignature(String params) {
        // Реализуйте метод для вычисления подписи
        // Пример: return MD5(params + APPLICATION_SECRET_KEY).toLowerCase();
        return "";
    }

    @Test
    public void testGetCountersSuccess() {
        String params = "application_key=" + APPLICATION_KEY + "&format=json";
        String sig = calculateSignature(params);

        Response response = given()
                .queryParam("application_key", APPLICATION_KEY)
                .queryParam("format", "json")
                .queryParam("sig", sig)
                .when()
                .get("fb.do?method=" + METHOD)
                .then()
                .statusCode(200)
                .body("result", notNullValue())
                .extract()
                .response();

        System.out.println(response.asString());
    }

    @Test
    public void testGetCountersMissingParams() {
        Response response = given()
                .queryParam("application_key", APPLICATION_KEY)
                .queryParam("format", "json")
                .when()
                .get("fb.do?method=" + METHOD)
                .then()
                .statusCode(400)
                .body("error_code", equalTo(101))
                .extract()
                .response();

        System.out.println(response.asString());
    }

    @Test
    public void testGetCountersInvalidSignature() {
        Response response = given()
                .queryParam("application_key", APPLICATION_KEY)
                .queryParam("format", "json")
                .queryParam("sig", "invalid_signature")
                .when()
                .get("fb.do?method=" + METHOD)
                .then()
                .statusCode(401)
                .body("error_code", equalTo(104))
                .extract()
                .response();

        System.out.println(response.asString());
    }
}
