package com.ugc.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ConversionUtils {

    public static String createMD5Hash(String stringIn) throws NoSuchAlgorithmException {
        return createMD5Hash(stringIn, false);
    }

    public static String createMD5Hash(String stringIn, boolean returnInUppercase) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] bytes = stringIn.getBytes();
        digest.update(bytes, 0, bytes.length);
        String hash = padMD5Hash(new BigInteger(1, digest.digest()).toString(16));

        if (returnInUppercase)
            return hash.toUpperCase();

        return hash;
    }

    private static String padMD5Hash(String hash) {
        String result = hash;
        while (result.length() < 32)
            result = "0" + result;

        return result;
    }

}
