## 1、字符串[常量池](https://so.csdn.net/so/search?q=%E5%B8%B8%E9%87%8F%E6%B1%A0&spm=1001.2101.3001.7020)Java内部加载

```java
public class StringInternDemo {

    public static void main(String[] args) {

        String str1 = new StringBuilder("58").append("tongcheng").toString();
        System.out.println(str1);
        System.out.println(str1.intern());
        System.out.println(str1 == str1.intern());

        System.out.println();

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2);
        System.out.println(str2.intern());
        System.out.println(str2 == str2.intern());

    }

}
```

### [intern](https://so.csdn.net/so/search?q=intern&spm=1001.2101.3001.7020)() 解释：

```java
Returns a canonical representation for the string object.
A pool of strings, initially empty, is maintained privately by the class String.
When the intern method is invoked, if the pool already contains a string equal to this String object as determined by the equals(Object) method, then the string from the pool is returned. Otherwise, this String object is added to the pool and a reference to this String object is returned.
It follows that for any two strings s and t, s.intern() == t.intern() is true if and only if s.equals(t) is true.
All literal strings and string-valued constant expressions are interned. String literals are defined in section 3.10.5 of the The Java™ Language Specification.
Returns:
a string that has the same contents as this string, but is guaranteed to be from a pool of unique strings.
```

**方法区和运行时常量池溢出**

