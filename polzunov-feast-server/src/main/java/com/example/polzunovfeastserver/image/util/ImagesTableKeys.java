package com.example.polzunovfeastserver.image.util;

public final class ImagesTableKeys {
    private ImagesTableKeys() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static final String FOREIGN_EVENT = "images_fkey_event_id";

    public static final String UNIQUE_URL = "images_ukey_url";

    public static final String UNIQUE_PATH = "images_ukey_path";
}
