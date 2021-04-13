package season3;

/**
 * @author EDing3
 * @create 2021/4/13 15:37
 */
public class StringDemo {
    public static void main(String[] args) {

        String str1 = new StringBuilder("58").append("tongcheng").toString();
        System.out.println(str1);
        System.out.println(str1.intern());
        System.out.println(str1 == str1.intern());//true

        System.out.println();

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2);
        System.out.println(str2.intern());
        System.out.println(str2 == str2.intern());//false



//            String str1 = "abc";
//            String str2 = new String("def");
//            String str3 = "abc";
//            String str4 = str2.intern();
//            String str5 = "def";
//            System.out.println(str1 == str3);//true
//            System.out.println(str2 == str4);//false
//            System.out.println(str4 == str5);//true

    }

}
