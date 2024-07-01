package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import listener.RetryListenerJunit5;
import models.People;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import utils.JsonHelper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Tag("UNIT")
@ExtendWith(RetryListenerJunit5.class)
public class SimpleTest {
//    private static Stream<Arguments> testPeople(){
//        return Stream.of(
//                Arguments.of(new People("Marsel", "22", "male")),
//                Arguments.of(new People("Marsel", "23", "male"))
//        );
//    }

    private String name;
    @BeforeEach
    public void before(){
        name = "Marsel"+ LocalDateTime.now();
        System.out.println("TEST: START");
    }
    @AfterEach
    public void after(){
        System.out.println("TEST: END");
    }

    @Test
    @Tag("SMOKE")
    public void testTwoLessThanThree(){
        int a = 3;
        int b = 2;
        System.out.println(name);
        Assertions.assertTrue(a<4, "Чисто "+a+" больше чем число "+b);// Ожидаю assert на true , Далее сообщение


    }

    @Test
    @DisplayName("Результат сложения 5+2=7") //меняет имя в консоли
    @Disabled("Тест не критичный, исправим через месяц") // не нужно заускать тест
    public void sumTest() {
        int a = 5;
        int b = 2;
        System.out.println(name);
        Assertions.assertEquals(7, a+b);

    }

    @ParameterizedTest// параметры в тесте
//    @CsvFileSource(resources = "/people.csv", delimiter = ',') // параметры которые мы передаем читаем из файла, делиметр(разделитель)
    @MethodSource("testPeople") //в случае если нужно насоздавать объекты (в тест будут подставляться данные с типом данных People)
    public void TwoPlusTwoEqualFourTest(People people){
        System.out.println(people.getName()+" "+people.getAge()+" "+people.getSex());
        Assertions.assertTrue(name.contains("s"));
    }
    @Test
    public void jsonJacksonStasTest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/test/resources/stas.json");
        People people = objectMapper.readValue(file, People.class);
        System.out.println(people);
        String json = objectMapper.writeValueAsString(people);
        System.out.println(json);
    }
    @Test
    public void jsonPeopleTest() throws IOException {
        People people = JsonHelper.fromJson("src/test/resources/stas.json", People.class);
        System.out.println(people);
        System.out.println(JsonHelper.toJson(people));
    }
}
