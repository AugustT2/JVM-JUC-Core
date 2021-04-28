package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author EDing3
 * @create 2021/4/28 17:22
 */
public class FutureTaskForMultiCompute {
    public static void main(String[] args) throws InterruptedException,
            ExecutionException {
        FutureTaskForMultiCompute inst = new FutureTaskForMultiCompute();
        // 创建任务集合
        List<FutureTask<Integer>> taskList = new ArrayList<FutureTask<Integer>>();
        ExecutorService execut = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 10; i++) {
            // 传入Callable对象创建FutureTask对象
            // inst.new ComputeTask(i, " "+i); 不太明白什么意思
            FutureTask<Integer> task = new FutureTask<Integer>(
                    inst.new ComputeTask(i, "" + i));
            // 把多个future放在集合中
            taskList.add(task);
            // 提交给线程池执行任务，也可以通过exec.invokeAll(taskList)一次性提交所有任务;
            execut.submit(task);
            System.out.println("所有计算任务提交完毕, 主线程接着干其他事情！");
        }
        Integer result = 0;
        // 从future中取出值来
        for (FutureTask<Integer> future : taskList) {
            result = result + future.get();
        }
        // 打印求出来的值
        System.out.println("result=" + result);
    }

    public class ComputeTask implements Callable<Integer> {
        private Integer result = 0;
        private String taskName = "";

        ComputeTask(Integer result, String taskName) {
            this.result = result;
            this.taskName = taskName;
            System.out.println("生成子线程计算任务: " + taskName);
        }

        public String getTaskName() {
            return this.taskName;
        }

        @Override
        public Integer call() throws Exception {
            for (int i = 0; i < 10; i++) {
                result += i;

            }
            Thread.sleep(5000);
            System.out.println("子线程计算任务: " + taskName + " 执行完成!");
            return result;
        }

    }

//————————————————
//    版权声明：本文为CSDN博主「lovezhaohaimig」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//    原文链接：https://blog.csdn.net/lovezhaohaimig/article/details/80358692

    /**
     * java中有Future和FutureTask这两个类
     *
     * Future是一个接口，代表可以取消的任务，并可以获得任务的执行结果
     *
     * FutureTask 是基本的实现了Future和runnable接口
     *            实现runnable接口，说明可以把FutureTask实例传入到Thread中，在一个新的线程中执行。
     *            实现Future接口，说明可以从FutureTask中通过get取到任务的返回结果，也可以取消任务执行（通过interreput中断）
     *
     * FutureTask可用于异步获取执行结果或取消执行任务的场景。通过传入Runnable或者Callable的任务给FutureTask，直接调用其run方法或者放入线程池执行，之后可以在外部通过FutureTask的get方法异步获取执行结果，因此，FutureTask非常适合用于耗时的计算，主线程可以在完成自己的任务后，再去获取结果。另外，FutureTask还可以确保即使调用了多次run方法，它都只会执行一次Runnable或者Callable任务，或者通过cancel取消FutureTask的执行等
     *
     * 1.FutureTask执行多任务计算的使用场景
     *
     * 利用FutureTask和ExecutorService，可以用多线程的方式提交计算任务，主线程继续执行其他任务，当主线程需要子线程的计算结果时，在异步获取子线程的执行结果。
     * ————————————————
     * 版权声明：本文为CSDN博主「lovezhaohaimig」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/lovezhaohaimig/article/details/80358692
     */
}
