package jvm.learnjvm;

import java.util.ArrayList;
import java.util.List;

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
//         CONSTANT_LIST = new ArrayList<>();  // 编译错误

        // 但可以修改CONSTANT_LIST引用的对象内容
        CONSTANT_LIST.add("item");  // 允许

        // 字符串常量不可变
//         GREETING = "Hi";  // 编译错误
    }
}
