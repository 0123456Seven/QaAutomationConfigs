package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$x;

public class SelenideFileTests { //Тут читаем пдф файл и проверяем содержит ли он текст Ac-tester
    @Test
    public void readPdfTest() throws IOException {
        File pdf = new File("src/test/resources/test.pdf");
        PDF pdfReader = new PDF(pdf);
        String pdfText = pdfReader.text;
        Assertions.assertTrue(pdfText.contains("AC-Tester"));
    }
    @Test
    public void readPdfBrowserTest() throws IOException {
        Selenide.open("https://www.pdf995.com/samples/");
        File pdf = $x("//td[@data-sort='pdf.pdf']/a").download();
        PDF pdfReader = new PDF(pdf);
        Assertions.assertEquals("Software 995", pdfReader.author);
    }
}
