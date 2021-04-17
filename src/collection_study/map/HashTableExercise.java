package collection_study.map;

import java.util.Hashtable;

public class HashTableExercise {
    public static void main(String[] args) {
        Hashtable table = new Hashtable();
        table.put("1","2");
//        table.put("1",null);
//        table.put(null,"2");
        table.put("1","3");
        System.out.println(table.toString());
    }
}
