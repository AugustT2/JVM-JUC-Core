package javase.collection_study.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author EDing3
 * @create 2021/4/26 11:23
 */
public class ConcurrentHashMapDemo {
    public static void main(String[] args) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("aaa","1111");
        System.out.println(map.toString());
    }
}
