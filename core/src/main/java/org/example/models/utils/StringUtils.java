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
}
