//функции для валидации полей места

export function validatePlaceName(name) {
    if (!name || name.length < 2 || name.length > 256) {
        return 'Название должно быть от 2 до 256 символов'
    }
    return true
}

export function validateAddress(address) {
    if (!address || address.length < 2 || address.length > 512) {
        return 'Адрес должен быть от 2 до 512 символов'
    }
    return true
}
