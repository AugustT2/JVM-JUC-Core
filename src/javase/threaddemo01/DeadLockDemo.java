package javase.threaddemo01;

/**
 * @author EDing3
 * @create 2021/4/22 14:46
 */
public class DeadLockDemo {
    public static void main(String[] args) {
        Object obj1 = new Object();
        Object obj2 = new Object();


        DeadDemo dd1 = new DeadDemo(obj1, obj2);
        DeadDemo dd2 = new DeadDemo(obj2, obj1);
        new Thread(dd1).start();
        new Thread(dd2).start();
    }


}

class DeadDemo implements Runnable {

    public Object obj1;
    public Object obj2;


    public DeadDemo(Object obj1, Object obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    @Override
    public void run() {
        synchronized (obj1) {
            System.out.println("获取到了锁obj1");
            synchronized (obj2) {
                System.out.println("获取到了锁obj2");
            }
        }
    }
}
