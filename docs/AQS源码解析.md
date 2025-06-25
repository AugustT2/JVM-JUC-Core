AQS源码解析

1.一定要根据源码看图，不然很迷糊

画图工具draw.io   

https://app.diagrams.net/

![image-20250624221515680](C:\Users\Alex\AppData\Roaming\Typora\typora-user-images\image-20250624221515680.png)

2.公平锁和非公平锁相比，就是多了一个判断队列是否有值。

![image-20250625185028484](C:\Users\Alex\AppData\Roaming\Typora\typora-user-images\image-20250625185028484.png)