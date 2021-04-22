package javase.iodemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopy {
    public static void main(String[] args) {
//        myCopy();
//        laoshiDemo();
        renameFile();

    }

    private static void renameFile() {
        File file1 = new File("C:\\Users\\Alex\\Pictures\\Screenshots\\屏幕截图(4).png");
        System.out.println(file1.getAbsoluteFile());
        File file2 = new File("C:\\Users\\Alex\\Pictures\\Screenshots\\屏幕(3).png");
        //rename()方法要求file1一定存在，file2一定不存在
        Boolean b = file1.renameTo(file2);
        System.out.println(b);
    }

    private static void laoshiDemo() {
        //完成 文件拷贝， 将 e:\\Koala.jpg 拷贝 c:\\
        //思路分析
        //1. 创建文件的输入流 , 将文件读入到程序
        //2. 创建文件的输出流， 将读取到的文件数据， 写入到指定的文件.
        String srcFilePath = "C:\\Users\\Alex\\Pictures\\Screenshots\\屏幕截图(3).png";
        String destFilePath = "C:\\Users\\Alex\\Pictures\\Screenshots\\屏幕截图(5).png";
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(srcFilePath);
            fileOutputStream = new FileOutputStream(destFilePath);
            //定义一个字节数组,提高读取效果
            byte[] buf = new byte[1024];
            int readLen = 0;
            while ((readLen = fileInputStream.read(buf)) != -1) {
            //读取到后， 就写入到文件 通过 fileOutputStream
            //即， 是一边读， 一边写
                fileOutputStream.write(buf, 0, readLen);//一定要使用这个方法
            }
            System.out.println("拷贝 ok~");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭输入流和输出流， 释放资源
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if
                (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static void myCopy() {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream("C:\\Users\\Alex\\Pictures\\Screenshots\\屏幕截图(3).png");
            outputStream = new FileOutputStream("C:\\Users\\Alex\\Pictures\\Screenshots\\屏幕截图(4).png");
            byte[] bytes = new byte[1024];
            int readLength = 0;
            while ((readLength = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, readLength);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
