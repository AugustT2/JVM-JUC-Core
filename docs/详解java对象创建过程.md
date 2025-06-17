# 详解java对象创建过程

[TOC]

在需要生成目录的地方输入：[TOC] 然后回车即可。

Java对象的创建过程主要分为以下几个步骤：

## 1. 类加载检查

当JVM遇到new指令时，首先检查这个指令的参数是否能在常量池中定位到一个类的符号引用，并检查这个符号引用代表的类是否已被加载、解析和初始化过。如果没有，则必须先执行相应的类加载过程。



## 2. 分配内存

在类加载检查通过后，JVM将为新生对象分配内存。对象所需的内存大小在类加载完成后便可完全确定。分配方式有两种：

- **指针碰撞（Bump the Pointer）**：适用于Java堆内存规整的情况
- **空闲列表（Free List）**：适用于Java堆内存不规整的情况

## 3. 初始化零值

内存分配完成后，JVM将分配到的内存空间都初始化为零值（不包括对象头）。这一步保证了对象的实例字段在Java代码中可以不赋初始值就直接使用。

## 4. 设置对象头

JVM需要对对象进行必要的设置，这些信息存放在对象头（Object Header）中，包括：

- 对象的哈希码
- 对象的GC分代年龄
- 元数据指针（指向类的元数据）
- 锁状态标志
- 线程持有的锁
- 偏向线程ID
- 偏向时间戳等

## 5. 执行`<init>`方法

从JVM角度看，对象创建才刚刚开始。执行

```
<init>
```

方法（即构造方法），按照程序员的意愿初始化对象。



## 6. 建立引用关系

将对象引用压入操作数栈，建立与局部变量表或对象字段的引用关系。

## 示例代码展示

```
javapublic class ObjectCreation {
    private int value;
    
    public ObjectCreation(int value) {
        this.value = value;
    }
    
    public static void main(String[] args) {
        ObjectCreation obj = new ObjectCreation(10);
    }
}
```

在这个例子中，当执行

```
new ObjectCreation(10)
```

时，JVM会按照上述步骤创建对象，最终在堆中分配内存并初始化对象。



需要注意的是，如果创建的是数组或Class对象，过程会略有不同，但基本流程相似。



Feedback submitted



Me

举例每一步具体讲下

Revert

# Java对象创建过程详解

让我们通过一个具体的例子来详细说明Java对象创建的每个步骤。假设我们有以下简单的类：

```
javapublic class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public void sayHello() {
        System.out.println("Hello, my name is " + name);
    }
}

public class Main {
    public static void main(String[] args) {
        Person person = new Person("张三", 25);
        person.sayHello();
    }
}
```

## 1. 类加载检查

当JVM执行到new Person("张三", 25)时：

- 检查常量池中是否有Person类的符号引用
- 检查Person类是否已加载、解析和初始化

- 如果未加载，则执行类加载过程：
  - 加载：查找并加载Person.class文件
  - 验证：验证字节码的合法性
  - 准备：为类变量分配内存并初始化默认值
  - 解析：将符号引用转换为直接引用（将常量池中的符号引用替换为直接引用的过程.直接引用为直接指向目标的指针或者相对偏移量
    等.  ）
  - 初始化：执行**类**构造器<clinit>()方法

## 2. 分配内存

- JVM确定对象大小：Person类有两个实例变量（String引用和int）
- 在堆中分配内存空间：
  - 如果使用指针碰撞：
    - 堆内存规整
    - 移动指针_top到新位置
  - 如果使用空闲列表：
    - 堆内存不规整
    - 从空闲列表中找到足够大的空间分配

## 3. 初始化零值

- 将分配的内存空间初始化为零值：
  - name引用初始化为null
  - age初始化为0
- 注意：这与字段的显式初始化不同，这是在构造方法执行前完成的

## 4. 设置对象头

- 设置Mark Word（存储对象自身的运行时数据）：
  - 哈希码
  - GC分代年龄（初始为0）
  - 锁状态标志（01表示无锁）
  - 其他标志位
- 设置类型指针（指向Person类的元数据）
- 如果是数组，还会设置数组长度

## 5. 执行`<init>`方法

- 执行构造方法中的代码：

  ```java
  public Person(String name, int age) {
      this.name = name;  // 设置name字段
      this.age = age;    // 设置age字段
  }
  ```

- 执行顺序：

  1. 调用父类构造器（如果有显式或隐式的super()调用）
  2. 执行实例变量初始化（如private int x = 10;）
  3. 执行构造方法体中的代码

## 6. 建立引用关系

- 将新创建的Person对象的引用赋值给局部变量person
- 此时person变量指向堆中新创建的Person对象

## 内存布局示例

```
+------------------+  <-- 对象在堆中的起始地址
|     Mark Word    |  <-- 存储对象的运行时数据
+------------------+
|   Class Pointer  |  <-- 指向方法区中的Person类元数据
+------------------+
|   name (引用)     |  <-- 指向字符串常量池中的"张三"
+------------------+
|   age (int)      |  <-- 25
+------------------+
```

