package stepikauthtest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class StepikUIAuthTests {
    private String login = "mefefef@bk.ru";
    private String password = "mars891500";
    @Test
    public void uiAuthTest(){
        Selenide.open("https://stepik.org/catalog");
        $x("//a[@class='ember-view navbar__auth navbar__auth_login st-link st-link_style_button']").click();
        $(By.id("id_login_email")).sendKeys(login);
        $(By.id("id_login_password")).sendKeys(password);
        $x("//button[@type='submit']").click();
        $x("//img[@alt='User avatar']").should(Condition.visible).click();// проверяем видим ли мы элемент аватара
        $x("//li[@data-qa=\"menu-item-profile\"]").should(Condition.visible).click();
        $x("//h1").should(Condition.text("Марсель Багаутдинов"));

    }
}
