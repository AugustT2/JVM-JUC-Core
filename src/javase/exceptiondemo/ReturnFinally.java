package javase.exceptiondemo;

/**
 * 演示try-catch-finally中return语句的行为
 */
public class ReturnFinally {
    public static void main(String[] args) {
        // 1. finally 块总是会执行
        System.out.println("=== 1. finally 块总是会执行 ===");
        try {
            System.out.println("执行try块");
            int result = 10 / 2; // 不会抛出异常
        } finally {
            System.out.println("finally块被执行");
        }

        // 2. try 和 catch 中有 return 时，finally 仍然会执行
        System.out.println("\n=== 2. try 中有 return，finally 仍然执行 ===");
        System.out.println("test1() 返回值: " + test1());

        // 3. 返回值在 finally 执行前确定
        System.out.println("\n=== 3. 返回值在 finally 执行前确定 ===");
        System.out.println("test2() 返回值: " + test2());

        // 4. finally 中修改基本数据类型不会影响返回值
        System.out.println("\n=== 4. finally 中修改基本数据类型 ===");
        System.out.println("test3() 返回值: " + test3());

        // 5. finally 中修改引用类型会影响返回值
        System.out.println("\n=== 5. finally 中修改引用类型 ===");
        System.out.println("test4() 返回值: " + test4().getValue());

        // 6. finally 中有 return 会覆盖之前的返回值（不推荐）
        System.out.println("\n=== 6. finally 中有 return 会覆盖返回值 ===");
        System.out.println("test5() 返回值: " + test5());

        // 7. catch 和 finally 中都有 return
        System.out.println("\n=== 7. catch 和 finally 中都有 return ===");
        try {
            System.out.println("test6() 返回值: " + test6());
        } catch (Exception e) {
            System.out.println("捕获到异常: " + e.getMessage());
        }
    }

    // 示例2: try 中有 return，finally 仍然会执行
    static int test1() {
        try {
            System.out.println("test1 - 执行try块");
            return 1;
        } finally {
            System.out.println("test1 - 执行finally块");
        }
    }

    // 示例3: 返回值在 finally 执行前确定
    static int test2() {
        int i = 0;
        try {
            System.out.println("test2 - try块中 i = " + i);
            return i; // 返回值0被保存，finally执行完毕后返回
        } finally {
            i = 100; // 修改i不会影响返回值
            System.out.println("test2 - finally块中 i = " + i);
        }
    }

    // 示例4: finally 中修改基本数据类型
    static int test3() {
        int result = 10;
        try {
            System.out.println("test3 - try块中 result = " + result);
            return result;
        } finally {
            result = 20; // 不会影响返回值
            System.out.println("test3 - finally块中 result = " + result);
        }
    }

    // 示例5: finally 中修改引用类型
    static ValueHolder test4() {
        ValueHolder holder = new ValueHolder(10);
        try {
            System.out.println("test4 - try块中 value = " + holder.getValue());
            return holder;
        } finally {
            holder.setValue(20); // 修改对象状态会影响返回值
            System.out.println("test4 - finally块中 value = " + holder.getValue());
        }
    }

    // 示例6: finally 中有 return（不推荐）
    static int test5() {
        try {
            System.out.println("test5 - 执行try块");
            return 1;
        } finally {
            System.out.println("test5 - 执行finally块并返回2");
            return 2; // 会覆盖try中的返回值
        }
    }

    // 示例7: catch 和 finally 中都有 return
    static int test6() {
        try {
            System.out.println("test6 - 执行try块");
            int result = 10 / 0; // 抛出异常
            return 1;
        } catch (ArithmeticException e) {
            System.out.println("test6 - 捕获到异常");
            return 2; // 这个返回值会被finally中的return覆盖
        } finally {
            System.out.println("test6 - 执行finally块并返回3");
            return 3; // 会覆盖catch中的返回值
        }
    }

    static class ValueHolder {
        private int value;

        public ValueHolder(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