由于运行时常量池是方法区的一部分，所以这两个区域的溢出测试可以放到一起进行。[HotSpot](https://so.csdn.net/so/search?q=HotSpot&spm=1001.2101.3001.7020)从JDK 7开始逐步“去永久代”的计划，并在JDK 8中完全使用元空间来代替永久代的背景故事，在此我们就以测试代码来观察一下，使用"永久代"还是“元空间"来实现方法区，对程序有什么实际的影响。

String:intern()是一个本地方法，它的作用是如果字符串常量池中已经包含一个等于此String对象的字符串，则返回代表池中这个字符串的String对象的引用；否则，会将此String对象包含的字符串添加到常量池中，并且返回此String对象的引用。在JDK 6或更早之前的HotSpot虚拟机中，常量池都是分配在永久代中，我们可以通过-XX:PermSize和-XX:MaxPermSize限制永久代的大小，即可间接限制其中常量池的容量。

运行结果：

```java
58tongcheng
58tongcheng
true

java  //自己new
java  //Java内部已经存在的
false
```

### 原因:

-   按照代码结果，java字符串答案为false必然是两个不同的java，那另外一个java字符串如何加载进来的？
    
-   有一个初始化的java字符串(JDK出娘胎自带的)，在加载sun.misc.Version这个类的时候进入常量池
    

源码：

```java
public final class System {

    /* register the natives via the static initializer.  //通过静态初始化器注册本机。
     *
     * VM will invoke the initializeSystemClass method to complete  //VM将调用initializeSystemClass方法来完成
     * the initialization for this class separated from clinit.  //该类的初始化与clinit分离。
     * Note that to use properties set by the VM, see the constraints //注意，要使用VM设置的属性，请参见约束
     * described in the initializeSystemClass method.  //在initializeSystemClass方法中描述。
     */
    private static native void registerNatives();
    static {
        registerNatives();
    }
    
    //省略.......
    
    //初始化System类，在线程初始化完以后
    private static void initializeSystemClass() {
     	//省略.......
        sun.misc.Version.init();
		//省略.......
    }
    
}
```

Version源码：

```java
public class Version {
    private static final String launcher_name = "java";
    //省略.......
}
```

### OpenJDK8底层源码说明

-   类加载器和rt.jar - 根加载器提前部署加载rt.jar
    
-   OpenJDK8源码
    
-   考查点 - intern()方法，判断true/false？- 《深入理解java虚拟机》书原题是否读过经典JVM书籍
    

```
这段代码在JDK 6中运行，会得到两个false，而在JDK 7中运行，会得到一个true和一个false。产生差异的原因是，在JDK 6中，intern()方法会把首次遇到的字符串实例复制到永久代的字符串常量池中存储，返回的也是永久代里面这个字符串实例的引用，而由StringBuilder创建的字符串对象实例在Java堆上，所以必然不可能是同一个引用，结果将返回false。

而JDK 7(以及部分其他虚拟机，例如JRockit）的intern()方法实现就不需要再拷贝字符串的实例到永久代了，既然字符串常量池已经移到Java堆中，那只需要在常量池里记录一下首次出现的实例引用即可，因此intern()返回的引用和由StringBuilder创建的那个字符串实例就是同一个。而对str2比较返回false，这是因为“java”这个字符串在执行StringBuilder.toString()之前就已经出现过了，字符串常量池中已经有它的引用，不符合intern()方法要求“首次遇到"”的原则，“计算机软件"这个字符串则是首次出现的，因此结果返回true。
```

## 2、两数求和（算法）

题目：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 的那 两个 整数，并返回它们的数组下标。你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。你可以按任意顺序返回答案。

```
示例1：
输入：nums = [2,7,11,15], target = 9
输出：[0,1]
解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。

示例2：
输入：nums = [3,2,4], target = 6
输出：[1,2]
```

代码：

```java
public class Solution {

    /**
     * 题目：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 的那 两个 整数，
     * 并返回它们的数组下标。你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。你可以按任意顺序返回答案。
     * @param args
     */
    public static void main(String[] args) {
        int nums[] = {2,7,11,15};
        int target = 9;
        int[] index = getIndex2(nums, target);
        for (int i = 0; i < index.length; i++) {
            System.out.println(index[i]);
        }

    }

    //暴力破解法 时间复杂度O(N^2)
    public static int[] getIndex(int[] nums,int target){
        for (int i = 0; i < nums.length; i++) {
            for (int j = i+1; j < nums.length; j++) {
                if (nums[i]+nums[j]==target){
                    return new int[]{i,j};
                }
            }
        }
        return null;
    }

    //使用hash 时间复杂度O(N)
    public static int[] getIndex2(int[] nums,int target){
        /**
         * int nums[] = {2,7,11,15};
         *  int target = 17;
         */
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int diff = target - nums[i];
            if (hashMap.containsKey(diff)){
                return new int[]{i,hashMap.get(diff)};
            }
            //key->值，Value->索引
            hashMap.put(nums[i],i);
        }
        return null;
    }
}
```

算法来源：[力扣](https://leetcode.cn/problems/two-sum/)

## 3、可重入锁

### 1、理论

-   可重入锁又名递归锁是
-   指在同一个线程在外层方法获取锁的时候，再进入该线程的内层方法会自动获取锁(前提，锁对象得是同一个对象），不会因为之前已经获取过还没释放而阻塞。
-   Java中ReentrantLock和synchronized都是可重入锁，可重入锁的一个优点是可一定程度避免死锁。

可重入锁将字分开解释：

-   可：可以
-   重：再次
-   入：进入
-   锁：同步锁
-   进入什么？ - 进入同步域（即同步代码块/方法或显示锁锁定的代码）

一句话：一个线程中的多个流程可以获取同一把锁，持有这把同步锁可以再次进入。自己可以获取自己的内部锁。

可重入锁的种类：

-   隐式锁（即synchronized关键字使用的锁）默认是可重入锁。
    -   同步块
    -   同步方法
-   Synchronized的重入的实现机理。
-   显式锁（即Lock）也有ReentrantLock这样的可重入锁。

### 2、可重入锁的代码验证-上

可重入锁的种类：

-   隐式锁（即synchronized关键字使用的锁）默认是可重入锁。
    -   同步块

```java
public class Demo1 {

    static Object objectLockA = new Object();

    public static void m1(){
        new Thread(()->{
            synchronized (objectLockA){
                System.out.println(Thread.currentThread().getName()+"\t"+"外层....");
                synchronized (objectLockA){
                    System.out.println(Thread.currentThread().getName()+"\t"+"中层....");
                    synchronized (objectLockA){
                        System.out.println(Thread.currentThread().getName()+"\t"+"内层....");
                    }
                }
            }
        },"t1").start();
    }

    public static void main(String[] args) {
        m1();
    }
}
```

运行结果：

```
t1	外层....
t1	中层....
t1	内层....
```

-   同步方法

```java
public class Demo1 {

    public static synchronized void m1() {
        System.out.println("===外");
        m2();
    }

    public static synchronized void m2() {
        System.out.println("===中");
        m3();
    }

    public static synchronized void m3() {
        System.out.println("===内");

    }

    public static void main(String[] args) {
        m1();
    }
}
```

运行结果：

```
===外
===中
===内
```

### 3、可重入锁的代码验证-下

Synchronized的重入的实现机理：

-   每个锁对象拥有一个锁计数器和一个指向持有该锁的线程的指针。
    
-   当执行monitorenter时，如果目标锁对象的计数器为零，那么说明它没有被其他线程所持有，Java虚拟机会将该锁对象的持有线程设置为当前线程，并且将其计数器加1。
    
-   在目标锁对象的计数器不为零的情况下，如果锁对象的持有线程是当前线程，那么Java虚拟机可以将其计数器加1，否则需要等待，直至持有线程释放该锁。
    

当执行monitorexit时，Java虚拟机则需将锁对象的计数器减1。计数器为零代表锁已被释放。

显式锁（即Lock）也有ReentrantLock这样的可重入锁

```java
public class Demo1 {

    static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(()->{
            lock.lock();
            try {
                System.out.println("--外层");
                lock.lock();
                try {
                    System.out.println("--内层");
                }finally {

                    lock.unlock();
                }
            }finally {
                lock.unlock();
            }
        },"t1").start();

        new Thread(()->{
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName()+"\t"+"----调用开始");
            }finally {
                lock.unlock();
            }
        },"t2").start();
    }
}
```

运行结果：

```
--外层
--内层
t2	----调用开始
```

注意：lock() 和 unlock() 要一一对应

## 4、LockSupport

### 1、LockSupport是什么

-   LockSupport是用来创建锁和其他同步类的基本线程阻塞原语。
    
-   LockSupport中的park()和 unpark()的作用分别是阻塞线程和解除阻塞线程。
    
-   总之，比wait/notify，await/signal更强。
    

3种让线程等待和唤醒的方法

-   方式1：使用Object中的wait()方法让线程等待，使用object中的notify()方法唤醒线程
-   方式2：使用JUC包中Condition的await()方法让线程等待，使用signal()方法唤醒线程
-   方式3：LockSupport类可以阻塞当前线程以及唤醒指定被阻塞的线程

### 2、waitNotify限制

Object类中的wait和notify方法实现线程等待和唤醒

示例1：

```java
public class WaitNotifyDemo {

	static Object lock = new Object();
	
	public static void main(String[] args) {
		new Thread(()->{
			synchronized (lock) {
				System.out.println(Thread.currentThread().getName()+"---- come in.");
				try {
					lock.wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println(Thread.currentThread().getName()+"--- 换醒.");
		}, "Thread A").start();
		
		new Thread(()->{
			synchronized (lock) {
				lock.notify();
				System.out.println(Thread.currentThread().getName()+"--- 通知.");
			}
		}, "Thread B").start();
	}
}
```

运行结果正常：

```
Thread A come in.
Thread B 通知.
Thread A 换醒.
```

示例2：

```java
public class WaitNotifyDemo {

	static Object lock = new Object();
	
	public static void main(String[] args) {
		new Thread(()->{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (lock) {
				System.out.println(Thread.currentThread().getName()+"---- come in.");
				try {
					lock.wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println(Thread.currentThread().getName()+"--- 换醒.");
		}, "Thread A").start();
		
		new Thread(()->{
			synchronized (lock) {
				lock.notify();
				System.out.println(Thread.currentThread().getName()+"--- 通知.");
			}
		}, "Thread B").start();
	}
}
```

运行结果：

```
Thread B--- 通知.
Thread A---- come in.
```

总结：

0bject类中的wait、notify、notifyAll用于线程等待和唤醒的方法，都必须在synchronized内部执行（必须用到关键字synchronized）

wait和notify方法必须要在同步块或者方法里面且成对出现使用，否则会抛出java.lang.IllegalMonitorStateException。

调用顺序要先wait后notify才OK。

### 3、awaitSignal限制

Condition接口中的await后signal方法实现线程的等待和唤醒，与Object类中的wait和notify方法实现线程等待和唤醒类似。

示例1：

```java
public class ConditionAwaitSignalDemo {
		
	public static void main(String[] args) {
		
		ReentrantLock lock = new ReentrantLock();
		Condition condition = lock.newCondition();
		
		new Thread(()->{
			
			try {
				System.out.println(Thread.currentThread().getName()+" ---come in.");
				lock.lock();
				condition.await();				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
			
			System.out.println(Thread.currentThread().getName()+" ---换醒.");
		},"Thread A").start();
		
		new Thread(()->{
			try {
				lock.lock();
				condition.signal();
				System.out.println(Thread.currentThread().getName()+"--- 通知.");
			}finally {
				lock.unlock();
			}
		},"Thread B").start();
	}
	
}
```

运行结果：

```
Thread A ---come in.
Thread B--- 通知.
Thread A ---换醒.
```

示例2：

```java
public class ConditionAwaitSignalDemo {
		
	public static void main(String[] args) {
		
		ReentrantLock lock = new ReentrantLock();
		Condition condition = lock.newCondition();
		
		new Thread(()->{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				System.out.println(Thread.currentThread().getName()+" ---come in.");
				lock.lock();
				condition.await();				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
			
			System.out.println(Thread.currentThread().getName()+" ---换醒.");
		},"Thread A").start();
		
		new Thread(()->{
			try {
				lock.lock();
				condition.signal();
				System.out.println(Thread.currentThread().getName()+"--- 通知.");
			}finally {
				lock.unlock();
			}
		},"Thread B").start();
	}
	
}
```

运行结果：

```
Thread B--- 通知.
Thread A ---come in.
```

总结：

await和signal方法必须要在同步块或者方法里面且成对出现使用，否则会抛出java.lang.IllegalMonitorStateException。

调用顺序要先await后signal才OK。

**传统的synchronized和Lock实现等待唤醒通知的约束**：线程先要获得并持有锁，必须在锁块(synchronized或lock)中必须要先等待后唤醒，线程才能够被唤醒

### 4、LockSupport方法介绍

LockSupport类中的park等待和unpark唤醒

LockSupport是用来创建锁和其他同步类的基本线程阻塞原语。

LockSupport类使用了一种名为Permit（许可）的概念来做到阻塞和唤醒线程的功能，每个线程都有一个许可（permit），permit只有两个值1和零，默认是零。

可以把许可看成是一种(0.1)信号量（Semaphore），但与Semaphore不同的是，许可的累加上限是1。

阻塞：

-   park()和unpark(object blocker)
-   阻塞当前线程/阻塞传入的具体线程

源码：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/8a3cfec6abdd2f8893f1b57f80a5a8cd.png#pic_center)

唤醒：

-   unpark(Thread thread)
-   唤醒处于阻塞状态的指定线程

源码：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/7320eaa55c5177942754e36fd26742d5.png#pic_center)

示例：

```java
package com.demo.juc.three;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {

	public static void main(String[] args) {
		Thread a = new Thread(()->{
//			try {
//				TimeUnit.SECONDS.sleep(2);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			System.out.println(Thread.currentThread().getName() + " come in. " + System.currentTimeMillis());
			LockSupport.park(); //被阻塞。..等待通知等待放行，它要通过需要许可证
			System.out.println(Thread.currentThread().getName() + " 换醒. " + System.currentTimeMillis());
		}, "Thread A");
		a.start();
		
		Thread b = new Thread(()->{
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			LockSupport.unpark(a);
			System.out.println(Thread.currentThread().getName()+" 通知.");
		}, "Thread B");
		b.start();
	}
	
}

```

运行结果：

```
Thread A come in. 1669000839267
Thread B 通知.
Thread A 换醒. 1669000840271
```

正常 + 无锁块要求。

先前错误的先唤醒后等待顺序，LockSupport可无视这顺序。

**形象的理解**

线程阻塞需要消耗凭证(permit)，这个凭证最多只有1个。

当调用park方法时

-   如果有凭证，则会直接消耗掉这个凭证然后正常退出。
-   如果无凭证，就必须阻塞等待凭证可用。

而unpark则相反，它会增加一个凭证，但凭证最多只能有1个，累加无放。

**面试题**

为什么可以先唤醒线程后阻塞线程？

-   因为unpark获得了一个凭证，之后再调用park方法，就可以名正言顺的凭证消费，故不会阻塞。

为什么唤醒两次后阻塞两次，但最终结果还会阻塞线程？

-   因为凭证的数量最多为1（不能累加），连续调用两次 unpark和调用一次 unpark效果一样，只会增加一个凭证；而调用两次park却需要消费两个凭证，证不够，不能放行。

## 5、AQS

### 1、AQS理论初步

是什么？AbstractQueuedSynchronizer 抽象队列同步器。

源码：

```java
public class ReentrantLock implements Lock, java.io.Serializable {

    abstract static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -5179523762034025860L;
    }
    
    ....

}
```

```
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    
    ...
    
}
```

是用来构建锁或者其它同步器组件的重量级基础框架及整个JUC体系的基石，通过内置的FIFO队列来完成资源获取线程的排队工作，并通过一个int类型变量表示持有锁的状态

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/4c291e18120fc7bf41219fbee59b0a34.png#pic_center)

CLH：Craig、Landin and Hagersten队列，是一个单向链表，AQS中的队列是CLH变体的虚拟双向队列FIFO。

### 2、AQS能干嘛

AQS为什么是JUC内容中最重要的基石？

和AQS有关的

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/be1b96be2811cd5e255e982be38d7d81.png#pic_center)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/2f37645e883e813333a1985f514ea3a4.png#pic_center)

进一步理解锁和同步器的关系

-   **锁：**面向锁的**使用者** - 定义了程序员和锁交互的使用层APl，隐藏了实现细节，你调用即可
-   **同步器：**面向锁的**实现者** - 比如Java并发大神DougLee，提出统一规范并简化了锁的实现，屏蔽了同步状态管理、阻塞线程排队和通知、唤醒机制等。

**能干嘛？**

加锁会导致阻塞 - 有阻塞就需要排队，实现排队必然需要有某种形式的队列来进行管理

**解释说明**

抢到资源的线程直接使用处理业务逻辑，抢不到资源的必然涉及一种排队等候机制。抢占资源失败的线程继续去等待(类似银行业务办理窗口都满了，暂时没有受理窗口的顾客只能去候客区排队等候)，但等候线程仍然保留获取锁的可能且获取锁流程仍在继续(候客区的顾客也在等着叫号，轮到了再去受理窗口办理业务)。

既然说到了排队等候机制，那么就一定会有某种队列形成，这样的队列是什么数据结构呢?

如果共享资源被占用，就需要一定的阻塞等待唤醒机制来保证锁分配。这个机制主要用的是CLH队列的变体实现的，将暂时获取不到锁的线程加入到队列中，这个队列就是AQS的抽象表现。它将请求共享资源的线程封装成队列的结点(Node)，通过CAS、自旋以及LockSupportpark)的方式，维护state变量的状态，使并发达到同步的控制效果。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/f39b3e7f7b6c2f897f134a788896000f.png#pic_center)

### 3、AQS源码体系-上

**官网解释：**

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/f9ad6d9013ee61f6cbf4554bad27f526.png#pic_center)

```
提供一个框架来实现阻塞锁和依赖先进先出（FIFO）等待队列的相关同步器（信号量、事件等）。此类被设计为大多数类型的同步器的有用基础，这些同步器依赖于单个原子“int”值来表示状态。子类必须定义更改此状态的受保护方法，以及定义此状态在获取或释放此对象方面的含义。给定这些，这个类中的其他方法执行所有排队和阻塞机制。子类可以维护其他状态字段，但是只有使用方法getState（）、setState（int）和compareAndSetState（int，int）操作的原子更新的’int’值在同步方面被跟踪。

```

有阻塞就需要排队，实现排队必然需要队列

AQS使用一个volatile的int类型的成员变量来表示同步状态，通过内置的FIFo队列来完成资源获取的排队工作将每条要去抢占资源的线程封装成一个Node，节点来实现锁的分配，通过CAS完成对State值的修改。

源码：

```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {

    private static final long serialVersionUID = 7373984972572414691L;

    protected AbstractQueuedSynchronizer() { }

    static final class Node {
        /** Marker to indicate a node is waiting in shared mode */
        static final Node SHARED = new Node();
        /** Marker to indicate a node is waiting in exclusive mode */
        static final Node EXCLUSIVE = null;

        /** waitStatus value to indicate thread has cancelled */
        static final int CANCELLED =  1;
        /** waitStatus value to indicate successor's thread needs unparking */
        static final int SIGNAL    = -1;
        /** waitStatus value to indicate thread is waiting on condition */
        static final int CONDITION = -2;
        /**
         * waitStatus value to indicate the next acquireShared should
         * unconditionally propagate
         */
        static final int PROPAGATE = -3;

        volatile int waitStatus;


        volatile Node prev;

        volatile Node next;

        /**
         * The thread that enqueued this node.  Initialized on
         * construction and nulled out after use.
         */
        volatile Thread thread;

        
        Node nextWaiter;

        /**
         * Returns true if node is waiting in shared mode.
         */
        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {    // Used to establish initial head or SHARED marker
        }

        Node(Thread thread, Node mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }


    private transient volatile Node head;


    private transient volatile Node tail;


    private volatile int state;


    protected final int getState() {
        return state;
    }


    protected final void setState(int newState) {
        state = newState;
    }
    
    .....
        
}
```

### 4、AQS源码体系-下

内部结构：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/c675ce5347cba307f04047e8bb494b52.png#pic_center)

**AQS自身**

1、AQS的int变量 - AQS的同步状态state成员变量

```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {

    ...

    private volatile int state;
    
    ...
}

```

state成员变量相当于银行办理业务的受理窗口状态。

-   零就是没人，自由状态可以办理
-   大于等于1，有人占用窗口，等着去

2、AQS的CLH队列

-   CLH队列(三个大牛的名字组成)，为一个双向队列
-   银行候客区的等待顾客

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/fcdf73ee4c5f04691915a18b3e552271.png#pic_center)

```
等待队列是“CLH”（Craig、Landin和Hagersten）锁队列的变体。CLH锁通常用于旋转锁。相反，我们使用它们来阻止同步器，但是使用相同的基本策略，即在其节点的前一个线程中保存一些关于该线程的控制信息。每个节点中的“status”字段跟踪线程是否应该阻塞。当一个节点的前一个节点释放时，它会发出信号。否则，队列的每个节点都充当一个特定的通知样式监视器，其中包含一个等待线程。状态字段并不控制线程是否被授予锁等。如果线程是队列中的第一个线程，它可能会尝试获取。但是，第一并不能保证成功，它只会给人争取的权利。因此，当前发布的内容线程可能需要重新等待。
要排队进入CLH锁，您可以将其作为新的尾部进行原子拼接。要出列，只需设置head字段。
```

3、小总结

-   有阻塞就需要排队，实现排队必然需要队列
-   state变量+CLH变种的双端队列

**AbstractQueuedSynchronizer内部类Node**

1、Node的int变量

-   Node的等待状态waitState成员变量
    
    -   ```
        volatile int waitStatus
        ```
    
-   说人话
    
    -   等候区其它顾客(其它线程)的等待状态
    -   队列中每个排队的个体就是一个Node

2、Node类讲解

内部结构

```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    
    ...
    //Node封装每一个的线程
    static final class Node {
        //表示线程以共享的模式等待锁
        /** Marker to indicate a node is waiting in shared mode */
        static final Node SHARED = new Node();
        
        //表示线程正在以独占的方式等待锁
        /** Marker to indicate a node is waiting in exclusive mode */
        static final Node EXCLUSIVE = null;

        //线程被取消了
        /** waitStatus value to indicate thread has cancelled */
        static final int CANCELLED =  1;

        //后继线程需要唤醒
        /** waitStatus value to indicate successor's thread needs unparking */
        static final int SIGNAL    = -1;
        
        //等待condition唤醒
        /** waitStatus value to indicate thread is waiting on condition */
        static final int CONDITION = -2;
        
        //共享式同步状态获取将会无条件地传播下去
        * waitStatus value to indicate the next acquireShared should     
        static final int PROPAGATE = -3;

        //当前节点在队列中的状态（重点）
        //说人话：
        //等候区其它顾客(其它线程)的等待状态
        //队列中每个排队的个体就是一个Node
        //初始为0，状态上面的几种
         * Status field, taking on only the values:
        volatile int waitStatus;

        //前驱节点（重点）
         * Link to predecessor node that current node/thread relies on
        volatile Node prev;

        //后继节点（重点）
         * Link to the successor node that the current node/thread
        volatile Node next;

        //表示处于该节点的线程
         * The thread that enqueued this node.  Initialized on
        volatile Thread thread;

        //指向下一个处于CONDITION状态的节点
         * Link to next node waiting on condition, or the special
        Node nextWaiter;

         * Returns true if node is waiting in shared mode.
        final boolean isShared() {

        //返回前驱节点，没有的话抛出npe
         * Returns previous node, or throws NullPointerException if null.
        final Node predecessor() throws NullPointerException {

        Node() {    // Used to establish initial head or SHARED marker

        Node(Thread thread, Node mode) {     // Used by addWaiter

        Node(Thread thread, int waitStatus) { // Used by Condition
    }
	...
}

```

属性说明：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/55b546719b49ec20b049f816cc1a6281.png#pic_center)

AQS同步队列的基本结构：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/ba332bd4f74c4a639f59642a0ed78ca7.png#pic_center)

## 6、AQS源码解读

### 1、从ReentrantLock开始解读AQS

1、Lock接口的实现类，基本都是通过**聚合**了一个**队列同步器**的子类完成线程访问控制的。

```java
public class ReentrantLock implements Lock, java.io.Serializable {
    private static final long serialVersionUID = 7373984872572414699L;

    private final Sync sync;

    abstract static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -5179523762034025860L;
        abstract void lock();

        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }

        protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }

        protected final boolean isHeldExclusively() {
            // While we must in general read state before owner,
            // we don't need to do so to check if current thread is owner
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        // Methods relayed from outer class

        final Thread getOwner() {
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }

        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }

        final boolean isLocked() {
            return getState() != 0;
        }

        /**
         * Reconstitutes the instance from a stream (that is, deserializes it).
         */
        private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }
    }
    
    .....
        
}
```

2、ReentrantLock基本原理

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/9866d43dbd117e784c4e65fff49c0313.png#pic_center)

3、从最简单的lock方法开始看看公平和非公平

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/87d4dc624e99e5390b9f60c93d73253a.png#pic_center)

可以明显看出公平锁与非公平锁的lock()方法唯一的区别就在于公平锁在获取同步状态时多了一个限制条件：hasQueuedPredecessors() hasQueuedPredecessors是公平锁加锁时判断等待队列中是否存在有效节点的方法

对比公平锁和非公平锁的tyAcquire()方法的实现代码，其实差别就在于非公平锁获取锁时比公平锁中少了一个判断!hasQueuedPredecessors()

hasQueuedPredecessors()中判断了是否需要排队，导致公平锁和非公平锁的差异如下：

**公平锁**：公平锁讲究先来先到，线程在获取锁时，如果这个锁的等待队列中已经有线程在等待，那么当前线程就会进入等待队列中;

**非公平锁**：不管是否有等待队列，如果可以获取锁，则立刻占有锁对象。也就是说队列的第一个排队线程在unpark()，之后还是需要竞争锁（存在线程竞争的情况下)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/77074bba35027a84c5021817fe2df676.png#pic_center)

```undefined
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    
    ......
    
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
}
```

整个ReentrantLock 的加锁过程，可以分为三个阶段：

1.  尝试加锁；
2.  加锁失败，线程入队列；
3.  线程入队列后，进入阻赛状态。

### 2、AQS源码深度解读02

以非公平锁为例

主要方法：

```
lock()->compareAndSetState(0, 1)
acquire(1)->tryAcquire(arg)->nonfairTryAcquire(int acquires)
addWaiter(Node.EXCLUSIVE)->enq(node)->acquireQueued(addWaiter(Node.EXCLUSIVE), arg)
shouldParkAfterFailedAcquire(p, node)->parkAndCheckInterrupt()
```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/7519eb6e0822e372e56b8035bb0dc410.png#pic_center)

**示例：**

```java
package com.demo.juc.three;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: jxm
 * @Description:
 * @Date: 2022/11/21 14:13
 * @Version: 1.0
 */
public class AQSDemo {

    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock(true);

        /**
         * 带入一个银行办理业务的案例来模拟我们的AQs 如何进行线程的管理和通知唤醒机制
         * 3个线程模拟3个来银行网点，受理窗口办理业务的顾客
         */

        //A顾客就是第一个顾客，此时受理窗口没有任何人，A可以直接去办理
        new Thread(()->{
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " come in -------");
                try {
                    TimeUnit.SECONDS.sleep(20);//模拟办理业务时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }finally {
                lock.unlock();
            }
        },"线程 A").start();


        //第2个顾客，第2个线程---->，由于受理业务的窗口只有一个(只能一个线程持有锁)，此时B只能等待，
        //进入候客区
        new Thread(()->{
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " come in -------");
            }finally {
               lock.unlock();
            }

        },"线程 B").start();


        //第3个顾客，第3个线程---->，由于受理业务的窗口只有一个(只能一个线程持有锁)，此时C只能等待，
        //进入候客区
        new Thread(()->{
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " come in -------");
            }finally {
                lock.unlock();
            }
        },"线程 C").start();
    }
}

```

#### 线程A

**lock()加锁**

程序初始状态理解图

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/c9b44c42b95ad8330f1aa8084bb7162c.png#pic_center)

启动程序：线程a加锁

```java
public void lock() {
    sync.lock();
}
```

```java
static final class NonfairSync extends Sync {
    private static final long serialVersionUID = 7316153563782823691L;

    /**
     * Performs lock.  Try immediate barge, backing up to normal
     * acquire on failure.
     */
    final void lock() {
        if (compareAndSetState(0, 1)) //CAS，线程a进来，最开始银行业务窗口没有人办理业务，status=0,返回true
            setExclusiveOwnerThread(Thread.currentThread());//设置独占的所有者线程，显然一开始是线程A
        else
            acquire(1);
    }

    protected final boolean tryAcquire(int acquires) {
        return nonfairTryAcquire(acquires);
    }
}
```

线程A开始办业务了，此时程序状态理解图：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/a518d5dc43faf024e79808af260d4c34.png#pic_center)

#### 线程B

```java
static final class NonfairSync extends Sync {
    private static final long serialVersionUID = 7316153563782823691L;

    final void lock() {
        if (compareAndSetState(0, 1)) 
            setExclusiveOwnerThread(Thread.currentThread());
        else
            acquire(1); //线程B走这里
    }

    protected final boolean tryAcquire(int acquires) {
        return nonfairTryAcquire(acquires);
    }
}
```

```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    
    ......
    
    public final void acquire(int arg) {
    if (!tryAcquire(arg) &&  //第一步,正常情况tryAcquire(arg)返回false
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
    }
    
    ......
}
```

**tryAcquire(arg)方法：**

```java
static final class NonfairSync extends Sync {
        private static final long serialVersionUID = 7316153563782823691L;

        /**
         * Performs lock.  Try immediate barge, backing up to normal
         * acquire on failure.
         */
        final void lock() {
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }
		
    	//第二步
        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
   }
```

```java
public class ReentrantLock implements Lock, java.io.Serializable {
    
    ...
    
    abstract static class Sync extends AbstractQueuedSynchronizer {
            private static final long serialVersionUID = -5179523762034025860L;

            abstract void lock();
			
        	//第三步
            final boolean nonfairTryAcquire(int acquires) {
                final Thread current = Thread.currentThread(); //此时是线程B
                int c = getState(); //线程A正在进行业务，c=1 ;
                if (c == 0) { //极端情况下，线程A办完业务，离开业务窗口，此时c=0;
                    if (compareAndSetState(0, acquires)) {
                        setExclusiveOwnerThread(current);
                        return true;
                    }
                }
                else if (current == getExclusiveOwnerThread()) { //getExclusiveOwnerThread()是线程A;如果线程A = 线程A
                    int nextc = c + acquires; //1+1 ; nextc = 2
                    if (nextc < 0) // overflow
                        throw new Error("Maximum lock count exceeded");
                    setState(nextc);
                    return true;
                }
                return false;
            }
        
        ...
    }
    
    ....
}
```

**addWaiter(Node.EXCLUSIVE):**

```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    
    ......
    
    public final void acquire(int arg) {
    if (!tryAcquire(arg) &&  //正常情况tryAcquire(arg)返回false,取反则为true,走addWaiter(Node.EXCLUSIVE) 方法
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) //Node.EXCLUSIVE表示排他的，值为null
        selfInterrupt();
    }
    
    //static final Node EXCLUSIVE = null;
}
```

```java
public class ReentrantLock implements Lock, java.io.Serializable {
    
    ...

    private Node addWaiter(Node mode) {
            Node node = new Node(Thread.currentThread(), mode); //此时是线程B
            // Try the fast path of enq; backup to full enq on failure
            Node pred = tail; //此时队列没有任何线程，tail为null
            if (pred != null) {
                node.prev = pred;
                if (compareAndSetTail(pred, node)) {
                    pred.next = node;
                    return node;
                }
            }
            enq(node);//B线程走这里，进入队列
            return node;
  }
  ....
}
```

```java
private Node enq(final Node node) { //node 为线程B
        for (;;) {
            Node t = tail;
            if (t == null) { // Must initialize 初始化
                if (compareAndSetHead(new Node())) //设置头结点
                    tail = head;
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }
```

此时程序里理解图：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/15745ff5ed778f224efd724b3ff6a2a9.png#pic_center)

```java
private Node enq(final Node node) { //node 为B节点
        for (;;) {
            Node t = tail; //此时尾结点tail为哨兵节点，不为null
            if (t == null) {
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {
                node.prev = t; //B节点的前指针指向哨兵节点
                if (compareAndSetTail(t, node)) { //设置尾结点；设置节点B为尾结点
                    t.next = node; //哨兵节点的下一个节点为B节点
                    return t;
                }
            }
        }
   }
```

线程B已经进入队列，此次程序理解图：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/9558509f1f3c3faffa6492898646c3d3.png#pic_center)

双向链表中，第一个节点为虚节点(也叫哨兵节点)，其实并不存储任何信息，只是占位。真正的第一个有数据的节点，是从第二个节点开始的。

#### 线程C

```java
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&  //此时是线程C ,正常情况tryAcquire(arg)返回false,取反则为true,走addWaiter(Node.EXCLUSIVE) 方法
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) //Node.EXCLUSIVE表示排他的，值为null
        selfInterrupt();
    }
```

```java
public class ReentrantLock implements Lock, java.io.Serializable {

    private Node addWaiter(Node mode) {
            Node node = new Node(Thread.currentThread(), mode); //此时是线程C，node为节点C
            Node pred = tail; //此时尾结点为节点B
            if (pred != null) {
                node.prev = pred; //C节点的前一个节点为B节点
                if (compareAndSetTail(pred, node)) { //比较并交换，设置C节点为尾指针（节点）
                    pred.next = node; //B节点的下一个几点指向节点C
                    return node;//返回节点C
                }
            }
            enq(node);//
            return node;
  }
  ....
}
```

此时程序理解图

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/b44d033b5ab20aa4c6beb0cc98f726b1.png#pic_center)

#### acquireQueued() 方法

```java
//获取排队的
final boolean acquireQueued(final Node node, int arg) { //根据队列先进先出特性，此时node节点为B节点
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();//p为哨兵节点
                if (p == head && tryAcquire(arg)) { //继续尝试抢占
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) && 
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed) //如果为true
                cancelAcquire(node); //则取消排队，有些线性等着不想等了，自动取消（极端情况）
        }
    }
```

```java
final Node predecessor() throws NullPointerException {
    Node p = prev; //B节点的前一个节点为哨兵节点
    if (p == null)
        throw new NullPointerException();
    else
        return p;
}
```

```java
//检查并更新未能获取的节点的状态。如果线程应阻塞，则返回true。这是所有采集回路中的主要信号控制。需要pred==node.prev。
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {//此时pred为哨兵节点，node为B节点
        int ws = pred.waitStatus; //此时ws=0
        if (ws == Node.SIGNAL) //Node.SIGNAL = -1
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             */
            return true;
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             */
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE.  Indicate that we
             * need a signal, but don't park yet.  Caller will need to
             * retry to make sure it cannot acquire before parking.
             */
            //Node.SIGNAL = -1
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);//CAS,哨兵节点的 ws = -1
        }
        return false;
    }
```

```java
//获取排队的
final boolean acquireQueued(final Node node, int arg) { //根据队列先进先出特性，此时node节点为B节点
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();//p为哨兵节点
                if (p == head && tryAcquire(arg)) { //继续尝试抢占
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) && //此时shouldParkAfterFailedAcquire(p, node)返回false
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node); 
        }
    }
```

```java
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {//此时pred为哨兵节点，node为B节点
        int ws = pred.waitStatus; //此时ws=-1
        if (ws == Node.SIGNAL) //Node.SIGNAL = -1
            return true;
        if (ws > 0) {
            
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }
```

```java
final boolean acquireQueued(final Node node, int arg) { //根据队列先进先出特性，此时node节点为B节点
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();//p为哨兵节点
                if (p == head && tryAcquire(arg)) { //继续尝试抢占
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) && //此时shouldParkAfterFailedAcquire(p, node)返回true
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node); 
        }
    }
```

parkAndCheckInterrupt()

```java
private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this); //此时节点B别挂起，被阻塞（正在排队等待中），表示正真的进入队列
        return Thread.interrupted();
    }
```

图中的哨兵节点的waitStatus由0变为-1（Node.SIGNAL）。此时程序理解图：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/6790373a7a945efd4694f08a5e174740.png#pic_center)

同理：线程C也一样挂起，被阻塞

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/2606312cc17e5ad7f46e47c84fa864d5.png#pic_center)

#### ReentrantLock.unLock()

接下来讨论ReentrantLock.unLock()方法。假设线程A工作结束，调用unLock()，释放锁占用。

```java
new Thread(()->{
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " come in -------");
                try {
                    TimeUnit.SECONDS.sleep(20);//模拟办理业务时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }finally {
                lock.unlock();
            }
        },"线程 A").start();
```

```java
public class ReentrantLock implements Lock, java.io.Serializable { 
    ...
    public void unlock() {
            sync.release(1);
        }
    ...
}
```

```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    
    public final boolean release(int arg) {
        if (tryRelease(arg)) { //走 tryRelease(arg)方法
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
	}
    
}

```

tryRelease方法：

```java
public class ReentrantLock implements Lock, java.io.Serializable { 
    
    .....
    
    abstract static class Sync extends AbstractQueuedSynchronizer {

        ....

        protected final boolean tryRelease(int releases) {
            int c = getState() - releases; //此时c=1-1=0
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);//设置独占所有者线程为null
            }
            setState(c); //设置status为0
            return free;
        }

        ....
    }
    
    ....
}
```

此时程序理解图：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/877e7ec11369992d03ffc4b625ec549b.png#pic_center)

```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    
    public final boolean release(int arg) {
        if (tryRelease(arg)) { //返回true
            Node h = head; //此时头结点（h）为哨兵节点，waitStatus = -1
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h); //走 unparkSuccessor
            return true;
        }
        return false;
	}
    
}

```

```java
private void unparkSuccessor(Node node) { //node为哨兵节点

    int ws = node.waitStatus;
    if (ws < 0)
        compareAndSetWaitStatus(node, ws, 0); //比较并交换，设置waitStatus为0

    Node s = node.next; //s 为B节点
    if (s == null || s.waitStatus > 0) {
        s = null;
        for (Node t = tail; t != null && t != node; t = t.prev)
            if (t.waitStatus <= 0)
                s = t;
    }
    if (s != null)
        LockSupport.unpark(s.thread); //解锁B节点，被唤醒
}
```

```java
private final boolean parkAndCheckInterrupt() {
    LockSupport.park(this); //刚才被阻塞的B线程被释放
    return Thread.interrupted(); //线程没有被中断过，默认返回false
}
```

```java
final boolean acquireQueued(final Node node, int arg) { //此时node结点为B节点
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
            final Node p = node.predecessor(); //p为哨兵节点
            if (p == head && tryAcquire(arg)) { //走tryAcquire(arg)
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return interrupted;
            }
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```

此时程序理解图：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/0e308a4db8659ce6a4ca1a270b1a8168.png#pic_center)

```java
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread(); //此时是B线程
    int c = getState(); //c=0
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true; //最终返回true
        }
    }
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```

```java
final boolean acquireQueued(final Node node, int arg) { //此时node结点为B节点
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
            final Node p = node.predecessor(); //p为哨兵节点
            if (p == head && tryAcquire(arg)) { //tryAcquire(arg)返回true
                setHead(node); 
                p.next = null; // help GC
                failed = false;
                return interrupted;
            }
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```

```java
private void setHead(Node node) { //node节点为B
    head = node; //头结点指向B节点
    node.thread = null;
    node.prev = null;
}
```

此时程序理解图

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/03dc0e70c535e47db52ccb69822197bc.png#pic_center)

```java
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) //最终acquireQueued(addWaiter(Node.EXCLUSIVE), arg) 返回false
        selfInterrupt();
}
```

## 7、Spring

### 1、Spring的AOP顺序

AOP常用注解：

-   @Before 前置通知：目标方法之前执行
    
-   @After 后置通知：目标方法之后执行（始终执行)
    
