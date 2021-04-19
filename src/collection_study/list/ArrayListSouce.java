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

        hello("nb", "plus", "jira");
        //局部变量需要先初始化才能使用，否则编译不通过
        int nb = 0;
        System.out.println(nb);
    }

    private static void hello(String... strings) {
        for (int i = 0; i < strings.length; i++) {
            System.out.println("第" + i + "个参数: \t" + strings[i]);
        }
    }

}


