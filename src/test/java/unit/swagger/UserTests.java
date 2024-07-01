package unit.swagger;

import unit.assertions.AssertableResponse;
import unit.assertions.Conditions;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import models.swagger.FullUser;
import models.swagger.Info;
import models.swagger.JwtAuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static unit.assertions.Conditions.hasMessage;
import static io.restassured.RestAssured.given;
import static utils.RandomTestData.getRandomUser;

public class UserTests {
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://85.192.34.140:8080";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
    }
    @Test
    public void positiveRegisterTest(){
        FullUser user = getRandomUser();
        Info info =  given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/unit/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());
        Assertions.assertEquals("success", info.getStatus());
        Info errorInfo = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/unit/signup")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Login already exist", errorInfo.getMessage());
    }
    @Test
    public void negativeRegisterTest(){
        Random random = new Random();
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = getRandomUser();
        Info info =  given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/unit/signup")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        new AssertableResponse(given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/unit/signup")
                .then()).should(hasMessage("Missing login or password"))
                .should(Conditions.hasStatusCode(400));

    }
    @Test
    public void positiveAdminAuthTest(){

        JwtAuthData jwtAuthData = JwtAuthData.builder()
                .username("admin")
                .password("admin")
                .build();
        String response = given()
                .contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/unit/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
        Assertions.assertNotNull(response);
    }
    @Test
    public void positiveNewUserAuthTest(){
        Random random = new Random();
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = getRandomUser();
        Info info =  given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/unit/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        JwtAuthData jwtAuthData = JwtAuthData.builder()
                .username(user.getLogin())
                .password(user.getPass())
                .build();
        String response = given()
                .contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/unit/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
        Assertions.assertNotNull(response);
    }
    @Test
    public void negativeNewUserAuthTest(){
        JwtAuthData jwtAuthData = JwtAuthData.builder()
                .username("wef34afew")
                .password("wefwef2g")
                .build();
        given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/unit/login")
                .then()
                .statusCode(401);

    }

    @Test
    public void positiveGetUserInfoTest(){
        JwtAuthData jwtAuthData = JwtAuthData.builder()
                .username("admin")
                .password("admin")
                .build();
        String token = given()
                .contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/unit/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
        Assertions.assertNotNull(token);
        given().auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .get("/unit/user")
                .then()
                .statusCode(200);


    }
    @Test
    public void negativeGetUserInfoWithoutJwtTest(){
        given()
                .get("/unit/user")
                .then().statusCode(401);
    }
    @Test
    public void positiveUpdateUserPassTest(){
        Random random = new Random();
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = getRandomUser();
        Info info =  given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/unit/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());
        Assertions.assertEquals("success", info.getStatus());

        JwtAuthData jwtAuthData = JwtAuthData.builder()
                .username(user.getLogin())
                .password(user.getPass())
                .build();
        String token = given()
                .contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/unit/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");

        Map<String, String> password = new HashMap<>();
        String updatedPassValue = "newPassUpdated";
        password.put("password", updatedPassValue);

        Info infoUpdatedPass = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(password)
                .put("/unit/user")
                .then()
                .extract()
                .jsonPath()
                .getObject("info", Info.class);
        Assertions.assertEquals("User password successfully changed", infoUpdatedPass.getMessage());

        jwtAuthData.setPassword(updatedPassValue);
        token = given()
                .contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/unit/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");

        given()
                .auth().oauth2(token)
                .get("/unit/user")
                .then().statusCode(200)
                .extract().as(FullUser.class);
        Assertions.assertNotEquals(user.getPass(), updatedPassValue);
    }
    @Test
    public void negativeDeleteAdminTest(){
        JwtAuthData jwtAuthData = JwtAuthData.builder()
                .username("admin")
                .password("admin")
                .build();
        String token = given()
                .contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/unit/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
        Info info = given().auth().oauth2(token)
                .delete("/unit/user")
                .then()
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Cant delete base users", info.getMessage());

    }

    @Test
    public void positiveDeleteUserTest(){
        FullUser user = getRandomUser();
        given()
                .contentType(ContentType.JSON)
                .body(user)
                .post("/unit/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        JwtAuthData jwtAuthData = JwtAuthData.builder()
                .username(user.getLogin())
                .password(user.getPass())
                .build();
        String token = given()
                .contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/unit/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
        Info info = given().auth().oauth2(token)
                .delete("/unit/user")
                .then()
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User successfully deleted", info.getMessage());
    }

    @Test
    public void positiveGetAllUsersTest(){
        List<String> users = given().get("/unit/users").then().extract().as(new TypeRef<List<String>>() {
        });
        Assertions.assertTrue(users.size()>=3);

    }

}
