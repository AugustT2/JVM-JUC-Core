package java8;

public class OverridableImpl implements Defaulable{
    @Override
    public String notRequired() {
        return "Defaulable.super.notRequired();";
    }
}

