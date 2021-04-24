package javase.collection_study.list;

import java.util.ArrayDeque;

public class ArrayDequeDemo {
    public static void main(String[] args) {
        /**
         * 1.public class ArrayDeque<E> extends AbstractCollection<E>
         *                            implements Deque<E>, Cloneable, Serializable
         * 2.public interface Deque<E> extends Queue<E>
         *
         */

        ArrayDeque<Integer> stack = new ArrayDeque<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.add(4);
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.peek());
    }
}
