package javase;

public class reflectDemo {
    public static void main(String[] args) {
        System.out.println(String.class.getName());
        System.out.println(byte.class.getName() );
        System.out.println(new Object[3].getClass().getName());
        System.out.println(new Object[3].getClass().toGenericString());
    }
}
