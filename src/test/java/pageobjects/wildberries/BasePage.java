package pageobjects.wildberries;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage { //Базовая страница
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;

    public BasePage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        js = (JavascriptExecutor) driver;
    }

    public String getTextJs(By element){
        return (String) js.executeScript("return arguments[0].textContent;", driver.findElement(element));// достаем текст из js
    }
    public void jsClick(By element){
        js.executeScript("arguments[0].click;", driver.findElement(element)); //js клик
    }
    public void waitPageLoadsWb(){
        By pageLoader = By.xpath("//div[@class='general-preloader']");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(pageLoader));// Ждем пока элемент не пропадет
    }
    public void waitForElementUpdated(By locator){
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(locator)));
    }
    public WebElement waitForTextPresentedInList(By list, String value){
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(list));
        return driver.findElements(list).stream()
                .filter(x->x.getText().contains(value))
                .findFirst()
                .orElseThrow(()->new NoSuchElementException("Города нет"+value));
    }

}
