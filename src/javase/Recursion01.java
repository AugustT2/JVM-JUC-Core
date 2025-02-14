package javase;

public class Recursion01 {
    public static void main(String[] args) {
//        test(4);

//        factorial(5);

//        System.out.println(factorial(6));
//        System.out.println(peach(1));
//        int n = 7;
//        System.out.println(fibonacci(n));
        System.out.println(getStepNum(4));
    }

    private static int fibonacci(int n) {
        if (n == 1 |n ==2 ) {
            return 1;
        }else {
            return fibonacci(n-1) + fibonacci(n-2);
        }
    }

    private static int factorial(int i) {
        if(i ==1) {
            return 1;
        }else {
            return i * factorial(i - 1);
        }
    }

    private static void test(int i) {
        if(i>2) {
            test(i-1);
        }
        System.out.println(i);
    }

    public static int  peach(int day) {
        if(day == 10) {//第 10 天， 只有 1 个桃
            return 1;
        } else if ( day >= 1 && day <=9 ) {
            return (peach(day + 1) + 1) * 2;//规则， 自己要想
        } else {
            System.out.println("day 在 1-10");
            return -1;
        }
    }

    //有n节阶梯，可以以此走一步，也可以走两步或者三不，一共有多少种走法
    public static int getStepNum(int n) {
        if(n==1) return 1;
        if(n==2) return 2;
        if(n==3) return 3;
        if(n>3)  return getStepNum(n-1)+getStepNum(n-2) + getStepNum(n-3);
        else return 0;
    }
}
