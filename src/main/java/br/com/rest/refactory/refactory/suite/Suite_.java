package br.com.rest.refactory.refactory.suite;

import br.com.rest.core.BaseTest;
import br.com.rest.refactory.AccountTest;
import br.com.rest.refactory.AuthTest;
import br.com.rest.refactory.BalanceTest;
import br.com.rest.refactory.MovementTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountTest.class,
        MovementTest.class,
        BalanceTest.class,
        AuthTest.class
})
public class Suite_ extends BaseTest {

    @BeforeClass
    public static void login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "teste@teste.de");
        login.put("senha", "123456");

        String token = given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token");

        requestSpecification.header("authorization", "JWT " + token);

        get("/reset").then().statusCode(200);
    }

}
