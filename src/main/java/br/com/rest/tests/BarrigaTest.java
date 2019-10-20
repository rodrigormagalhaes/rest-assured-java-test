package br.com.rest.tests;

import br.com.rest.core.BaseTest;
import br.com.rest.core.Movement;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest {

    private static String accountName = "Conta " + System.nanoTime();
    private static Integer accountId;
    private static Integer movementId;

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
    }

    @Test
    public void t01_shouldIncludeAccountWithSuccess() {
        accountId = RestAssured.given()
                        .body("{\"nome\": \"" + accountName + "\"}")
                .when()
                    .post("/contas")
                .then()
                    .statusCode(201)
                    .extract().path("id")
        ;
    }

    @Test
    public void t02_shouldAlterAccountWithSuccess() {
        RestAssured.given()
                .body("{\"nome\": \""+ accountName+" alterada\"}")
                .pathParam("id", accountId)
                .when()
                .put("/contas/{id}")
                .then()
                .statusCode(200)
                .body("nome", Matchers.is(accountName + " alterada"))
        ;
    }

    @Test
    public void t03_shouldNotCreateAccountWithSameName() {
        RestAssured.given()
                .body("{\"nome\": \""+ accountName +" alterada\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", Matchers.is("Já existe uma conta com esse nome!"))
        ;
    }

    @Test
    public void t04_shouldIncludeMovementWithSuccess() {
        Movement movement = getValidMovement();

        movementId = RestAssured.given()
                    .body(movement)
                .when()
                    .post("/transacoes")
                .then()
                    .statusCode(201)
                    .extract().path("id")
        ;
    }

    @Test
    public void t05_shouldValidateMovementRequiredField() {
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
    public void t06_shouldNotIncludeMovementWithFutureDate() {
        Movement movement = getValidMovement();
        movement.setData_transacao("21/10/2019");

        RestAssured.given()
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
    public void t07_shouldNotRemoveAccountWithMovement() {
        RestAssured.given()
                .pathParam("id", accountId)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(500)
                .body("constraint", Matchers.is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void t08_shouldCalculateAccountBalance() {
        RestAssured.given()
                .when()
                .get("/saldo")
                .then()
                .statusCode(200)
                .body("find{it.conta_id == " + accountId + "}.saldo", Matchers.is("100.00"))
        ;
    }

    @Test
    public void t09_shouldRemoveMovement() {
        RestAssured.given()
                .pathParam("id", movementId)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204)
        ;
    }

    @Test
    public void t10_shouldNotAccessAPIWithoutToken() {
        FilterableRequestSpecification filterableReq = (FilterableRequestSpecification) RestAssured.requestSpecification;
        filterableReq.removeHeader("Authorization");

        RestAssured.given()
                .when()
                .get("/contas")
                .then()
                .statusCode(401)
        ;
    }

    private Movement getValidMovement() {
        Movement movement = new Movement();

        movement.setConta_id(accountId);
        movement.setDescricao("Movement desc");
        movement.setEnvolvido("Person involved");
        movement.setTipo("REC");
        movement.setData_transacao("18/10/2019");
        movement.setData_pagamento("19/10/2019");
        movement.setValor(100f);
        movement.setStatus(true);

        return movement;
    }


}

