/**
 * Преобразует дату в строку ISO, которая представляет дату и время в текущем часовом поясе, но без индикатора часового пояса
 */
export function toLocalIsoString(date: Date): string {
    const pad = function(num: number) {
        const norm = Math.floor(Math.abs(num));
        return (norm < 10 ? '0' : '') + norm;
    };
    return date.getFullYear() +
        '-' + pad(date.getMonth() + 1) +
        '-' + pad(date.getDate()) +
        'T' + pad(date.getHours()) +
        ':' + pad(date.getMinutes()) +
        ':' + pad(date.getSeconds());
}
