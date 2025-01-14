package javase.genericdemo;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author EDing3
 * @create 2021/4/21 11:37
 */
public class GenericExercise02 {
    public static void main(String[] args) {
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(new Employee("tom", 20000, new MyDate(1980,12,11)));
        employees.add(new Employee("jack", 12000, new MyDate(2001,12,12)));
        employees.add(new Employee("tom", 50000, new MyDate(1980,12,10)));
        System.out.println("employees=" + employees);
        employees.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee emp1, Employee emp2) {
//先按照 name 排序， 如果 name 相同， 则按生日日期的先后排序。 【即： 定制排序】
//先对传入的参数进行验证
                if(!(emp1 instanceof Employee && emp2 instanceof Employee)) {
                    System.out.println("类型不正确..");
                    return 0;
                }
                //比较 name
                int i = emp1.getName().compareTo(emp2.getName());
                if(i != 0) {
                    return i;
                }
                //下面是对 birthday 的比较， 因此， 我们最好把这个比较， 放在 MyDate 类完成
                //封装后， 将来可维护性和复用性， 就大大增强.
                return emp1.getBirthday().compareTo(emp2.getBirthday());
            }
        });
        System.out.println("==对雇员进行排序==");
        System.out.println(employees);
    }
    /*
    *
    * 定义 Employee 类
    * 1) 该类包含： private 成员变量 name,sal,birthday， 其中 birthday 为 MyDate 类的对象；
    * 2) 为每一个属性定义 getter, setter 方法；
    * 3) 重写 toString 方法输出 name, sal, birthday
    * 4) MyDate 类包含: private 成员变量 month,day,year； 并为每一个属性定义 getter, setter 方法；
    * 5) 创建该类的 3 个对象， 并把这些对象放入 ArrayList 集合中（ArrayList 需使用泛型来定义） ， 对集合中的元素进
    行排序， 并遍历输出：
    * *
    排序方式： 调用 ArrayList 的 sort 方法 ,
    * 传入 Comparator 对象[使用泛型]， 先按照 name 排序， 如果 name 相同， 则按生日日期的先后排序。 【即： 定制排序】
    * 有一定难度 15min , 比较经典 泛型使用案例 GenericExercise02.java
    */
}

//当name和birthday的值相同时，认为是相同员工，不可以添加到set
class Employee {
    private String name;
    private int sal;
    private MyDate birthday;

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", sal=" + sal +
                ", birthday=" + birthday +
                '}';
    }

    public Employee(String name, int sal, MyDate birthday) {
        this.name = name;
        this.sal = sal;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSal() {
        return sal;
    }

    public void setSal(int sal) {
        this.sal = sal;
    }

    public MyDate getBirthday() {
        return birthday;
    }

    public void setBirthday(MyDate birthday) {
        this.birthday = birthday;
    }
}

class MyDate implements  Comparable<MyDate> {
    private int year;
    private int month;
    private int day;

    public MyDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public int compareTo(MyDate o) {
        int yearMinus = year -o.getYear();
        if(yearMinus != 0) {
            return yearMinus;
        }

        int monthMinus = year -o.getMonth();
        if(yearMinus != 0) {
            return yearMinus;
        }

        int datMinus = year -o.getDay();
        if(yearMinus != 0) {
            return yearMinus;
        }

        return 0;
    }

    @Override
    public String toString() {
        return "MyDate{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}
