package br.com.test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;

public class Teste {

    @Test
    public void test_() {
        Response response =  RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");

        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue((response.getStatusCode() == 200));
        Assert.assertEquals("201",response.getStatusCode());

        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(201);
    }



}
