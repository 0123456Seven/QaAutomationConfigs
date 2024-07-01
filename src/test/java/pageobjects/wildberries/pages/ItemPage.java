package pageobjects.wildberries.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pageobjects.wildberries.BasePage;

public class ItemPage extends BasePage {
    private By itemTitleElement = By.xpath("//h1[@class='product-page__title']");

    private By priceItemElement = By.xpath("//span[@class='price-block__price']");
    public ItemPage(WebDriver driver){
        super(driver);
    }
    public String getItemName(){
        String text = driver.findElement(itemTitleElement).getText();
        return text;
    }
    public Integer getItemPrice(){
        String priceText = getTextJs(priceItemElement);
        priceText = priceText.replaceAll("[^0-9.]", "");
        return Integer.parseInt(priceText);
    }

}
