package com.example.polzunovfeastserver.event;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
public class EventParameter {
    private List<Long> categoryIds;
    private Integer ageLimit;
    private Boolean canceled;
    private OffsetDateTime start;
    private OffsetDateTime end;
}
