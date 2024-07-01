package pageobjects.wildberries;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pageobjects.wildberries.pages.ItemPage;
import pageobjects.wildberries.pages.SearchResultPage;
import pageobjects.wildberries.pages.MainPage;

public class WbFilterTests extends BaseTest{
    @BeforeEach
    public void openSite(){
        driver.get("https://www.wildberries.ru/");
    }
    @Test
    public void searchResultTest(){
        String expectedItem = "Iphone";
        Integer expectedPriceMin = 30000;
        Integer expectedPriceMax = 60000;

        MainPage mainPage = new MainPage(driver);
        mainPage.searchItem(expectedItem);

        SearchResultPage searchResultPage = new SearchResultPage(driver);
        searchResultPage.openFilters();
        searchResultPage.setMinPrice(expectedPriceMin);
        searchResultPage.setMaxPrice(expectedPriceMax);
        searchResultPage.applyFilterButton();
        searchResultPage.openItem();

        ItemPage itemPage = new ItemPage(driver);
        String actualName = itemPage.getItemName();
        Integer actualPrice = itemPage.getItemPrice();

        Assertions.assertTrue(actualName.toLowerCase().contains(expectedItem.toLowerCase()));
        Assertions.assertTrue(actualPrice >= expectedPriceMin && actualPrice <= expectedPriceMax);

        }
}
