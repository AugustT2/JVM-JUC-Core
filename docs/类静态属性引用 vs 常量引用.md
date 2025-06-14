# 类静态属性引用 vs 常量引用

## 1. 类静态属性引用的对象

```java
javapublic class Example {
    // 静态变量
    private static Object staticObj = new Object();
    
    // 静态常量
    private static final Object STATIC_FINAL_OBJ = new Object();
}
```

**特点**：

- 使用`static`修饰的类变量
- 存储在方法区（JDK 8 之前）或堆中（JDK 8 及以后）
- 生命周期与类相同，从类加载开始到 JVM 结束
- 可以被重新赋值（除非同时被final修饰）
- 是 GC Roots 的一部分，引用的对象不会被垃圾回收

## 2. 常量引用的对象

```java
javapublic class Example {
    // 字符串常量（字面量）
    private static final String STR_CONSTANT = "Hello";
    
    // 基本类型常量
    private static final int INT_CONSTANT = 42;
    
    // 编译时常量
    private static final long TIMESTAMP = System.currentTimeMillis(); // 非常量表达式
}
```

**特点**：

- 使用`static final`修饰的变量
- 如果是基本类型或字符串字面量，会在编译期确定值并存储在运行时常量池
- 如果是对象引用，则存储在堆中，引用存储在运行时常量池
- 一旦初始化后就不能再修改
- 是 GC Roots 的一部分，引用的对象不会被垃圾回收

## 主要区别

| 特性       | 类静态属性引用                                 | 常量引用                                            |
| :--------- | :--------------------------------------------- | :-------------------------------------------------- |
| 声明方式   | `static [类型]`                                | `static final [类型]`                               |
| 是否可修改 | 是（除非同时是 final）                         | 否                                                  |
| 存储位置   | 堆中（JDK8+）                                  | 运行时常量池（基本类型/字符串字面量）或堆中（对象） |
| 初始化时机 | 类加载的准备阶段（默认值）→ 初始化阶段（赋值） | 编译期（基本类型/字符串字面量）或类加载时（对象）   |
| 内存回收   | 类卸载时释放                                   | 类卸载时释放                                        |
| 典型用途   | 共享的类级别状态                               | 配置参数、固定值                                    |

## 示例说明

```java
public class MemoryExample {
    // 静态属性 - 可修改
    public static List<String> staticList = new ArrayList<>();
    
    // 常量引用 - 不可修改引用（但对象本身可能可变）
    public static final List<String> CONSTANT_LIST = new ArrayList<>();
    
    // 真正的不可变常量
    public static final String GREETING = "Hello";
    
    public static void main(String[] args) {
        // 可以修改staticList的引用
        staticList = new ArrayList<>();  // 允许
        
        // 不能修改CONSTANT_LIST的引用
        // CONSTANT_LIST = new ArrayList<>();  // 编译错误
        
        // 但可以修改CONSTANT_LIST引用的对象内容
        CONSTANT_LIST.add("item");  // 允许
        
        // 字符串常量不可变
        // GREETING = "Hi";  // 编译错误
    }
}
```

## 内存模型影响

1. **静态属性**：
   - 存储在堆中（JDK 8+）
   - 被类的所有实例共享
   - 可能导致内存泄漏，如果持有大对象或集合
2. **常量**：
   - 基本类型和字符串字面量存储在运行时常量池
   - 对象常量存储在堆中，引用在运行时常量池
   - 如果常量引用大对象，也会占用堆内存

## 最佳实践

1. 优先使用`static final`定义常量
2. 对于集合类常量，考虑使用不可变集合
3. 避免在静态集合中存储大量数据
4. 注意静态集合可能导致的内存泄漏问题



# 元空间 (Metaspace) 存储内容

元空间是 JDK 8 引入的，用于替代永久代的方法区实现。它存储了类的元数据信息，主要包含以下内容：

## 1. 类元数据 (Class Metadata)

- 类的结构信息
- 方法元数据（方法名、返回类型、参数等）
- 字段元数据（字段名、类型、修饰符等）
- 方法字节码
- 方法表和虚方法表（vtable 和 itable）
- 常量池（运行时常量池）
- 注解信息

## 2. 类加载器数据

