package javase;

import java.util.Scanner;

public class TestScanner {
    //编写一个 main 方法
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("please input a value:");
        String next = sc.next();
        System.out.println(next);
    }

}
