package org.example.models.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

public class StringUtils {
    private StringUtils() {

    }
    private static final Random random = new SecureRandom();

    public static String randomString(int length) {
        byte[] array = new byte[length];
        random.nextBytes(array);

        return new String(array, StandardCharsets.UTF_8);
    }

    public static String randomAlphabeticString(int length) {
        int leftLimit = 97;
        int rightLimit = 122;

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
