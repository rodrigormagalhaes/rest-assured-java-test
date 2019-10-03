package br.com.test;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;

public class FileTest {

    @Test
    public void shouldForceSendFile() {
        RestAssured.given()
                .when()
                    .post("http://restapi.wcaquino.me/upload")
                .then()
                    .statusCode(404)
                    .body("error", Matchers.is("Arquivo não enviado"))
        ;
    }

    @Test
    public void shouldDoFileUpload() {
        RestAssured.given()
                .multiPart("arquivo", new File("src/main/resources/teste.png"))
                .when()
                    .post("http://restapi.wcaquino.me/upload")
                .then()
                    .statusCode(200)
                    .body("name", Matchers.is("teste.png"))
        ;
    }

    @Test
    public void shouldNotDoLargeFileUpload() {
        RestAssured.given()
                .multiPart("arquivo", new File("src/main/resources/Reactor.pdf"))
                .when()
                    .post("http://restapi.wcaquino.me/upload")
                .then()
                    .time(Matchers.lessThan(3000L))
                    .statusCode(413)
        ;
    }
}
