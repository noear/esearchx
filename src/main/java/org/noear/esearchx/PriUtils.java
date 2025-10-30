package org.noear.esearchx;

import org.noear.snack4.ONode;
import org.noear.snack4.Options;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * 内部工具（外部别用它）
 * */
public class PriUtils {
    private static final Options nodeOptions = Options.of();
    public static ONode newNode() {
        return new ONode(nodeOptions);
    }

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
