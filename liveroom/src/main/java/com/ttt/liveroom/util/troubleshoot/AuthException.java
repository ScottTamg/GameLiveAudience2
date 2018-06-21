package com.ttt.liveroom.util.troubleshoot;

/**
 * @author Muyangmin
 * @since 1.0.0
 */
public class AuthException extends RuntimeException {
    public AuthException(String detailMessage) {
        super(detailMessage);
    }

    public AuthException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
