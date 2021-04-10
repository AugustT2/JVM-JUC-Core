package jvm.learnjvm;

public class JVMNote {
    public static void main(String[] args) {
        System.out.println("1");
        // Exception in thread "main" java.lang.StackOverflowError
        m1();
        System.out.println("4");
    }

    private static void m1() {
        m1();
    }
}
