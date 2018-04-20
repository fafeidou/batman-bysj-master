package com.batman.bysj.common.mail.batmanbysjcommonmail;

public class MailClientException extends RuntimeException {

    public MailClientException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public MailClientException(String message) {
        super(message);
    }
}
