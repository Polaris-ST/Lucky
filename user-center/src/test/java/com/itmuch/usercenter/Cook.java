package com.itmuch.usercenter;

public interface Cook {
    public void makefood();
}

class DemoOSInvokeCook {
    public static void invokeCook(Cook cook) {
        cook.makefood();
    }

    public static void main(String[] args) {
        DemoOSInvokeCook.invokeCook(() -> System.out.println("cooking"));
    }
}
