package Week_04.src.main.java;

import java.util.concurrent.CountDownLatch;

public class Demo5{
    private static volatile int value = 0;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private Integer get() throws InterruptedException {
        countDownLatch.await();
        return value;
    }
    private void sum() {
        value = Fibonacci.calc_fibonacci(25);
        countDownLatch.countDown();
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        Demo5 demo = new Demo5();

        new Thread(() -> {
            demo.sum();
        }).start();

        int result = demo.get();
        System.out.println("异步计算结果为："+result);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }
}
