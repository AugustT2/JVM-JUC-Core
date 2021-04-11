package jvm.learnjvm;

class CodeZy {
    public CodeZy() {
        System.out.println("Code的构造方法111");
    }
    {
        System.out.println("Code的构造代码块222");
    }
    static {
        System.out.println("Code的构造代码块333");
    }
}
public class CodeBlockDemo{
    {
        System.out.println("CodeBlockDemo的构造代码块444");
    }
    static {
        System.out.println("CodeBlockDemo的构造代码块555");
    }
    public CodeBlockDemo() {
        System.out.println("CodeBlockDemo的构造方法666");
    }

    //573212146
    //如果CodeBlockDemo extends CodeZy，则357 21 21 2146，最后一步是2146需要注意。
    public static void main(String[] args) {
        System.out.println("-----fen ge xian__CodeBlockDemo的main方法777___");
        new CodeZy();
        System.out.println("----------");
        new CodeZy();
        System.out.println("----------------");
        new CodeBlockDemo();
    }
}
