package javase;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author EDing3
 * @create 2021/4/28 12:30
 */
public class UnSafeDemo {
    public static void main(String[] args) throws Exception {
        Integer[] arr = {2,5,1,8,10};

        //获取Unsafe对象
        Unsafe unsafe = getUnsafe();
        //获取Integer[]的基础偏移量
        int baseOffset = unsafe.arrayBaseOffset(Integer[].class);
        //获取Integer[]中元素的偏移间隔
        int indexScale = unsafe.arrayIndexScale(Integer[].class);

        //获取数组中索引为2的元素对象
        Object o = unsafe.getObjectVolatile(arr, (2 * indexScale) + baseOffset);
        System.out.println(o); //1

        //设置数组中索引为2的元素值为100
        unsafe.putOrderedObject(arr,(2 * indexScale) + baseOffset,100);

        System.out.println(Arrays.toString(arr));//[2, 5, 100, 8, 10]
    }

    //反射获取Unsafe对象
    public static Unsafe getUnsafe() throws Exception {
//        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
//        theUnsafe.setAccessible(true);
//        return (Unsafe) theUnsafe.get(null);
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe)theUnsafe.get("");
        return unsafe;

    }
}
/**
 * //构造方法私有化
 * private Unsafe() {}
 * //采用饿汉式单例
 * private static final Unsafe theUnsafe = new Unsafe();
 *
 * //获取实例对象
 * public static Unsafe getUnsafe() {
 *     //获取调用该方法的调用者信息
 *     Class<?> caller = Reflection.getCallerClass();
 *     //判断该调用这的类加载器是否是系统类加载器(系统类加载器为null)
 *     if (!VM.isSystemDomainLoader(caller.getClassLoader()))
 *         throw new SecurityException("Unsafe");
 *     return theUnsafe;
 * }
 *
 * //返回一个静态字段的偏移量
 * public native long staticFieldOffset(Field f);
 *
 * //返回一个非静态字段字段的偏移量
 * public native long objectFieldOffset(Field f);
 *
 * //获取给定对象指定偏移量的int值
 * public native int getInt(Object o, long offset);
 *
 * //把给定对象指定偏移量上的值设置为整型变量ｘ
 * public native void putInt(Object o, long offset, int x);
 *
 * //获取给定内存地址上的byte值
 * public native byte    getByte(long address);
 *
 * //把给定内存地址上的值设置为byte值ｘ
 * public native void    putByte(long address, byte x);
 *
 * //获取给定内存地址上的值，该值是表示一个内存地址
 * public native long getAddress(long address);
 *
 * //分配一块本地内存，这块内存的大小便是给定的大小．这块内存的值是没有被初始化的
 * public native long allocateMemory(long bytes);
 *
 * //定义一个类
 * public native Class<?> defineClass(String name, byte[] b, int off, int len,
 *                                        ClassLoader loader,
 *                                        ProtectionDomain protectionDomain);
 *
 * //定义一个匿名类
 * public native Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches);
 *
 * //获取给定对象上的锁（jvm中有monitorenter 和 monitorexit两个指令）
 * public native void monitorEnter(Object o);
 *
 * //释放给定对象上的锁（jvm中有monitorenter 和 monitorexit
 * public native void monitorExit(Object o);
 *
 * //CAS操作，修改对象指定偏移量上的（CAS简介见末尾）
 * public final native boolean compareAndSwapObject(Object o, long offset,
 *                                                  Object expected,
 *                                                  Object x);
 *
 * //CAS操作
 * public final native boolean compareAndSwapInt(Object o, long offset,
 *                                                   int expected,
 *                                                   int x);
 *
 * //CAS操作
 * public final native boolean compareAndSwapLong(Object o, long offset,
 *                                                    long expected,
 *                                                    long x);
 *
 * //获取被volatile关键字修饰的字段的值
 * public native Object getObjectVolatile(Object o, long offset);
 *
 * //取消阻塞线程 thread,如果 thread 已经是非阻塞状态，
 * //那么下次对该 thread 进行park操作时就不会阻塞
 * public native void unpark(Object thread);
 *
 * //阻塞当前线程，isAbsolute的作用不是很清楚．（如果该方法调用前在线程非阻塞情况下调用了unpart方法，那么此次调用该方法不会令当前线程阻塞）
 * public native void park(boolean isAbsolute, long time);
 *
 * //CAS操作
 * public final int getAndAddInt(Object o, long offset, int delta) {
 *         int v;
 *         do {
 *             v = getIntVolatile(o, offset);
 *         } while (!compareAndSwapInt(o, offset, v, v + delta));
 *         return v;
 * }
 * ————————————————
 * 版权声明：本文为CSDN博主「RockyPeng3」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/wilsonpeng3/article/details/46387515
 *
 *
 * Field.get(null)
 *
 * lppl010_ 2019-08-14 23:02:12  226  收藏
 * 分类专栏： JVM
 * 版权
 * https://blog.csdn.net/u010286334/article/details/72932307
 *
 *
 *
 * 1、field.get(null)
 *
 *
 *
 *        public static void main(String[] args){
 * Field field=MainTest.class.getDeclaredField("name"); //static
 * field.setAccessible(true);
 * Object o=field.get(null);
 * System.out.println("o="+o);  //输出string
 * }
 * public static String name="string";
 *
 *
 * 1)如果field的name是一个static的变量，field.get(param)，param是任意的都可以，返回类中当前静态变量的值。
 *
 * 2)如果是非静态变量，field.get(obj)，obj必须是当前类的实例对象，返回实例对象obj的变量值。
 *
 * 2、field.set(null,string)
 *
 * 同上，field是一个静态变量时，会修改当前类中该变量的值为string；
 *
 * field是非静态变量，则需要field(obj,string)，则修改obj这个实例对象中的field的值为string。
 *
 */
