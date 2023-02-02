package com.dip.unifiedviewer.util;

public class StringUtil {

    private StringUtil() {}

    public static String getParsedString(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '\n') {
                continue;
            } else if (ch == '\\') {
                if ((i > 0 && str.charAt(i-1) != '\\') && (i+1 < str.length() && str.charAt(i+1) != '\\')) {
                    if (i < str.length()-1 && str.charAt(i+1) == 'n') i++;
                    continue;
                }
            }
            stringBuilder.append(ch);
        }
        return stringBuilder.toString();
    }
}
