package cn.github.note.basic.concurrent;

import java.util.concurrent.CompletableFuture;

public class CompletaFutureDemo {
    public static void main(String[] args) {
        CompletableFuture<Double> async = CompletableFuture.supplyAsync(CompletaFutureDemo::fetchPrice);
        // 如果执行成功
        async.thenAccept((d) -> {
            System.out.println("value: " + d.toString());
        });

        // 如果执行异常:
        async.exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });

    }


    static Double fetchPrice() {
        return 5 + Math.random() * 2;
    }
}
