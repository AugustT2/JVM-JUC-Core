package javase.collection_study.set;

import java.util.LinkedHashSet;
import java.util.Set;

@SuppressWarnings("{all}")
public class LinkedHashSetSource {
    public static void main(String[] args) {
        Set set = new LinkedHashSet();
        set.add(new String("AA"));
        set.add(456);
        set.add(456);
        set.add(459);
        System.out.println(set.toString());
    }
}
