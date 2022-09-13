package javase.iodemo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FilesWalk {
    public static void main(String[] args) throws IOException {
        String dir = "D:\\迅雷下载\\代码";
        Stream<Path> walk = Files.walk(Paths.get(dir), 2);
        walk.forEach(filepath -> {
            System.out.println(filepath);
            /**
             * D:\迅雷下载\代码
             * D:\迅雷下载\代码\eladmin-master.zip
             * D:\迅雷下载\代码\eladmin-qd-master.zip
             * D:\迅雷下载\代码\先看源码说明.docx
             */
        });

        Stream<Path> walk2 = Files.walk(Paths.get(dir));
        walk2.forEach(filePath -> {
            if(Files.isRegularFile(filePath)) {
                System.out.println(filePath);
                /**
                 * D:\迅雷下载\代码\eladmin-master.zip
                 * D:\迅雷下载\代码\eladmin-qd-master.zip
                 * D:\迅雷下载\代码\先看源码说明.docx
                 */
            }
        });
    }
}
