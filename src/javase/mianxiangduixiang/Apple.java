package javase.mianxiangduixiang;

/**
 * @author EDing3
 * @create 2021/4/23 15:34
 */
public class Apple {
    //this理解为指向自身的一个引用，super就是指向父类的引用
    /*int i = 0;
    Apple eatApple(){
        i++;
        return this;
    }
    public static void main(String[] args) {
        Apple apple = new Apple();
        System.out.println(apple);
        Apple apple1 = apple.eatApple().eatApple();
        System.out.println(apple1 == apple);

    }*/

    private int num;
    private String color;
    public Apple(int num){
//        this.num = num; //this()表示调用构造方法，只能放在第一行，super()也是一样
        this(num,"blue");
    }
    public Apple(String color){
        this(1,color);
    }
    public Apple(int num, String color) {
        this.num = num;
        this.color = color;
    }
//不写默认是protected
     protected void eat() {
        System.out.println("eat");
    }

}

class Apple1 extends Apple{

    public Apple1(int num) {
        super(num);
    }

    public static void main(String[] args) {
        Apple1 a = new Apple1(1);
        a.eat();
        a.getClass();
    }
}