## 实际执行流程

```java
Person person = new Person("张三", 25);
```

这行代码的字节码可能类似于：

```
0: new           #2                  // class Person
3: dup
4: ldc           #3                  // String 张三
6: bipush        25
8: invokespecial #4                  // Method Person."<init>":(Ljava/lang/String;I)V
11: astore_1
12: return
```

这个例子展示了从类加载到对象初始化的完整过程，涵盖了JVM在底层执行的所有关键步骤。

# 符号引用转直接引用的简单解释

## 简单来说

想象你在写一封信：

- **符号引用**：就像写"给我妈妈写信"，"妈妈"是一个符号，具体是谁要看上下文
- **直接引用**：就像写"给住在北京市朝阳区XX小区1号楼101的张女士"，这是具体的地址

## 实际例子

```
java// 源代码
student.setName("张三");

// 编译后的字节码中
// 符号引用：Student.setName:(Ljava/lang/String;)V
// (这就像写"给学生对象的setName方法")

// 运行时JVM会转换成
// 直接引用：0x12345678 (方法在内存中的实际地址)
// (这就像具体的门牌号)
```

## 关键点

1. **符号引用**：<font color='red'>是编译时使用的名字和描述</font>
2. **直接引用**：<font color='red'>是运行时实际的内存地址或偏移量</font>
3. **转换时机**：类加载时或方法第一次被调用时
4. **目的**：让Java程序不关心具体内存布局，实现"一次编译，到处运行"

简单说，就是把程序中的"名字"变成计算机能直接使用的"地址"的过程。

# `<clinit>` 与 `<init>` 方法对比

## 1. `<clinit>` 方法

**作用**：类构造器方法，用于**类初始化**

- **触发时机**：类被首次主动使用时（创建实例、访问静态变量/方法等）

- **执行次数**：每个类只执行一次

- 内容

  ：按顺序执行

  - 静态变量的显式初始化
  - 静态代码块中的代码

**示例**：

```
javapublic class Example {
    static int a = 10;          // 静态变量初始化
    static {                    // 静态代码块
        System.out.println("静态代码块执行");
        a = 20;
    }
    static int b = initB();     // 静态方法调用
    
    static int initB() {
        return 30;
    }
}
```

## 2. `<init>` 方法

**作用**：实例构造器方法，用于**对象初始化**

- **触发时机**：每次创建新对象时

- **执行次数**：每个对象创建时执行一次

- 内容

  ：按顺序执行

  - 调用父类的

    ```
    <init>
    ```

    方法（隐式或显式）

  - 实例变量的显式初始化

  - 构造代码块中的代码

  - 构造方法体中的代码

**示例**：

```
javapublic class Example {
    private String name = "默认名字";  // 实例变量初始化
    {                               // 构造代码块
        System.out.println("构造代码块执行");
    }
    
    public Example() {              // 构造方法
        this.name = "张三";
        System.out.println("构造方法执行");
    }
    
    public Example(String name) {   // 重载的构造方法
        this();                     // 调用本类其他构造方法
        this.name = name;
    }
}
```

## 3. 关键区别

| 特性           | `<clinit>`       | `<init>`                             |
| :------------- | :--------------- | :----------------------------------- |
| 调用时机       | 类加载时         | 对象创建时                           |
| 执行次数       | 每个类只执行一次 | 每个对象执行一次                     |
| 是否线程安全   | 是（JVM保证）    | 否（需要手动同步）                   |
| 是否可见       | 对类可见         | 对对象实例可见                       |
| 是否可重载     | 不能重载         | 可以重载（多个构造方法）             |
| 是否可显式调用 | 不能显式调用     | 可通过`new`、`super()`、`this()`调用 |

## 4. 执行顺序示例

```
javapublic class Parent {
    static {
        System.out.println("父类静态代码块");
    }
    
    {
        System.out.println("父类构造代码块");
    }
    
    public Parent() {
        System.out.println("父类构造方法");
    }
}

public class Child extends Parent {
    static {
        System.out.println("子类静态代码块");
    }
    
    {
        System.out.println("子类构造代码块");
    }
    
    public Child() {
        System.out.println("子类构造方法");
    }
    
    public static void main(String[] args) {
        new Child();
    }
}
```

**输出**：

```
父类静态代码块
子类静态代码块
父类构造代码块
父类构造方法
子类构造代码块
子类构造方法
```

这个例子展示了类初始化和对象初始化的完整顺序。

# Minor GC 与 Full GC 触发时机

## Minor GC (新生代垃圾回收)

### 触发条件

- **新生代空间不足**：当Eden区满时触发
- **新对象分配失败**：新对象无法在Eden区分配足够空间时

### 特点

- 只回收新生代(Eden + Survivor区)
- 执行频率高，但通常停顿时间短
- 采用复制算法

### 执行过程

