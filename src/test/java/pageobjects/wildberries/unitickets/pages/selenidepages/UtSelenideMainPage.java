package pageobjects.wildberries.unitickets.pages.selenidepages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pageobjects.wildberries.unitickets.pages.seleniumpages.UtMainPage;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class UtSelenideMainPage {
    private SelenideElement cityFromField = $x("//input[@placeholder='Откуда']");
    private ElementsCollection listOfCitiesFrom = $$x("//div[@class='origin field active']//div[@class='city']");
    private SelenideElement cityToField = $x("//input[@placeholder='Куда']");
    private ElementsCollection listOfCitiesTo = $$x("//div[@class='destination field active']//div[@class='city']");
    private SelenideElement dateForward = $x(" //input[@placeholder='Туда']");
    private SelenideElement dateBack = $x("//input[@placeholder='Обратно']");
    private String dayInCalendar =  "//span[text()='%d']";
    private SelenideElement searchButton = $x("//div[@class='search_btn']");
    public UtSelenideMainPage setCityFrom(String city){
        cityFromField.clear();
        cityFromField.sendKeys(city);
        cityFromField.click();
        listOfCitiesFrom.find(Condition.partialText(city)).click();//Проверяет, содержит ли текст элемента подстроку city.
        // То есть, этот метод ищет элемент, текст которого содержит заданное значение city.
        return this;
    }
    public UtSelenideMainPage setCityTo(String city){
        cityToField.clear();
        cityToField.sendKeys(city);
        cityToField.click();
        listOfCitiesTo.find(Condition.partialText(city)).click();
        return this;
    }
    public UtSelenideMainPage setDayForward(int day){
        dateForward.click();
        getDay(day).click();
        return this;
    }
    public UtSelenideMainPage setDayBack(int day){
        dateBack.click();
        getDay(day).click();
        return this;
    }
    private SelenideElement getDay(int day){
        SelenideElement dayLocator = $x(String.format(dayInCalendar,day));
        return dayLocator;
    }
    public UtSearchSelenidePage search(){
        searchButton.click();
        return Selenide.page(UtSearchSelenidePage.class);
    }
}
