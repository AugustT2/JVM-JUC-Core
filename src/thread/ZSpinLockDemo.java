package thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ZSpinLockDemo {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void lock() {
        while (!atomicReference.compareAndSet(null, Thread.currentThread())) {}
        System.out.println(Thread.currentThread().getName() + "\t success lock, 我在厕所里了，等我拉完你才能进来，嘿嘿~");
    }

    public void unlock() {
        atomicReference.compareAndSet(Thread.currentThread(), null);
        System.out.println(Thread.currentThread().getName() + "\t success unlock，我拉完了，你可以进去了");
    }

    public static void main(String[] args) {
        ZSpinLockDemo zSpinLockDemo = new ZSpinLockDemo();
        new Thread(()-> {
            zSpinLockDemo.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            zSpinLockDemo.unlock();
        }, "想拉肚子的A").start();

        new Thread(()-> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            zSpinLockDemo.lock();
            zSpinLockDemo.unlock();
        }, "想拉肚子的B").start();
    }
}
