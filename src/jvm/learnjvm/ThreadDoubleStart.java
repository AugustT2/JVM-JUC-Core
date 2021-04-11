package jvm.learnjvm;

public class ThreadDoubleStart {
    public static void main(String[] args) {
        Thread thread = new Thread("nb的线程");
        thread.start();
        System.out.println(thread.getName());
        //IllegalThreadStateException
        thread.start();
    }
}
