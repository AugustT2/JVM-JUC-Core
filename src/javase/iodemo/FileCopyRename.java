package javase.iodemo;

import java.io.*;

/**
 * 将单级目录H:\\aa  目录下，.java   复制到   h:\\bb,并重命名为.jdg文件
 */

public class FileCopyRename {
    public static void main(String[] args) throws IOException {

        File inputFile = new File("h:\\aa");
        File outputFileFolder = new File("h:\\bb");

        if(!outputFileFolder.exists()){
            outputFileFolder.mkdir();   //若没有指定文件夹，就创建
        }
        File[] files = inputFile.listFiles(new FilenameFilter() {   //条件判断子目录

            @Override
            public boolean accept(File dir, String name) {
                // TODO Auto-generated method stub
                return new File(dir,name).isFile() && name.endsWith(".java");
            }
        });

        for (File file : files) {
            String name = file.getName();
            File outputFile = new File(outputFileFolder,name);
            copyFile(file,outputFile);
        }

        files = outputFileFolder.listFiles();   //获取复制完的目标目录下的子文件
        for (File file : files) {

            renameFile(outputFileFolder,file);

        }


    }

    private static void renameFile(File outputFileFolder, File file) {
        // TODO Auto-generated method stub
        String name = file.getName().replace(".java", ".jdg");
        File newNameFile =new File(outputFileFolder,name);

        file.renameTo(newNameFile);

    }

    private static void copyFile(File inputFile, File outputFile) throws IOException {
        // TODO Auto-generated method stub

        BufferedInputStream bis=new BufferedInputStream(new FileInputStream(inputFile));
        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(outputFile));

        byte[] b=new byte[1024];
        int len=0;
        while((len = bis.read(b))!= -1){
            bos.write(b, 0, len);
        }

        bis.close();
        bos.close();

    }

//    版权声明：本文为CSDN博主「Blog_Zyx」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//    原文链接：https://blog.csdn.net/Blog_Zyx/article/details/76714271
}
