package br.com.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpVerbsTest {

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

    @Test
    public void shouldCustomizeURL() {
        RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Rodrigo\", \"age\": 99}")
                .when()
                .put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", Matchers.is(1))
                .body("name", Matchers.is("Rodrigo"))
                .body("age", Matchers.is(99))
                .body("salary", Matchers.is(1234.5678f))
        ;
    }

    @Test
    public void shouldCustomizeURL_2() {
        RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Rodrigo\", \"age\": 99}")
                .pathParam("entidade", "users")
                .pathParam("userId", "1")
                .when()
                .put("https://restapi.wcaquino.me/{entidade}/{userId}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", Matchers.is(1))
                .body("name", Matchers.is("Rodrigo"))
                .body("age", Matchers.is(99))
                .body("salary", Matchers.is(1234.5678f))
        ;
    }

    @Test
    public void shouldDeleteUser() {
        RestAssured.given()
                    .log().all()
                .when()
                    .delete("https://restapi.wcaquino.me/users/1")
                .then()
                    .statusCode(204)
        ;
    }

    @Test
    public void shouldNotDeleteInexistenteUser() {
        RestAssured.given()
                .log().all()
                .when()
                .delete("https://restapi.wcaquino.me/users/1000")
                .then()
                .statusCode(400)
                .body("error", Matchers.is("Registro inexistente"))
        ;
    }

    @Test
    public void shouldSaveUserUsingMap() {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("name", "Usuário via map");
        params.put("age", 71);

        RestAssured.given()
                .log().all()
                .contentType("application/json")
                .body(params)
                .when()
                .post("https://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.is(Matchers.notNullValue()))
                .body("name", Matchers.is("Usuário via map"))
                .body("age", Matchers.is(71))
        ;
    }

    @Test
    public void shouldSaveUserUsingObject() {
        User user = new User("Usuário via objeto", 35);

        RestAssured.given()
                .log().all()
                .contentType("application/json")
                .body(user)
                .when()
                .post("https://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.is(Matchers.notNullValue()))
                .body("name", Matchers.is("Usuário via objeto"))
                .body("age", Matchers.is(35))
        ;
    }

    @Test
    public void shoulDeserializeObjectWhenSaveUserUsingObject() {
        User user = new User("Usuário deserializado", 35);

        User insertedUser = RestAssured.given()
                .log().all()
                .contentType("application/json")
                .body(user)
                .when()
                .post("https://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(User.class)
        ;

        Assert.assertThat(insertedUser.getId(), Matchers.notNullValue());
        Assert.assertEquals("Usuário deserializado", insertedUser.getName());
        Assert.assertThat(insertedUser.getAge(), Matchers.is(35));
    }

    @Test
    public void shouldSaveUserWithXMLUsingObjetc() {
        User user = new User("Rodrigo M", 38);

        RestAssured.given()
                .log().all()
                .contentType(ContentType.XML)
                .body(user)
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
    public void shouldDeserializeXMLWhenSaveUser() {
        User user = new User("Rodrigo M", 38);

        User insertedUser = RestAssured.given()
                .log().all()
                .contentType(ContentType.XML)
                .body(user)
                .when()
                .post("https://restapi.wcaquino.me/usersXML")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(User.class)
        ;

        Assert.assertThat(insertedUser.getId(), Matchers.notNullValue());
        Assert.assertThat(insertedUser.getName(), Matchers.is("Rodrigo M"));
        Assert.assertThat(insertedUser.getAge(), Matchers.is(38));
        Assert.assertThat(insertedUser.getSalary(), Matchers.nullValue());

    }

}


