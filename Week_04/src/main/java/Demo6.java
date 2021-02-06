package Week_04.src.main.java;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Demo6 {
    private static volatile Integer value = null;

    private void sum() {
        value = Fibonacci.calc_fibonacci(25);
    }

    private Integer get() {
        return value;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        Demo6 demo = new Demo6();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(() -> {
            demo.sum();
            return demo.get();
        });

        new Thread(futureTask).start();

        int result = futureTask.get();
        System.out.println("异步计算结果为：" + result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }
}
