package br.com.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void shouldDoAuthWithTokenJWT() {
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "teste@teste.de");
        login.put("senha", "123456");


        //Login na api e pegar o token
        String token = RestAssured.given()
                .body(login)
                .contentType(ContentType.JSON)
                .when()
                .post("http://barrigarest.wcaquino.me/signin")
                .then()
                .statusCode(200)
                .extract().path("token")
        ;

        //Obter as contas
        RestAssured.given()
                .header("Authorization", "JWT " + token)
                .when()
                    .get("http://barrigarest.wcaquino.me/contas")
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("nome", Matchers.hasItem("Conta para extrato"))
        ;

    }

    @Test
    public void shouldAccessWebApp() {
        //login
        String cookie = RestAssured.given()
                        .formParam("email", "teste@teste.de")
                        .formParam("senha", "123456")
                        .contentType(ContentType.URLENC.withCharset("UTF-8"))
                    .when()
                        .post("http://seubarriga.wcaquino.me/logar")
                    .then()
                        .statusCode(200)
                        .extract().header("set-cookie")
        ;

        cookie = cookie.split("=")[1].split(";")[0];
        System.out.println(cookie);

        //obter conta
        String body = RestAssured.given()
                .cookie("connect.sid", cookie)
                .when()
                    .get("http://seubarriga.wcaquino.me/contas")
                .then()
                    .statusCode(200)
                    .body("html.body.table.tbody.tr[0].td[0]", Matchers.is("Conta para alterar"))
                    .extract().body().asString()
        ;

        System.out.println("------>>>>>");

        XmlPath xmlPath = new XmlPath(XmlPath.CompatibilityMode.HTML, body);

        System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));

    }



}