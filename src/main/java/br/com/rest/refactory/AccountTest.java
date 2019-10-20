package br.com.rest.refactory;

import br.com.br.com.utils.BarrigaUtils;
import br.com.rest.core.BaseTest;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class AccountTest extends BaseTest {

    @Test
    public void shouldIncludeAccountWithSuccess() {
        given()
                .body("{\"nome\": \"Conta teste inserida\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(201)
        ;
    }

    @Test
    public void shouldAlterAccountWithSuccess() {
        Integer accountId = BarrigaUtils.getAccountId("Conta para alterar");

        given()
                .body("{\"nome\": \"Conta alterada\"}")
                .pathParam("id", accountId)
                .when()
                .put("/contas/{id}")
                .then()
                .statusCode(200)
                .body("nome", Matchers.is("Conta alterada"))
        ;
    }

    @Test
    public void shouldNotCreateAccountWithSameName() {
        given()
                .body("{\"nome\": \"Conta mesmo nome\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", Matchers.is("Já existe uma conta com esse nome!"))
        ;
    }

}
