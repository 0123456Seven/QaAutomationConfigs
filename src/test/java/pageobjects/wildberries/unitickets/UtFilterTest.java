package pageobjects.wildberries.unitickets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pageobjects.wildberries.BaseTest;
import pageobjects.wildberries.unitickets.pages.seleniumpages.UtMainPage;

public class UtFilterTest extends BaseTest {
    @BeforeEach
    public void openSite(){
        driver.get("https://uniticket.ru/");
    }
    @Test
    public void filterTest(){
        int expectedDayForward = 27;
        int expectedDayBack = 30;
        UtMainPage mainPage = new UtMainPage(driver);
        mainPage.setCityFrom("Казань")
                .setCityTo("Дубай")
                .setDayForward(expectedDayForward);
    }
}
