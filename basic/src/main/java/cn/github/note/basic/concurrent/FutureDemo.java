package cn.github.note.basic.concurrent;

import java.util.concurrent.*;

public class FutureDemo {
    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        Callable<String> task = new Task();
        Future<String> future = threadPool.submit(task);
        System.out.println("start..");
        String res = future.get();
        System.out.println("result: " + res);
        System.out.println("over.");
        threadPool.shutdown();
    }
}

class Task implements Callable<String> {

    @Override
    public String call() throws Exception {
        TimeUnit.SECONDS.sleep(5);
        Thread thread = Thread.currentThread();
        System.out.println("id: " + thread.getId() + " name: " + thread.getName());
        return "hello";
    }
}