1. 将Eden和From Survivor区中存活的对象复制到To Survivor区
2. 清空Eden和From Survivor区
3. 交换From和To Survivor区

## Full GC (全局垃圾回收)

### 触发条件

1. **老年代空间不足**：老年代剩余空间不足以容纳从新生代晋升的对象
2. **元空间/永久代空间不足**：类元数据占用空间超过指定值
3. **System.gc()调用**：建议JVM执行Full GC，但不保证立即执行
4. **空间分配担保失败**：Minor GC后存活对象过多，老年代剩余空间不足
5. **大对象直接进入老年代**：大对象无法在新生代分配时
6. **CMS GC失败**：并发模式失败时，会触发Full GC

### 特点

- 回收整个Java堆(新生代 + 老年代)和方法区/元空间
- 执行频率低，但停顿时间长
- 通常采用标记-清除-整理算法(Serial/Parallel Old)或标记-清除算法(CMS)

## 相关JVM参数

```
bash# 新生代相关
-Xmn256m                  # 设置新生代大小
-XX:NewRatio=2            # 老年代/新生代=2
-XX:SurvivorRatio=8       # Eden/Survivor=8

# GC日志
-XX:+PrintGCDetails       # 打印GC详细日志
-XX:+PrintGCDateStamps    # 打印GC时间戳
-Xloggc:gc.log           # 将GC日志输出到文件

# Full GC相关
-XX:+DisableExplicitGC    # 禁止System.gc()触发Full GC
-XX:CMSInitiatingOccupancyFraction=70  # CMS在老年代使用70%时触发GC
```

## 优化建议

1. 避免频繁创建大对象
2. 合理设置新生代和老年代比例
3. 避免频繁调用System.gc()
4. 根据应用特点选择合适的垃圾收集器
5. 监控GC日志，及时发现性能问题

## 常见问题排查

- **频繁Full GC**：可能是内存泄漏或新生代设置过小
- **Full GC后内存不释放**：可能是代码中有强引用导致对象无法回收
- **GC时间过长**：可能需要调整堆大小或更换垃圾收集器



# Java对象的内存结构

Java对象在内存中的存储布局可以分为三个部分：对象头(Header)、实例数据(Instance Data)和对齐填充(Padding)。

## 1. 对象头(Header)

对象头包含三部分信息：

### Mark Word (标记字段)

- 存储对象自身的运行时数据
- 在32位JVM中占32位，64位JVM中占64位
- 包含：
  - 哈希码(HashCode)
  - GC分代年龄
  - 锁状态标志
  - 线程持有的锁
  - 偏向线程ID
  - 偏向时间戳

### 类型指针(Class Pointer)

- 指向方法区中该对象的类元数据
- JVM通过这个指针确定对象是哪个类的实例
- 开启压缩指针(-XX:+UseCompressedOops)时占4字节，否则占8字节

### 数组长度(仅数组对象有)

- 如果是数组对象，对象头中还会存储数组长度
- 占4字节

## 2. 实例数据(Instance Data)

- 存储对象真正的有效信息
- 包括从父类继承的和自己定义的字段
- 存储顺序受JVM分配策略参数(-XX:FieldsAllocationStyle)和字段在Java源码中定义顺序的影响
- 基本类型直接存储值，引用类型存储引用地址

## 3. 对齐填充(Padding)

- 不是必须的，仅起占位符作用
- 由于HotSpot VM要求对象起始地址必须是8字节的整数倍，所以当对象大小不是8字节的整数倍时，需要填充对齐

## 示例

```
javapublic class Student {
    private int id;          // 4字节
    private String name;     // 引用类型，4字节(压缩指针)或8字节
    private boolean gender;  // 1字节
    // 对齐填充3字节(假设使用压缩指针)
}

// 对象大小计算(64位JVM，开启压缩指针):
// 对象头: 12字节 (8字节Mark Word + 4字节类型指针)
// 实例数据: 4(int) + 4(String引用) + 1(boolean) = 9字节
// 对齐填充: 7字节 (使总大小为8的倍数)
// 总计: 12 + 9 + 7 = 32字节
```

## 查看对象内存布局

可以使用JOL (Java Object Layout)工具查看对象内存布局：

```
java// 添加Maven依赖
// <dependency>
//     <groupId>org.openjdk.jol</groupId>
//     <artifactId>jol-core</artifactId>
//     <version>0.16</version>
// </dependency>

public class ObjectLayoutDemo {
    public static void main(String[] args) {
        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }
}
```

## 指针压缩

- 32位JVM最大支持4GB内存，因为2^32 = 4GB
- 64位JVM理论上支持2^64字节内存，但实际不需要这么大
- 指针压缩(-XX:+UseCompressedOops)将64位指针压缩为32位，节省内存
- 压缩后，最大堆内存约32GB

## 对象大小计算

- 普通对象：对象头 + 实例数据 + 对齐填充
- 数组对象：对象头 + 数组长度(4字节) + 数组元素 + 对齐填充

理解Java对象内存结构对于性能调优和内存优化非常重要。