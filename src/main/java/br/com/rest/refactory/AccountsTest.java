package br.com.rest.refactory;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

import java.util.HashMap;
import java.util.Map;

public class AccountsTest {

    @BeforeClass
    public static void login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "teste@teste.de");
        login.put("senha", "123456");

        String token = RestAssured.given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token");

        RestAssured.requestSpecification.header("authorization", "JWT " + token);

        RestAssured.get("/reset").then().statusCode(200);
    }
}
