package com.library.libraryapp.util;

public class StringUtils {

    public static boolean isNullOrEmpty(String... strings) {
        if (strings == null) return true;

        for (String s : strings) {
            if (s == null || s.isEmpty()) return true;
        }

        return false;
    }
}
