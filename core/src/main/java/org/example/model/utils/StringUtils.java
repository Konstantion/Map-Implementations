package org.example.model.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

public class StringUtils {
    private StringUtils() {

    }
    private static final Random random = new SecureRandom();

    public static String generateRandom(int length) {
        byte[] array = new byte[length];
        random.nextBytes(array);

        return new String(array, StandardCharsets.UTF_8);
    }
}
