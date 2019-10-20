package br.com.rest.refactory;

import br.com.rest.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.Test;

public class AuthTest extends BaseTest {

    @Test
    public void shouldNotAccessAPIWithoutToken() {
        FilterableRequestSpecification filterableReq = (FilterableRequestSpecification) RestAssured.requestSpecification;
        filterableReq.removeHeader("Authorization");

        RestAssured.given()
                .when()
                .get("/contas")
                .then()
                .statusCode(401)
        ;
    }

}
