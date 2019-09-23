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
                    .body("user.name", Matchers.is("Ana Julia"))
                    .body("user.@id", Matchers.is("3"))
                    .body("user.filhos.name.size()", Matchers.is(2))
                    .body("user.filhos.name[0]", Matchers.is("Zezinho"))
                    .body("user.filhos.name[1]", Matchers.is("Luizinho"))
                    .body("user.filhos.name", Matchers.hasItem("Luizinho"))
                    .body("user.filhos.name", Matchers.hasItems("Luizinho", "Luizinho"))
        ;
    }
}
