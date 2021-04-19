package javase.mianxiangduixiang;

import java.util.concurrent.TimeUnit;

/**
 * @author EDing3
 * @create 2021/4/19 15:35
 */
public class TemplateSon extends Template {
    @Override
    public void job() {
        try {
            TimeUnit.SECONDS.sleep(5L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Template t = new TemplateSon();
        t.calculateExecutionTime();

    }

    static class Pig {

    }

    class Monke{

    }

}
