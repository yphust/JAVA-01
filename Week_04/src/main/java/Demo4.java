package Week_04.src.main.java;

public class Demo4 {
    private volatile static int value = 0;

    private synchronized void sum() {
        value = Fibonacci.calc_fibonacci(25);
        this.notifyAll();
    }

    private synchronized Integer get() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value;
    }


    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        Demo4 demo = new Demo4();
        new Thread(() -> {
            demo.sum();
        }).start();
        int result = demo.get();

        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }
}
