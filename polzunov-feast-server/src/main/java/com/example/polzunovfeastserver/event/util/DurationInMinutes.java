package com.example.polzunovfeastserver.event.util;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.*;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

import static java.lang.String.format;

/**
 * This annotation is used in openapi.yaml, so if you rename or move it, make sure to update the openapi accordingly.
 * <p>
 * Period in minutes between start time and end time must be in range [min, max].
 * <ul>
 *     <li>min and max must be integers</li>
 *     <li>min and max must be positive or zero</li>
 *     <li>min must be less than max</li>
 * </ul>
 * {@link IllegalArgumentException} is thrown otherwise.
 * <br>
 * <br>
 * Supports only {@link OffsetDateTime} of start and end fields.
 * {@link DateTimeParseException} will be thrown if parsing fails.
 * <br>
 * If annotated object, start time or end time is null, object considered valid
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DurationInMinutes.Validator.class)
@Documented
public @interface DurationInMinutes {

    /**
     * name of the field of start time
     */
    String startField();

    /**
     * name of the field of end time
     */
    String endField();

    /**
     * Minimum duration in minutes
     * <br>
     * default = 0
     */
    int min() default 0;

    /**
     * Maximum duration in minutes
     * <br>
     * default = 1440 (24 hours)
     */
    int max() default 1440;

    String message() default "duration was %d min, but must be in range [%d, %d]";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<DurationInMinutes, Object> {

        String startField;
        String endField;
        int min;
        int max;

        @Override
        public void initialize(DurationInMinutes constraintAnnotation) {
            startField = constraintAnnotation.startField();
            endField = constraintAnnotation.endField();
            min = constraintAnnotation.min();
            max = constraintAnnotation.max();

            if (min < 0 || max < 0) {
                throw new IllegalArgumentException(format("min and max must be positive, min=%d, max=%d", min, max));
            }

            if (min > max) {
                throw new IllegalArgumentException(format("min must be less or equal to max, min=%d, max=%d", min, max));
            }
        }

        @Override
        public boolean isValid(Object object, ConstraintValidatorContext context) {
            if (object == null) {
                return true;
            }

            BeanWrapper beanWrapper = new BeanWrapperImpl(object);
            Object start = beanWrapper.getPropertyValue(startField);
            Object end = beanWrapper.getPropertyValue(endField);

            if (start == null || end == null) {
                return true;
            }

            OffsetDateTime startDateTime = OffsetDateTime.parse(start.toString());
            OffsetDateTime endDateTime = OffsetDateTime.parse(end.toString());

            long duration = Duration.between(startDateTime, endDateTime).toMinutes();
            String message = format(context.getDefaultConstraintMessageTemplate(), duration, min, max);
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();

            return duration >= min && duration <= max;
        }
    }
}
