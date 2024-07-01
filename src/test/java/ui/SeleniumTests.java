package ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class SeleniumTests {
    private WebDriver driver;
    private String downloadFolder = System.getProperty("user.dir") + File.separator + "build" + File.separator + "downloadFiles";
    @BeforeEach
    public void setUp(){
        ChromeOptions options = new ChromeOptions();
        //Указываем путь для скачивания файлов
        Map<String, String> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadFolder);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
    }
    @BeforeAll
    public static void downloadDriver(){
        WebDriverManager.chromedriver().setup();
    }
    @AfterEach
    public void close(){
        driver.close();
    }
    @Test
    public void simpleUiTest(){
        String expectedTitle = "Oleh Pendrak | YouTube";
        String actualTitle = driver.getTitle();

        driver.get("https://threadqa.ru");

        Assertions.assertEquals(expectedTitle, actualTitle);
    }
    @Test
    public void simpleFormTest(){

        String expectedFullName = "Marsel Bagautdinov";
        String expectedEmail = "mefefef@bk.ru";
        String expectedCurrentAddress = "Ufa";
        String expectedPermanentAddress = "Ufa, Hadii";

        driver.get("http://85.192.34.140:8081/");
        WebElement elementCard =  driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elementCard.click();
        WebElement elementTextBox = driver.findElement(By.xpath("//span[text()='Text Box']"));
        elementTextBox.click();
        WebElement fullNameField = driver.findElement(By.id("userName"));
        WebElement userEmail = driver.findElement(By.id("userEmail"));
        WebElement currentAddress = driver.findElement(By.id("currentAddress"));
        WebElement permanentAddress = driver.findElement(By.id("permanentAddress"));
        WebElement submitButton = driver.findElement(By.id("submit"));

        fullNameField.sendKeys(expectedFullName);
        userEmail.sendKeys(expectedEmail);
        currentAddress.sendKeys(expectedCurrentAddress);
        permanentAddress.sendKeys(expectedPermanentAddress);
        submitButton.click();

        WebElement outputName = driver.findElement(By.id("name"));
        WebElement outputEmail = driver.findElement(By.id("email"));
        WebElement outputCurrentAddress = driver.findElement(By.xpath("//div[@id='output']//p[@id='currentAddress']"));
        WebElement outputPermanentAddress = driver.findElement(By.xpath("//div[@id='output']//p[@id='permanentAddress']"));

        String actualName = outputName.getText();
        String actualEmail = outputEmail.getText();
        String actualCurrentAddress = outputCurrentAddress.getText();
        String actualPermanentAddress = outputPermanentAddress.getText();

        Assertions.assertTrue(actualName.contains(expectedFullName));
        Assertions.assertTrue(actualEmail.contains(expectedEmail));
        Assertions.assertTrue(actualCurrentAddress.contains(expectedCurrentAddress));
        Assertions.assertTrue(actualPermanentAddress.contains(expectedPermanentAddress));
    }

    @Test
    public void uploadTest(){
        driver.get("http://85.192.34.140:8081/");
        WebElement elementCard =  driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elementCard.click();
        WebElement elementTextBox = driver.findElement(By.xpath("//span[text()='Upload and Download']"));
        elementTextBox.click();
        WebElement uploadButton = driver.findElement(By.id("uploadFile"));
        uploadButton.sendKeys("D:/QaAuto/gradleLesson/src/test/resources/threadqa.jpeg");
        WebElement uploadedFakePath = driver.findElement(By.id("uploadedFilePath"));
        Assertions.assertTrue(uploadedFakePath.getText().contains("threadqa.jpeg"));
        int i = 0;
    }
    @Test
    public void downloadTest(){
        driver.get("http://85.192.34.140:8081/");
        WebElement elementCard =  driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elementCard.click();

        WebElement elementTextBox = driver.findElement(By.xpath("//span[text()='Upload and Download']"));
        elementTextBox.click();

        WebElement downloadButton = driver.findElement(By.id("downloadButton"));
        downloadButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(x-> Paths.get(downloadFolder, "sticker.png")).toFile().exists();

        File file = new File("build/downloadFiles/sticker.png");
        Assertions.assertTrue(file.length() != 0);
        Assertions.assertNotNull(file);
    }

}

