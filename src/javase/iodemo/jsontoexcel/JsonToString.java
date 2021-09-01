package javase.iodemo.jsontoexcel;

import java.io.File;

/**
 * @author EDing3
 * @create 2021/6/1 12:48
 * 将项目放入Springboot项目中执行，JSON文件转excel
 */
public class JsonToString {
//    public static void main(String[] args) throws IOException {
//        File inputFile = new File("C:\\logs\\aa");
//        File[] files = inputFile.listFiles();
//        //jackson的jar包
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<PoushengOrderConfirmation> allData = new ArrayList<>();
//        for (File file : files) {
//            String jsonStr = ConvertUtil.convertTextBodyToBean(new InputStreamReader(new FileInputStream(file)));
//            List<PoushengOrderConfirmation> temp = objectMapper.readValue(jsonStr,new TypeReference<List<PoushengOrderConfirmation>>() { });
//            allData.addAll(temp);
//        }
//        String fileName = "C:/logs/bb/cc.xlsx";
//        //导easyexcel的jar包
//        EasyExcel.write(fileName, PoushengOrderConfirmation.class).sheet("模板").doWrite(allData);
//    }
    public static void main(String[] args) {
        renameFiles();
//        reNameFiles();

    }

    private static void reNameFiles() {
        String srcFile = "C:\\Users\\chuan2021\\Downloads\\guli-han个人笔记-21-05(勿传播)";
        //        reName(filePath);

        //        String srcFile="F://datas";
        File file=new File(srcFile);
        //        isDirectory方法判断这个路径是不是一个文件夹
        if(!file.isDirectory()){
            System.out.println("这不是一个目录");
        }
        //        读取文件夹下的文件，存进一个文件数组
        File[] listfile=file.listFiles();
        //        挨个遍历，重命名
        for (int i = 0; i < listfile.length; i++) {
            if(listfile[i].isFile()){
                String filename= i + ".csv" ;
                File refile=new File(srcFile+File.separator+filename);
                listfile[i].renameTo(refile);
            }
        }
        System.out.println("okok");
    }

    private static void renameFiles() {
        String path = "C:\\Users\\chuan2021\\Downloads\\guli-han个人笔记-21-05(勿传播)";
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

    static boolean reName(String filePath) {


        File file = new File(filePath);
        String AD = "来一波强势的广告";
        String name = "";
        File[] list = file.listFiles();
        for (int i = 0; i < list.length; i++) {
            name = filePath+AD+list[i].getName();
            File file2 = new File(name);
            list[i].renameTo(file2);
        }
        return false;
    }


}
