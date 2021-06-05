package java8;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.Arrays;
import java.util.Comparator;

public class LambdaDemo1 {
    public static void main(String[] args) {
        /**
         * lambda表达式与函数式接口（只有一个方法（函数）的接口）
         */
        Arrays.asList("dabing", "dashan", "yaoyao", "didi").forEach( e -> System.out.println(e));

        //在上面这个代码中的参数e的类型是由编译器推理得出的，你也可以显式指定该参数的类型
        Arrays.asList("dabing", "dashan", "yaoyao", "didi").forEach((String e) -> System.out.println(e));

        //复杂语法块，使用大括号括起来
        Arrays.asList( "a", "b", "d" ).forEach( e -> {
            System.out.print( e );
            System.out.print( e );
        } );

        //Lambda表达式可以引用类成员和局部变量（会将这些变量隐式得转换成final的），例如下列两个代码块的效果完全相同
        String separator = ",";
        Arrays.asList( "a", "b", "d" ).forEach(
                ( String e ) -> System.out.print( e + separator ) );
        final String separator2 = ",";
        Arrays.asList( "a", "b", "d" ).forEach(
                ( String e ) -> System.out.print( e + separator2 ) );

        //Lambda表达式有返回值，返回值的类型也由编译器推理得出。如果Lambda表达式中的语句块只有一行，
        // 则可以不用使用return语句，下列两个代码片段效果相同：
        Arrays.asList( "a", "b", "d" ).sort( ( e1, e2 ) -> e1.compareTo( e2 ) );
//        Arrays.asList( "a", "b", "d" ).sort(Comparator.naturalOrder());
//        Arrays.asList( "a", "b", "d" ).sort(String::compareTo);
        Arrays.asList( "a", "b", "d" ).sort( ( e1, e2 ) -> {
            int result = e1.compareTo( e2 );
            return result;
        } );

        System.out.println("===========================");
        /**
         * 接口中允许有默认方法和静态方法
         */
        Defaulable defaulable = DefaulableFactory.create( DefaultableImpl::new );
        System.out.println( defaulable.notRequired() );

        defaulable = DefaulableFactory.create( OverridableImpl::new );
        System.out.println( defaulable.notRequired() );

    }
}


