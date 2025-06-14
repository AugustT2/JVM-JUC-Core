package jvm.learnjvm;

public class BootstrapClassLoaderDemo {
    public static void main(String[] args) {
        // 获取 String 类的类加载器
        ClassLoader stringClassLoader = String.class.getClassLoader();
        System.out.println("String 类的类加载器: " + stringClassLoader);
        // 输出: null (表示由 Bootstrap ClassLoader 加载)

        // 获取核心类的类加载器
        ClassLoader intClassLoader = int.class.getClassLoader();
        System.out.println("int 的类加载器: " + intClassLoader);
        // 输出: null (基本类型由 JVM 创建，没有类加载器)

        // 获取 Bootstrap ClassLoader 加载的类路径
        System.out.println("Bootstrap ClassLoader 加载路径: " +
        System.getProperty("sun.boot.class.path"));

        // 或者使用以下方式获取更详细的信息
        System.out.println("\njava.ext.dirs: " + System.getProperty("java.ext.dirs"));
        System.out.println("java.class.path: " + System.getProperty("java.class.path"));
    }
}
