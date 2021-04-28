package javase.collection_study.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author EDing3
 * @create 2021/4/26 11:23
 */
public class ConcurrentHashMapDemo {
    private int age = 100;

    public static void main(String[] args) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("aaa","1111");
        System.out.println(map.toString());

        HashMap<String, String> map2 = new HashMap<>();
        map2.put("aaa", "bbb");
    }

    public void test(final int args) {
        class Inner {
            public  void pint(int b) {
                System.out.println(age);
                System.out.println(args);
            }
        }
    }
}
