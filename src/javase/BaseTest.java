package javase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class BaseTest {
    public static void main(String[] args) {
        int i = 1;
//        System.out.println(i instanceof Integer );
//        System.out.println(i instanceof Object );
//        Integer i1 = new Integer(1);
//        System.out.println(i1 instanceof Integer);
//        System.out.println(i1 instanceof Object);
//        System.out.println(null instanceof Object);

        //IntegerCache -128~127, 可以自定义最大值-XX:AutoBoxCacheMax=500，参考源码
        Integer i1 = 100;
        Integer i2 = 100;
        Integer i3 = 200;
        Integer i4 = 200;

        System.out.println(i1==i2);  //true
        System.out.println(i3==i4);  //false

/*        Double d1 = 1.0;

        Object object = null;
        System.out.println(new Object().equals(object));
        boolean equalsNull = object.equals(new Object());
        System.out.println(equalsNull);*/

        ArrayList<Object> list = new ArrayList<>();

        HashMap<Object, Object> map = new HashMap<>();
        Hashtable<String, String> table = new Hashtable<>();

        System.out.println(3*0.1==0.3);
        System.out.println(0.3 == 0.3);

    }
}
