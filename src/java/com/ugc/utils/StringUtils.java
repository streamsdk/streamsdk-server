package com.ugc.utils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.DecimalFormat;
import java.io.UnsupportedEncodingException;


public class StringUtils extends org.apache.commons.lang.StringUtils
{

    private static DecimalFormat _standardDecimalFormat;

    public static String getSizeInMB(long total) {
        return getStandardDecimalFormat().format(total / Math.pow(2, 20));
    }

    public static DecimalFormat getStandardDecimalFormat() {
        if (_standardDecimalFormat == null) {
            _standardDecimalFormat = (DecimalFormat) DecimalFormat.getInstance();
            _standardDecimalFormat.applyPattern("0.00");
        }
        return _standardDecimalFormat;
    }

    public static String getSizeInKB(long total) {
        return getStandardDecimalFormat().format(total / Math.pow(2, 10));
    }

    public static String getSizeInMBorKB(long total) {
        if (total > Math.pow(2, 20))
            return getSizeInMB(total) + "MB";
        return getSizeInKB(total) + "KB";
    }

    public static byte[] fromHexString(String hex) {
        byte[] data = new byte[hex.length() / 2];

        for (int i = 0; i < data.length; i++) {
            String subStr = hex.substring(2 * i, 2 * i + 2);
            data[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return data;
    }

    public static String toHexString(byte[] bytes) {
        if (bytes == null)
            return null;

        StringBuffer rv = new StringBuffer();
        for (byte aByte : bytes) {
            int b = (int) aByte;
            if (b < 0)
                b += 256;
            rv.append(b < 16 ? "0" : "").append(Integer.toHexString(b));
        }

        return rv.toString();
    }

    public static String strip(String src, Pattern pattern, boolean inclusive) {
        Matcher m = pattern.matcher(src);
        StringBuffer rv = new StringBuffer();
        int exPos = 0;
        while (m.find()) {
            if (inclusive)
                rv.append(src.substring(m.start(), m.end()));
            else {
                rv.append(src.substring(exPos, m.start()));
                exPos = m.end();
            }
        }
        if (!inclusive)
            rv.append(src.substring(exPos));

        return rv.toString();
    }

    /**
     * Given a comma sepeparated list of values build a Set of these values.
     *
     * @param value the string from which to extract values
     * @return Set of values
     */
    public static Set<String> extractCSValues(String value) {
        return extractCSValues(value, ",", false);
    }

    public static Set<String> extractCSValues(String value, String pattern, boolean shouldTrim) {
        Set<String> result = new HashSet<String>();
        if (value != null) {
            String[] extensions = split(value, pattern);
            for (String extension : extensions)
                result.add(shouldTrim ? extension.trim() : extension);
        }
        return result;
    }

    public static String[] extractCSValuesAsArray(String value, String pattern, boolean trim) {
        return value != null ? extractCSValuesAsArrayNoGuard(value, pattern, trim) : new String[0];
    }

    private static String[] extractCSValuesAsArrayNoGuard(String value, String pattern, boolean trim) {
        String[] extensions = split(value, pattern);
        for (int i = 0; trim && i < extensions.length; i++)
            extensions[i] = extensions[i].trim();
        return extensions;
    }

    /**
     * If running on Windoze a real path will contain nasty '\\' characters that
     * do funny things when embedded in rendered text.
     *
     * @param path the path to convert
     * @return A pathname with nice normal seperator characters.
     */
    public static String convertPathSeperators(String path) {
        return path.replace('\\', '/');
    }

    public static String calculatePathWithAlternativeExtension(String path, String extension) {
        return isEmpty(path) || !path.contains(".") ? "" : convertPathSeperators(path).substring(0, path.lastIndexOf(".")) + extension;
    }

    public static Map<String, String> getMapFromString(String value) {
        Map<String, String> result = new HashMap<String, String>();
        if (value != null) {
            String[] mapEntries = value.split("[\\s]*,[\\s]*");
            for (String mapEntry : mapEntries) {
                String[] entry = mapEntry.split("[\\s]*=[\\s]*");
                if (entry.length == 2)
                    result.put(entry[0], entry[1]);
            }
        }
        return result;
    }

    public static String quote(String string) {
        return string == null ? "" : string.replaceAll("'", "''");
    }

    public static String reConstructString(String string, String charSetName) {
        try {
            return new String(string.getBytes(), charSetName);
        } catch (UnsupportedEncodingException usee) {
            return string;
        }
    }

    /**
     * Takes a string and remove blank spaces and URL-encoded spaces (%20)
     *
     * @param s the string from which spaces are to be removed
     * @return cleaned string
     */
    public static String removeSpaces(String s) {
        return s.replaceAll(" ", "").replaceAll("%20", "");
    }

    public static boolean endsWith(String string, String... endings) {
        for (String end : endings)
            if (string.endsWith(end))
                return true;
        return false;
    }

    public static boolean notEquals(String lhs, String rhs) {
        return !equals(lhs, rhs);
    }

    public static boolean notEqualsIgnoreCase(String lhs, String rhs) {
        return !equalsIgnoreCase(lhs, rhs);
    }


    public static String hackedUrl(String str) {
        String newStr = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\\')
                c = '/';
            newStr = newStr + c;
        }
        return newStr;
    }

    public static String getTokenFromIndex(String s, String deliminater, int i) {
        StringTokenizer str = new StringTokenizer(s, deliminater);
        int count = 0;
        while (str.hasMoreElements()) {
            if (count == i)
                return (String) str.nextElement();
            str.nextElement();
            count++;
        }
        return "";
    }


    public static int countTotalTokens(String uri, String s) {
        StringTokenizer str = new StringTokenizer(uri, s);
        return str.countTokens();
    }

    public static String getIndexOf(String addressInfo, String s, int i) {

        int index = addressInfo.indexOf(s);
        return addressInfo.substring(i, index);

    }

    public static String parseToken(String token){

        String t[] = token.split("=");
        String realToken = t[1].substring(1, t[1].length() - 1);
        String tokenWihoutSpace = realToken.replace(" ", "");
        System.out.println(tokenWihoutSpace);

        return tokenWihoutSpace;
    }

}

