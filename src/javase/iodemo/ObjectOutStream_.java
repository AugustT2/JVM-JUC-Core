package javase.iodemo;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author 韩顺平
 * @version 1.0
 * 演示 ObjectOutputStream 的使用, 完成数据的序列化
 */
public class ObjectOutStream_ {
    public static void main(String[] args) throws Exception {
//序列化后， 保存的文件格式， 不是存文本， 而是按照他的格式来保存
        String filePath = "e:\\data.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
//序列化数据到 e:\data.dat
        oos.writeInt(100);// int -> Integer (实现了 Serializable)
        oos.writeBoolean(true);// boolean -> Boolean (实现了 Serializable)
        oos.writeChar('a');// char -> Character (实现了 Serializable)
        oos.writeDouble(9.5);// double -> Double (实现了 Serializable)
        oos.writeUTF("韩顺平教育");//String
//保存一个 dog 对象
        oos.writeObject(new Dog("旺财", 10, "日本", "白色"));
        oos.close();
        System.out.println("数据保存完毕(序列化形式)");

        //1.创建流对象
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src\\data.dat"));
// 2.读取， 注意顺序
        System.out.println(ois.readInt());
        System.out.println(ois.readBoolean());
        System.out.println(ois.readChar());
        System.out.println(ois.readDouble());
        System.out.println(ois.readUTF());
        System.out.println(ois.readObject());
        System.out.println(ois.readObject());
        System.out.println(ois.readObject());
// 3.关闭
        ois.close();
        System.out.println("以反序列化的方式读取(恢复)ok~");
    }
}

class Dog {
    private String name;
    private int age;
    private String country;
    private String color;

    public Dog(String name, int age, String country, String color) {
        this.name = name;
        this.age = age;
        this.country = country;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", country='" + country + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
