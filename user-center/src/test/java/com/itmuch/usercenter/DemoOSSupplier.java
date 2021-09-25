package com.itmuch.usercenter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DemoOSSupplier {
    public static boolean accept(Integer arr[], Predicate<Integer[]> s) {
        boolean test = s.test(arr);
        return test;
    }


    public static void main(String[] args) {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            personList.add(new Person("li" + i, 18 + i));
        }
        personList.stream().map(person -> person.name)     //用map先装配信息 将流的类型转换成String类型
                .filter(name -> name.length() > 0)  //用filter过滤信息 获取有用信息
                .forEach(System.out::println);  //便利获取到的信息
    }

    ;
}

