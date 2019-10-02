package br.com.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

    @Test
    public void shouldSaveUserWithXML() {
        RestAssured.given()
                .log().all()
                .contentType(ContentType.XML)
                .body("<user><name>Rodrigo M</name><age>38</age></user>")
                .when()
                .post("https://restapi.wcaquino.me/usersXML")
                .then()
                .log().all()
                .statusCode(201)
                .body("user.@id", Matchers.is(Matchers.notNullValue()))
                .body("user.name", Matchers.is("Rodrigo M"))
                .body("user.age", Matchers.is("38"))
        ;
    }

    @Test
    public void shouldUpadteUser() {
        RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Rodrigo\", \"age\": 99}")
                .when()
                .put("https://restapi.wcaquino.me/users/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", Matchers.is(1))
                .body("name", Matchers.is("Rodrigo"))
                .body("age", Matchers.is(99))
                .body("salary", Matchers.is(1234.5678f))
        ;
    }


}


