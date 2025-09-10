package thread;

import java.util.HashMap;
import java.util.Map;

public class TestJvisaulvm {
    //声明缓存对象
    private static final Map map = new HashMap();
    public static void main(String args[]){
        try {
            Thread.sleep(5000);//给打开visualvm时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //循环添加对象到缓存
        for(int i=0; i<500000;i++){
            DeadLockExample t = new DeadLockExample();
            map.put("key"+i,t);
        }
        System.out.println("first");
        //为dump出堆提供时间
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=0; i<500000;i++){
            DeadLockExample t = new DeadLockExample();
            map.put("key"+i,t);
        }
        System.out.println("second");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=0; i<3000000;i++){
            DeadLockExample t = new DeadLockExample();
            map.put("key"+i,t);
        }
        System.out.println("third");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=0; i<4000000;i++){
            DeadLockExample t = new DeadLockExample();
            map.put("key"+i,t);
        }
        System.out.println("forth");
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("qqqq");
    }

}
