package thread;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * 字面意思回环栅栏，通过它可以实现让一组线程等待至某个状态之后再全部同时执行
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier=new CyclicBarrier(7,()->{
            try {
                TimeUnit.SECONDS.sleep(2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("*****召唤神龙");
            try {
                TimeUnit.SECONDS.sleep(2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //这个神龙先执行完毕，才会执行下面的7个线程的后续任务，后续任务是一起执行的
        });
        for (int i = 1; i <=7 ; i++) {
            final int tempInt=i;
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+
                        "\t 收集到第"+tempInt+"颗龙珠");
                try{
                    cyclicBarrier.await();
                }catch (Exception e){
                    e.printStackTrace();
                }

                System.out.println("所有线程写入完毕，继续处理其他任务，比如数据操作");
            },String.valueOf(i)).start();
        }
    }
}
