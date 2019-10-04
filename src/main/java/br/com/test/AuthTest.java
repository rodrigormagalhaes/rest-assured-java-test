package br.com.test;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;

public class AuthTest {

    @Test
    public void shouldAccessSWAPI() {
        RestAssured.given()
                .when()
                    .get("https://swapi.co/api/people/1")
                .then()
                    .statusCode(200)
                    .body("name", Matchers.is("Luke Skywalker"))
        ;
    }
}
