package jvm.learnjvm;

public class MyObject {
    public static void main(String[] args) {
        Object o = new Object();
//        System.out.println(o.getClass().getClassLoader().getParent());
        System.out.println(o.getClass().getClassLoader());
        System.out.println("===============");
        MyObject myObject = new MyObject();
        System.out.println(myObject.getClass().getClassLoader().getParent().getParent());
        System.out.println(myObject.getClass().getClassLoader().getParent());
        System.out.println(myObject.getClass().getClassLoader());

    }
}
