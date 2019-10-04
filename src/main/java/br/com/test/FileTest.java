package br.com.test;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;

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
    @Ignore
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
    @Ignore
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

    @Test
    @Ignore
    public void shouldDoDownload() throws IOException {
        byte[] image = RestAssured.given()
                .when()
                .get("https://restapi.wcaquino.me/download")
                .then()
                .statusCode(200)
                .extract().asByteArray()
        ;

        File image_ = new File("src/main/resources/file.jpg");
        OutputStream out = new FileOutputStream(image_);
        out.write(image);
        out.close();

        Assert.assertThat(image_.length(), Matchers.lessThan(100000L));

    }


}
