package br.com.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.misc.Request;

import java.util.ArrayList;

public class UserXMLTest {

    public static RequestSpecification requestSpecification;
    public static ResponseSpecification responseSpecification;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://restapi.wcaquino.me";
//        RestAssured.port = 443;
//        RestAssured.basePath = "/v2";

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(200);
        responseSpecification = responseSpecBuilder.build();

        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
    }

    @Test
    public void shouldUseXML() {
        RestAssured.given()
                .when()
                    .get("/usersXML/3")
                .then()

                    .rootPath("user")
                    .body("name", Matchers.is("Ana Julia"))
                    .body("@id", Matchers.is("3"))
                    .rootPath("user.filhos")
                    .body("name.size()", Matchers.is(2))

                    .detachRootPath("filhos")
                    .body("filhos.name[0]", Matchers.is("Zezinho"))
                    .body("filhos.name[1]", Matchers.is("Luizinho"))

                    .appendRootPath("filhos")
                    .body("name", Matchers.hasItem("Luizinho"))
                    .body("name", Matchers.hasItems("Luizinho", "Luizinho"))
        ;
    }

    @Test
    public void shouldDoAdvancedSearchWithXML() {
        RestAssured.given()
                .when()
                    .get("/usersXML")
                .then()
                    .body("users.user.size()", Matchers.is(3))
                    .body("users.user.findAll{it.age.toInteger() <= 25}.size()", Matchers.is(2))
                    .body("users.user.@id", Matchers.hasItems("1", "2", "3"))
                    .body("users.user.find{it.age == 25}.name", Matchers.is("Maria Joaquina"))
                    .body("users.user.findAll{it.name.toString().contains('n')}.name", Matchers.hasItems("Maria Joaquina", "Ana Julia"))
                    .body("users.user.salary.find{it != null}.toDouble()", Matchers.is(1234.5678d))
                    .body("users.user.age.collect{it.toInteger() * 2}", Matchers.hasItems(40, 50, 60))
                    .body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", Matchers.is("MARIA JOAQUINA"))

        ;
    }

    @Test
    public void shouldDoAdvancedSearchWithXMLAndJava() {
        ArrayList<NodeImpl> names =
            RestAssured.given()
                    .when()
                    .get("/usersXML")
                    .then()
                    .statusCode(200)
                    .extract().path("users.user.name.findAll{it.toString().contains('n')}");

        Assert.assertEquals(2, names.size());
        Assert.assertEquals("Maria Joaquina".toUpperCase(), names.get(0).toString().toUpperCase());
        Assert.assertTrue("ANA JULIA".equalsIgnoreCase(names.get(1).toString()));
    }
}
