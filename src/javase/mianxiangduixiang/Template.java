package javase.mianxiangduixiang;

/**
 * @author EDing3
 * @create 2021/4/19 15:32
 */
public abstract class Template {
    public abstract void job();

    public void calculateExecutionTime() {
        long start = System.currentTimeMillis();
        job();
        long end = System.currentTimeMillis();
        System.out.println("一共执行花费了：\t" +(end -start)+"秒");
    }
}