-   @AfterReturning 返回后通知：执行方法结束前执行(异常不执行)
    
-   @AfterThrowing 异常通知：出现异常时候执行
    
-   @Around 环绕通知：环绕目标方法执行
    

面试题：你肯定知道spring，那说说aop的全部通知顺序springboot或springboot2对aop的执行顺序影响？说说你使用AOP中碰到的坑?

#### 1、spring4测试

案例：

pom.xml文件

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.9.RELEASE</version>
        <!-- 1.5.9.RELEASE -->
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <groupId>com.lun</groupId>
    <artifactId>HelloSpringBoot</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>HelloSpringBoot</name>
    <url>http://maven.apache.org</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>

        <!-- <version>1.5.9.RELEASE</version〉解决方案  start -->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-core</artifactId>
            <version>1.1.3</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-access</artifactId>
            <version>1.1.3</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.1.3</version>
        </dependency>

        <!-- <version>1.5.9.RELEASE</version〉解决方案   end -->

        <!-- web+actuator -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- SpringBoot与Redis整合依赖 -->
        <!--
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
         -->

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>

        <!-- jedis -->
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>3.1.0</version>
        </dependency>

        <!-- Spring Boot AOP技术-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

        <!-- redisson -->
        <dependency>
            <groupId>org.redisson</groupId>
            <artifactId>redisson</artifactId>
            <version>3.13.4</version>
        </dependency>

        <!-- 一般通用基础配置 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId><scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```

业务类：

```java
public interface CalcService {
	
