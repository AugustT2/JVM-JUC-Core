package javase.copy;



/**
 * @author EDing3
 * @create 2021/4/30 11:08
 *
 * 浅复制（复制引用但不复制引用的对象）
 * 深复制（复制对象和其应用对象）
 *
 * 代码不用看，记住这个就可以了
 */

class Person implements Cloneable{
    private String name;
    private Integer age;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException  {
        Person p=null;
        try{
            p=(Person) super.clone();
        }catch (CloneNotSupportedException e) {
        }
        return super.clone();
    }
}

public class BaseCopy {

    public static void main(String[] args) throws CloneNotSupportedException {
        Person person = new Person();
        person.setName("a");
        person.setAge(12);
        Person per1=(Person) person.clone();
        System.out.println(person.hashCode() == per1.hashCode());
        per1.setName("b");
        per1.setAge(14);;
        System.out.println(person.getName()+" "+person.getAge());
        System.out.println(per1.getName()+" "+per1.getAge());
    }

}
