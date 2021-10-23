package org.noear.esearchx.exception;

/**
 * 不存在异常
 *
 * @author noear
 * @since 1.4
 */
public class NoExistException extends RuntimeException {
    public NoExistException(String message) {
        super(message);
    }
}
