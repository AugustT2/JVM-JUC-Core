package jvm;

public class GCRootDemo {
    private byte[] byteArray = new byte[100*1024*1024];

//    private static GCRootDemo2 t2;
//    private static final GCRootDemo3 t3 =new GCRootDemo3(8);

    public static void m1() {
        GCRootDemo t1 = new GCRootDemo();
        System.gc();
        System.out.println("第一次gc完成");
    }

    public static void main(String[] args) {
        m1();
    }
}
