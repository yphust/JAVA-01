package Week_04.src.main.java;

public class Fibonacci {
    public static int calc_fibonacci(int arg){
        if(arg < 2){
            return 1;
        }else{
            return calc_fibonacci(arg - 1) + calc_fibonacci(arg - 2);
        }
    }
}
