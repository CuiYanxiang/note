package cn.github.note.basic.java8.function;

class FunctionInterfaceDemo {
    public static void main(String[] args) {
        IFuncInterface iFunc = () -> System.out.println("I like study");
        iFunc.learn();
    }
}

@FunctionalInterface
interface IFuncInterface {
    void learn();
}
