package cn.github.note.basic.java8.consumer;

import java.util.function.Consumer;

public class ConsumerDemo {

    public static void main(String[] args) {
        Consumer<Person> consumer = (p) -> System.out.println("hello");
        consumer.accept(new Person());
    }
}

class Person {

}
