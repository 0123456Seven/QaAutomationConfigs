package unit;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import models.fakeapi.Address;
import models.fakeapi.Geolocation;
import models.fakeapi.Name;
import models.fakeapi.UserRoot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SimpleApiRefactoredTests {
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "https://fakestoreapi.com";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());// Вынесение логирования, чтобы не дублироваться

    }
    @Test
    public void getAllUsersTest(){
        given()
                .get("/users")
                .then()
                .statusCode(200);
    }
    @Test
    public void getSingleUserTest(){
        int userId = 2;
        UserRoot userRoot = given()
                .pathParam("userId", userId)
                .get("/users/{userId}")
                .then()
                .extract().as(UserRoot.class);
        Assertions.assertEquals(userId, userRoot.getId());
        Assertions.assertTrue(userRoot.getAddress().getZipcode().matches("\\d{5}-\\d{4}"));
    }
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 20})
    public void getUsersWithLimitTest(int limitSize){
        List<UserRoot> users = given()
                .queryParam("limit", limitSize)
                .get("/users")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("", UserRoot.class);
        Assertions.assertEquals(limitSize, users.size());
    }
    @ParameterizedTest
    @ValueSource(strings = {"desc" , "asc"})
    public void getAllUsersSortByDesc(String sortType){
        List<UserRoot> sortedResponse = given()
                .queryParam("sort", sortType)
                .get("/users")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("", UserRoot.class);
        List<UserRoot> notSortedResponse = given()
                .get("/users")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("", UserRoot.class);
        List<Integer> sortedResponseId = sortedResponse.stream().map(UserRoot::getId).collect(Collectors.toList());
        List<Integer> notSortedResponseId = sortedResponse.stream().map(UserRoot::getId).collect(Collectors.toList());

        List<Integer> sortedByCode = notSortedResponseId.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());;
        Assertions.assertEquals(sortedResponseId, sortedByCode);
    }
    private UserRoot getTestUser(){
        Random random = new Random();
        Name name = new Name("Thomas", "Anderson");
        Geolocation geolocation = new Geolocation("-31.2132", "81.1231");
        Address address = Address.builder()
                .city("Moscow")
                .street("7563 old road")
                .number(random.nextInt(100))
                .zipcode("12354-2534")
                .geolocation(geolocation).build();
        UserRoot bodyRequest = UserRoot.builder()
                .email("mefefef@bk.ru")
                .username("mars")
                .password("wegh2342jerw")
                .name(name)
                .address(address)
                .phone(String.valueOf(random.nextInt(100000)+7000)).build();
        return bodyRequest;
    }
    @Test
    public void addNewUserTest(){
        UserRoot user = getTestUser();
        Integer userId = given()
                .body(user)
                .post("/users")
                .then()
                .statusCode(200)
                .extract().jsonPath().getInt("id");
        Assertions.assertNotNull(userId);
    }
    @Test
    public void updateUserTest() {
        UserRoot user = getTestUser();
        String oldPassword = user.getPassword();
        user.setPassword("newpass111");

        System.out.println("User ID: " + user.getId());
        System.out.println("User Body: " + user);

        UserRoot updatedUser = given()
                .body(user)
                .pathParam("userId", 7)
                .put("/users/{userId}")
                .then()
                .statusCode(200)
                .extract().as(UserRoot.class);

        Assertions.assertNotEquals(updatedUser.getPassword(), oldPassword);
    }
    @Test
    public void deleteUserTest(){
        given().delete("https://fakestoreapi.com/users/7")
                .then().log().all()
                .statusCode(200);
    }
    @Test
    public void authUserTest(){
        Map<String, String> userAuth = new HashMap<>();
        userAuth.put("username", "johnd");
        userAuth.put("password", "m38rmF$");
        given().contentType(ContentType.JSON).body(userAuth)
                .post("https://fakestoreapi.com/auth/login")
                .then().log().all()
                .body("token", notNullValue());
    }

}
