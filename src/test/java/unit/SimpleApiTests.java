package unit;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.fakeapi.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SimpleApiTests {
    @Test
    public void getAllUsersTest(){
        given()//Точка старта написания запроса
                .get("https://fakestoreapi.com/users")
                .then()
                .log().all()
                .statusCode(200);


    }
    @Test
    public void getSingleUserTest(){
        int userId = 2;
        given()
                .pathParam("userId", userId)
                .get("https://fakestoreapi.com/users/{userId}")
                .then().log().all()
                .body("id", equalTo(userId))//проверяется равен ли id по адресу userId
                .body("address.zipcode", matchesPattern("\\d{5}-\\d{4}"));//проверяется zipcode первые 5 цифр - 4 цифры
    }
    @Test
    public void getAllUsersWithLimitTest(){
        int limitSize = 3;
        given()
                .queryParam("limit", limitSize)
                .get("https://fakestoreapi.com/users")
                .then().log().all()
                .body("", hasSize(limitSize));
    }
    @Test
    public void getAllUsersSortByDesc(){
        String sortType = "desc";
        Response sortedResponse = given().queryParam("sort", sortType)
                .get("https://fakestoreapi.com/users")
                .then().log().all()
                .extract().response();
        Response notSortedResponse = given()
                .get("https://fakestoreapi.com/users")
                .then().log().all()
                .extract().response();
        List<Integer> sortedResponseId = sortedResponse.jsonPath().getList("id");
        List<Integer> notSortedResponseId = notSortedResponse.jsonPath().getList("id");

        List<Integer> sortedByCode = notSortedResponseId.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());;
        Assertions.assertEquals(sortedResponseId, sortedByCode);
    }
    @Test
    public void addNewUserTest(){
        Name name = new Name("Thomas", "Anderson");
        Geolocation geolocation = new Geolocation("-31.2132", "81.1231");
        Address address = Address.builder()
                .city("Moscow")
                .street("7563 old road")
                .number(100)
                .zipcode("12354-2534")
                .geolocation(geolocation).build();
        UserRoot bodyRequest = UserRoot.builder()
                .email("mefefef@bk.ru")
                .username("mars")
                .password("wegh2342jerw")
                .name(name)
                .address(address)
                .phone("1-464-435-4823").build();
        given().body(bodyRequest).post("https://fakestoreapi.com/users")
                .then().log().all();
    }
    private UserRoot getTestUser(){
        Name name = new Name("Thomas", "Anderson");
        Geolocation geolocation = new Geolocation("-31.2132", "81.1231");
        Address address = Address.builder()
                .city("Moscow")
                .street("7563 old road")
                .number(100)
                .zipcode("12354-2534")
                .geolocation(geolocation).build();
        UserRoot bodyRequest = UserRoot.builder()
                .email("mefefef@bk.ru")
                .username("mars")
                .password("wegh2342jerw")
                .name(name)
                .address(address)
                .phone("1-464-435-4823").build();
        return bodyRequest;
    }
    @Test
    public void updateUserTest(){
        UserRoot userRoot = getTestUser();
        String oldPassword = userRoot.getPassword();
        userRoot.setPassword("newpass111");
        given().body(userRoot)
                .put("https://fakestoreapi.com/users/7")
                .then().log().all()
                .body("password", not(equalTo(oldPassword)));
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
