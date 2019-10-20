package br.com.rest.refactory;

import br.com.br.com.utils.BarrigaUtils;
import br.com.rest.core.BaseTest;
import br.com.rest.core.Movement;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class MovementTest extends BaseTest {

    @Test
    public void shouldIncludeMovementWithSuccess() {
        Movement movement = BarrigaUtils.getValidMovement();

        given()
                .body(movement)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
        ;
    }

    @Test
    public void shouldValidateMovementRequiredField() {
        RestAssured.given()
                .body("{}")
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", Matchers.hasSize(8))
                .body("msg", Matchers.hasItems(
                        "Data da Movimentação é obrigatório",
                        "Data do pagamento é obrigatório",
                        "Descrição é obrigatório",
                        "Interessado é obrigatório",
                        "Valor é obrigatório",
                        "Valor deve ser um número",
                        "Conta é obrigatório",
                        "Situação é obrigatório"))
        ;
    }

    @Test
    public void shouldNotIncludeMovementWithFutureDate() {
        Movement movement = BarrigaUtils.getValidMovement();
        movement.setData_transacao("21/10/2019");

        given()
                .body(movement)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", Matchers.hasSize(1))
                .body("msg", Matchers.hasItems("Data da Movimentação deve ser menor ou igual à data atual"))
        ;
    }

    @Test
    public void shouldNotRemoveAccountWithMovement() {
        Integer accountId = BarrigaUtils.getAccountId("Conta com movimentacao");

        given()
                .pathParam("id", accountId)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(500)
                .body("constraint", Matchers.is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void shouldRemoveMovement() {
        Integer movementId = BarrigaUtils.getMovementId("Movimentacao para exclusao");

        given()
                .pathParam("id", movementId)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204)
        ;
    }
}
