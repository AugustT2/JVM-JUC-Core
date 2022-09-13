import javase.iodemo.FileInputStream_;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


public class ListUtil {
    public static void main(String[] args) throws IOException {

        List<Integer> integers=new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        integers.add(6);
        integers.add(7);
        integers.add(8);
        integers.add(9);
        integers.add(10);
        integers.add(11);
        integers.add(12);
        integers.add(13);
        integers.add(14);
        integers.add(15);
        integers.add(16);
        List<List<Integer>> lists=averageAssign(integers, 7);
        System.out.println(lists);

        String abc = "A_B_C_6";
        System.out.println(abc.substring(abc.length()-1));

        //方法一，获取绝对路径后再io流操作
        String path=Thread.currentThread().getContextClassLoader().getResource("mysql.properties").getPath();
        System.out.println("path:"+ path);
        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        Properties properties = new Properties();
        properties.load(bufferedInputStream);
        System.out.println(properties.get("ip"));

        //方法二，利用ResourceBundle， 注意配置文件需要以properties结尾，传进去的参数不要带properties
        ResourceBundle bundle=ResourceBundle.getBundle("mysql", Locale.ENGLISH);
        String className=bundle.getString("ip");
        System.out.println(className);

        for (int i = 0; i < 10; i++) {
            System.out.println(Math.random());
            int code = (int) ((Math.random() * 9 + 1) * 100000);
            System.out.println(code);
        }

        System.out.println(System.currentTimeMillis());
    }





    /**
     * 将一个list均等分成n个list,主要通过偏移量来实现的
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n){
        List<List<T>> result=new ArrayList<List<T>>();
        int remaider=source.size()%n;  //(先计算出余数)
        int number=source.size()/n;  //然后是商
        int offset=0;//偏移量
        for(int i=0;i<n;i++){
            List<T> value=null;
            if(remaider>0){
                value=source.subList(i*number+offset, (i+1)*number+offset+1);
                remaider--;
                offset++;
            }else{
                value=source.subList(i*number+offset, (i+1)*number+offset);
            }
            result.add(value);
        }
        return result;
    }
}
