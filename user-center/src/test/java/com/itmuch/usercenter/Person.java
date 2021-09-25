package com.itmuch.usercenter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    String name;
    int age;

}

class PersonDTO {
    public static void main(String[] args) {

        Person person[] = {new Person("zs", 18),
                new Person("ls", 20),
                new Person("st", 5),
                new Person("zy", 17)};
        Arrays.sort(person, (Person o1, Person o2) -> {
            return o1.age - o2.age;
        });

        for (Person s :
                person) {
            System.out.println(s.age + "" + s.name);
        }
    }

}

