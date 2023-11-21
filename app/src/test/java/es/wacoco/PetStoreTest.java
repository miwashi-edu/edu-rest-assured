package es.wacoco;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class PetStoreTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    void getPetById() {
        given().when().get("/pet/1").then().statusCode(200).body("id", equalTo(1));
    }

    @Test
    void petNotFound() {
        given().when().get("/pet/0").then().statusCode(404);
    }

    @Test
    void findPetsByStatusAvailable() {
        given().queryParam("status", "available")
                .when().get("/pet/findByStatus")
                .then().statusCode(200).body("status", everyItem(equalTo("available")));
    }

    @Test
    void findPetsByStatusSold() {
        given().queryParam("status", "sold")
                .when().get("/pet/findByStatus")
                .then().statusCode(200).body("status", everyItem(equalTo("sold")));
    }

    @Test
    void findPetsByStatusPending() {
        given().queryParam("status", "pending")
                .when().get("/pet/findByStatus")
                .then().statusCode(200).body("status", everyItem(equalTo("pending")));
    }

    @Test
    void createPet() {
        String petJson = "{ \"id\": 10, \"name\": \"Max\", \"status\": \"available\" }";

        given().contentType(ContentType.JSON).body(petJson)
                .when().post("/pet")
                .then().statusCode(200).body("name", equalTo("Max"));
    }

    @Test
    void updatePet() {
        String petJson = "{ \"id\": 10, \"name\": \"Buddy\", \"status\": \"sold\" }";

        given().contentType(ContentType.JSON).body(petJson)
                .when().put("/pet")
                .then().statusCode(200).body("name", equalTo("Buddy"));
    }

    @Test
    void deletePet() {
        given().when().delete("/pet/10").then().statusCode(200);
    }

    @Test
    void loginUser() {
        given().queryParam("username", "user1").queryParam("password", "password1")
                .when().get("/user/login")
                .then().statusCode(200).body("message", containsString("logged in user session:"));
    }

    @Test
    void logoutUser() {
        given().when().get("/user/logout").then().statusCode(200).body("message", equalTo("ok"));
    }
}
