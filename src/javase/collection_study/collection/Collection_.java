package javase.collection_study.collection;

import java.util.*;

public class Collection_ {
    public static void main(String[] args) {
//        Collection
        List<String> list = Arrays.asList("aaa", "bbb", "ccc");
//        list.add("ddd");
//        System.out.println(list);
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
//        iterator.next();
        for (String s : list) {
            System.out.println(s);
        }
//        System.out.println(list);
    }

}
