package br.com.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;

public class DataSubmissionTest {

    @Test
    public void shouldSubmitValueByQuery() {
        RestAssured
                .given()
                    .log().all()
                .when()
                    .get("https://restapi.wcaquino.me/v2/users?format=xml")
                .then()
                    .log().all()
                    .statusCode(200)
                    .contentType(ContentType.XML)
      ;
    }

    @Test
    public void shouldSubmitValueByParam() {
        RestAssured
                .given()
                .log().all()
                .queryParam("format", "xml")
                .when()
                .get("https://restapi.wcaquino.me/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.XML)
                .contentType(Matchers.containsString("utf-8"))
        ;
    }

    @Test
    public void shouldSubmitValueByHeader() {
        RestAssured
                .given()
                .accept(ContentType.XML)
                .when()
                    .get("https://restapi.wcaquino.me/v2/users")
                .then()
                    .log().all()
                    .statusCode(200)
                    .contentType(ContentType.XML)
                    .contentType(Matchers.containsString("utf-8"))
        ;
    }





}
