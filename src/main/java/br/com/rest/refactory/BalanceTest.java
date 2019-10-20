package br.com.rest.refactory;

import br.com.br.com.utils.BarrigaUtils;
import br.com.rest.core.BaseTest;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class BalanceTest extends BaseTest {

    @Test
    public void shouldCalculateAccountBalance() {
        Integer accountId = BarrigaUtils.getAccountId("Conta para saldo");

        given()
                .when()
                .get("/saldo")
                .then()
                .statusCode(200)
                .body("find{it.conta_id == " + accountId + "}.saldo", Matchers.is("534.00"))
        ;
    }

}
