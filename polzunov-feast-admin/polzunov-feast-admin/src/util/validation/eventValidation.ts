//Функции для валидации полей события

export function validateEventName(name) {
    if (!name || name.length < 2 || name.length > 128) {
        return 'Название должно быть от 2 до 128 символов'
    }
    return true
}

export function validateDescription(description) {
    if (description && description.length > 1024) {
        return 'Описание должно быть не больше 1024 символов'
    }
    return true
}

export function validateDuration(startDateTime, endDateTime) {
    if (!(startDateTime || endDateTime)) {
        return 'Время начала и окончания должны быть указаны'
    }

    const start = new Date(startDateTime)
    const end = new Date(endDateTime)

    if (start <= Date.now() || end <= Date.now()) {
        return 'Время начала и окончания должны быть в будущем'
    }

    if (!isDurationInRange(start, end)) {
        return 'Время начала и окончания должны быть в диапазоне от 5 минут до 24 часов'
    }
    return true
}

function isDurationInRange(start, end) {
    const durationInMilliseconds = end.getTime() - start.getTime();
    const minDuration = 5 * 60 * 1000; // 5 minutes in milliseconds
    const maxDuration = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

    return durationInMilliseconds >= minDuration && durationInMilliseconds <= maxDuration;
}

export const defaultAgeLimit = 0

export function validateAgeLimit(value) {
    if (!value) {
        return true
    }
    const ageLimit = parseInt(value)
    if (isNaN(ageLimit) || ageLimit < 0 || ageLimit > 21) {
        return 'Возрастное ограничение должно быть от 0 до 21'
    }
    return true
}
