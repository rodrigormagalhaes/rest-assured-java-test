package br.com.rest.tests;

import br.com.rest.core.BaseTest;
import br.com.rest.core.Movement;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BarrigaTest extends BaseTest {

    private String token;

    @Before
    public void login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "teste@teste.de");
        login.put("senha", "123456");

        token = RestAssured.given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token");
    }

    @Test
    public void shouldNotAccessAPIWithoutToken() {
        RestAssured.given()
                .when()
                .get("/contas")
                .then()
                .statusCode(401)
        ;
    }

    @Test
    public void shouldIncludeAccountWithSuccess() {
        RestAssured.given()
                        .header("authorization", "JWT " + token)
                        .body("{\"nome\": \"Conta x\"}")
                .when()
                    .post("/contas")
                .then()
                    .statusCode(201)
        ;
    }

    @Test
    public void shouldAlterAccountWithSuccess() {
        RestAssured.given()
                .header("authorization", "JWT " + token)
                .body("{\"nome\": \"Conta x alterada\"}")
                .when()
                .put("/contas/38276")
                .then()
                .statusCode(200)
                .body("nome", Matchers.is("Conta x alterada"))
        ;
    }

    @Test
    public void shouldNotCreateAccountWithSameName() {
        RestAssured.given()
                .header("authorization", "JWT " + token)
                .body("{\"nome\": \"Conta x alterada\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", Matchers.is("Já existe uma conta com esse nome!"))
        ;
    }

    @Test
    public void shouldIncludeMovementWithSuccess() {
        Movement movement = new Movement();
        movement.setConta_id(38276);
        movement.setDescricao("Movement desc");
        movement.setEnvolvido("Person involved");
        movement.setTipo("REC");
        movement.setData_transacao("18/10/2019");
        movement.setData_pagamento("19/10/2019");
        movement.setValor(100f);
        movement.setStatus(true);

        RestAssured.given()
                    .header("authorization", "JWT " + token)
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
                .header("authorization", "JWT " + token)
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


}

