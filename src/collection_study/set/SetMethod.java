package collection_study.set;

import java.util.HashSet;
import java.util.Set;

/**
 * @author EDing3
 * @create 2021/4/16 14:17
 */
public class SetMethod {
    public static void main(String[] args) {
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
    }
}

class Dog {
    private String name;

    public Dog(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                '}';
    }
}