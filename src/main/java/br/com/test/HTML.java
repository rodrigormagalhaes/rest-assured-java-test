package br.com.test;

import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class HTML {

    @Test
    public void shouldDoSearchWithHTML() {
        given()
        .when()
            .get("https://restapi.wcaquino.me/v2/users")
        .then()
            .statusCode(200)
            .contentType(ContentType.HTML)
            .body("html.body.div.table.tbody.tr.size()", is(3))
            .body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
            .appendRootPath("html.body.div.table.tbody")
            .body("tr.find{it.toString().startsWith('2')}.td[1]", is("Maria Joaquina"))
        ;
    }
}
