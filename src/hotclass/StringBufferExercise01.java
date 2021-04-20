package hotclass;

/**
 * @author EDing3
 * @create 2021/4/20 15:02
 */
public class StringBufferExercise01 {
    public static void main(String[] args) {
        String str = null;// ok
        StringBuffer sb = new StringBuffer(); //ok
        System.out.println(sb);
        sb.append(str);//需要看源码 , 底层调用的是 AbstractStringBuilder 的 appendNull
        System.out.println(sb.length());//4
        System.out.println(sb.toString().intern() == "null");
        System.out.println(sb);//null

        //下面的构造器， 会抛出 NullpointerException
        StringBuffer sb1 = new StringBuffer(str);//看底层源码 super(str.length() + 16);
        System.out.println(sb1);
    }
}
