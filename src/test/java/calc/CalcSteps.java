package calc;

import io.qameta.allure.Step;

public class CalcSteps {
    @Step("Складываем числа {a} + {b}")
    public int sum(int a, int b){
        return a+b;
    }
    @Step("Проверяем, что {result}>0")
    public boolean isPosititve(int result){
        return result>0;
    }
}
