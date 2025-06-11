package jvm;

import java.util.HashMap;
import java.util.WeakHashMap;

public class WeakHashMapDemo {
    public static void main(String[] args) {
        myHashMap();
        System.out.println("===============");
        myWeakHashMap();
        System.out.println("===============");
        myWeakHashMap2();
    }

    private static void myHashMap() {
        HashMap<Integer, String> map = new HashMap<>();
        Integer key = 1;
        String value = "HashMap";
        map.put(key, value);
        System.out.println(map);

        key = null;
        System.out.println(map);
        System.gc();
        System.out.println(map + "\t" + map.size());
    }

    private static void myWeakHashMap() {
        WeakHashMap<Integer, String> map = new WeakHashMap<>();
        Integer key = 2;
        String value = "WeakHashMap";
        map.put(key, value);
        System.out.println(map);

        key = null;
        System.out.println(map);

        System.gc();
        System.out.println(map + "\t" + map.size());
    }

    /**
     * 我理解你的疑问。在
     * WeakHashMap
     *  中，即使你将 key 设置为 null 并调用了 System.gc()，
     * WeakHashMap
     *  的 size() 仍然显示为 1，这看起来似乎不符合弱引用的预期行为。让我解释一下原因：
     *
     * 自动装箱的缓存：在你的代码中，你使用了 Integer key = 2。Java 会对小整数（-128 到 127）进行缓存，这意味着 Integer.valueOf(2) 会返回缓存中的对象，而不是创建新对象。
     * 强引用仍然存在：
     * WeakHashMap
     *  的弱引用是针对 key 的，而不是 value。虽然你将 key 变量设置为 null，但是 Integer 对象 2 可能仍然被 JVM 的整数缓存所引用。
     * 垃圾回收的不确定性：System.gc() 只是建议 JVM 进行垃圾回收，但不保证立即执行。即使执行了，由于整数缓存的原因，Integer(2) 对象可能仍然有强引用。
     * 要看到
     * WeakHashMap
     *  的预期行为，可以尝试以下修改：
     */
    private static void myWeakHashMap2() {
        WeakHashMap<Integer, String> map = new WeakHashMap<>();
        // 使用 new Integer() 来避免使用缓存
        Integer key = new Integer(1000);  // 使用一个大数字，不在缓存范围内
        String value = "WeakHashMap";
        map.put(key, value);
        System.out.println(map);

        key = null;
        System.out.println(map);

        // 尝试强制垃圾回收
        System.gc();
        // 给 GC 一些时间
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(map + "\t" + map.size());
    }
}
