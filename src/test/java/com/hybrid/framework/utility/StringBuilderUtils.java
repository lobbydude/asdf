package com.hybrid.framework.utility;

public class StringBuilderUtils {

    public static String build(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string);
        }
        return sb.toString();
    }
}
