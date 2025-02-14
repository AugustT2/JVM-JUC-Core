package javase.genericdemo;

/**
 * @author EDing3
 * @create 2021/4/21 10:47
 */
public class Generic03 {
    public static void main(String[] args) {
        //注意， 特别强调： E 具体的数据类型在定义 Person 对象的时候指定,即在编译期间， 就确定 E 是什么类型
        Person<String> person = new Person<String>("韩顺平教育");
        person.show(); //String

        Person<Integer> person2 = new Person<Integer>(100);
        person2.show();//Integer
    }
}
//泛型的作用是： 可以在类声明时通过一个标识表示类中某个属性的类型，
// 或者是某个方法的返回值的类型， 或者是参数类型
class Person<E> {
    E s;//E 表示 s 的数据类型, 该数据类型在定义 Person 对象的时候指定,即在编译期间， 就确定 E 是什么类型

    public Person(E s) {//E 也可以是参数类型
        this.s = s;
    }

    public E f() {//返回类型使用 E
        return s;
    }

    public void show() {
        System.out.println(s.getClass());//显示 s 的运行类型
    }
}
