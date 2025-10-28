package javase.threaddemo01;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class BatchDataProcessor {

    public static void main(String[] args) {
        BatchDataProcessor processor = new BatchDataProcessor();
        // 10万条数据，每批1000条
        processor.processAllData(100_000, 1000);
    }

    // 使用CompletableFuture并行处理所有批次
    public void processAllData(int totalSize, int batchSize) {
        // 1. 获取所有数据
        List<String> allData = fetchUpstreamData(totalSize);

        // 2. 自定义线程池（根据机器CPU核心数调整）
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(32, Runtime.getRuntime().availableProcessors() * 2));

        // 3. 拆分批次并提交异步任务
        List<CompletableFuture<Integer>> futures = IntStream.range(0, (allData.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> allData.subList(i * batchSize, Math.min((i + 1) * batchSize, allData.size())))
                .map(batch -> CompletableFuture.supplyAsync(() -> processBatch(batch), executor))
                .collect(Collectors.toList());

        // 4. 合并所有批次的结果
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        CompletableFuture<List<Integer>> allResults = allFutures.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join) // 获取每个批次的结果
                        .collect(Collectors.toList())
        );

        // 5. 最终汇总（回调非阻塞）
        allResults.whenComplete((results, ex) -> {
            if (ex != null) {
                System.err.println("Error processing batches: " + ex.getMessage());
            } else {
                int totalProcessed = results.stream().mapToInt(Integer::intValue).sum();
                System.out.println("All batches completed. Total processed: " + totalProcessed);
            }
            executor.shutdown(); // 关闭线程池
        });

        // 主线程可继续执行其他逻辑（非阻塞）
        System.out.println("Main thread continues to work...");
    }

    // 模拟从上游获取10万条数据（实际可能是数据库查询、API调用等）
    //返回一个 IntStream（整数流），表示从 startInclusive到 endExclusive-1的连续整数序列。
    private List<String> fetchUpstreamData(int totalSize) {
        return IntStream.range(0, totalSize)
                .mapToObj(i -> "Data-" + i)
                .collect(Collectors.toList());
    }

    // 处理单个数据批次（实际可能是写入数据库、调用外部服务等）
    private int processBatch(List<String> batch) {
        System.out.println("Processing batch of size: " + batch.size() + " on thread: " + Thread.currentThread().getName());
        // 模拟处理耗时
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return batch.size(); // 返回处理成功的记录数
    }




}
