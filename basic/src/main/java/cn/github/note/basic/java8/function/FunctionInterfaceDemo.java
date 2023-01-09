package cn.github.note.basic.java8.function;

import java.util.function.Function;

class FunctionInterfaceDemo {
    public static void main(String[] args) {
        IFuncInterface iFunc = () -> System.out.println("I like study");
        iFunc.learn();

        System.out.println("===================");
        show(iFunc);
        System.out.println("===================");
        show(new IFuncInterface() {
            @Override
            public void learn() {
                System.out.println("匿名内部类重写方法");
            }
        });

        System.out.println("================");
        change("hello", (x) -> x + " word");

        System.out.println("================");
        change2("111",
                x -> x + "222",
                s -> Integer.parseInt(s)
        );
    }

    static void show(IFuncInterface fun) {
        fun.learn();
    }

    static void change(String str, Function<String, String> fun) {
        String word = fun.apply(str);
        System.out.println("old str: " + str + ", new str: " + word + ", length: " + word.length());
    }

    static void change2(String str, Function<String, String> fun, Function<String, Integer> fun2) {
        Integer i = fun.andThen(fun2).apply(str);
        System.out.println("length: " + i.toString().length());
    }
}

@FunctionalInterface
interface IFuncInterface {
    void learn();
}
