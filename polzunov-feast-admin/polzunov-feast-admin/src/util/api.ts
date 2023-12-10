import {AdminApi, DefaultApi, RootApi} from "@/openapi";

/**
 * Константа для получения доступа к API root-пользователя
 */
export const rootApi = new RootApi()

/**
 * Константа для получения доступа к API администратора
 */
export const adminApi = new AdminApi()

/**
 * Константа для получения доступа к API, доступное как админам, так пользователям, либо не требующее авторизации
 * Пример использования: defaultApi.getAllEvents().then(value => console.log(value)).catch(reason => console.log(reason))
 */
export const defaultApi = new DefaultApi()
