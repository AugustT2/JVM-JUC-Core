package javase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class reflectDemo {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        System.out.println(String.class.getName());
        //通过反射获取对象的两种方式
        String s = String.class.newInstance();
        Constructor<String> declaredConstructor = String.class.getDeclaredConstructor(String.class);
        String wagnba = declaredConstructor.newInstance("wagnba");
        System.out.println(wagnba);
        System.out.println(byte.class.getName() );
        System.out.println(new Object[3].getClass().getName());
        System.out.println(new Object[3].getClass().toGenericString());
    }
}
