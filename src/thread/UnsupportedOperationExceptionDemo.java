package thread;

import java.util.Arrays;
import java.util.List;

/**
 * @author EDing3
 * @create 2021/4/14 17:31
 */
public class UnsupportedOperationExceptionDemo {
    public static void main(String[] args) {
        //定义一个字符长度为5的字符串
        String[] strings = new String[5];
        strings[0] = "a";
        strings[1] = "b";
        strings[2] = "c";
        strings[3] = "d";
        strings[4] = "e";

        //调用Arrays中的asList方法将String[]转化为List<String>
        List<String> list = Arrays.asList(strings);
        System.out.println("list<String>:" + list.toString());

        //为list添加一个元素
        //Exception in thread "main" java.lang.UnsupportedOperationException
        //综上所论，如果在将String[]转化为List< String >的时候，是不能对转化出来的结果进行add，remove操作的，因为他们并不是我们熟悉的ArrayList，而是Arrays里面的内部类ArrayList。
        list.add("f");
        System.out.println("list<String>:" + list.toString());
    }
}
