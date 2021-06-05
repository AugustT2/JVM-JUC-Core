package thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author EDing3
 * @create 2021/6/3 11:30
 */
public class WaitNotifyDemo {
    public static void main(String[] args) {
        lockAwaitSignal();
    }

    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    private static void lockAwaitSignal() {
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t" + "------come in");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\t" + "------被唤醒");
            } finally {
                lock.unlock();
                System.out.println("release a");
            }
        }, "A").start();


        new Thread(() -> {
            //之前有一个疑问，这个lock不时被上面锁住了吗，怎么这里还能锁，原因是a线程调用完await()方法后会释放锁资源，会修改state的值为0，b线程lock时判断是0就加锁来了
            //而b线程第加锁失败后会调用addWaiter()方法然后被加到了CLH队友中排队去了，并且自身的waitStatus为-1，之后会调用park()方法阻塞在哪儿等待，等a修改state为0嗯，b就成功上位了
            lock.lock();
            try {
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "\t" + "------通知");
            } finally {
                lock.unlock();
                System.out.println("release b");
            }
        }, "B").start();
    }

}
