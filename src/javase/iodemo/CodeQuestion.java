package javase.iodemo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author EDing3
 * @create 2021/4/23 9:54
 *  看一个中文乱码问题
 */
public class CodeQuestion {
    public static void main(String[] args) throws IOException {
        //读取 e:\\a.txt 文件到程序
        //思路
        //1. 创建字符输入流 BufferedReader [处理流]
        //2. 使用 BufferedReader 对象读取 a.txt
        //3. 默认情况下， 读取文件是按照 utf-8 编码
        String filePath = "C:\\temp\\a.txt";
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String s = br.readLine();
        System.out.println("读取到的内容: " + s);
        br.close();
        //InputStreamReader
        //OutputStreamWriter
    }
}
