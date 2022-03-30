package org.noear.esearchx;

/**
 * 常量配置
 *
 * @author noear
 * @since 1.0
 */
public class Constants {
    /**
     * 最大请求数
     * */
    public static int HttpMaxRequests = 20000;
    /**
     * 最大同域名请求数
     * */
    public static int HttpMaxRequestsPerHost = 10000;
    /**
     * 连接超时（秒）
     * */
    public static int HttpConnectTimeoutSeconds = 10;
    /**
     * 写超时（秒）
     * */
    public static int HttpWriteTimeoutSeconds = 10;
    /**
     * 读超时（秒）
     * */
    public static int HttpReadTimeoutSeconds = 60;
}
