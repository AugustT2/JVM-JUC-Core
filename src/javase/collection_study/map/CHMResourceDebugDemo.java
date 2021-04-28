package javase.collection_study.map;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author EDing3
 * @create 2021/4/27 14:33
 */
public class CHMResourceDebugDemo {
    public static void main(String[] args) throws Exception {
        final ConcurrentHashMap chm = new ConcurrentHashMap(32);

        for (int i = 0; i < 47; i++) {
            chm.put("abc" + i, i);
        }

//        String s = "通话";
//        String s2 = "重地";
//        System.out.println(s.hashCode());
//        System.out.println(s2.hashCode());

        new Thread() {
            @Override
            public void run() {
                chm.put("通话", "11");
                System.out.println("-----------");
            }
        }.start();

        Thread.sleep(1000);

        new Thread() {
            @Override
            public void run() {
                chm.put("重地", "22");
                System.out.println("===========");
            }
        }.start();

        Thread.sleep(1000);
        new Thread() {
            @Override
            public void run() {
                chm.put("abc5", "xx");
                System.out.println("...............");
            }
        }.start();
    }
}
