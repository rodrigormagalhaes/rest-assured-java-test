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
}
