package pageobjects.wildberries.unitickets.pages.seleniumpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pageobjects.wildberries.BasePage;

public class UtMainPage extends BasePage {
    private By cityFromField = By.xpath("//input[@placeholder='Откуда']");
    private By listOfCitiesFrom = By.xpath("//div[@class='origin field active']//div[@class='city']");
    private By cityToField = By.xpath("//input[@placeholder='Куда']");
    private By listOfCitiesTo = By.xpath("//div[@class='destination field active']//div[@class='city']");
    private By dateForward = By.xpath(" //input[@placeholder='Туда']");
    private By dateBack = By.xpath("//input[@placeholder='Обратно']");
    private String dayInCalendar =  "//span[text()='%d']";
    private By searchButton = By.xpath("//input[@class='search_btn']");

    public UtMainPage(WebDriver driver) {
        super(driver);
    }

    public UtMainPage setCityFrom(String city){
        driver.findElement(cityFromField).clear();
        driver.findElement(cityFromField).sendKeys(city);
        waitForTextPresentedInList(listOfCitiesFrom, city).click();
        return this;
    }
    public UtMainPage setCityTo(String city){
        driver.findElement(cityToField).clear();
        driver.findElement(cityToField).sendKeys(city);
        waitForTextPresentedInList(listOfCitiesTo, city).click();
        return this;
    }
    public UtMainPage setDayForward(int day){
        driver.findElement(dateForward).click();
        getDay(day).click();
        return this;
    }
    private WebElement getDay(int day){
        By dayLocator = By.xpath(String.format(dayInCalendar,day));
        return driver.findElement(dayLocator);
    }
    public void search(){
        driver.findElement(searchButton).click();
    }



}
