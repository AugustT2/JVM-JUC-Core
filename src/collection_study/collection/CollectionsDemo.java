package collection_study.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsDemo {
    public static void main(String[] args) {
        List list = new ArrayList<>();
        list.add("zhangsan");
        list.add("lisi");
        list.add("wangwu");
        System.out.println(list);
        List dest = new ArrayList<>();
//        for (Object o : list) {
//            dest.add("");
//        }
        for (int i = 0; i < 10; i++) {
            dest.add("666"+i);
        }
        Collections.copy(dest, list);
        System.out.println(dest);
        System.out.println(dest.size());
    }
}
