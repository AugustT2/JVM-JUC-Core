package javase.threaddemo01;

/**
 * @author EDing3
 * @create 2021/4/21 17:13
 */
public class SellTicket {
    public static void main(String[] args) {
//测试
// SellTicket01 sellTicket01 = new SellTicket01();
// SellTicket01 sellTicket02 = new SellTicket01();
// SellTicket01 sellTicket03 = new SellTicket01();
//
// //这里我们会出现超卖..
// sellTicket01.start();//启动售票线程
// sellTicket02.start();//启动售票线程
// sellTicket03.start();//启动售票线程
        System.out.println("===使用实现接口方式来售票=====");
        SellTicket02 sellTicket02 = new SellTicket02();
        new Thread(sellTicket02).start();//第 1 个线程-窗口
        new Thread(sellTicket02).start();//第 2 个线程-窗口
        new Thread(sellTicket02).start();//第 3 个线程-窗口

        //测试一把
        SellTicket03 sellTicket03 = new SellTicket03();
        new Thread(sellTicket03).start();//第 1 个线程-窗口
        new Thread(sellTicket03).start();//第 2 个线程-窗口
        new Thread(sellTicket03).start();//第 3 个线程-窗口
    }
} //使用 Thread 方式

class SellTicket01 extends Thread {
    private static int ticketNum = 100;//让多个线程共享 ticketNum

    @Override
    public void run() {
        while (true) {
            if (ticketNum <= 0) {
                System.out.println("售票结束...");
                break;
            }
            //休眠 50 毫秒, 模拟
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("窗口 " + Thread.currentThread().getName() + " 售出一张票"
                    + " 剩余票数=" + (--ticketNum));
        }
    }
}

//实现接口方式
class SellTicket02 implements Runnable {
    private static int ticketNum = 100;//让多个线程共享 ticketNum

    @Override
    public void run() {
        while (true) {
            if (ticketNum <= 0) {
                System.out.println("售票结束...");
                break;
            } //休眠 50 毫秒, 模拟
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("窗口 " + Thread.currentThread().getName() + " 售出一张票"
                    + " 剩余票数=" + (--ticketNum));//1 - 0 - -1 - -2
        }

    }
}

//实现接口方式, 使用 synchronized 实现线程同步
class SellTicket03 implements Runnable {
    private int ticketNum = 100;//让多个线程共享 ticketNum
    private boolean loop = true;//控制 run 方法变量
    Object object = new Object();

    //同步方法（静态的） 的锁为当前类本身
//老韩解读
//1. public synchronized static void m1() {} 锁是加在 SellTicket03.class
//2. 如果在静态方法中， 实现一个同步代码块.
/*
synchronized (SellTicket03.class) {
System.out.println("m2");
}
*/
    //总结起来说，只要保持是同一个对象即可
    public synchronized static void m1() {
    }

    public static void m2() {
        synchronized (SellTicket03.class) {
            System.out.println("m2");
        }
    }
    //老韩说明

    //1. public synchronized void sell() {} 就是一个同步方法
//2. 这时锁在 this 对象
//3. 也可以在代码块上写 synchronize ,同步代码块, 互斥锁还是在 this 对象
    public /*synchronized*/ void sell() { //同步方法, 在同一时刻， 只能有一个线程来执行 sell 方法
        synchronized (/*this*/ object) {
            if (ticketNum <= 0) {
                System.out.println("售票结束...");
                loop = false;
                return;
            }
            //休眠 50 毫秒, 模拟
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("窗口 " + Thread.currentThread().getName() + " 售出一张票"
                    + " 剩余票数=" + (--ticketNum));//1 - 0 - -1 - -2
        }
    }

    @Override
    public void run() {
        while (loop) {
            sell();//sell 方法是一共同步方法
        }
    }
}
