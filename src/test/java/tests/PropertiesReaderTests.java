package tests;

import lombok.SneakyThrows;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.AppConfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class PropertiesReaderTests {
    @Test
    @SneakyThrows
    public void simpleReader(){
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/test/resources/project.properties");
        properties.load(fileInputStream);

        String url = properties.getProperty("url");
        boolean isProduction = Boolean.parseBoolean(properties.getProperty("is_production"));
        int threads = Integer.parseInt(properties.getProperty("threads"));
        System.out.println(url);
        System.out.println(isProduction);
        System.out.println(threads);

    }
    @Test
    @Tag("SMOKE")
    public void ownerReaderTest(){
        AppConfig appConfig = ConfigFactory.create(AppConfig.class);
        String url = appConfig.url();
        System.out.println(url);
    }
}
