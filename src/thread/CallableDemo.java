package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());
        new Thread(futureTask, "AA").start();
        int result01 = 100;
        //get尽量方法后面，不然如果线程没有执行完，会一直阻塞等线程执行完了再去取返回值，然后在往下执行代码。
        int result02 = futureTask.get();
        System.out.println("result=" + (result01 + result02));
    }
}

class MyThread implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("callable come in ...");
        return 1024;
    }
}
