package javase.filedemo;

import java.io.File;

/**
 * @author EDing3
 * @create 2021/4/22 16:10
 */
public class FileInformation {
    public static void main(String[] args) {
        File file = new File("C:\\temp\\a.txt");
        //调用相应的方法， 得到对应信息
        System.out.println("文件名字=" + file.getName());
//getName、 getAbsolutePath、 getParent、 length、 exists、 isFile、 isDirectory
        System.out.println("文件绝对路径=" + file.getAbsolutePath());
        System.out.println("文件父级目录=" + file.getParent());
        System.out.println("文件大小(字节)=" + file.length());
        System.out.println("文件是否存在=" + file.exists());//T
        System.out.println("是不是一个文件=" + file.isFile());//T
        System.out.println("是不是一个目录=" + file.isDirectory());//F
//        new File("c:\\temp\\aaaa.txt").deleteOnExit();
//        new File("c:\\temp\\aaaa.txt").delete();
        File file1 = new File("c:\\temp\\aaaa\\bbb");
        if(file1.exists()) {
            System.out.println("alread exists");
        }else {
            file1.mkdirs();
        }
    }
}
