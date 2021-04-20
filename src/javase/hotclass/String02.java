package javase.hotclass;

/**
 * @author EDing3
 * @create 2021/4/20 11:16
 */
public class String02 {
    public static void main(String[] args) {
        String a = "hello";
        String b = "world";
        b.concat("aaa");
        b.replace("w", "b");
        System.out.println(b);
        String c = a + b;
        String d = "hello" + "world";
        String e = "helloworld";
        System.out.println(c == d);
        System.out.println(e == d);

        A obj = new A();


    }
}

class A {
    private String name;


}
