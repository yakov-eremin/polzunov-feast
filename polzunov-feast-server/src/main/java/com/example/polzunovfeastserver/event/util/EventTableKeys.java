package com.example.polzunovfeastserver.event.util;

public final class EventTableKeys {
    private EventTableKeys() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static final String FOREIGN_PLACE = "events_fkey_place_id";

    /**
     * Technically this is an 'event_categories' table constraint, not 'events', but it will be applied in EventEntity
     */
    public static final String FOREIGN_CATEGORY = "event_categories_fkey_category_id";

    /**
     * Technically this is an 'event_categories' table constraint, not 'events', but it will be applied in EventEntity
     */
    public static final String FOREIGN_EVENT = "event_categories_fkey_event_id";
}
