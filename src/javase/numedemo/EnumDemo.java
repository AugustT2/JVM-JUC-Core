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
        System.out.println(Season.FALL.compareTo(Season.SPRING));
        System.out.println("==========");
//        Enum
        System.out.println(Season.WATH.toString());

    }
}


@SuppressWarnings("all")
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
