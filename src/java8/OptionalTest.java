package java8;

import java.util.Optional;

public class OptionalTest {
    public static void main(String[] args) {
        /**
         * 如果Optional实例持有一个非空值，则isPresent()方法返回true，否则返回false；
         * orElseGet()方法，Optional实例持有null，则可以接受一个lambda表达式生成的默认值；
         * orElse()方法与orElseGet()方法类似，但是在持有null的时候返回传入的默认值。
         * map()方法可以将现有的Opetional实例的值转换成新的值；
         */
        Optional< String > fullName = Optional.ofNullable( null );
        /**
         * public static <T> Optional<T> ofNullable(T value) {
         *         return value == null ? empty() : of(value);
         *     }
         */

        System.out.println( "Full Name is set? " + fullName.isPresent() );
        System.out.println( "Full Name: " + fullName.orElseGet( () -> "[none]" ) );
        System.out.println("Full Name: " + fullName.orElse("nbaaaa"));
        /**
         * public T orElseGet(Supplier<? extends T> other) {
         *         return value != null ? value : other.get();
         *     }
         */
        System.out.println( fullName.map( s -> "Hey " + s + "!" ).orElse( "Hey Stranger!" ) );
        /**
         * public<U> Optional<U> map(Function<? super T, ? extends U> mapper) {
         *         Objects.requireNonNull(mapper);
         *         if (!isPresent())
         *             return empty();
         *         else {
         *             return Optional.ofNullable(mapper.apply(value));
         *         }
         *     }
         */
        /**
         * Full Name is set? false
         * Full Name: [none]
         * Hey Stranger!
         */
    }
}
