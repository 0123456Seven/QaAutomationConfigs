package unit.swagger;

import unit.assertions.Conditions;
import unit.services.UserService;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import models.swagger.FullUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserNewTests {
    private static UserService userService;
    private static Random random;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://85.192.34.140:8080/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),new AllureRestAssured());
        random = new Random();
        userService = new UserService();
    }

    private FullUser getRandomUser() {
        int randomNumber = Math.abs(random.nextInt());
        return FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();
    }
    private FullUser getAdminUser() {
        return FullUser.builder()
                .login("admin")
                .pass("admin")
                .build();
    }
    @Test
    public void positiveRegisterTest() {
        FullUser randomUser = getRandomUser();
        userService.register(randomUser)
                .should(Conditions.hasStatusCode(201))
                .should(Conditions.hasMessage("User created"));
    }

    @Test
    public void positiveRegisterWithGamesTest() {

    }

    @Test
    public void negativeRegisterLoginExistsTest() {
        FullUser randomUser = getRandomUser();
        userService.register(randomUser);
        userService.register(randomUser)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Login already exist"));
    }

    @Test
    public void negativeRegisterNoPasswordTest() {
        FullUser randomUser = getRandomUser();
        randomUser.setPass(null);

        userService.register(randomUser)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Missing login or password"));
    }

    @Test
    public void positiveAdminAuthTest() {
        FullUser admin = getAdminUser();
        String token = userService.auth(admin)
                .should(Conditions.hasStatusCode(200))
                .asJwt();

        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveNewUserAuthTest() {
        FullUser randomUser = getRandomUser();
        userService.register(randomUser);

        String token = userService.auth(randomUser)
                .should(Conditions.hasStatusCode(200))
                .asJwt();

        Assertions.assertNotNull(token);
    }

    @Test
    public void negativeAuthTest() {
        FullUser randomUser = getRandomUser();
        userService.auth(randomUser)
                .should(Conditions.hasStatusCode(401));
    }

    @Test
    public void positiveGetUserInfoTest() {
        FullUser user = getAdminUser();
        String token = userService.auth(user).asJwt();
        userService.getUserInfo(token)
                .should(Conditions.hasStatusCode(200));
    }

    @Test
    public void negativeGetUserInfoInvalidJwtTest() {
        userService.getUserInfo("fake jwt")
                .should(Conditions.hasStatusCode(401));
    }

    @Test
    public void negativeGetUserInfoWithoutJwtTest() {
        userService.getUserInfo()
                .should(Conditions.hasStatusCode(401));
    }

    @Test
    public void positiveChangeUserPassTest() {
        FullUser randomUser = getRandomUser();
        String oldPassword = randomUser.getPass();
        userService.register(randomUser);

        String token = userService.auth(randomUser).asJwt();

        String updatedPassValue = "newpassUpdated";

        userService.updatePass(updatedPassValue, token)
                .should(Conditions.hasStatusCode(200))
                .should(Conditions.hasMessage("User password successfully changed"));

        randomUser.setPass(updatedPassValue);

        token = userService.auth(randomUser).should(Conditions.hasStatusCode(200)).asJwt();

        FullUser updatedUser = userService.getUserInfo(token).as(FullUser.class);

        Assertions.assertNotEquals(oldPassword, updatedUser.getPass());
    }

    @Test
    public void negativeChangeAdminPasswordTest() {
        FullUser user = getAdminUser();

        String token = userService.auth(user).asJwt();

        String updatedPassValue = "newpassUpdated";
        userService.updatePass(updatedPassValue, token)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Cant update base users"));
    }

    @Test
    public void negativeDeleteAdminTest() {
        FullUser user = getAdminUser();

        String token = userService.auth(user).asJwt();

        userService.deleteUser(token)
                .should(Conditions.hasStatusCode(400))
                .should(Conditions.hasMessage("Cant delete base users"));
    }

    @Test
    public void positiveDeleteNewUserTest() {
        FullUser randomUser = getRandomUser();
        userService.register(randomUser);
        String token = userService.auth(randomUser).asJwt();

        userService.deleteUser(token)
                .should(Conditions.hasStatusCode(200))
                .should(Conditions.hasMessage("User successfully deleted"));
    }

    @Test
    public void positiveGetAllUsersTest() {
        List<String> users = userService.getAllUsers().asList(String.class);
        Assertions.assertTrue(users.size() >= 3);
    }
}
