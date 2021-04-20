package season3;

/**
 * @author EDing3
 * @create 2021/4/13 15:37
 */
public class StringDemo {
    public static void main(String[] args) {

        compare01();
        System.out.println("======");
        compare02();

//            String str1 = "abc";
//            String str2 = new String("def");
//            String str3 = "abc";
//            String str4 = str2.intern();
//            String str5 = "def";
//            System.out.println(str1 == str3);//true
//            System.out.println(str2 == str4);//false
//            System.out.println(str4 == str5);//true

    }

    private static void compare02() {
        String str1 = new StringBuffer("58").append("tongcheng").toString();
        System.out.println(str1);
        System.out.println(str1.intern());
        System.out.println(str1 == str1.intern());//false

        System.out.println();

        String str2 = new StringBuffer("ja").append("va").toString();
        System.out.println(str2);
        System.out.println(str2.intern());
        System.out.println(str2 == str2.intern());//false
    }

    private static void compare01() {
        String str1 = new StringBuilder("58").append("tongcheng").toString();
        System.out.println(str1);
        System.out.println(str1.intern());
        System.out.println(str1 == str1.intern());//true

        System.out.println();

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2);
        System.out.println(str2.intern());
        System.out.println(str2 == str2.intern());//false
    }

}
