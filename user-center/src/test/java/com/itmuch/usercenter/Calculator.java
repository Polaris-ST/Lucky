package com.itmuch.usercenter;

public interface Calculator {
    int calc(int a, int b);
}

class DemoOSInvokeCalc {
    public static void main(String[] args) {
        DemoOSInvokeCalc.invokeCalc(10, 20, (x, y) -> x + y);

    }

    public static void invokeCalc(int a, int b, Calculator calculator) {
        int calc = calculator.calc(a, b);
        System.out.println(calc);
    }
}
