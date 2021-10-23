package org.noear.esearchx;

import java.nio.charset.Charset;
import java.util.Base64;

class PriUtils {
    /**
     * 是否为空
     * */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 是否不为空
     * */
    public static boolean isNotEmpty(String str) {
        return !(str == null || str.length() == 0);
    }

    /**
     * Base64 编码
     * */
    public static String b64Encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(Charset.forName("UTF-8")));
    }
}
