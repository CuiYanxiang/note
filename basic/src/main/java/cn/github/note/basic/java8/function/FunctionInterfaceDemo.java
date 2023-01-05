package cn.github.note.basic.java8.function;

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
    }

    static void show(IFuncInterface fun) {
        fun.learn();
    }
}

@FunctionalInterface
interface IFuncInterface {
    void learn();
}
