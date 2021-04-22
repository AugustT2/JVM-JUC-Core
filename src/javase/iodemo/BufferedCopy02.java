package javase.iodemo;

import java.io.*;

/**
 * @author 韩顺平
 * @version 1.0
 * 演示使用 BufferedOutputStream 和 BufferedInputStream 使用
 * 使用他们， 可以完成二进制文件拷贝.
 * 思考： 字节流可以操作二进制文件， 可以操作文本文件吗？ 当然可以
 */
public class BufferedCopy02 {
    public static void main(String[] args) {
        // String srcFilePath = "e:\\Koala.jpg";
        // String destFilePath = "e:\\hsp.jpg";
        // String srcFilePath = "e:\\0245_韩顺平零基础学 Java_引出 this.avi";
        // String destFilePath = "e:\\hsp.avi";
        String srcFilePath = "e:\\a.java";
        String destFilePath = "e:\\a3.java";
        //创建 BufferedOutputStream 对象 BufferedInputStream 对象
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            //因为 FileInputStream 是 InputStream 子类
            bis = new BufferedInputStream(new FileInputStream(srcFilePath));
            bos = new BufferedOutputStream(new FileOutputStream(destFilePath));
            //循环的读取文件， 并写入到 destFilePath
            byte[] buff = new byte[1024];
            int readLen = 0;
            //当返回 -1 时， 就表示文件读取完毕
            while ((readLen = bis.read(buff)) != -1) {
                bos.write(buff, 0, readLen);
            }
            System.out.println("文件拷贝完毕~~~");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流 , 关闭外层的处理流即可， 底层会去关闭节点流
            try {
                if (bis != null) {
                    bis.close();
                }
                if (
                        bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
