package javase.collection_study.set;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author EDing3
 * @create 2021/4/16 14:17
 */
public class SetMethod {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Set set = new HashSet();
        set.add("alex");
        set.add("Frank");
        set.add("tom");
        set.add("spring");
        set.add("null");
        set.add("Frank");
        //虽然取出的顺序和插入的顺序不一致，但是每次取出的顺序是一致的
        for (int i = 0; i < 10; i++) {
            System.out.println(set);
        }
        set = new HashSet();
        set.add("lucy");
        set.add("lucy");
        set.add(new Dog("rabbit"));
        set.add(new Dog("rabbit"));
        set.add(new String("tiger"));
        set.add(new String("tiger"));
        System.out.println("set:" + set);
        Class c = Class.forName("javase.collection_study.set.Dog");
        Class<Dog> dogClass = Dog.class;
//        Dog o = (Dog)c.newInstance();
        Constructor wangclass = c.getConstructor(String.class);
        System.out.println(dogClass.toString());
        Dog wangcai1 = (Dog)wangclass.newInstance("wangcai");
        Method method = c.getMethod("wangwang");
        wangcai1.wangwang();
        method.invoke(wangcai1);
    }
}

class Dog {
    private String name;

    public Dog() {}

    public Dog(String name) {
        this.name = name;
    }

    public void wangwang() {
        System.out.println("wangwangwangwang.....");
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                '}';
    }
}
