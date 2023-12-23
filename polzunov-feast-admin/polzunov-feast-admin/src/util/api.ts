import {RootApi} from "@/openapi/apis/RootApi.ts";
import {AdminApi} from "@/openapi/apis/AdminApi.ts";
import {DefaultApi} from "@/openapi/apis/DefaultApi.ts";
import {Configuration, ConfigurationParameters, DefaultConfig} from "@/openapi/runtime.ts";
import {getCookie} from "@/util/utils.ts";

const cfgParams: ConfigurationParameters = {
    basePath: DefaultConfig.basePath,
    fetchApi: DefaultConfig.fetchApi,
    middleware: DefaultConfig.middleware,
    queryParamsStringify: DefaultConfig.queryParamsStringify,
    username: DefaultConfig.username,
    password: DefaultConfig.password,
    apiKey: DefaultConfig.apiKey,
    headers: DefaultConfig.headers,
    credentials: DefaultConfig.credentials,
    accessToken: getCookie('accessToken')
}

/**
 * Используется для установки токена в cfgParams при авторизации
 * @param token
 */
export function setAccessToken(token: string) {
    cfgParams.accessToken = token
}

const cfg = new Configuration(cfgParams)

/**
 * Константа для получения доступа к API root-пользователя
 */
export const rootApi = new RootApi(cfg)

/**
 * Константа для получения доступа к API администратора
 */
export const adminApi = new AdminApi(cfg)

/**
 * Константа для получения доступа к API, доступное как админам, так пользователям, либо не требующее авторизации
 */
export const defaultApi = new DefaultApi(cfg)
