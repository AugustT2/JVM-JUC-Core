package season3.mianxiangduixiang;

/**
 * @author EDing3
 * @create 2021/4/19 15:57
 */
public class InnerOutter {

    private void out() {
        System.out.println("out");

    }

    private void out2() {
         class Inner {
            private void inner() {
                System.out.println("in");
                out();
            }
        }

        Inner inner = new Inner();
         inner.inner();
    }




}
