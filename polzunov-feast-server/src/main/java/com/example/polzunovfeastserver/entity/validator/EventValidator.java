package com.example.polzunovfeastserver.entity.validator;

import org.openapitools.model.Event;
import org.springframework.validation.Errors;

public class EventValidator extends AbstractValidator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Event.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        rejectIfBlank(errors, "name");

        rejectIfNull(errors, "startDateTime");
        rejectIfNotDateTime(errors, "startDateTime");

        rejectIfNull(errors, "endDateTime");
        rejectIfNotDateTime(errors, "endDateTime");

        rejectIfNull(errors, "place");

        rejectIfBlank(errors, "place.name");

        rejectIfBlank(errors, "place.address");

        rejectIfNull(errors, "place.latitude");

        rejectIfNull(errors, "place.longitude");
    }
}
