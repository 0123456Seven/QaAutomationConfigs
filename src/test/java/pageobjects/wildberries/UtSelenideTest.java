package pageobjects.wildberries;


import com.codeborne.selenide.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pageobjects.wildberries.unitickets.pages.selenidepages.UtSelenideMainPage;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;

public class UtSelenideTest {
    @BeforeEach
    public void initSettings(){
//        Configuration.headless = true; // скрывает видимую часть браузера
        Configuration.timeout = 30000; //указывает таймаут

    }
    @Test
    public void firstSelenideTest(){
        Selenide.open("https://uniticket.ru/");
        int expectedDayForward = 28;
        int expectedDayBack = 30;

        UtSelenideMainPage mainPage = new UtSelenideMainPage();

        mainPage.setCityFrom("Казань")
                .setCityTo("Дубай")
                .setDayForward(expectedDayForward)
                .setDayBack(expectedDayBack)
                .search()
                .waitForPage()
                .waitForTitleDisappear()
                .assertMainDayBack(expectedDayBack)
                .assertMainDayForward(expectedDayForward)
                .assertAllDaysBackShouldHaveDay(expectedDayBack)
                .assertAllDaysForwardShouldHaveDay(expectedDayForward);
//        String expectedTitle  = "Поиск дешевых авиабилетов";
//        titleElement.should(Condition.text(expectedTitle)); //проверка текста
////        Assertions.assertEquals(actualTitle, expectedTitle);
//        ElementsCollection elements = $$x("//input");// поиск коллекции элементов
//        elements.find(Condition.partialText("Казань")).click();
//        List<String> listTexts = elements.texts(); //сходу получаем все тексты у элементов
////        elements.asDynamicIterable().stream().map(x->x.setValue("Ufa")).collect(Collectors.toList()).//возможность перебирать стримом
////        elements.should(CollectionCondition.) - работа с текстом проверки
    }
}
