package org.noear.esearchx.exception;

/**
 * 请求异常
 *
 * @author noear
 * @since 1.0
 */
public class RequestException extends RuntimeException {
    public RequestException(String message) {
        super(message);
    }
}
