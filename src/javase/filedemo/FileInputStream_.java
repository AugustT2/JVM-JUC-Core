package javase.filedemo;

import java.io.*;

/**
 * @author EDing3
 * @create 2021/4/22 16:41
 */
public class FileInputStream_ {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String a = "一";
        System.out.println("UTF-8编码长度:" + a.getBytes("UTF-8").length);
        System.out.println("GBK编码长度:" + a.getBytes("GBK").length);
        System.out.println("GB2312编码长度:" + a.getBytes("GB2312").length);
        System.out.println("==========================================");

        readFile01();
        readFile02();
        writeFile();
        writeMe();
    }

    /*
     *
     * 演示读取文件...
     * 单个字节的读取， 效率比较低
     * -> 使用 read(byte[] b)
     */

    public static void readFile01() {
        String filePath = "C:\\temp\\b.txt";
        int readData = 0;
        FileInputStream fileInputStream = null;
        try {
            //创建 FileInputStream 对象， 用于读取 文件
            fileInputStream = new FileInputStream(filePath);
            //从该输入流读取一个字节的数据。 如果没有输入可用， 此方法将阻止。
            //如果返回-1 , 表示读取完毕
            while ((readData = fileInputStream.read()) != -1) {
                System.out.print((char) readData);//转成 char 显示
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件流， 释放资源
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } /*
     *
     * 使用 read(byte[] b) 读取文件， 提高效率
     */

    public static void readFile02() {
        String filePath = "C:\\temp\\b.txt";
        //字节数组
        byte[] buf = new byte[8]; //一次读取 8 个字节.
        int readLen = 0;
        FileInputStream fileInputStream = null;
        try {
            //创建 FileInputStream 对象， 用于读取 文件
            fileInputStream = new FileInputStream(filePath);
            //从该输入流读取最多 b.length 字节的数据到字节数组。 此方法将阻塞， 直到某些输入可用。
            //如果返回-1 , 表示读取完毕
            //如果读取正常, 返回实际读取的字节数
            while ((readLen = fileInputStream.read(buf)) != -1) {

                System.out.print(new String(buf, 0, readLen));//显示
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件流， 释放资源.
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     *
     * 演示使用 FileOutputStream 将数据写到文件中,
     * 如果该文件不存在， 则创建该文件
     */
    public static void writeFile() {
//创建 FileOutputStream 对象
        String filePath = "C:\\temp\\c.txt";
        FileOutputStream fileOutputStream = null;
        try {
        //得到 FileOutputStream 对象 对象
        //老师说明
        //1. new FileOutputStream(filePath) 创建方式， 当写入内容是， 会覆盖原来的内容
        //2. new FileOutputStream(filePath, true) 创建方式， 当写入内容是， 是追加到文件后面
            fileOutputStream = new FileOutputStream(filePath, true);
            //写入一个字节
            //fileOutputStream.write('H');//
            //写入字符串
            String str = "hsp,world!";
            //str.getBytes() 可以把 字符串-> 字节数组
            //fileOutputStream.write(str.getBytes());
            /*
            write(byte[] b, int off, int len) 将 len 字节从位于偏移量 off 的指定字节数组写入此文件输出流
            */
            fileOutputStream.write(str.getBytes(), 0, 3);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeMe() {
        OutputStream os = null;
        try {
            os = new FileOutputStream("c:\\temp\\d.txt", true);
            os.write('a');
            os.write("chenhuan".getBytes(), 4, 4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

}
