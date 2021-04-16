package collection_study.list;

import java.util.Vector;

/**
 * @author EDing3
 * @create 2021/4/16 11:00
 */
public class VectorSource {
    public static void main(String[] args) {
        Vector vector = new Vector<Integer>();
        //默认进来是10，然后不够用了就2倍扩容。也可以通过第二个参数capacityIncrement指定每次扩容的大小。
//        Vector vector = new Vector<Integer>(10, 20);

        for (int i = 0; i < 10; i++) {
            vector.add(i);
        }
        vector.add(100);
        System.out.println(vector.size());
    }
}
