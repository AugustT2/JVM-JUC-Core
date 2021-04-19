package season3;

/**
 * @author EDing3
 * @create 2021/4/19 17:02
 */
public class EnumDemo {
    public static void main(String[] args) {
        System.out.println(Season.FALL.getCode()+"\t"+Season.FALL.getName()+"\t"+Season.HOLIDAY.getDesc()+"\t"+Season.SPRING.toString());
        System.out.println(Season.SUMMER.toString());
    }
}

enum Season {
    SPRING(1, "spring", "warm"),
    SUMMER(2, "summer", "hot"),
    FALL(3, "fall", "cool"),
    HOLIDAY(4, "holiday", "cold");

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
