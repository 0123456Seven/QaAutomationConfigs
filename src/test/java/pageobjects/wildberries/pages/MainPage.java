package pageobjects.wildberries.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import pageobjects.wildberries.BasePage;

public class MainPage extends BasePage {
    private WebDriver driver;
    private By searchFieldElement = By.id("searchInput");
    private By cartButtonElement = By.xpath("//a[@data-wba-header-name='Cart']");
    private By loginButtonElement = By.xpath("//a[@data-wba-header-name='Login']");

    public MainPage(WebDriver driver){
        super(driver);
        waitPageLoadsWb();
    }

    public void searchItem(String item){
        driver.findElement(searchFieldElement).sendKeys(item);
        driver.findElement(searchFieldElement).sendKeys(Keys.ENTER);
    }
}
