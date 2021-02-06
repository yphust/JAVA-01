package Week_04.src.main.java;

/**
 * 利用sleep，让主线程等待子线程运行结束
 */
public class Demo1 {

    private volatile static Integer value = 0;

    private void sum() {
        value = Fibonacci.calc_fibonacci(25);
    }



    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();

        Demo1 demo = new Demo1();

        new Thread(() -> {
            demo.sum();
        }).start();

        Thread.sleep(1000);

        System.out.println("异步计算结果为：" + value);
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

    }

}
