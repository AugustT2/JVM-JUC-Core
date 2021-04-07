package thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ZCyclicBarrier {
    public static void main(String[] args) {
        System.out.println("只有10个人都加载好了，游戏才会开始。");
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10, () -> {
                System.out.println("开始了哈：欢迎来到英雄联盟，敌军还有30s到达战场");}
        );
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t 成功到了加载100%");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },"第"+String.valueOf(i)+"位召唤师：").start();
        }

    }
}