- 类加载器的引用
- 类加载器的元数据
- 类加载器加载的类信息

## 3. 符号引用 (Symbol References)

- 类和接口的全限定名
- 字段的名称和描述符
- 方法的名称和描述符

## 4. 方法区中的其他信息

- 类静态变量（在 JDK 8 中已移至堆中）
- 运行时常量池（字符串常量池在 JDK 7 及以后已移至堆中）

## 不存储在元空间的内容

1. **字符串常量**：存储在堆中的字符串常量池
2. **静态变量**：存储在堆中，属于类对象的一部分
3. **类实例**：存储在堆中
4. **基本类型常量**：内联在代码中或存储在运行时常量池，，这个不算，这个算元空间

## 元空间特点

- 使用本地内存（Native Memory）而非 JVM 堆内存
- 默认不限制大小（受限于系统内存）
- 可以动态扩展和收缩
- 垃圾回收由元空间自己的垃圾收集器处理

## 相关 JVM 参数

- ```
  -XX:MetaspaceSize
  ```

  ：初始大小（默认约 21MB）

- ```
  -XX:MaxMetaspaceSize
  ```

  ：最大大小（默认无限制）

- ```
  -XX:MinMetaspaceFreeRatio
  ```

  ：GC 后最小元空间空闲比例

- ```
  -XX:MaxMetaspaceFreeRatio
  ```

  ：GC 后最大元空间空闲比例

## 监控元空间

可以使用以下工具监控元空间使用情况：

- ```
  jstat -gcmetacapacity [pid]
  ```

- ```
  jcmd [pid] VM.metaspace
  ```

- VisualVM 或其他 JVM 监控工具

## 常见问题

- **元空间溢出**：通常由类加载器泄漏或动态生成大量类导致
- 调优建议：适当设置`-XX:MaxMetaspaceSize`防止无限增长



# 运行时常量池 vs 字符串常量池

## 运行时常量池 (Runtime Constant Pool)

### 特点

- **位置**：JDK 8+ 中位于元空间（Metaspace）
- 内容：
  - 类、接口、字段和方法的符号引用
  - 字面量（Literal）
  - 编译期生成的各种字面量和符号引用
  - 类和接口的全限定名
  - 字段的名称和描述符
  - 方法的名称和描述符

### 示例

```java
public class ConstantPoolExample {
    // 字面量会进入运行时常量池
    private final int intValue = 123;
    private final double doubleValue = 3.14;
    private final String str = "Hello";  // 字符串字面量会同时进入字符串常量池
}
```

## 字符串常量池 (String Pool)

### 特点

- **位置**：JDK 7+ 中位于堆内存
- 内容：
  - 字符串字面量（如 "hello"）
  - 通过 `String.intern()`方法添加的字符串
- 特点：
  - 避免重复创建相同的字符串对象
  - 提高内存使用效率
  - 通过==可以比较字符串是否引用同一个对象

### 示例

```java
public class StringPoolExample {
    public static void main(String[] args) {
        String s1 = "Hello";  // 字符串字面量，会放入字符串常量池
        String s2 = "Hello";  // 复用字符串常量池中的对象
        String s3 = new String("Hello");  // 在堆中创建新对象
        
        System.out.println(s1 == s2);  // true，引用相同
        System.out.println(s1 == s3);  // false，引用不同
        
        String s4 = s3.intern();  // 将s3字符串放入常量池（如果不存在）并返回引用
        System.out.println(s1 == s4);  // true
    }
}
```

## 主要区别

| 特性           | 运行时常量池           | 字符串常量池                       |
| :------------- | :--------------------- | :--------------------------------- |
| 存储位置       | 元空间（JDK 8+）       | 堆内存（JDK 7+）                   |
| 存储内容       | 类的符号引用、字面量等 | 字符串字面量和 `intern()` 的字符串 |
| 作用范围       | 每个类/接口一个        | 整个 JVM 共享一个                  |
| 是否包含字符串 | 包含字符串字面量的引用 | 包含字符串对象本身                 |
| 垃圾回收       | 类卸载时回收           | 可以被垃圾回收                     |

## 内存模型演变

### JDK 6 及之前

- 运行时常量池和字符串常量池都在方法区（永久代）

### JDK 7

