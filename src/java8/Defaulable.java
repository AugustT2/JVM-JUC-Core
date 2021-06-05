package java8;

public interface Defaulable {
    // Interfaces now allow default methods, the implementer may or
    // may not implement (override) them.
    //接口中可以有默认方法
    default String notRequired() {
        return "Default implementation";
    }

    default void notNedd() {
        System.out.println("override if necessary");
    }
}
