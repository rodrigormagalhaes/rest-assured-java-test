package br.com.test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertThat;

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

    @Test
    public void teste2() {
        get("http://restapi.wcaquino.me/ola").then().statusCode(202);

    }

    @Test
    public void teste_3() {
        given()
        .when()
            .get("http://restapi.wcaquino.me/ola")
        .then()
                .statusCode(202);
    }

    @Test
    public void testHamcrest() {
        assertThat("Rodrigo", Matchers.is("Rodrigo"));
        assertThat(123, Matchers.isA(Integer.class));
        assertThat(123, Matchers.greaterThan(22));

        List<Integer> odd = Arrays.asList(1, 3, 5, 7, 9);
        assertThat(odd, Matchers.hasSize(5));
        assertThat(odd, Matchers.hasItem(5));

    }

    @Test
    public void shouldValidateBody() {
        given()
                .when()
                .get("http://restapi.wcaquino.me/ola")
                .then()
                .statusCode(200)
                .body(Matchers.is("Ola Mundo!"))
                .body(Matchers.containsString("Mundo"))
                .body(Matchers.is(Matchers.not(Matchers.nullValue())));
    }

}
