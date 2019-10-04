package br.com.test;

import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers;
import org.junit.Test;
import org.xml.sax.SAXParseException;

public class SchemaTest {

    @Test
    public void shouldValidateSchemaXML() {
        RestAssured.given()
                .when()
                    .get("http://restapi.wcaquino.me/usersXML")
                .then()
                    .statusCode(200)
                    .body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
        ;

    }

    @Test(expected = SAXParseException.class)
    public void shouldNotValidateInvalidSchemaXML() {
        RestAssured.given()
                .when()
                    .get("http://restapi.wcaquino.me/invalidUsersXML")
                .then()
                    .statusCode(200)
                    .body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
        ;

    }
}
