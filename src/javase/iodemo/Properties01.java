package javase.iodemo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author EDing3
 * @create 2021/4/23 10:22
 */
public class Properties01 {
    public static void main(String[] args) throws IOException {
        method01();
        //使用 Properties 类来读取 mysql.properties 文件
        //1. 创建 Properties 对象
        Properties properties = new Properties();
        //2. 加载指定配置文件
        properties.load(new FileReader("src\\mysql.properties"));
        //3. 把 k-v 显示控制台
        properties.list(System.out);
        //4. 根据 key 获取对应的值
        String user = properties.getProperty("username");
        String pwd = properties.getProperty("password");
        System.out.println("用户名=" + user);
        System.out.println("密码是=" + pwd);
    }

    private static void method01() throws IOException {
        //读取 mysql.properties 文件， 并得到 ip, user 和 pwd
        BufferedReader br = new BufferedReader(new FileReader("src\\mysql.properties"));
        String line = "";
        while ((line = br.readLine()) != null) { //循环读取
            String[] split = line.split("=");
        //如果我们要求指定的 ip 值
            if("ip".equals(split[0])) {
                System.out.println(split[0] + "值是: " + split[1]);
            }
            System.out.println(split[0] + "值是: " + split[1]);
        }
        br.close();
    }
}
