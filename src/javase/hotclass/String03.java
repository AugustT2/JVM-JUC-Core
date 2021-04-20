package javase.hotclass;

/**
 * @author EDing3
 * @create 2021/4/20 14:41
 */
public class String03 {
    public static void main(String[] args) {
        // 5.split 分割字符串, 对于某些分割字符， 我们需要 转义比如 | \\等
        String poem = "锄禾日当午,汗滴禾下土,谁知盘中餐,粒粒皆辛苦";
        //老韩解读：
        // 1. 以 , 为标准对 poem 进行分割 , 返回一个数组
        // 2. 在对字符串进行分割时， 如果有特殊字符， 需要加入 转义符 \
        String[] split = poem.split(",");
        System.out.println("==分割后内容===");
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }
        poem = "E:\\aaa\\bbb";
        split = poem.split("\\\\");
        System.out.println("==分割后内容===");
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }


        /* 占位符有:
        * %s 字符串 %c 字符 %d 整型 %.2f 浮点型
        *
        * */
        String name = "john";
        int age = 10;
        double score = 56.857;
        char gender = '男';
        //老韩解读
        //1. %s , %d , %.2f %c 称为占位符
        //2. 这些占位符由后面变量来替换
        //3. %s 表示后面由 字符串来替换
        //4. %d 是整数来替换
        //5. %.2f 表示使用小数来替换， 替换后， 只会保留小数点两位, 并且进行四舍五入的处理
        //6. %c 使用 char 类型来替换
        String formatStr = "我的姓名是%s 年龄是%d， 成绩是%.2f 性别是%c.希望大家喜欢我！ ";
        String info2 = String.format(formatStr, name, age, score, gender);
//        info2 = String.format("woshi :%sdbsfsdfs", name);
        System.out.println("info2=" + info2);
        compare666();
    }

    private static void compare666() {
        String s1 = "自由之路";
//s2也指向字符串常量池中的"自由之路"
        String s2 = "自由之路";
//s3指向堆中的某个对象
        String s3 = new String("自由之路");
//因为字符串常量池中已经存在"自由之路"的引用，直接返回这个引用
        String s4 = s3.intern();

//创建一个字符串对象
        String s5 = new String("ddd");
//常量池中不存在指向"ddd"的引用，创建一个"ddd"对象，并将其引用存入常量池
        String s6 = s5.intern();
//创建一个字符串对象
        String s7 = new String("ddd");
//常量池中存在指向"ddd"的引用，直接返回
        String s8 = s7.intern();

        System.out.println("s1==s2:"+(s1==s2));
        System.out.println("s1==s3:"+(s1==s3));
        System.out.println("s1==s4:"+(s1==s4));

        System.out.println("s5==s6:"+(s5==s6));
        System.out.println("s6==s8:"+(s6==s8));
        System.out.println("s7==s8:"+(s7==s8));
    }


}
