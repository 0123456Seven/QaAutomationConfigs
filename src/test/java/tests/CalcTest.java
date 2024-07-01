package tests;

import calc.CalcSteps;
import io.qameta.allure.Allure;
import io.qameta.allure.Issue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class CalcTest {
    @Test
    @Issue("Microsoft-4531")
    public void sumTest(){
        CalcSteps calcSteps = new CalcSteps();
        int result = calcSteps.sum(1, 4);
        boolean isOK = calcSteps.isPosititve(result);
        Assertions.assertTrue(isOK);
    }
    @Test
    public void sumStepsTest(){
        int a = 5;
        int b = 4;
        AtomicInteger result = new AtomicInteger();
        Allure.step("Прибавляем переменную "+a+" к переменной"+b, step->{
            result.set(a + b);
        });
        Allure.step("Проверяем, что результат "+result.get()+" больше 0", step->{
            Assertions.assertTrue(result.get()>0);
        });
    }
}
