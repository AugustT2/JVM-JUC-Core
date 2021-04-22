package javase.threaddemo01;

import java.util.concurrent.TimeUnit;

/**
 * @author EDing3
 * @create 2021/4/22 10:54
 */
public class ThreadState_ {
    public static void main(String[] args) throws InterruptedException {
        T t = new T();
        System.out.println(t.getName() + " 状态 " + t.getState());
        t.start();
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5L);
                    System.out.println("wo shuiwanle");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2");

        while (Thread.State.TERMINATED != t.getState()) {
            System.out.println(t.getName() + " 状态 " + t.getState());
            System.out.println(t2.getName() + " 状态 " + t2.getState());
            Thread.sleep(500);
        }
        System.out.println(t.getName() + " 状态 " + t.getState());
    }
}

class T extends Thread {
    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < 10; i++) {
                System.out.println("hi " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            break;
        }
    }
}
