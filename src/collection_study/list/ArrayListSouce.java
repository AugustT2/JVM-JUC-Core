package collection_study.list;

import java.util.ArrayList;

/**
 * @author EDing3
 * @create 2021/4/16 10:00
 */
public class ArrayListSouce {
    public static void main(String[] args) {
//        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<>(8);
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        for (int i = 0; i <15; i++) {
            list.add(i);
        }
    }
}
