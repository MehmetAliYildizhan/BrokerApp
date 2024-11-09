package com.example.brokerr.util;
import java.util.Base64;

public class Base64Util {
    public static String encode(String input) {
        if (input == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    // Base64 formatındaki metni çözer
    public static String decode(String base64Input) {
        if (base64Input == null) {
            return null;
        }
        byte[] decodedBytes = Base64.getDecoder().decode(base64Input);
        return new String(decodedBytes);
    }
}