	int div(int x, int y);
}
```

```
@Service
public class CalcServiceImpl implements CalcService {

    @Override
    public int div(int x, int y) {
        int result = x / y;
        System.out.println("===>CalcServiceImpl被调用，计算结果为：" + result);
        return result;
    }
}
```

新建一个切面类MyAspect并为切面类新增两个注解：

-   @Aspect 指定一个类为切面类
-   @Component 纳入Spring容器管理

```java
@Aspect
@Component
public class MyAspect {
    @Before("execution(public int sguigu.threee.service.impl.CalcServiceImpl.*(..))")
    public void beforeNotify() {
        System.out.println("********@Before我是前置通知");
    }

    @After("execution(public int sguigu.threee.service.impl.CalcServiceImpl.*(..))")
    public void afterNotify() {
        System.out.println("********@After我是后置通知");
    }

    @AfterReturning("execution(public int sguigu.threee.service.impl.CalcServiceImpl.*(..))")
    public void afterReturningNotify() {
        System.out.println("********@AfterReturning我是返回后通知");
    }

    @AfterThrowing(" execution(public int sguigu.threee.service.impl.CalcServiceImpl.*(..))")
    public void afterThrowingNotify() {
        System.out.println("********@AfterThrowing我是异常通知");
    }

    @Around(" execution(public int sguigu.threee.service.impl.CalcServiceImpl.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object retvalue = null;
        System.out.println("我是环绕通知之前AAA");
        retvalue = proceedingJoinPoint.proceed();
        System.out.println("我是环绕通知之后BBB");
        return retvalue ;
    }
}
```

测试：

```java
package sguigu.threee;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;
import org.springframework.test.context.junit4.SpringRunner;
import sguigu.threee.service.CalcService;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AopTest {

    @Resource
    private CalcService calcService;

    @Test
    public void testAop4() {
        System.out.println("Spring Version :"+SpringVersion.getVersion()+" ,Spring Boot Version :"+
                 SpringBootVersion.getVersion());

        calcService.div(10, 2);//正常情况
    }

}

```

正常情況运行结果：

```java
Spring Version :4.3.13.RELEASE ,Spring Boot Version :1.5.9.RELEASE
我是环绕通知之前AAA
********@Before我是前置通知
===>CalcServiceImpl被调用，计算结果为：5
我是环绕通知之后BBB
********@After我是后置通知
********@AfterReturning我是返回后通知
```

测试发生异常

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class AopTest {

    @Resource
    private CalcService calcService;

    @Test
    public void testAop4() {
        System.out.println("Spring Version :"+SpringVersion.getVersion()+" ,Spring Boot Version :"+
                 SpringBootVersion.getVersion());

        calcService.div(10, 0);//异常情况
    }

}
```

异常情況运行结果：

```java
Spring Version :4.3.13.RELEASE ,Spring Boot Version :1.5.9.RELEASE
我是环绕通知之前AAA
********@Before我是前置通知
********@After我是后置通知
********@AfterThrowing我是异常通知

java.lang.ArithmeticException: / by zero

	at sguigu.threee.service.impl.CalcServiceImpl.div(CalcServiceImpl.java:17)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
```

**小结**

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/ea874452c4475e2d3ec4aa549ddb63e4.png#pic_center)

AOP执行顺序：

-   正常情况下：@Before前置通知----->@After后置通知----->@AfterRunning正常返回
-   异常情况下：@Before前置通知----->@After后置通知----->@AfterThrowing方法异常

#### 2、spring5测试

pom.xml文件

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.4.7</version>
        <!-- 1.5.9.RELEASE -->
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <groupId>com.lun</groupId>
    <artifactId>HelloSpringBoot</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>HelloSpringBoot</name>
    <url>http://maven.apache.org</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>

        <!-- SpringBoot与Redis整合依赖 -->
        <!--
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
         -->

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>

        <!-- jedis -->
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>3.1.0</version>
        </dependency>

        <!-- Spring Boot AOP技术-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

        <!-- redisson -->
        <dependency>
            <groupId>org.redisson</groupId>
            <artifactId>redisson</artifactId>
            <version>3.13.4</version>
        </dependency>

        <!-- 一般通用基础配置 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId><scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```

测试

```java
package sguigu.threee;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;
import org.springframework.test.context.junit4.SpringRunner;
import sguigu.threee.service.CalcService;

import javax.annotation.Resource;

/**
 * @Author: jxm
 * @Description:
 * @Date: 2022/11/23 16:20
 * @Version: 1.0
 */
@SpringBootTest
public class AopTest {

    @Resource
    private CalcService calcService;

    @Test
    public void testAop4() {
        System.out.println("Spring Version :"+SpringVersion.getVersion()+" ,Spring Boot Version :"+
                 SpringBootVersion.getVersion());

        calcService.div(10, 0);
    }

    @Test
    public void testAop5() {
        System.out.println("Spring Version :"+SpringVersion.getVersion()+" ,Spring Boot Version :"+
                SpringBootVersion.getVersion());

        calcService.div(10, 2);
    }

}
```

正常测试结果

```java
Spring Version :5.3.8 ,Spring Boot Version :2.4.7
我是环绕通知之前AAA
********@Before我是前置通知
===>CalcServiceImpl被调用，计算结果为：5
********@AfterReturning我是返回后通知
********@After我是后置通知
我是环绕通知之后BBB
```

测试异常结果：

```
@Test
    public void testAop5() {
        System.out.println("Spring Version :"+SpringVersion.getVersion()+" ,Spring Boot Version :"+
                SpringBootVersion.getVersion());

        calcService.div(10, 0);
    }

```

```java
Spring Version :5.3.8 ,Spring Boot Version :2.4.7
我是环绕通知之前AAA
********@Before我是前置通知
********@AfterThrowing我是异常通知
********@After我是后置通知

java.lang.ArithmeticException: / by zero

```

总结：

-   环绕通知：包裹其他的通知
    
-   @After后置通知 在 返回后通知 后面（与spring4不一样），类似捕捉异常在finally
    
    ```java
    try {
    
    }finally {
    	@After
    }
    ```
    

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/3e7589dbddfbef767571c075f40db8ef.png#pic_center)

### 2、Spring循环依赖

面试题：

-   你解释下spring中的三级缓存？
-   三级缓存分别是什么？三个Map有什么异同？
-   什么是循环依赖？请你谈谈？看过spring源码吗？
-   如何检测是否存在循环依赖？实际开发中见过循环依赖的异常吗？
-   多例的情况下，循环依赖问题为什么无法解决？

#### 1.什么是循环依赖？

多个bean之间相互依赖，形成了一个闭环。比如：A依赖于B、B依赖于C、C依赖于A。

通常来说，如果问Spring容器内部如何解决循环依赖，一定是指默认的单例Bean中，属性互相引用的场景。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/2d85034f362a64227ef4990bc16652bc.png#pic_center)

#### 2.两种注入方式对循环依赖的影响

[循环依赖官网说明](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-dependency-resolution)

```
If you use predominantly constructor injection, it is possible to create an unresolvable circular dependency scenario.

For example: Class A requires an instance of class B through constructor injection, and class B requires an instance of class A through constructor injection. If you configure beans for classes A and B to be injected into each other, the Spring IoC container detects this circular reference at runtime, and throws a BeanCurrentlyInCreationException.

One possible solution is to edit the source code of some classes to be configured by setters rather than constructors. Alternatively, avoid constructor injection and use setter injection only. In other words, although it is not recommended, you can configure circular dependencies with setter injection.

Unlike the typical case (with no circular dependencies), a circular dependency between bean A and bean B forces one of the beans to be injected into the other prior to being fully initialized itself (a classic chicken-and-egg scenario).

如果主要使用构造函数注入，则有可能创建一个无法解决的循环依赖场景。

例如：类A通过构造函数注入需要类B的实例，而类B通过构造函数注入要求类A的实例。如果将类A和B的bean配置为相互注入，Spring IoC容器将在运行时检测到该循环引用，并抛出 BeanCurrentlyInCreationException。

一种可能的解决方案是编辑某些类的源代码，以便由setter而不是构造函数进行配置。或者，避免构造函数注入，只使用setter注入。换句话说，虽然不建议使用setter注入，但您可以配置循环依赖关系。

与典型的情况（没有循环依赖关系）不同，bean a和bean B之间的循环依赖关系迫使其中一个bean在完全初始化之前注入另一个bean（典型的鸡和蛋场景）。
```

**结论**

我们AB循环依赖问题只要A的注入方式是setter且singleton ，就不会有循环依赖问题。

#### 3\. javaEE代码验证案例

循环依赖现象在spring容器中注入依赖的对象，有2种情况

-   构造器方式注入依赖（不可行）
-   以set方式注入依赖（可行）

**1、构造器方式注入依赖（不可行）**

```java
package com.guigu.service;

import org.springframework.stereotype.Component;

@Component
public class ServiceA{
    
    private ServiceB serviceB;
    
    public ServiceA(ServiceB serviceB){
        this.serviceB = serviceB;
    }

}
```

```java
package com.guigu.service;

import org.springframework.stereotype.Component;

@Component
public class ServiceB{
    private ServiceA serviceA;
    
    public ServiceB(ServiceA serviceA){
        this.serviceA = serviceA;
    }
}

```

```java
public class ClientConstructor{

    public static void main(String[] args){
        //new ServiceA(new ServiceB(new ServiceA()));//这会抛出编译异常
    }
}

```

**2、以set方式注入依赖（可行）**

```java
package com.guigu.service;

import org.springframework.stereotype.Component;

@Component
public class ServiceA{
    private ServiceB serviceB;
    
//    public ServiceA(ServiceB serviceB){
//        this.serviceB = serviceB;
//    }

    public void setServiceB(ServiceB serviceB) {
        this.serviceB = serviceB;
        System.out.println("A里面注入了B");
    }
}

```

```java
package com.guigu.service;

import org.springframework.stereotype.Component;

@Component
public class ServiceB{
    private ServiceA serviceA;
    
//    public ServiceB(ServiceA serviceA){
//        this.serviceA = serviceA;
//    }

    public void setServiceA(ServiceA serviceA) {
        this.serviceA = serviceA;
        System.out.println("B里面注入A");
    }
}
```

```java
package com.guigu.test;

import com.guigu.service.ServiceA;
import com.guigu.service.ServiceB;

public class ClientSet{
    public static void main(String[] args){
        //创建serviceAA
        ServiceA a = new ServiceA();
        //创建serviceBB
        ServiceB b = new ServiceB();
        //将serviceA入到serviceB中
        b.setServiceA(a);
        //将serviceB法入到serviceA中
        a.setServiceB(b);
    }
}
```

运行结果：

```
B里面注入A
A里面注入了B
```

#### 4、spring代码演示

**创建spring项目**

pom.xml文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springboot-demo</artifactId>
        <groupId>com.foundbyte.demo</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring-test</artifactId>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.5.RELEASE</version>
        </dependency>
    </dependencies>

</project>
```

xml文件

默认的单例(Singleton)的场景是**支持**循环依赖的，不报错

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context-4.0.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx-4.0.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop-4.0.xsd">

    <bean id="a" class="com.guigu.entity.A">
        <property name="b" ref="b"/>
    </bean>
    <bean id="b" class="com.guigu.entity.B">
        <property name="a" ref="a"/>
    </bean>

</beans>
```

创建两个beans：A，B

```java
package com.guigu.entity;

public class A {

	public A(){
		System.out.println("-- created A--");
	}

	private B b;

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}
}
```

```java
package com.guigu.entity;

public class B {

   private A a;

