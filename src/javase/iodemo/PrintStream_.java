package javase.iodemo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author EDing3
 * @create 2021/4/23 10:03
 */
public class PrintStream_ {

    public static void main(String[] args) throws IOException {
        m1();
        m2();

    }

    private static void m2() throws IOException {
        PrintStream out = System.out;
//在默认情况下， PrintStream 输出数据的位置是 标准输出， 即显示器
        //在默认情况下， PrintStream 输出数据的位置是 标准输出， 即显示器
        /**
         * public void print(String s) {
         *         if (s == null) {
         *             s = "null";
         *         }
         *         write(s);
         *     }
         */
        out.print("john, hello");
//因为 print 底层使用的是 write , 所以我们可以直接调用 write 进行打印/输出
        out.write("韩顺平,你好".getBytes());
        out.close();
        //我们可以去修改打印流输出的位置/设备
        //1. 输出修改成到 "e:\\f1.txt"
        //2. "hello, 韩顺平教育~" 就会输出到 e:\f1.txt
        //3. public static void setOut(PrintStream out) {
        // checkIO();
        // setOut0(out); // native 方法， 修改了 out
        // }
        System.setOut(new PrintStream("C:\\temp\\b.txt"));
        System.out.println("hello, 韩顺平教育~");
    }

    private static void m1() throws IOException {
        //    PrintWriter printWriter = new PrintWriter(System.out);
        PrintWriter printWriter = new PrintWriter(new FileWriter("C:\\temp\\b.txt", true));
        printWriter.print("hi, 北京你好~~~~");
        printWriter.close();//flush + 关闭流, 才会将数据写入到文件
    }


}
