package javase.arraydemo;

import java.util.Arrays;
import java.util.List;

public class ArraysMethod02 {
    public static void main(String[] args) {
        Integer[] arr = {1, 2, 90, 123, 567};
// binarySearch 通过二分搜索法进行查找， 要求必须排好
// 老韩解读
//1. 使用 binarySearch 二叉查找
//2. 要求该数组是有序的. 如果该数组是无序的， 不能使用 binarySearch
//3. 如果数组中不存在该元素， 就返回 return -(low + 1); // key not found.
        int index = Arrays.binarySearch(arr, 57);
        System.out.println("index=" + index);
//copyOf 数组元素的复制
// 老韩解读
//1. 从 arr 数组中， 拷贝 arr.length 个元素到 newArr 数组中
//2. 如果拷贝的长度 > arr.length 就在新数组的后面 增加 null
//3. 如果拷贝长度 < 0 就抛出异常 NegativeArraySizeException
//4. 该方法的底层使用的是 System.arraycopy()
        Integer[] newArr = Arrays.copyOf(arr, arr.length);
        System.out.println("==拷贝执行完毕后==");
        System.out.println(Arrays.toString(newArr));
//ill 数组元素的填充
        Integer[] num = new Integer[]{9,3,2};
//老韩解读
//1. 使用 99 去填充 num 数组， 可以理解成是替换原理的元素
        Arrays.fill(num, 99);
        System.out.println("==num 数组填充后==");
        System.out.println(Arrays.toString(num));
//equals 比较两个数组元素内容是否完全一致,顺序不一致也是错
        Integer[] arr2 = {1, 2, 90, 123};
        Integer[] arr3 = {2, 1, 90, 123};
        System.out.println(Arrays.equals(arr2, arr3));
//老韩解读
//1. 如果 arr 和 arr2 数组的元素一样， 则方法 true;
//2. 如果不是完全一样， 就返回 false
        boolean equals = Arrays.equals(arr, arr2);
        System.out.println("equals=" + equals);
//asList 将一组值， 转换成 list
//老韩解读
//1. asList 方法， 会将 (2,3,4,5,6,1)数据转成一个 List 集合
//2. 返回的 asList 编译类型 List(接口)
//3. asList 运行类型 java.util.Arrays#ArrayList, 是 Arrays 类的
// 静态内部类 private static class ArrayList<E> extends AbstractList<E>
// implements RandomAccess, java.io.Serializable
        List asList = Arrays.asList(2,3,4,5,6,1);
        System.out.println("asList=" + asList);
        System.exit(0);
        System.out.println("asList 的运行类型" + asList.getClass());
    }
}
