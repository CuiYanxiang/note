package cn.github.note.basic.java8.consumer;

import java.util.function.Consumer;

public class ConsumerDemo {

    public static void main(String[] args) {
        Consumer<Person> consumer = (p) -> System.out.println("hello");
        consumer.accept(new Person());

        System.out.println("============");

        method("hi", (name) -> System.out.println(name.toUpperCase()));

        method2("test1",
                x -> System.out.println(x.toUpperCase()),
                c -> System.out.println(c.substring(0, c.length() - 1))
        );

    }

    static void method(String name, Consumer<String> str) {
        str.accept(name);
    }

    static void method2(String name, Consumer<String> con1, Consumer<String> con2) {
        con1.andThen(con2).accept(name);
    }
}

class Person {

}
