package br.com.test;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;

public class UserXMLTest {

    @Test
    public void shouldUseXML() {
        RestAssured.given()
                .when()
                    .get("http://restapi.wcaquino.me/usersXML/3")
                .then()
                    .statusCode(200)

                    .rootPath("user")
                    .body("name", Matchers.is("Ana Julia"))
                    .body("@id", Matchers.is("3"))
                    .rootPath("user.filhos")
                    .body("name.size()", Matchers.is(2))

                    .detachRootPath("filhos")
                    .body("filhos.name[0]", Matchers.is("Zezinho"))
                    .body("filhos.name[1]", Matchers.is("Luizinho"))

                    .appendRootPath("filhos")
                    .body("name", Matchers.hasItem("Luizinho"))
                    .body("name", Matchers.hasItems("Luizinho", "Luizinho"))
        ;
    }

    @Test
    public void shouldDoAdvancedSearchWithXML() {
        RestAssured.given()
                .when()
                    .get("http://restapi.wcaquino.me/usersXML")
                .then()
                    .statusCode(200)
                    .body("users.user.size()", Matchers.is(3))
                    .body("users.user.findAll{it.age.toInteger() <= 25}.size()", Matchers.is(2))
                    .body("users.user.@id", Matchers.hasItems("1", "2", "3"))
                    .body("users.user.find{it.age == 25}.name", Matchers.is("Maria Joaquina"))
                    .body("users.user.findAll{it.name.toString().contains('n')}.name", Matchers.hasItems("Maria Joaquina", "Ana Julia"))
                    .body("users.user.salary.find{it != null}.toDouble()", Matchers.is(1234.5678d))
                    .body("users.user.age.collect{it.toInteger() * 2}", Matchers.hasItems(40, 50, 60))
                    .body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", Matchers.is("MARIA JOAQUINA"))

        ;
    }
}
