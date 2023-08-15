package com.example.polzunovfeastserver.event.util.table_key;

public final class EventTableKeys {
    private EventTableKeys() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static final String FOREIGN_PLACE = "events_fkey_place_id";

    public static final String FOREIGN_MAIN_IMAGE_URL = "events_fkey_main_image_url_id";
}
