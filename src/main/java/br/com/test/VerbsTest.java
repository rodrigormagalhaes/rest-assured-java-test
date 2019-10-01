package br.com.test;

import io.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

public class VerbsTest {

    @Test
    public void shouldSaveUser() {
        RestAssured.given()
                    .log().all()
                    .contentType("application/json")
                    .body("{ \"name\": \"Rodrigo\", \"age\": 99}")
                .when()
                    .post("https://restapi.wcaquino.me/users")
                .then()
                    .log().all()
                    .statusCode(201)
                    .body("id", Matchers.is(Matchers.notNullValue()))
                    .body("name", Matchers.is("Rodrigo"))
                    .body("age", Matchers.is(99))
                ;
    }

    @Test
    public void shouldNotSaveUserWithoutName() {
        RestAssured.given()
                    .log().all()
                    .contentType("application/json")
                    .body("{ \"age\": 99}")
                .when()
                    .post("https://restapi.wcaquino.me/users")
                .then()
                    .log().all()
                    .statusCode(400)
                    .body("id", Matchers.is(Matchers.nullValue()))
                    .body("error", Matchers.is("Name é um atributo obrigatório"))
        ;
    }


}