- 字符串常量池移动到堆内存
- 运行时常量池仍在方法区

### JDK 8+

- 字符串常量池在堆内存
- 运行时常量池在元空间（Metaspace）

## 实际应用

### 字符串比较

```java
String s1 = "Hello";  // 字符串常量池
String s2 = "Hello";  // 复用常量池中的字符串
String s3 = new String("Hello");  // 堆中新对象

System.out.println(s1 == s2);  // true
System.out.println(s1 == s3);  // false
System.out.println(s1.equals(s3));  // true
```

### 使用 `intern()` 方法

```
javaString s1 = new String("Hello").intern();  // 将字符串放入常量池
String s2 = "Hello";
System.out.println(s1 == s2);  // true
```

## 性能考虑

1. 字符串常量池可以避免重复创建相同的字符串对象
2. 过多使用`intern()`可能导致字符串常量池过大
3. 在 JDK 7+ 中，字符串常量池位于堆中，可以更好地被垃圾回收

## 总结

- 运行时常量池存储类的元数据信息
- 字符串常量池专门用于存储字符串字面量和`intern()`的字符串
- 理解这两者的区别对于理解 Java 内存模型和字符串处理非常重要



# 基本类型常量的存储位置

基本类型常量的存储位置取决于其声明方式和作用域：

## 1. 静态基本类型常量

```java
public class Example {
    // 静态基本类型常量
    public static final int MAX_VALUE = 100;
    private static final double PI = 3.14159;
}
```

- **存储位置**：编译后直接内联到使用处（编译时常量）
- 特点：
  - 如果常量是编译时常量（编译时就能确定值），会直接内联到字节码中
  - 不会在内存中单独存储该常量
  - 可以通过`javap -c`查看字节码确认

## 2. 实例基本类型常量

```java
public class Example {
    // 实例基本类型常量
    private final int instanceId = 1;
    private final long timestamp = System.currentTimeMillis();
}
```

- **存储位置**：堆内存（作为对象实例的一部分）
- 特点：
  - 每个对象实例都会有一份
  - 存储在对象头之后的对象实例数据中

## 3. 局部基本类型常量

```java
public void method() {
    final int localConst = 42;
    // ...
}
```

- **存储位置**：Java 虚拟机栈的局部变量表
- 特点：
  - 存储在方法栈帧的局部变量表中
  - 生命周期与方法调用相同

## 4. 基本类型数组中的常量

```java
final int[] numbers = {1, 2, 3, 4, 5};
```

* **存储位置**：堆内存

- 特点：
  - 数组对象存储在堆中
  - 基本类型值直接存储在数组对象中

## 特殊说明

1. **编译时常量**：
   - 如果基本类型常量是编译时常量（使用`static final`修饰，且在编译时能确定值）
   - 会被直接内联到使用处，不会在运行时常量池中存储
2. **运行时常量**：
   - 如果基本类型常量的值在运行时才能确定（如`final int x = new Random().nextInt()`）
   - 会存储在运行时常量池中

## 验证示例

```java
public class PrimitiveConstantExample {
    // 编译时常量
    public static final int COMPILE_TIME_CONST = 100;
    
    // 运行时常量
    public static final long RUNTIME_CONST = System.currentTimeMillis();
    
    public static void main(String[] args) {
        // 局部常量
        final int localConst = 42;
        
        // 实例常量
        final InstanceConstants instance = new InstanceConstants();
    }
}

class InstanceConstants {
    // 实例常量
    private final int instanceId = 1;
}
```

## 总结

| 常量类型         | 存储位置                  | 特点                                       |
| :--------------- | :------------------------ | :----------------------------------------- |
| 静态基本类型常量 | 内联到字节码/运行时常量池 | 编译时常量会内联，运行时常量在运行时常量池 |
| 实例基本类型常量 | 堆内存（对象实例中）      | 每个对象实例一份                           |
| 局部基本类型常量 | 虚拟机栈（局部变量表）    | 方法调用期间有效                           |
| 基本类型数组元素 | 堆内存                    | 作为数组对象的一部分存储                   |

这种存储策略使得基本类型常量的访问非常高效，特别是对于编译时常量，因为它们在编译时就会被直接替换为具体的值。