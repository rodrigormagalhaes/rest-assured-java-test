package br.com.br.com.utils;

import br.com.rest.core.Movement;

import static io.restassured.RestAssured.get;

public class BarrigaUtils {

    public static Integer getAccountId(String name) {
        return get("/contas?nome=" + name).then().extract().path("id[0]");
    }

    public static Movement getValidMovement() {
        Movement movement = new Movement();

        movement.setConta_id(BarrigaUtils.getAccountId("Conta para movimentacoes"));
        movement.setDescricao("Movement desc");
        movement.setEnvolvido("Person involved");
        movement.setTipo("REC");
        movement.setData_transacao("18/10/2019");
        movement.setData_pagamento("19/10/2019");
        movement.setValor(100f);
        movement.setStatus(true);

        return movement;
    }

    public static Integer getMovementId(String description) {
        return get("/transacoes?descricao=" + description).then().extract().path("id[0]");
    }
}
