package thread;

public class ZSingletonDemo {
    private  ZSingletonDemo() {
        System.out.println(Thread.currentThread().getName() +"\t构造方法私有");
    }

    private static volatile ZSingletonDemo instance = null;

    //双边检查机制（在synchronized  synchronized前后）
    public static ZSingletonDemo getZSingleton() {
        System.out.println(Thread.currentThread().getName());
        if(instance == null) {
            synchronized (ZSingletonDemo.class){
                if(instance == null) {
                    System.out.println(Thread.currentThread().getName());
                    instance =  new ZSingletonDemo();
                }
            }
        }
        return  instance;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(()-> {
                ZSingletonDemo.getZSingleton();
            },String.valueOf(i)).start();
        }
    }
}
