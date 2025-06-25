package thread;

public class JoinTest {
    // 1.现在有T1、T2、T3三个线程，你怎样保证T2在T1执行完后执行，T3在T2执行完后执行

    /**
     * 1.join() 方法：简单直接，但会阻塞调用线程
     * 单线程池：代码简洁，推荐使用
     * public class SingleThreadExecutorTest {
     *     public static void main(String[] args) {
     *         ExecutorService executor = Executors.newSingleThreadExecutor();
     *
     *         executor.submit(() -> {
     *             System.out.println("T1 开始执行");
     *             try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
     *             System.out.println("T1 执行完成");
     *         });
     *
     *         executor.submit(() -> {
     *             System.out.println("T2 开始执行");
     *             try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
     *             System.out.println("T2 执行完成");
     *         });
     *
     *         executor.submit(() -> {
     *             System.out.println("T3 开始执行");
     *             try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
     *             System.out.println("T3 执行完成");
     *         });
     *
     *         executor.shutdown();
     *     }
     * }
     * 2.CountDownLatch：更灵活，适合复杂场景;两个latch来实现
     * public class CountDownLatchTest {
     *     public static void main(String[] args) {
     *         CountDownLatch latch1 = new CountDownLatch(1);
     *         CountDownLatch latch2 = new CountDownLatch(1);
     *
     *         Thread t1 = new Thread(() -> {
     *             System.out.println("T1 开始执行");
     *             try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
     *             System.out.println("T1 执行完成");
     *             latch1.countDown();  // 通知T1已完成
     *         });
     *
     *         Thread t2 = new Thread(() -> {
     *             try {
     *                 latch1.await();  // 等待T1完成
     *                 System.out.println("T2 开始执行");
     *                 Thread.sleep(1000);
     *                 System.out.println("T2 执行完成");
     *                 latch2.countDown();  // 通知T2已完成
     *             } catch (InterruptedException e) {
     *                 e.printStackTrace();
     *             }
     *         });
     *
     *         Thread t3 = new Thread(() -> {
     *             try {
     *                 latch2.await();  // 等待T2完成
     *                 System.out.println("T3 开始执行");
     *                 Thread.sleep(1000);
     *                 System.out.println("T3 执行完成");
     *             } catch (InterruptedException e) {
     *                 e.printStackTrace();
     *             }
     *         });
     *
     *         t1.start();
     *         t2.start();
     *         t3.start();
     *     }
     * }
     * 3.CompletableFuture：函数式风格，代码简洁，推荐使用（Java 8+）
     * public class CompletableFutureTest {
     *     public static void main(String[] args) {
     *         CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
     *             System.out.println("T1 开始执行");
     *             try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
     *             System.out.println("T1 执行完成");
     *         }).thenRunAsync(() -> {
     *             System.out.println("T2 开始执行");
     *             try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
     *             System.out.println("T2 执行完成");
     *         }).thenRunAsync(() -> {
     *             System.out.println("T3 开始执行");
     *             try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
     *             System.out.println("T3 执行完成");
     *         });
     *
     *         // 等待所有任务完成
     *         future.join();
     *     }
     * }
     */
    public static void main(String[] args) {
        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("t1");
            }
        });
        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 引用t1线程，等待t1线程执行完
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t2");
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 引用t2线程，等待t2线程执行完
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t3");
            }});
        t3.start();//这里三个线程的启动顺序可以任意，大家可以试下！
        t2.start();
        t1.start();
    }
}
