package Week_04.src.main.java;

/**
 * 使用while死循环等待异步线程调用结束
 */
public class Demo3 {

    private volatile static Integer value = null;

    private void sum() {
        value = Fibonacci.calc_fibonacci(25);
    }

    private int getValue(){
        while (true){
            if(value != null){
                return value;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();

        Demo3 demo = new Demo3();

        new Thread(() -> {
            demo.sum();
        }).start();


        System.out.println("异步计算结果为：" + demo.getValue());
        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

    }

}