   public B(){
      System.out.println("-- created B--");
   }

   public A getA() {
      return a;
   }

   public void setA(A a) {
      this.a = a;
   }
}
```

测试

```java
package com.guigu.test;

import com.guigu.entity.A;
import com.guigu.entity.B;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientSpringContainer {

   public static void main(String[] args) {
      ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
      A a = context.getBean("a", A.class);
      B b = context.getBean("b", B.class);
   }
}
```

运行结果 正常

```
-- created A--
-- created B--
```

原型(Prototype)的场景是**不支持**循环依赖的，会报错

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context-4.0.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx-4.0.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop-4.0.xsd">

    <bean id="a" class="com.guigu.entity.A" scope="prototype">
        <property name="b" ref="b"></property>
    </bean>
    <bean id="b" class="com.guigu.entity.B" scope="prototype">
        <property name="a" ref="a"></property>
    </bean>

</beans>

```

运行结果异常 BeanCurrentlyInCreationException：

```
Caused by: org.springframework.beans.factory.BeanCurrentlyInCreationException: Error creating bean with name 'a': Requested bean is currently in creation: Is there an unresolvable circular reference?
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:264)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)
	at org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefinitionValueResolver.java:367)
	... 17 more
```

#### 5、重要结论：spring内部通过3级缓存来解决循环依赖

第一级缓存（也叫单例池）singletonObjects：存放已经经历了完整生命周期的Bean对象。

第二级缓存：earlySingletonObjects，存放早期暴露出来的Bean对象，Bean的生命周期未结束（属性还未填充完）。

第三级缓存：Map<String, ObjectFactory<?>> singletonFactories，存放可以生成Bean的工厂。

重要结论 ：只有单例的bean会通过三级缓存提前暴露来解决循环依赖的问题，而非单例的bean，每次从容器中获取都是一个新的对象，都会重新创建，所以非单例的bean是没有缓存的，不会将其放到三级缓存中。

**DefaultSingletonBeanRegistry** 源码

```java
public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {

	...

	/** Cache of singleton objects: bean name to bean instance. */ //一级缓存
	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

	/** Cache of singleton factories: bean name to ObjectFactory. */ //三级缓存
	private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

	/** Cache of early singleton objects: bean name to bean instance. */ //二级缓存
	private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);
 
    ...
    
}

```

### 3、Spring循环依赖debug源码

#### 1.前置知识

实例化 - 内存中申请一块内存空间，如同租赁好房子，自己的家当还未搬来。

初始化属性填充 - 完成属性的各种赋值，如同装修，家具，家电进场。

**1、3个Map和四大方法，总体相关对象**

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/f7aea9bbf4dd1501f97ac780cba4adcd.png#pic_center)

第一层singletonObjects存放的是已经初始化好了的Bean,

第二层earlySingletonObjects存放的是实例化了，但是未初始化的Bean,

第三层singletonFactories存放的是FactoryBean。假如A类实现了FactoryBean,那么依赖注入的时候不是A类，而是A类产生的Bean

```java
package org.springframework.beans.factory.support;

...

public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {

	...

	/** 
	单例对象的缓存:bean名称—bean实例，即:所谓的单例池。
	表示已经经历了完整生命周期的Bean对象
	第一级缓存
	*/
	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

	/**
	早期的单例对象的高速缓存: bean名称—bean实例。
	表示 Bean的生命周期还没走完（Bean的属性还未填充）就把这个 Bean存入该缓存中
	也就是实例化但未初始化的 bean放入该缓存里
	第二级缓存
	*/
	private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

	/**
	单例工厂的高速缓存:bean名称—ObjectFactory
	表示存放生成 bean的工厂
	第三级缓存
	*/
	private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
 
    ...
}
```

```java
@FunctionalInterface
public interface ObjectFactory<T> {

	T getObject() throws BeansException;

}
```

**2、A / B两对象在三级缓存中的迁移说明**

1.  A创建过程中需要B，于是A将自己放到三级缓里面，去实例化B。
2.  B实例化的时候发现需要A，于是B先查一级缓存，没有，再查二级缓存，还是没有，再查三级缓存，找到了A然后把三级缓存里面的这个A放到二级缓存里面，并删除三级缓存里面的A。
3.  B顺利初始化完毕，将自己放到一级缓存里面（此时B里面的A依然是创建中状态)，然后回来接着创建A，此时B已经创建结束，直接从一级缓存里面拿到B，然后完成创建，并将A自己放到一级缓存里面。

###　２.DEBUG源码

dubug流程略　 详 [视频](https://www.bilibili.com/video/BV1Hy4y1B78T?p=36&vd_source=35daf6b9ab0bbe1cad537b7be24ee545)

Spring循环依赖debug源码图过程图

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/0775c2b45b80ab362703bbdc1beb8a72.png#pic_center)

自己dubug断点

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/f72e8052a149bc07b2f37c6452347202.png#pic_center)

#### 3.**再次**A / B两对象在三级缓存中的迁移说明

1.  A创建过程中需要B，于是A将自己放到三级缓里面，去实例化B。
2.  B实例化的时候发现需要A，于是B先查一级缓存，没有，再查二级缓存，还是没有，再查三级缓存，找到了A然后把三级缓存里面的这个A放到二级缓存里面，并删除三级缓存里面的A。
3.  B顺利初始化完毕，将自己放到一级缓存里面（此时B里面的A依然是创建中状态)，然后回来接着创建A，此时B已经创建结束，直接从一级缓存里面拿到B，然后完成创建，并将A自己放到一级缓存里面。

#### ４.小总结

Spring创建 bean主要分为两个步骤，创建原始bean对象，接着去填充对象属性和初始化

每次创建 bean之前，我们都会从缓存中查下有没有该bean，因为是单例，只能有一个

当我们创建 beanA的原始对象后，并把它放到三级缓存中，接下来就该填充对象属性了，这时候发现依赖了beanB，接着就又去创建beanB，同样的流程，创建完beanB填充属性时又发现它依赖了beanA又是同样的流程，

不同的是：这时候可以在三级缓存中查到刚放进去的原始对象beanA.所以不需要继续创建，用它注入 beanB，完成 beanB的创建

既然 beanB创建好了，所以 beanA就可以完成填充属性的步骤了，接着执行剩下的逻辑，闭环完成

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/0ed33bf66cbf9fee691f2ba65a42468b.png#pic_center)

Spring解决循环依赖依靠的是Bean的"中间态"这个概念，而这个中间态指的是**已经实例化但还没初始化的状态—>半成债**。实例化的过程又是通过构造器创建的，如果A还没创建好出来怎么可能提前曝光，所以构造器的循环依赖无法解决。

Spring为了解决单例的循坏依赖问题，使用了三级缓存：

其中一级缓存为单例池(singletonObjects)。

二级缓存为提前曝光对象(earlySingletonObjects)。

三级级存为提前曝光对象工厂(singletonFactories) 。

假设A、B循环引用，实例化A的时候就将其放入三级缓存中，接着填充属性的时候，发现依赖了B，同样的流程也是实例化后放入三级缓存，接着去填充属性时又发现自己依赖A，这时候从缓存中查找到早期暴露的A，没有AOP代理的话，直接将A的原始对象注入B，完成B的初始化后，进行属性填充和初始化，这时候B完成后，就去完成剩下的A的步骤，如果有AOP代理，就进行AOP处理获取代理后的对象A，注入B，走剩下的流程。

#### 5\. Spring解决循环依赖过程：

1.  调用doGetBean()方法，想要获取beanA，于是调用getSingleton()方法从缓存中查找beanA
2.  在getSingleton()方法中，从一级缓存中查找，没有，返回null
3.  doGetBean()方法中获取到的beanA为null，于是走对应的处理逻辑，调用getSingleton()的重载方法（参数为ObjectFactory的)
4.  在getSingleton()方法中，先将beanA\_name添加到一个集合中，用于标记该bean正在创建中。然后回调匿名内部类的creatBean方法
5.  进入AbstractAutowireCapableBeanFactory#ndoCreateBean，先反射调用构造器创建出beanA的实例，然后判断:是否为单例、是否允许提前暴露引用(对于单例一般为true)、是否正在创建中（即是否在第四步的集合中）。判断为true则将beanA添加到【三级缓存】中
6.  对beanA进行属性填充，此时检测到beanA依赖于beanB，于是开始查找beanB
7.  调用doGetBean()方法，和上面beanA的过程一样，到缓存中查找beanB，没有则创建，然后给beanB填充属性
8.  此时 beanB依赖于beanA，调用getSingleton()获取beanA，依次从一级、二级、三级缓存中找，此时从三级缓存中获取到beanA的创建工厂，通过创建工厂获取到singletonObject，此时这个singletonObject指向的就是上面在doCreateBean()方法中实例化的beanA
9.  这样beanB就获取到了beanA的依赖，于是beanB顺利完成实例化，并将beanA从三级缓存移动到二级缓存中
10.  随后beanA继续他的属性填充工作，此时也获取到了beanB，beanA也随之完成了创建，回到getsingleton()方法中继续向下执行，将beanA从二级缓存移动到一级缓存中

## 8、Redis

面试题：

-   redis传统五大数据类型的落地应用
-   知道分布式锁吗？有哪些实现方案？你谈谈对redis分布式锁的理解，删key的时候有什么问题?
-   redis缓存过期淘汰策略
-   redis的LRU算法简介

### 1、版本说明

安装redis6.0.8：

