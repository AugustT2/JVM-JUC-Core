/*
 *
 * This software is the confidential and proprietary information of sipai Company.
 *
 */
package javase.iodemo;

import java.io.File;

/**
 * TODO
 *
 * @author chuan2021
 * @version V1.0
 * @since 2021-11-17 21:42
 */
public class FileConvertUtil {

    public static void main(String[] args) {
        //文件重命名：txt转为md
        RenameTxtToMd();
    }

    //文件重命名：txt转为md
    private static void RenameTxtToMd() {
        String path = "C:\\Users\\chuan2021\\Downloads\\谷粒21-10";
        File inputFile = new File(path);
        File[] files = inputFile.listFiles();
        for (File file : files) {
            String fileName = file.getName();
            if(fileName.endsWith(".txt")) {
                fileName = fileName.replace(".txt", ".md");
                //不用File.separator来拼接路径会把文件生成在项目根路径
                String newPathName = path+File.separator+fileName;
                File targetFile = new File(newPathName);
                boolean b = file.renameTo(targetFile);
                System.out.println(file.getName() + "=====》变成了===》"+ newPathName);
            }
        }
    }
}
