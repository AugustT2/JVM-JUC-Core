package thread;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collector;

public class ContainerNotSafeDemo {
    public static void main(String[] args) {
//        listNotSafe();
//        setNoSafe();
//        mapNotSafe();
        testListNotSafe();
    }

    private static void mapNotSafe() {
        //Map<String,String> map=new HashMap<>();
        Map<String, String> map = new ConcurrentHashMap<>();
        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 8));
                System.out.println(Thread.currentThread().getName() + "\t" + map);
            }, String.valueOf(i)).start();
        }
    }

    private static void setNoSafe() {
        //Set<String> set=new HashSet<>();
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(Thread.currentThread().getName() + "\t" + set);
            }, String.valueOf(i)).start();
        }
    }

    private static void listNotSafe() {
//        List<String> list=new ArrayList<>();
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(Thread.currentThread().getName() + "\t" + list);
            }, String.valueOf(i)).start();
        }
    }

    private static void  testListNotSafe() {
//        ArrayList<String> list = new ArrayList<>();
        /**
         * 当多个线程同时修改 ArrayList 时（比如一个线程在添加元素，另一个线程在遍历），就会抛出 ConcurrentModificationException
         * System.out.println(list) 会调用 ArrayList 的 toString() 方法
         * toString() 方法内部会使用迭代器遍历列表
         * 如果在遍历过程中其他线程修改了列表，就会抛出异常
         */
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 30; i++) {
            new Thread(()-> {
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(Thread.currentThread().getName() + "\t" + list);
            }, String.valueOf(i)).start();
        }

    }
}
