package collection_study.list;

/**
 * @author EDing3
 * @create 2021/4/16 11:40
 */
public class LinkedList01 {
    public static void main(String[] args) {
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        a.next = b;
        b.next = c;
        c.prev = b;
        b.prev = a;
        System.out.println("========从头到尾遍历双向链表：");
        Node first = a;
        while (true) {
            if(first == null) {
                System.out.println("遍历结束");
                break;
            }
            System.out.println(first);
            first = first.next;
        }
        System.out.println("=======从尾到头遍历双向链表：");
        Node last = c;
        while (true) {
            if(last == null) {
                System.out.println("遍历结束");
                break;
            }
            System.out.println(last);
            last = last.prev;
        }

        System.out.println("-------------------------");
        Node tom = new Node("tom");
        tom.next = c;
        tom.prev = b;
        b.next = tom;
        c.prev = tom;
        System.out.println("========从头到尾遍历双向链表：");
        first = a;
        while (true) {
            if(first == null) {
                System.out.println("遍历结束");
                break;
            }
            System.out.println(first);
            first = first.next;
        }


        System.out.println("=======从尾到头遍历双向链表：");
        last = c;
        while (true) {
            if(last == null) {
                System.out.println("遍历结束");
                break;
            }
            System.out.println(last);
            last = last.prev;
        }
    }
}

class Node {
    public Object item;
    public Node prev;
    public Node next;
    public Node(Object name) {
        this.item = name;
    }

    @Override
    public String toString() {
        return "Node name = " + item;
    }
}
