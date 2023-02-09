package in.reqres;

import in.reqres.models.getuserspage.BaseModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static in.reqres.Specs.request;
import static in.reqres.Specs.response;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class APITests {

    @Test
    void checkListOfUsersFirstPageTest() {
        given()
                .spec(request)
                .when()
                .get("/users?page=1")
                .then()
                .spec(response)
                .body("data.findAll{it.last_name =~/.*?is/}.last_name.flatten()",
                        hasItem("Morris"));
    }

    @Test
    void checkListOfUsersFifthPageTest() {
        BaseModel model = given()
                .spec(request)
                .when()
                .get("/users?page=2")
                .then()
                .spec(response)
                .extract().as(BaseModel.class);
        Assertions.assertEquals(6, model.getPerPage());

    }

    @Test
    void checkSingleUserNameTest() {
        given()
                .spec(request)
                .when()
                .get("https://reqres.in/api/users/5")
                .then()
                .spec(response)
                .body("data.first_name", is("Charles"));
    }

    @Test
    void checkSingleUserNotFoundTest() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users/30")
                .then()
                .log().status()
                .statusCode(404)
                .log().body()
                .assertThat().body(is("{}"));
    }

    @Test
    void checkUserCreationTest() {
        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().body()
                .statusCode(201)
                .body("id", is(notNullValue()));

    }

    @Test
    void checkNoBodyCreationTest() {
        given()
                .log().uri()
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().body()
                .statusCode(415);
    }
}

