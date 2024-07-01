package pageobjects.wildberries.pages;

import org.openqa.selenium.By;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import pageobjects.wildberries.BasePage;

public class SearchResultPage extends BasePage {
    private WebDriver driver;
    private By allFiltersButtonElement = By.xpath(" //button[@class='dropdown-filter__btn dropdown-filter__btn--all']");
    private By startPriceFieldElement = By.xpath("//input[@name='startN']");
    private By endPriceFieldElement = By.xpath("//input[@name='endN']");
    private By showButtonElement = By.xpath(" //button[@class='filters-desktop__btn-main btn-main']");
    private By items = By.xpath("//div[@class=\"product-card-list\"]//article");

    public SearchResultPage(WebDriver driver){
        super(driver);
        waitForElementUpdated(items);
    }
    public void openItem(){
        driver.findElements(items).get(0).click();
        waitPageLoadsWb();
    }
    public void openFilters(){
        driver.findElement(allFiltersButtonElement).click();
    }
    public void setMinPrice(Integer minPrice){
        driver.findElement(startPriceFieldElement).click();
        driver.findElement(startPriceFieldElement).sendKeys(Keys.LEFT_CONTROL+"A");
        driver.findElement(startPriceFieldElement).sendKeys(Keys.BACK_SPACE);
        driver.findElement(startPriceFieldElement).sendKeys(String.valueOf(minPrice));
    }
    public void setMaxPrice(Integer maxPrice){
        driver.findElement(endPriceFieldElement).click();
        driver.findElement(endPriceFieldElement).sendKeys(Keys.LEFT_CONTROL+"A");
        driver.findElement(endPriceFieldElement).sendKeys(Keys.BACK_SPACE);
        driver.findElement(endPriceFieldElement).sendKeys(String.valueOf(maxPrice));
    }
    public void applyFilterButton(){

        driver.findElement(showButtonElement).click();
    }


}
