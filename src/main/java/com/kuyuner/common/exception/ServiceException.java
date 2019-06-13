package com.kuyuner.common.exception;

/**
 * 业务异常
 *
 * @author administrator
 */
public class ServiceException extends RuntimeException {
    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