-   [Redis官网](https://redis.io/)
-   [Redis中文网](http://www.redis.cn/)
-   安全Bug按照官网提示，（如果安装6.x版本）升级成为6.0.8
    -   **进入Redis命令行**，输入`info`，返回关于Redis服务器的各种信息（包括版本号）和统计数值。

### 2、细节说明

redis基本类型：

-   string（字符类型）
-   list（列表类型）
-   set（集合类型）
-   zset（sorted set）（有序集合类型）
-   hash（散列类型）

其他redis的类型

-   bitmap（位图）
-   HyperLogLogs（统计）
-   GEO（地理）
-   Stream

[官网命令大全](http://www.redis.cn/commands.html)

备注

-   **命令不区分大小写**，而key是区分大小写的
-   help @类型名词

### 3、String类型使用场景

**1.最常用**

-   SET key value
-   GET key

**2.同时设置/获取多个键值**

-   MSET key value \[key value…\]
-   MGET key \[key…\]

**3.数值增减**

-   递增数字 INCR key（可以不用预先设置key的数值。如果预先设置key但值不是数字，则会报错)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/ab68292b455a69e12b1ce81b3d855d10.png#pic_center)

-   增加指定的整数 INCRBY key increment
-   递减数值 DECR key
-   减少指定的整数 DECRBY key decrement

**4.获取字符串长度**

-   STRLEN key

**5.分布式锁**

-   SETNX key value
-   SET key value \[EX seconds\] \[PX milliseconds\] \[NX|XX\]
    -   EX：key在多少秒之后过期
    -   PX：key在多少毫秒之后过期
    -   NX：当key不存在的时候，才创建key，效果等同于setnx
    -   XX：当key存在的时候，覆盖key

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/f74ad5a6ae6ca0ad2267d87f59fb8ba8.png#pic_center)

**6、应用场景**

-   商品编号、订单号采用INCR命令生成

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/59540126a645334cbd6427f3fb9bf926.png#pic_center)

-   是否喜欢的文章

### 4、hash类型使用场景

**Redis的Hash类型相当于Java中Map<String, Map<Object, Object>>**

一次设置一个字段值 HSET key field value

一次获取一个字段值 HGET key field

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/527232732ea59a28ca52857345fa1a2a.png#pic_center)

一次设置多个字段值 HMSET key field value \[field value …\]

一次获取多个字段值 HMGET key field \[field …\]

获取所有字段值 HGETALL key

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/c78693e658f6b334d0a3d96e99bab649.png#pic_center)

获取某个key内的全部数量 HLEN

删除一个key HDEL

**应用场景 - 购物车早期，当前小中厂可用**

新增商品 hset shopcar:uid1024 334488 1

新增商品 hset shopcar:uid1024 334477 1

增加商品数量 hincrby shopcar:uid1024 334477 1

商品总数 hlen shopcar:uid1024

全部选择 hgetall shopcar:uid1024

### 5、list类型使用场景

向列表左边添加元素 LPUSH key value \[value …\]

向列表右边添加元素 RPUSH key value \[value …\]

查看列表 LRANGE key start stop

获取列表中元素的个数 LLEN key

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/c38b076b5951d534c18b461ff9b07b66.png#pic_center)

**应用场景 - 微信文章订阅公众号**

1.  大V作者李永乐老师和ICSDN发布了文章分别是11和22
2.  阳哥关注了他们两个，只要他们发布了新文章，就会安装进我的List
    -   lpush likearticle:uid 11 22
3.  查看阳哥自己的号订阅的全部文章，类似分页，下面0~10就是一次显示10条
    -   lrange likearticle:uid 0

### 6、set类型使用场景

添加元素 SADD key member \[member …\]

删除元素 SREM key member \[member …\]

获取集合中的所有元素 SMEMBERS key

判断元素是否在集合中 SISMEMBER key member

获取集合中的元素个数 SCARD key

从集合中随机弹出一个元素，元素不删除 SRANDMEMBER key \[数字\]

从集合中随机弹出一个元素，出一个删一个 SPOP key\[数字\]

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/4912bffec033da4292ef8e45a4083aaf.png#pic_center)

集合运算

-   集合的差集运算A - B
    -   属于A但不属于B的元素构成的集合
    -   SDIFF key \[key …\]
-   集合的交集运算A ∩ B
    -   属于A同时也属于B的共同拥有的元素构成的集合
    -   SINTER key \[key …\]
-   集合的并集运算A U B
    -   属于A或者属于B的元素合并后的集合
    -   SUNION key \[key …\]

**应用场景**

**1、微信抽奖小程序**

 ![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/db8b05cc3deb4ac6e0c8104976dbde70.png#pic_center)

||
|1、用户ID，理解参与按钮|sadd key 用户ID|
|2、显示已有多少人参与了，上图显示23208人参加了|scard key|
|3、抽奖（从set中任意选取N个人中奖）|srandmember key 2 随机抽奖2个人，元素不删除
spop key 3 随机抽奖3个人，元素会删除|

**2、微信朋友圈点赞**

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/0d8dd49f4f06bad204f9ebf42420daa7.png#pic_center)

||
|新增点赞|sadd pub:msgId 点赞用户id1 点赞用户id2|
|取消点赞|srem pub:msgId 点赞用户id|
|展现所有点赞过的用户|smembers pub:msgId|
|点赞用户数统计，就是常见的点赞红色数字|scard pub:msgId|
|判断某个朋友是否楼主点赞过|sismember pub:msgId 用户id|

**3、共同关注的人**

取交集

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/15af4ebfd33bd3efdcb4a5228b7841ee.png#pic_center)

**4、我关注的人也关注他(大家爱好相同）**

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/aafe7ab2f0a26dfe67a6b2740d3abbed.png#pic_center)

**5、QQ内推可能认识的人**

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/25b44d77e9e97f4e51c658e522103bb5.png#pic_center)

### 7、zset类型使用场景

向有序集合中加入一个元素和该元素的分数

添加元素 ZADD key score member \[score member …\]

按照元素分数从小到大的顺序返回索引从start到stop之间的所有元素 ZRANGE key start stop \[WITHSCORES\]

获取元素的分数 ZSCORE key member

删除元素 ZREM key member \[member …\]

获取指定分数范围的元素 ZRANGEBYSCORE key min max \[WITHSCORES\] \[LIMIT offset count\]

增加某个元素的分数 ZINCRBY key increment member

获取集合中元素的数量 ZCARD key

获得指定分数范围内的元素个数 ZCOUNT key min max

按照排名范围删除元素 ZREMRANGEBYRANK key start stop

获取元素的排名

-   从小到大 ZRANK key member
-   从大到小 ZREVRANK key member

**应用场景**

1、根据商品销售对商品进行排序显示

思路：定义商品销售排行榜(sorted set集合），key为goods:sellsort，分数为商品销售数量。

||
|商品编号1001的销量是9，商品编号1002的销量是15|zadd goods:sellsort 9 1001 15 1002|
|有一个客户又买了2件商品1001，商品编号1001销量加2|zincrby goods:sellsort 2 1001|
|求商品销量前10名|ZRANGE goods:sellsort 0 9 withscores|

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/918f1376d6d589008a6243a2cd3d08ca.png#pic_center)

2、抖音热搜

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/509377d8b2667bfc4f31726c6f4db52a.png#pic_center)

||
|点击视频|ZINCRBY hotvcr:20200919 1 八佰
ZINCRBY hotvcr:20200919 15 八佰 2 花木兰|
|展示当日排行前10条|ZREVRANGE hotvcr:20200919 0 9 withscores|

## 9、redis分布式锁

粉丝反馈题目：

-   Redis除了拿来做缓存，你还见过基于Redis的什么用法?
-   Redis做分布式锁的时候有需要注意的问题?
-   如果是Redis是单点部署的，会带来什么问题? 那你准备怎么解决单点问提
-   集群模式下，比如主从模式，有没有什么问题呢?
-   那你简单的介绍一下Redlock 吧？你简历上写redisson,你谈谈
-   Redis分布式锁如何续期？看门狗知道吗？

### 1、boot整合redis搭建超卖程序

使用场景：多个服务间 + 保证同一时刻内 + 同一用户只能有一个请求（防止关键业务出现数据冲突和并发错误）

建两个Module：boot\_redis01，boot\_redis02

pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<parent>
	    <groupId>org.springframework.boot</groupId>
	    <artifactId>spring-boot-starter-parent</artifactId>
	    <version>2.3.3.RELEASE</version>
	    <relativePath/> <!-- lookup parent from repository -->
	</parent>

	<groupId>sguigu.threee</groupId>
	<artifactId>boot_redis01</artifactId> <!--boot_redis02-->
	<version>1.0.0-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>boot_redis01</name> <!--boot_redis02-->

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	</properties>

	<dependencies>

		<!-- web+actuator -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
		
		<!-- SpringBoot与Redis整合依赖 -->

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-redis</artifactId>
		</dependency>
		
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-pool2</artifactId>
		</dependency>
		
		<!-- jedis -->
		<dependency>
			<groupId>redis.clients</groupId>
			<artifactId>jedis</artifactId>
			<version>3.1.0</version>
		</dependency>

		<!-- Spring Boot AOP技术-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-aop</artifactId>
		</dependency>
		
		<!-- redisson -->
		<dependency>
			<groupId>org.redisson</groupId>
			<artifactId>redisson</artifactId>
			<version>3.13.4</version>
		</dependency>

		<!-- 一般通用基础配置 -->
		<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-devtools</artifactId>
		    <scope>runtime</scope>
		    <optional>true</optional>
		</dependency>
		
		<dependency>
		    <groupId>org.projectlombok</groupId>
		    <artifactId>lombok</artifactId>
		    <optional>true</optional>
		</dependency>
		
		<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-test</artifactId><scope>test</scope>
		    <exclusions>
		        <exclusion>
		            <groupId>org.junit.vintage</groupId>
		            <artifactId>junit-vintage-engine</artifactId>
		        </exclusion>
		    </exclusions>
		</dependency>

	</dependencies>
	
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>


```

application.properties

```properties
server.port=1111
#2222

#=========================redis相关配香========================
#Redis数据库索引（默认方0）
spring.redis.database=0
#Redis服务器地址
spring.redis.host=192.168.111.147
#Redis服务器连接端口
spring.redis.port=6379
#Redis服务器连接密码（默认为空）
spring.redis.password=
#连接池最大连接数（使用负值表示没有限制）默认8
spring.redis.lettuce.pool.max-active=8
#连接池最大阻塞等待时间（使用负值表示没有限制）默认-1
spring.redis.lettuce.pool.max-wait=-1
#连接池中的最大空闲连接默认8
spring.redis.lettuce.pool.max-idle=8
#连接池中的最小空闲连接默犬认0
spring.redis.lettuce.pool.min-idle=0

```

主启动类

```java
@SpringBootApplication
public class BootRedis01Application{
    
    public static void main(String[] args){
        SpringApplication.run(BootRedis01Application.class, args);
    }
}
```

业务代码：

```java
import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
    
}
```

```java
@RestController
public class GoodController{

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/buy_goods")
    public String buy_Goods(){

        String result = stringRedisTemplate.opsForValue().get("goods:001");// get key ====看看库存的数量够不够
        int goodsNumber = result == null ? 0 : Integer.parseInt(result);
        if(goodsNumber > 0){
            int realNumber = goodsNumber - 1;
            stringRedisTemplate.opsForValue().set("goods:001", String.valueOf(realNumber));
            System.out.println("成功买到商品，库存还剩下: "+ realNumber + " 件" + "\t服务提供端口" + serverPort);
            return "成功买到商品，库存还剩下:" + realNumber + " 件" + "\t服务提供端口" + serverPort;
        }else{
            System.out.println("商品已经售完/活动结束/调用超时,欢迎下次光临" + "\t服务提供端口" + serverPort);
        }

        return "商品已经售完/活动结束/调用超时,欢迎下次光临" + "\t服务提供端口" + serverPort;
    }
    
}

```

测试

-   redis：`set goods:001 100`
-   浏览器：http://localhost:1111/buy\_goods

boot\_redis02拷贝boot\_redis01

### 2、redis分布式锁

#### 1、单机版的锁

-   synchronized
-   ReentraLock

```java
class X {
    private final ReentrantLock lock = new ReentrantLock();
    // ...

    public void m() {
        lock.lock();  // block until condition holds//不见不散
        try {
            // ... method body 业务逻辑
        } finally {
            lock.unlock()
        }
    }
     
     
    public void m2() {

       	if(lock.tryLock(timeout, unit)){//尝试加锁，过时不候
            try {
            // ... method body 业务逻辑
            } finally {
                lock.unlock()
            }   
        }else{
            // perform alternative actions 执行替代操作
        }
   }
 }

```

#### 2、分布式锁

分布式部署后，单机锁还是出现超卖现象，需要分布式锁

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/d8351d36d4067815180e1985cff39d2a.png#pic_center)

redis cluster

Nginx配置负载均衡

Nginx配置文件修改内容

```xml
upstream myserver{
    server 127.0.0.1:1111;
    server 127.0.0.1:2222;
}

server {
    listen       80;
    server_name  localhost;

    #charset koi8-r;

    #access_log  logs/host.access.log  main;

    location / {
        # 负责用到的配置
        proxy_pass  http://myserver;
        root   html;
        index  index.html index.htm;
    }

    #error_page  404              /404.html;

    # redirect server error pages to the static page /50x.html
    #
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
    	root   html;
    }
}

```

启动两个微服务：1111，2222，多次访问 http://{nginx ip}，服务提供端口在1111，2222两者之间横跳，结果没什么问题

使用JMeter模拟高并发：

redis：`set goods:001 100`，恢复到100

用到Apache JMeter，100个线程在1s内访问http://{nginx ip}。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/ff94c3d0f7a167afeeda7e05e7a29852.png#pic_center)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/5f3d1c3b5f7364523f74ea853a5a5140.png#pic_center)

启动测试，后台打印如下：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/5c5e64690298024d8c95a440e5f479fb.png#pic_center)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/8c8f51109d457ac9ac8958fb0399d125.png#pic_center)

这就是所谓分布式部署后在高并发的情况下出现超卖现象

##### 问题1：可能无法释放锁

Redis具有极高的性能，且其命令对分布式锁支持友好，借助SET命令即可实现加锁处理。

[SET](https://redis.io/commands/set)

-   EX seconds – Set the specified expire time, in seconds.
-   PX milliseconds – Set the specified expire time, in milliseconds.
-   NX – Only set the key if it does not already exist.
-   XX – Only set the key if it already exist.

在Java层面

```java
public static final String REDIS_LOCK = "redis_lock";

@Autowired
private StringRedisTemplate stringRedisTemplate;

public void m(){
    String value = UUID.randomUUID().toString() + Thread.currentThread().getName();

    Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value);


    if(!flag) {
        return "抢锁失败";
    }
 
    ...//业务逻辑
    
    stringRedisTemplate.delete(REDIS_LOCK);
}

```

##### 问题2：服务器宕机，没办法保证解锁

**上面Java源码分布式锁问题**：出现异常的话，可能无法释放锁，必须要在代码层面finally释放锁。

解决方法：try…finally…

```java
public static final String REDIS_LOCK = "redis_lock";

@Autowired
private StringRedisTemplate stringRedisTemplate;

public void m(){
    String value = UUID.randomUUID().toString() + Thread.currentThread().getName();

    try{
		Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value);

   		if(!flag) {
        	return "抢锁失败";
	    }
        
    	...//业务逻辑
            
    }finally{
	    stringRedisTemplate.delete(REDIS_LOCK);   
    }
}

```

部署了微服务jar包的机器挂了，代码层面根本没有走到finally这块，没办法保证解锁，这个key没有被删除，需要加入一个过期时间限定key。

```java
public static final String REDIS_LOCK = "redis_lock";

@Autowired
private StringRedisTemplate stringRedisTemplate;

public void m(){
    String value = UUID.randomUUID().toString() + Thread.currentThread().getName();

    try{
		Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_LOCK, value);
		//设定时间
        stringRedisTemplate.expire(REDIS_LOCK, 10L, TimeUnit.SECONDS);
        
   		if(!flag) {
        	return "抢锁失败";
	    }
        
    	...//业务逻辑
            
    }finally{
	    stringRedisTemplate.delete(REDIS_LOCK);   
    }
}

```

##### 问题3：没有原子性

**新问题**：设置key+过期时间分开了，必须要合并成一行具备原子性。

解决方法：

```java
public static final String REDIS_LOCK = "redis_lock";

@Autowired
private StringRedisTemplate stringRedisTemplate;

public void m(){
    String value = UUID.randomUUID().toString() + Thread.currentThread().getName();

    try{
		Boolean flag = stringRedisTemplate.opsForValue()//使用另一个带有设置超时操作的方法
            .setIfAbsent(REDIS_LOCK, value, 10L, TimeUnit.SECONDS);
        
   		if(!flag) {
        	return "抢锁失败";
	    }
        
    	...//业务逻辑
            
    }finally{
	    stringRedisTemplate.delete(REDIS_LOCK);   
    }
}

```

##### 问题4：张冠李戴，删除了别人的锁

**另一个新问题**：张冠李戴，删除了别人的锁

```java
public static final String REDIS_LOCK = "redis_lock";

@Autowired
private StringRedisTemplate stringRedisTemplate;

public void m(){
    String value = UUID.randomUUID().toString() + Thread.currentThread().getName();

    try{
		Boolean flag = stringRedisTemplate.opsForValue()//使用另一个带有设置超时操作的方法
            .setIfAbsent(REDIS_LOCK, value, 10L, TimeUnit.SECONDS);
        
   		if(!flag) {
        	return "抢锁失败";
	    }
        
    	...//业务逻辑,这里可能需要需要12s,而redis设置过期时间为10s
          //加入线程A 强盗锁，执行业务需要12s,redis 10s 则释放锁，线程B强盗锁，线程B正在执行业务逻辑，此时线程A执行业务完成，释放锁，
          //则有可能删除 B线程的锁
            
    }finally{
	    stringRedisTemplate.delete(REDIS_LOCK);   
    }
}

