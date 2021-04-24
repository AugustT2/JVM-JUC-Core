package javase.collection_study.set;

import java.util.Comparator;
import java.util.TreeSet;

public class TreeSetDemo {
    public static void main(String[] args) {
//        TreeSet set = new TreeSet();
        TreeSet set = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                int a = ((String) o1).compareTo((String) o2);
                return a;
            }
        });
        set.add("777");
        set.add("666");
        set.add("778");
        set.add("799");
        set.add(null);
        System.out.println(set);
    }
}
