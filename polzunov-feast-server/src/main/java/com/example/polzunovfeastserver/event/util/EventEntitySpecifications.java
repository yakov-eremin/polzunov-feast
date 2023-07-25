package com.example.polzunovfeastserver.event.util;

import com.example.polzunovfeastserver.event.EventParameter;
import com.example.polzunovfeastserver.event.entity.EventEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public final class EventEntitySpecifications {
    private EventEntitySpecifications() {
        throw new UnsupportedOperationException("This is an utility class.");
    }

    public static Specification<EventEntity> where(EventParameter parameter) {
        Specification<EventEntity> spec = Specification.where(empty());

        if (parameter.getCategoryIds() != null) {
            spec = spec.and(categoryIn(parameter.getCategoryIds()));
        }
        if (parameter.getAgeLimit() != null) {
            spec = spec.and(ageLimitLessOrEqualTo(parameter.getAgeLimit()));
        }
        if (parameter.getCanceled() != null) {
            spec = spec.and(isCanceled(parameter.getCanceled()));
        }
        if (parameter.getStart() != null) {
            spec = spec.and(startGreaterOrEqualTo(parameter.getStart()));
        }
        if (parameter.getEnd() != null) {
            spec = spec.and(endLessOrEqualTo(parameter.getEnd()));
        }
        return spec;
    }

    private static Specification<EventEntity> categoryIn(Iterable<Long> categoryIds) {
        return (root, query, cb) ->
                cb.in(root.get("categories").get("id")).value(categoryIds);
    }

    private static Specification<EventEntity> ageLimitLessOrEqualTo(int ageLimit) {
        return (root, query, cb) ->
                cb.le(root.get("ageLimit"), ageLimit);
    }

    private static Specification<EventEntity> startGreaterOrEqualTo(OffsetDateTime dateTime) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("startTime"), dateTime);
    }

    private static Specification<EventEntity> endLessOrEqualTo(OffsetDateTime dateTime) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("endTime"), dateTime);
    }

    private static Specification<EventEntity> isCanceled(Boolean canceled) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("canceled"), canceled);
    }

    private static Specification<EventEntity> empty() {
        return (root, query, cb) -> cb.conjunction();

    }
}