```

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/35278a95b67b16fe6fb8d17898ddcf67.png#pic_center)

解决方法：只能自己删除自己的，不许动别人的。

```java
public static final String REDIS_LOCK = "redis_lock";

@Autowired
private StringRedisTemplate stringRedisTemplate;

public void m(){
    String value = UUID.randomUUID().toString() + Thread.currentThread().getName();

    try{
		Boolean flag = stringRedisTemplate.opsForValue()//使用另一个带有设置超时操作的方法
            .setIfAbsent(REDIS_LOCK, value, 10L, TimeUnit.SECONDS);
        
   		if(!flag) {
        	return "抢锁失败";
	    }
        
    	...//业务逻辑
            
    }finally{
        if(stringRedisTemplate.opsForValue().get(REDIS_LOCK).equals(value)) {
            stringRedisTemplate.delete(REDIS_LOCK);
        }
    }
}

```

##### 问题5：finally块的判断 + del删除操作不是原子性的

finally块的判断 + del删除操作不是原子性的

解决办法：

1.  用lua脚本
2.  用redis自身的事务

Redis事务复习，[Redis学习笔记](https://blog.csdn.net/u011863024/article/details/107476187)

事务介绍：

-   Redis的事条是通过MULTI，EXEC，DISCARD和WATCH这四个命令来完成。
    
-   Redis的单个命令都是**原子性**的，所以这里确保事务性的对象是**命令集合**。
    
-   Redis将命令集合序列化并确保处于一事务的**命令集合连续且不被打断**的执行。
    
-   Redis**不支持回滚**的操作。
    

|命令|描述|
|---|---|
|MULTI|标记一个事务的开始|
|EXEC|执行所有事务块内的命令|
|DISCARD|取消事务，放弃执行事务块内的所有命令|
|UNWATCH|取消 WATCH 命令对所有 key 的监视。|
|WATCH key \[key …\]|监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断。|

**正常情况下：**

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/3e1bd1aa86949a4e12d235ac9d5b906b.png#pic_center)

**被其他线程串改情况下：**

线程A:

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/b2d1048d636374f1f2c8b197ce08e757.png#pic_center)

线程B串改：

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/149769306fadc6bd9a363c4052018d55.png#pic_center)

提交事务：返回null

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/b0891115326b84e266dc6bc440c708d5.png#pic_center)

###### 用redis自身的事务解决

```java
public static final String REDIS_LOCK = "redis_lock";

@Autowired
private StringRedisTemplate stringRedisTemplate;

public void m(){
    String value = UUID.randomUUID().toString() + Thread.currentThread().getName();

    try{
		Boolean flag = stringRedisTemplate.opsForValue()//使用另一个带有设置超时操作的方法
            .setIfAbsent(REDIS_LOCK, value, 10L, TimeUnit.SECONDS);
        
   		if(!flag) {
        	return "抢锁失败";
	    }
        
    	...//业务逻辑
            
    }finally{
        while(true){
            stringRedisTemplate.watch(REDIS_LOCK); //开始监控
            if(stringRedisTemplate.opsForValue().get(REDIS_LOCK).equalsIgnoreCase(value)){
                stringRedisTemplate.setEnableTransactionSupport(true); //设置事务支持
                stringRedisTemplate.multi(); //提交事务
                stringRedisTemplate.delete(REDIS_LOCK); //删除索
                List<Object> list = stringRedisTemplate.exec();
                if (list == null) { //表示被篡改过
                    continue;
                }
            }
            stringRedisTemplate.unwatch();//关闭监控
            break;
        } 
    }
}

```

###### 用lua脚本解决

Redis调用Lua脚本通过eval命令保证代码执行的原子性

添加 RedisUtils：

```java
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {

	private static JedisPool jedisPool;
	
	static {
		JedisPoolConfig jpc = new JedisPoolConfig();
		jpc.setMaxTotal(20);
		jpc.setMaxIdle(10);
		jedisPool = new JedisPool(jpc);
	}
	
	public static JedisPool getJedis() throws Exception{
		if(jedisPool == null)
			throw new NullPointerException("JedisPool is not OK.");
		return jedisPool;
	}
	
}

```

```java
public static final String REDIS_LOCK = "redis_lock";

@Autowired
private StringRedisTemplate stringRedisTemplate;

public void m(){
    String value = UUID.randomUUID().toString() + Thread.currentThread().getName();

    try{
		Boolean flag = stringRedisTemplate.opsForValue()//使用另一个带有设置超时操作的方法
            .setIfAbsent(REDIS_LOCK, value, 10L, TimeUnit.SECONDS);
		//设定时间
        //stringRedisTemplate.expire(REDIS_LOCK, 10L, TimeUnit.SECONDS);
        
   		if(!flag) {
        	return "抢锁失败";
	    }
        
    	...//业务逻辑
            
    }finally{
    	Jedis jedis = RedisUtils.getJedis();
    	
        //lua脚本
    	String script = "if redis.call('get', KEYS[1]) == ARGV[1] "
    			+ "then "
    			+ "    return redis.call('del', KEYS[1]) "
    			+ "else "
    			+ "    return 0 "
    			+ "end";
    	
    	try {
    		
    		Object o = jedis.eval(script, Collections.singletonList(REDIS_LOCK),Collections.singletonList(value));
    		
    		if("1".equals(o.toString())) {
    			System.out.println("---del redis lock ok.");
    		}else {
    			System.out.println("---del redis lock error.");
    		}
    		
    	}finally {
    		if(jedis != null) 
    			jedis.close();
    	}
    }
}

```

##### 问题6：分布式锁如何续期

确保RedisLock过期时间大于业务执行时间的问题

Redis分布式锁如何续期？

集群 + CAP对比ZooKeeper 对比ZooKeeper，重点，CAP

-   Redis AP : redis异步复制造成的锁丢失，比如：主节点没来的及把刚刚set进来这条数据给从节点，就挂了。（高性能）
-   ZooKeeper CP : 保证master节点复制到所有从节数据才通知完成 （高可用）

CAP

-   C：Consistency（强一致性）
-   A：Availability（可用性）
-   P：Partition tolerance（分区容错性）

综上所述

Redis集群环境下，**我们自己写的也不OK**，直接上RedLock之Redisson落地实现。

[Redisson官方网站](https://redisson.org/)

Redisson配置类

```java
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RedisConfig {

    @Bean
    public Redisson redisson() {
    	Config config = new Config();
    	config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0);
    	return (Redisson)Redisson.create(config);
    }
    
}

```

Redisson解决方案：

```java
public static final String REDIS_LOCK = "redis_lock";

@Autowired
private StringRedisTemplate stringRedisTemplate;

@Autowired
private Redisson redisson;

public void m(){
    
    RLock redissonLock = redisson.getLock(REDIS_LOCK);
    redissonLock.lock();
    try{
    	...//业务逻辑
            
    }finally{
    	redissonLock.unlock();
    }
}

```

回到实例:

```java
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodController{

	public static final String REDIS_LOCK = "REDIS_LOCK";
	
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${server.port}")
    private String serverPort;
    
    @Autowired
    private Redisson redisson;
    
    @GetMapping("/buy_goods")
    public String buy_Goods(){
    	
    	RLock redissonLock = redisson.getLock(REDIS_LOCK);
    	redissonLock.lock();
    	try {
	        String result = stringRedisTemplate.opsForValue().get("goods:001");// get key ====看看库存的数量够不够
	        int goodsNumber = result == null ? 0 : Integer.parseInt(result);
	        if(goodsNumber > 0){
	            int realNumber = goodsNumber - 1;
	            stringRedisTemplate.opsForValue().set("goods:001", String.valueOf(realNumber));
	            System.out.println("成功买到商品，库存还剩下: "+ realNumber + " 件" + "\t服务提供端口" + serverPort);
	            return "成功买到商品，库存还剩下:" + realNumber + " 件" + "\t服务提供端口" + serverPort;
	        }else{
	            System.out.println("商品已经售完/活动结束/调用超时,欢迎下次光临" + "\t服务提供端口" + serverPort);
	        }
	
	        return "商品已经售完/活动结束/调用超时,欢迎下次光临" + "\t服务提供端口" + serverPort;
    	}finally {
    		redissonLock.unlock();
    	}
    }
    
}

```

重启boot\_redis01，boot\_redis02，Nginx，重置Redis：`set goods:001 100`，启动JMeter，100个线程访问http://{nginxIP}/buy\_goods。最后，后台输出：

正常

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/690d76f73feae2f9eb8d99c22a5332c1.png#pic_center)

##### 问题7：让代码更加严谨，防止报IllegalMonitorStateException异常

```java
public static final String REDIS_LOCK = "REDIS_LOCK";

@Autowired
private Redisson redisson;

@GetMapping("/doSomething")
public String doSomething(){

    RLock redissonLock = redisson.getLock(REDIS_LOCK);
    redissonLock.lock();
    try {
        //doSomething
    }finally {
    	//添加后，更保险  
        //可避免如下异常：IllegalMonitorStateException: attempt to unlock lock，not loked by current thread by node id:da6385f-81a5-4e6c-b8c0
		if(redissonLock.isLocked() && redissonLock.isHeldByCurrentThread()) {
    		redissonLock.unlock();
    	}
    }
}

```

#### 3、分布式锁总结

synchronized单机版oK，上分布式

nginx分布式微服务单机锁不行

取消单机锁，上Redis分布式锁setnx

只加了锁，没有释放锁，出异常的话，可能无法释放锁,必须要在代码层面finally释放锁

宕机了，部署了微服务代码层面根本没有走到finally这块，没办法保证解锁，这个key没有被删除，需要有lockKey的过期时间设定

为redis的分布式锁key，增加过期时间，此外，还必须要setnx+过期时间必须同一行，保证原子性

必须规定只能自己删除自己的锁,你不能把别人的锁删除了，防止张冠李戴，1删2，2删3

Redis集群环境下，我们自己写的也不oK直接上RedLock之Redisson落地实现

### 3、redis内存调整默认查看

一些面试题：

1.  生产上你们你们的redis内存设置多少？
2.  如何配置、修改redis的内存大小
3.  如果内存满了你怎么办？
4.  redis清理内存的方式？定期删除和惰性删除了解过吗
5.  redis缓存淘汰策略
6.  redis的LRU了解过吗？可否手写一个LRU算法

#### 1、**查看Redis最大占用内存**

在配置文件redis.conf的maxmemory参数，maxmemory是bytes字节类型，注意转换。

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/ac10e242fe6f13211ce4cbaa7fb235b0.png#pic_center)

默认是注掉的，表示默认，不设置

如果不设置最大内存大小或者设置最大内存大小为0，在64位操作系统下不限制内存大小，在32位操作系统下最多使用3GB内存

#### 2、**一般生产上你如何配置？**

一般推荐Redis设置内存为最大物理内存的四分之三。

#### 3、**如何修改redis内存设置**

1.  通过配置文件，修改配置文件redis.conf的maxmemory参数，如：`maxmemory 104857600`

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/9fdd196c86c75a5a30a5d9014466cc6a.png#pic_center)

2.  通过redis命令修改
    
    1.  config set maxmemory 1024 设置
    2.  config get maxmemory 查看

#### 4、**什么命令查看redis内存使用情况?**

命令：info memory

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/27d456bcfea570f7a788fa639da3a0b5.png#pic_center)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/e6fea37e3e28ddbefc61d572f5a62013.png#pic_center)

### 4、redis打满内存OOM

真要打满了会怎么样？如果Redis内存使用超出了设置的最大值会怎样?

改配置，设置最大内存值设为1byte

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/2280c55a65d1f3f8b5e013af9a1f4ff4.png#pic_center)

结论：

1.  设置了maxmemory的选项假如redis内存使用达到上限
2.  没有加上过期时间就会导致数据写满maxmemory为了避免类似情况，引出下一章内存淘汰策略

### 5、redis内存淘汰策略

版本：6.0.8

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/c6805da7f505c08de92b6042638437e2.png#pic_center)

默认使用 noeviction ：不在驱逐

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/37c999cef8028542fe7a03eef47bcd01.png#pic_center)

#### 1、往redis里写的数据是怎么没了的？

1、**redis过期键的删除策略**

-   如果一个键是过期的，那它到了过期时间之后是不是马上就从内存中被被删除呢？
    
-   如果回答yes，你自己走还是面试官送你？
    
-   如果不是，那过期后到底什么时候被删除呢？？是个什么操作？
    

2、**三种不同的删除策略**

1.  定时删除 - 总结：对CPU不友好，用处理器性能换取存储空间（拿时间换空间）
2.  惰性删除 - 总结：对memory不友好，用存储空间换取处理器性能（拿空间换时间）
3.  上面两种方案都走极端 - 定期删除 - 定期抽样key，判断是否过期（存在漏网之鱼）

**定时删除**：

Redis不可能时时刻刻遍历所有被设置了生存时间的key，来检测数据是否已经到达过期时间，然后对它进行删除。

立即删除能保证内存中数据的最大新鲜度，因为它保证过期键值会在过期后马上被删除，其所占用的内存也会随之释放。但是立即删除对cpu是最不友好的。因为删除操作会占用cpu的时间，如果刚好碰上了cpu很忙的时候，比如正在做交集或排序等计算的时候，就会给cpu造成额外的压力，让CPU心累，时时需要删除，忙死。

这会产生大量的性能消耗，同时也会影响数据的读取操作。

**惰性删除**：

数据到达过期时间，不做处理。等下次访问该数据时

如果未过期，返回数据；

发现已过期，删除，返回不存在。

惰性删除策略的缺点是，它**对内存是最不友好的**。

如果一个键已经过期，而这个键又仍然保留在数据库中，那么只要这个过期键不被删除，它所占用的内存就不会释放。

在使用惰性删除策略时，如果数据库中有非常多的过期键，而这些过期键又恰好没有被访问到的话，那么它们也许永远也不会被删除（除非用户手动执行FLUSHDB），我们甚至可以将这种情况看作是一种内存泄漏 – 无用的垃圾数据占用了大量的内存，而服务器却不会自己去释放它们，这对于运行状态非常依赖于内存的Redis服务器来说，肯定不是一个好消息。

**定期删除**：

定期删除策略是前两种策略的折中：

定期删除策略每隔一段时间执行一次删除过期键操作，并通过限制删除操作执行的时长和频率来减少删除操作对CPU时间的影响。

周期性轮询Redis库中的时效性数据，来用随机抽取的策略，利用过期数据占比的方式控制删除频度

特点1：CPU性能占用设置有峰值，检测频度可自定义设置

特点2：内存压力不是很大，长期占用内存的冷数据会被持续清理

总结：周期性抽查存储空间（**随机抽查，重点抽查**）

举例：

redis默认每个100ms检查，是否有过期的key，有过期key则删除。注意：redis不是每隔100ms将所有的key检查一次而是随机抽取进行检查(如果每隔100ms，全部key进行检查，redis直接进去ICU)。因此，如果只采用定期删除策略，会导致很多key到时间没有删除。

定期删除策略的难点是确定删除操作执行的时长和频率:如果删除操作执行得太频繁，或者执行的时间太长，定期删除策略就会退化成定时删除策略，以至于将CPU时间过多地消耗在删除过期键上面。如果删除操作执行得太少，或者执行的时间太短，定期删除策略又会和惰性删除束略一样，出现浪费内存的情况。因此，如果采用定期删除策略的话，服务器必须根据情况，合理地设置删除操作的执行时长和执行频率。

上述2步骤====>大量过期的key堆积在内存中，导致redis内存空间紧张或者很快耗尽

**上述步骤都过堂了，还有漏洞吗？**

#### 2、**内存淘汰策略登场**

1.  noeviction：不会驱逐任何key
2.  volatile-lfu：对所有设置了过期时间的key使用LFU算法进行删除
3.  volatile-Iru：对所有设置了过期时间的key使用LRU算法进行删除
4.  volatile-random：对所有设置了过期时间的key随机删除
5.  volatile-ttl：删除马上要过期的key
6.  allkeys-lfu：对所有key使用LFU算法进行删除
7.  allkeys-Iru：对所有key使用LRU算法进行删除
8.  allkeys-random：对所有key随机删除

**上面总结**

-   2\*4得8
-   2个维度
    -   过期键中筛选
    -   所有键中筛选
-   4个方面
    -   LRU
    -   LFU
    -   random
    -   ttl（Time To Live）
-   8个选项

**一般生产使用第二种： volatile-lfu** ，具体还得根据公司

**如何修改配置**

1、修改配置文件redis.conf

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/6b5fb0243fd68d667741167a695e80f5.png#pic_center)

2、命令

-   config set maxmemory-policy allkeys-lru
-   config get maxmemory- policy

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/4f3cced3d81b131461c34608ddae0454.png#pic_center)

### 6、lru算法简介

Redis的LRU了解过吗？可否手写一个LRU算法

**是什么**：LRU是Least Recently Used的缩写，即最近最少使用，是一种常用的页面置换算法，选择最近最久未使用的数据予以淘汰。

**算法来源**

[力扣](https://leetcode.cn/problems/lru-cache/)

### 7、lru的思想

**设计思想**

1.  所谓缓存，必须要有读+写两个操作，按照命中率的思路考虑，写操作+读操作时间复杂度都需要为O(1)
2.  特性要求
    1.  必须要有顺序之分，一区分最近使用的和很久没有使用的数据排序。
    2.  和读操作一次搞定。
    3.  如果容量(坑位)满了要删除最不长用的数据，每次新访问还要把新的数据插入到队头(按照业务你自己设定左右那一边是队头)

![在这里插入图片描述](https://i-blog.csdnimg.cn/blog_migrate/56f083e6d805df5b2540d772608e7673.png#pic_center)

查找快、插入快、删除快，且还需要先后排序---------->什么样的数据结构可以满足这个问题？

你是否可以在O(1)时间复杂度内完成这两种操作？

如果一次就可以找到，你觉得什么数据结构最合适？

答案：LRU的算法核心是哈希链表

-   本质就是HashMap + DoubleLinkedList
    
-   时间复杂度是O(1)，哈希表+双向链表的结合体
    

### 8、手写LRU

#### 1、巧用LinkedHashMap完成lru算法

```java
package sguigu.threee.test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: jxm
 * @Description:
 * @Date: 2022/12/1 10:36
 * @Version: 1.0
 */
