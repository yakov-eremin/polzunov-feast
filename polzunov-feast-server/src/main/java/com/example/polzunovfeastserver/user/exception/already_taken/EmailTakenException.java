package com.example.polzunovfeastserver.user.exception.already_taken;

public class EmailTakenException extends UserFieldTakenException {
    public EmailTakenException(String message) {
        super(message);
    }
}
