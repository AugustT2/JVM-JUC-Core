package javase.numedemo;

/**
 * @author EDing3
 * @create 2021/4/19 17:02
 */
@SuppressWarnings("all")
public class EnumDemo {
    public static void main(String[] args) {
        System.out.println(Season.FALL.getCode()+"\t"+Season.FALL.getName()+"\t"+Season.HOLIDAY.getDesc()+"\t"+Season.SPRING.toString());
        System.out.println(Season.SUMMER.toString());

        Season season = Season.FALL;
        Season season2 = Season.FALL;
        System.out.println(season);
        System.out.println(season == season2);
        System.out.println(Season.valueOf(Season.class,"FALL"));
        System.out.println(season.ordinal());
        System.out.println(season.name());
        Season[] values = Season.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println(values[i]);
        }
        for (Season value : Season.values()) {
            System.out.println(value.ordinal()+"---"+value);
        }
        System.out.println(Season.FALL.compareTo(Season.SPRING));
        System.out.println("==========");
//        Enum
        System.out.println(Season.WATH.toString());

    }
}


@SuppressWarnings("all")
/**
 * 1) 当我们使用 enum 关键字开发一个枚举类时， 默认会继承 Enum 类, 而且是一个 final 类[如何证明],老师使用 javap 工
 * 具来演示
 * 2) 传统的 public static final Season2 SPRING = new Season2("春天", "温暖"); 简化成 SPRING("春天", "温暖")， 这里必
 * 须知道， 它调用的是哪个构造器.
 * 3) 如果使用无参构造器 创建 枚举对象， 则实参列表和小括号都可以省略
 * 4) 当有多个枚举对象时， 使用,间隔， 最后有一个分号结尾
 * 5) 枚举对象必须放在枚举类的行首.
 */
enum SingleTon {
    A;
}
enum Season {
    SPRING(1, "spring", "warm"),
    SUMMER(2, "summer", "hot"),
    FALL(3, "fall", "cool"),
    HOLIDAY(4, "holiday", "cold"),
    WATH;

    private int code;
    private String name;
    private String desc;

    private Season() {
    }

    private Season(int code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Season{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
