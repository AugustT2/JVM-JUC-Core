package java8;

import java.lang.annotation.*;

/**
 * 正如我们所见，这里的Filter类使用@Repeatable(Filters.class)注解修饰，
 * 而Filters是存放Filter注解的容器，编译器尽量对开发者屏蔽这些细节。
 * 这样，Filterable接口可以用两个Filter注解注释（这里并没有提到任何关于Filters的信息）。
 * 当然也可以用Filtes注解@Filters(value= {@Filter( "filter1" ), @Filter( "filter2" )})，只能2选1
 * 同@ComponentScans(value = {@ComponentScan("com.bingo.spring.controller"),@ComponentScan("com.bingo.spring.beans")})
 */
public class RepeatingAnnotations {
    @Target( ElementType.TYPE )
    @Retention( RetentionPolicy.RUNTIME )
    public @interface Filters {
        Filter[] value();
    }

    @Target( ElementType.TYPE )
    @Retention( RetentionPolicy.RUNTIME )
    @Repeatable( Filters.class )
    public @interface Filter {
        String value();
    };

    @Filter( "filter1" )
    @Filter( "filter2" )
//    @Filters(value= {@Filter( "filter1" ), @Filter( "filter2" )})
    public interface Filterable {
    }

    public static void main(String[] args) {
        for( Filter filter: Filterable.class.getAnnotationsByType( Filter.class ) ) {
            System.out.println( filter.value() );
        }
//        filter1
//        filter2
    }
}