public class LRUCache<K,V> extends LinkedHashMap<K,V> {

    private int capacity; //缓存坑位

    public LRUCache(int capacity){

        /**
         * accessOrder 访问顺序
         */
        super(capacity,0.75F,true);
        this.capacity = capacity;
    }

    //最近最少使用
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return super.size() > capacity;
    }

    public static void main(String[] args) {

        LRUCache<Integer, String> lruCache = new LRUCache<>(3);
        lruCache.put(1,"a");
        lruCache.put(2,"b");
        lruCache.put(3,"c");

        System.out.println(lruCache.keySet());

        lruCache.put(4,"d");

        System.out.println(lruCache.keySet());
        System.out.println();
        lruCache.put(3,"c");
        System.out.println(lruCache.keySet());
        lruCache.put(3,"c");
        System.out.println(lruCache.keySet());
        lruCache.put(3,"c");
        System.out.println(lruCache.keySet());
        System.out.println();
        lruCache.put(5,"e");
        System.out.println(lruCache.keySet());

    }
}

```

当访问排序为true (**super(capacity,0.75F,true)**) 运行结果

```java
[1, 2, 3]
[2, 3, 4]

[2, 4, 3]
[2, 4, 3]
[2, 4, 3]

[4, 3, 5]
```

当访问排序为false (**super(capacity,0.75F,false)**) 运行结果

```java
[1, 2, 3]
[2, 3, 4]

[2, 3, 4]
[2, 3, 4]
[2, 3, 4]

[3, 4, 5]
```

#### 2、手写LRU算法

```java
package sguigu.threee.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCacheDemo{

    //双向链表节点
    static class Node<K,V>{
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        public Node() {
            this.prev = this.next = null;
        }
        public Node(K key, V value) {
            super();
            this.key = key;
            this.value = value;
        }

    }

    //构建一个虚拟的双向链表，里面安放的就是我们的Node
    //新的插入头部，旧的从尾部移除
    class DoubleLinkedList<K,V>{

        Node<K,V> head; //头
        Node<K,V> tail; //尾

        //构造方法
        public DoubleLinkedList(){
            head = new Node<>();
            tail = new Node<>();
            head.next = tail;
            tail.prev = head;
        }

        //添加头部
        public void addHead(Node<K,V> node){
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        //删除结点
        public void removeNode(Node<K,V> node){
            node.next.prev = node.prev;
            node.prev.next = node.next;
            node.prev = null;
            node.next = null;

        }

        //获取最后一个节点
        public Node<K,V> getLast(){
            return tail.prev;
        }

    }

    private int cacheSize;
    private Map<Integer, Node<Integer, Integer>> map;
    private DoubleLinkedList<Integer, Integer> doubleLinkedList;

    public LRUCacheDemo(int cacheSize){
        this.cacheSize = cacheSize; //坑位
        map = new HashMap<>(); //查找
        doubleLinkedList = new DoubleLinkedList<>();
    }

    public int get(int key){
        if(!map.containsKey(key)){
            return -1;
        }
        //更新节点位置，将节点移置链表头
        Node<Integer, Integer> node = map.get(key);
        doubleLinkedList.removeNode(node);
        doubleLinkedList.addHead(node);
        return node.value;
    }

    //saveOrUpdate
    public void put(int key,int value){
        if(map.containsKey(key)){ //更新
            Node<Integer, Integer> node = map.get(key);
            //更新map
            node.value = value;
            map.put(key,node);
            //更新链表
            doubleLinkedList.removeNode(node);
            doubleLinkedList.addHead(node);
        }else {
            if(map.size() == cacheSize){ //坑位满了
                Node<Integer,Integer> lastNode = doubleLinkedList.getLast();
                map.remove(lastNode.key);
                doubleLinkedList.removeNode(lastNode);
            }
            //save
            Node<Integer, Integer> newNode = new Node<>(key, value);
            map.put(key,newNode);
            doubleLinkedList.addHead(newNode);
        }
    }




    public static void main(String[] args) {

        LRUCacheDemo lruCache = new LRUCacheDemo(3);
        lruCache.put(1,1);
        lruCache.put(2,2);
        lruCache.put(3,3);

        System.out.println(lruCache.map.keySet());

        lruCache.put(4,4);

        System.out.println(lruCache.map.keySet());
        System.out.println();
        lruCache.put(3,3);
        System.out.println(lruCache.map.keySet());
        lruCache.put(3,3);
        System.out.println(lruCache.map.keySet());
        lruCache.put(3,3);
        System.out.println(lruCache.map.keySet());
        System.out.println();
        lruCache.put(5,5);
        System.out.println(lruCache.map.keySet());

    }
}

```

原型结果：

```java
[1, 2, 3]
[2, 3, 4]

[2, 3, 4]
[2, 3, 4]
[2, 3, 4]

[3, 4, 5]
```

### 9、总结

```
  LRUCache<Integer, String> lruCache = new LRUCache<>(3);
    lruCache.put(1,"a");
    lruCache.put(2,"b");
    lruCache.put(3,"c");

    System.out.println(lruCache.keySet());

    lruCache.put(4,"d");

    System.out.println(lruCache.keySet());
    System.out.println();
    lruCache.put(3,"c");
    System.out.println(lruCache.keySet());
    lruCache.put(3,"c");
    System.out.println(lruCache.keySet());
    lruCache.put(3,"c");
    System.out.println(lruCache.keySet());
    System.out.println();
    lruCache.put(5,"e");
    System.out.println(lruCache.keySet());

}
```

}

````



当访问排序为true  (**super(capacity,0.75F,true)**) 运行结果

​```java
[1, 2, 3]
[2, 3, 4]

[2, 4, 3]
[2, 4, 3]
[2, 4, 3]

[4, 3, 5]
````

当访问排序为false (**super(capacity,0.75F,false)**) 运行结果

```java
[1, 2, 3]
[2, 3, 4]

[2, 3, 4]
[2, 3, 4]
[2, 3, 4]

[3, 4, 5]
```

#### 2、手写LRU算法

```java
package sguigu.threee.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCacheDemo{

    //双向链表节点
    static class Node<K,V>{
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        public Node() {
            this.prev = this.next = null;
        }
        public Node(K key, V value) {
            super();
            this.key = key;
            this.value = value;
        }

    }

    //构建一个虚拟的双向链表，里面安放的就是我们的Node
    //新的插入头部，旧的从尾部移除
    class DoubleLinkedList<K,V>{

        Node<K,V> head; //头
        Node<K,V> tail; //尾

        //构造方法
        public DoubleLinkedList(){
            head = new Node<>();
            tail = new Node<>();
            head.next = tail;
            tail.prev = head;
        }

        //添加头部
        public void addHead(Node<K,V> node){
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        //删除结点
        public void removeNode(Node<K,V> node){
            node.next.prev = node.prev;
            node.prev.next = node.next;
            node.prev = null;
            node.next = null;

        }

        //获取最后一个节点
        public Node<K,V> getLast(){
            return tail.prev;
        }

    }

    private int cacheSize;
    private Map<Integer, Node<Integer, Integer>> map;
    private DoubleLinkedList<Integer, Integer> doubleLinkedList;

    public LRUCacheDemo(int cacheSize){
        this.cacheSize = cacheSize; //坑位
        map = new HashMap<>(); //查找
        doubleLinkedList = new DoubleLinkedList<>();
    }

    public int get(int key){
        if(!map.containsKey(key)){
            return -1;
        }
        //更新节点位置，将节点移置链表头
        Node<Integer, Integer> node = map.get(key);
        doubleLinkedList.removeNode(node);
        doubleLinkedList.addHead(node);
        return node.value;
    }

    //saveOrUpdate
    public void put(int key,int value){
        if(map.containsKey(key)){ //更新
            Node<Integer, Integer> node = map.get(key);
            //更新map
            node.value = value;
            map.put(key,node);
            //更新链表
            doubleLinkedList.removeNode(node);
            doubleLinkedList.addHead(node);
        }else {
            if(map.size() == cacheSize){ //坑位满了
                Node<Integer,Integer> lastNode = doubleLinkedList.getLast();
                map.remove(lastNode.key);
                doubleLinkedList.removeNode(lastNode);
            }
            //save
            Node<Integer, Integer> newNode = new Node<>(key, value);
            map.put(key,newNode);
            doubleLinkedList.addHead(newNode);
        }
    }




    public static void main(String[] args) {

        LRUCacheDemo lruCache = new LRUCacheDemo(3);
        lruCache.put(1,1);
        lruCache.put(2,2);
        lruCache.put(3,3);

        System.out.println(lruCache.map.keySet());

        lruCache.put(4,4);

        System.out.println(lruCache.map.keySet());
        System.out.println();
        lruCache.put(3,3);
        System.out.println(lruCache.map.keySet());
        lruCache.put(3,3);
        System.out.println(lruCache.map.keySet());
        lruCache.put(3,3);
        System.out.println(lruCache.map.keySet());
        System.out.println();
        lruCache.put(5,5);
        System.out.println(lruCache.map.keySet());

    }
}

```

原型结果：

```java
[1, 2, 3]
[2, 3, 4]

[2, 3, 4]
[2, 3, 4]
[2, 3, 4]

[3, 4, 5]
```

### 9、总结

略