package org.example;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class SecondLabTest {

    private static final String baseUrl = "https://petstore.swagger.io/v2";
    private static final String USER = "/user",
            USER_USERNAME = baseUrl + USER + "/{username}",
            USER_LOGIN = baseUrl + USER + "/login",
            USER_LOGOUT = baseUrl + USER + "/logout";
    private static final String PET = baseUrl + "/pet",
            PET_FINDS_BY_ID = PET + "/{petId}";

    private final String username = Faker.instance().name().username();
    private final String firstName = Faker.instance().harryPotter().character();

    @BeforeClass
    public void setUp() {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyLoginAction() {
        Map<String, ?> body = Map.of(
                "username", "Hryshko",
                "password", "122-20з-1"
        );

        Response response = given().body(body).get(USER_LOGIN);
        response.then().statusCode(HttpStatus.SC_OK);

        RestAssured.requestSpecification.sessionId(response.jsonPath()
                .get("message")
                .toString()
                .replaceAll("[^0-9]", "")
        );
    }

    @Test
    public void verifyCreateAction() {
        Map<String, ?> body = Map.of(
                "username", username,
                "firstName", firstName,
                "lastName", Faker.instance().gameOfThrones().character(),
                "email", Faker.instance().internet().emailAddress(),
                "password", Faker.instance().internet().password(),
                "phone", Faker.instance().phoneNumber().phoneNumber(),
                "userStatus", Integer.valueOf("1")
        );
        given().body(body)
                .post(baseUrl + USER)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void verifyGetAction() {
        Response response = given()
                .contentType(ContentType.JSON)
                .get("https://petstore.swagger.io/v2/user/{username}", "string");

        response.then().statusCode(HttpStatus.SC_OK);

        response.then().body("firstName", equalTo("string"));
    }

    @Test
    public void verifyDeleteAction() {
        given().pathParams("username", username)
                .delete(USER_USERNAME)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void verifyLogoutAction() {
        given().get(USER_LOGOUT)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }


    //  HM
    @Test
    public void verifyCreatePet() {
        Map<String, Object> body = new HashMap<>();
        body.put("id", 1);
        body.put("category", Map.of("id", 3, "name", "Hryshko"));
        body.put("name", "122-20з-1");
        body.put("photoUrls", Collections.singletonList("https://example.com/photo.jpg"));
        body.put("tags", Collections.singletonList(Map.of("id", 3, "name", "122-20з-1")));
        body.put("status", "available");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(baseUrl + USER);

        response.then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void verifyUpdatePet() {
        String updatedName = "updated_122-20з-1";
        String updatedStatus = "sold";

        Map<String, Object> updateBody = new HashMap<>();
        updateBody.put("id", 1);
        updateBody.put("category", Map.of("id", 3, "name", "Hryshko"));
        updateBody.put("name", updatedName);
        updateBody.put("photoUrls", Collections.singletonList("https://example.com/updated_photo.jpg"));
        updateBody.put("tags", Collections.singletonList(Map.of("id", 3, "name", "122-20з-1")));
        updateBody.put("status", updatedStatus);

        Response updateResponse = given()
                .contentType(ContentType.JSON)
                .body(updateBody)
                .put(PET);

        updateResponse.then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void verifyGetPetById() {
        Response getResponse = given()
                .contentType(ContentType.JSON)
                .pathParams("petId", 1)
                .get(PET + "/{petId}", 1);

        getResponse.then().statusCode(HttpStatus.SC_OK);
        getResponse.then().body("id", equalTo(1));
    }
}
