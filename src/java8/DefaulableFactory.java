package java8;

import java.util.Collection;
import java.util.function.Supplier;

//Java 8带来的另一个有趣的特性是在接口中可以定义静态方法
public class DefaulableFactory {
    // Interfaces now allow static methods
    static Defaulable create( Supplier< Defaulable > supplier ) {
        return supplier.get();
    }

}
