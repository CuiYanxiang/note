package cn.github.note.basic.java8.supplier;

import java.util.function.Supplier;

public class SupplierDemo {
    public static void main(String[] args) {

        System.out.println(getString(() -> "hello word"));
        System.out.println("===========");

        Supplier<String> str = () -> "test";
        System.out.println(str.get().toUpperCase());

        String str2 = getString(new Supplier<String>() {
            @Override
            public String get() {
                return "aaa";
            }
        });
        System.out.println(str2);

    }

    static String getString(Supplier<String> supplier) {
        return supplier.get().toUpperCase();
    }
}
