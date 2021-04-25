package javase.iodemo;

import java.io.File;
import java.io.IOException;

/**
 * @author EDing3
 * @create 2021/4/22 15:07
 */
public class FileCreate {
    public static void main(String[] args) {
//        create01();
        create03();

        // File的两个常量

        System.out.println(File.pathSeparator); // ;

        System.out.println(File.separator); // \

    }

    //方式 1 new File(String pathname)
    public static void create01() {
//        String filePath = "e:\\news1.txt";
        String filePath = "C:\\temp\\a.TEXT";
        File file = new File(filePath);
        try {
            file.createNewFile();
            System.out.println("文件创建成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //方式 2 new File(File parent,String child) //根据父目录文件+子路径构建
    //e:\\news2.txt
    public void create02() {
        File parentFile = new File("e:\\");
        String fileName = "news2.txt";
        //这里的 file 对象， 在 java 程序中， 只是一个对象
        //只有执行了 createNewFile 方法， 才会真正的， 在磁盘创建该文件
        File file = new File(parentFile, fileName);
        try {
            file.createNewFile();
            System.out.println("创建成功~");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //方式 3 new File(String parent,String child) //根据父目录+子路径构建
    public static void create03() {
        //String parentPath = "e:\\";
        String parentPath = "C:\\temp";
        String fileName = "aaaa.txt";
        File file = new File(parentPath, fileName);
        try {
            file.createNewFile();
            System.out.println("创建成功~");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //下面四个都是抽象类
//InputStream
//OutputStream
//Reader //字符输入流
//Writer //字符输出流

}
