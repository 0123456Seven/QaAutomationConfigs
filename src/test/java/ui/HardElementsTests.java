package ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HardElementsTests {
    private WebDriver driver;
    @BeforeAll
    public static void downloadDriver(){
        WebDriverManager.chromedriver().setup();
    }
    @BeforeEach
    public void setUp(){
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
    }
    @AfterEach
    public void close(){
        driver.close();
    }

    @Test
    public void authTest(){
        driver.get("https://admin:admin@the-internet.herokuapp.com/basic_auth");
        String h3 = driver.findElement(By.xpath("//h3")).getText();
        Assertions.assertEquals("Basic Auth", h3);
    }
    @Test
    public void alertOk(){
        String expectedAlertText = "I am a JS Alert";
        String expectedResultText = "You successfully clicked an alert";
        driver.get("http://the-internet.herokuapp.com/javascript_alerts");
        driver.findElement(By.xpath("//button[@onclick='jsAlert()']")).click();
        String actualAlertText = driver.switchTo().alert().getText();

        driver.switchTo().alert().accept();

        String actualResultText = driver.findElement(By.id("result")).getText();

        Assertions.assertEquals(expectedAlertText, actualAlertText);
        Assertions.assertEquals(expectedResultText, actualResultText);

    }

    @Test
    public void iframeTest(){
        String login = "marsel.bagautdinov20@bk.ru";
        String password = "dG_O1NuA9ati";
        driver.get("https://mail.ru/");
        driver.findElement(By.xpath("//button[@class='resplash-btn resplash-btn_primary resplash-btn_mailbox-big cdobimp__1jdtl7f']")).click();
        WebElement iframeAuth = driver.findElement(By.xpath("//iframe[@class='ag-popup__frame__layout__iframe']"));
        driver.switchTo().frame(iframeAuth);
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys(login);
        driver.findElement(By.xpath("//button[@data-test-id='next-button']")).click();
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
        driver.findElement(By.xpath("//button[@data-test-id='submit-button']")).click();

        int i = 0;


    }
    @Test
    public void sliderTest(){
        driver.get("http://85.192.34.140:8081/");
        WebElement elementCard =  driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Widgets']"));
        elementCard.click();
        WebElement elementTextBox = driver.findElement(By.xpath("//span[text()='Slider']"));
        elementTextBox.click();

        WebElement sliderElement = driver.findElement(By.xpath("//input[@class='range-slider range-slider--primary']"));
        WebElement sliderValueElement = driver.findElement(By.id("sliderValue"));

//       Actions actions = new Actions(driver);
//       actions.dragAndDropBy(sliderElement, 50, 0).build().perform();
        int expectedValue= 85;
        int currentValue = Integer.parseInt(sliderElement.getAttribute("value"));
        int valueToMove = expectedValue - currentValue;
        for(int i = 0; i<valueToMove;i++){
            sliderElement.sendKeys(Keys.ARROW_RIGHT);
        }

        int actualValue = Integer.parseInt(sliderValueElement.getAttribute("value"));

        Assertions.assertEquals(expectedValue, actualValue);

    }

    @Test
    public void hoverTest(){
        driver.get("http://85.192.34.140:8081/");
        WebElement elementCard =  driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Widgets']"));
        elementCard.click();
        WebElement elementTextBox = driver.findElement(By.xpath("//span[text()='Menu']"));
        elementTextBox.click();

        WebElement menuItemElement = driver.findElement(By.xpath("//a[text()='Main Item 2']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(menuItemElement).build().perform();

        WebElement sublistElement = driver.findElement(By.xpath("//a[text()='SUB SUB LIST »']"));
        actions.moveToElement(sublistElement).build().perform();
//        actions.moveToElement(menuItemElement).moveToElement(sublistElement).build().perform();

        List<WebElement> lastElements = driver.findElements(By.xpath("//a[contains(text(), 'Sub Sub Item')]"));
        Assertions.assertEquals(2, lastElements.size());
    }
}
