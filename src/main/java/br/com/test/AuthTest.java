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

    @Test
    public void shoudGetWeatherAPI() {
        RestAssured.given()
                        .log().all()
                        .queryParam("q", "Uberlandia,BR")
                        .queryParam("appId", "e62a7205ac6c50a0e2bb248f587844b7")
                        .queryParam("units", "metric")
                    .when()
                        .get("http://api.openweathermap.org/data/2.5/weather")
                    .then()
                        .log().all()
                        .statusCode(200)
                        .body("name", Matchers.is("Uberlandia"))
                        .body("timezone", Matchers.is(-10800))
                        .body("main.temp", Matchers.greaterThan(10))
        ;
    }

    @Test
    public void shouldNotAccessWithoutPassword() {
        RestAssured.given()
                 .when()
                    .get("http://restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(401)
        ;

    }

    @Test
    public void shouldAccessWithBasicAuth() {
        RestAssured.given()
                 .when()
                    .get("http://admin:senha@restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", Matchers.is("logado"))
        ;

    }

    @Test
    public void shouldAccessWithBasicAuth_2() {
        RestAssured.given()
                    .auth().basic("admin", "senha")
                .when()
                    .get("http://restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", Matchers.is("logado"))
        ;

    }

    @Test
    public void shouldAccessWithBasicChallenge() {
        RestAssured.given()
                .auth().preemptive().basic("admin", "senha")
                .when()
                .get("http://restapi.wcaquino.me/basicauth2")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", Matchers.is("logado"))
        ;

    }



}