package org.example.models.utils;

public class IntegerUtils {
    private IntegerUtils() {

    }

    public static final int BILLION = 1_000_000_000;
    public static final int MILLION = 1_000_000;
    public static final int THOUSAND = 1_000;

    public static int millions(int i) {
        return MILLION * i;
    }

    public static int billions(int i) {
        return BILLION * i;
    }

    public static int thousands(int i) {
        return THOUSAND * i;
    }
}
