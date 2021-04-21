package javase.collection_study.set;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author EDing3
 * @create 2021/4/16 15:59
 */
@SuppressWarnings({"all"})
public class HashSetSource {
    public static void main(String[] args) {
        //分析HashSet底层源码
        Set set = new HashSet();
//        set.add("java");
//        set.add("php");
//        set.add("java");
        Employee emp1 = new Employee("Frank", new BigDecimal(100000), new MyDate("1992", "8", "7"));
        Employee emp2 = new Employee("Frank", new BigDecimal(200000), new MyDate("1992", "8", "7"));
        Employee emp3 = new Employee("WangPing", new BigDecimal(100000), new MyDate("1992", "8", "7"));
        set.add(emp1);
        set.add(emp2);
        set.add(emp3);
        System.out.println(set.toString());
    }
}

//当name和birthday的值相同时，认为是相同员工，不可以添加到set
class Employee {
    private String name;
    private BigDecimal sal;
    private MyDate myDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(name, employee.name) && Objects.equals(myDate, employee.myDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, myDate);
    }

    public Employee(String name, BigDecimal sal, MyDate myDate) {
        this.name = name;
        this.sal = sal;
        this.myDate = myDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", sal=" + sal +
                ", myDate=" + myDate +
                '}';
    }
}

class MyDate {
    private String year;
    private String month;
    private String day;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyDate myDate = (MyDate) o;
        return Objects.equals(year, myDate.year) && Objects.equals(month, myDate.month) && Objects.equals(day, myDate.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    public MyDate(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
