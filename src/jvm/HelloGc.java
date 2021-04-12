package jvm;

import java.util.concurrent.TimeUnit;

/**
 * @author EDing3
 * @create 2021/4/12 10:53
 */
public class HelloGc {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello----gc---");
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);

    }
}
