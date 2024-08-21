package com.example.polzunovfeastserver.notification.util;

public final class NotificationTokensTableKeys {
    private NotificationTokensTableKeys() {
        throw new UnsupportedOperationException("This is an utility class.");
    }

    public static final String FOREIGN_USER = "notification_tokens_fkey_user_id";

    /**
     * One user cannot have the same token twice
     */
    public static final String UNIQUE_USER_TOKEN = "notification_tokens_ukey_user_id_token";
}
