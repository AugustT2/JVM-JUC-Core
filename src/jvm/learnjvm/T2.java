package jvm.learnjvm;

import java.util.Random;

public class T2 {
    public static void main(String[] args) {
//        testOom();
        System.gc();
        for (int i = 0; i < 100; i++) {
            System.out.println("1111111");

        }

    }

    private static void testOom() {
//        testHeapMemory();
//        String str = "www.atguigu.com";
//        int i =1;
//        while (i>0) {
//            str += str + new Random().nextInt(88888888) + new Random().nextInt(99999999);
////            System.out.println(str);
//            i++;
//            if(i > 100){
//                testHeapMemory();
//            }
//        }
        byte[] bytes = new byte[40*1024*1024];
    }

    private static void testHeapMemory() {
//        System.out.println(Runtime.getRuntime().availableProcessors());
        System.out.println("-Xmx:默认是物理内存的1/4，当前最大堆内存是："+ Runtime.getRuntime().maxMemory()/1024/1024 + "MB");
        System.out.println("-Xms:默认是物理内存的1/64，当前实际堆内存是："+ Runtime.getRuntime().totalMemory()/1024/1024 + "MB");
        System.out.println(Runtime.getRuntime().freeMemory()/1024/1024 + "MB");
        System.out.print("=============================");
    }
}
