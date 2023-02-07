import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ReqresInTests {

    @Test
    void checkListOfUsersFirstPageTest() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users?page=1")
                .then()
                .log().status()
                .statusCode(200)
                .log().body()
                .body("data.id", is(notNullValue()));
    }

    @Test
    void checkListOfUsersFifthPageTest() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users?page=5")
                .then()
                .log().status()
                .statusCode(200)
                .log().body()
                .body("data", is(empty()));
    }

    @Test
    void checkSingleUserNameTest() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users/5")
                .then()
                .log().status()
                .statusCode(200)
                .log().body()
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

