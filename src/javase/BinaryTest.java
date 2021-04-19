package javase;

public class BinaryTest {
    public static void main(String[] args) {
        //n1 二进制
        int n1 = 0b110001100;
        //n2 10 进制
        int n2 = 1010;
        //n3 8 进制
        int n3 = 02456;
        //n4 16 进制
        int n4 = 0xA45;
        System.out.println("n1=" + n1);
        System.out.println("n2=" + n2);
        System.out.println("n3=" + n3);
        System.out.println("n4=" + n4);
        System.out.println(0x23A);
        System.out.println("---------");
        System.out.println(2&3);
        System.out.println(~-2);
        System.out.println(~2);
        System.out.println(2|3);
        System.out.println(2^3);
        int a = 0b10 ;
        System.out.println(a);
        int[] arr1 = {1,2,3};
        int[] arr2 = arr1;
        arr2[0] = 2;
//        System.out.println(arr1.toString());
//        System.out.println(arr2.toString());
        for (int i = 0; i < arr1.length; i++) {
            System.out.println(arr1[i]);
        }
        for (int i = 0; i < arr2.length; i++) {
            System.out.println(arr2[i]);
        }
        System.out.println("======");
        int a1 = 10;

        int b1 = a1;
        b1 = 20;
        System.out.println(a1 + "--" + b1);
    }
}
