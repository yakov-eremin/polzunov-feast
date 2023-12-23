import {RootApi} from "@/openapi/apis/RootApi.ts";
import {AdminApi} from "@/openapi/apis/AdminApi.ts";
import {DefaultApi} from "@/openapi/apis/DefaultApi.ts";
import {Configuration, DefaultConfig} from "@/openapi/runtime.ts";

const cfgParams = {
    basePath: DefaultConfig.basePath,
    fetchApi: DefaultConfig.fetchApi,
    middleware: DefaultConfig.middleware,
    queryParamsStringify: DefaultConfig.queryParamsStringify,
    username: DefaultConfig.username,
    password: DefaultConfig.password,
    apiKey: DefaultConfig.apiKey,
    headers: DefaultConfig.headers,
    credentials: DefaultConfig.credentials,
    accessToken: 'eyJraWQiOiI0NGQ1ZDg1MC03ZmUyLTQ2MTktYmIwNC04NGZkYjg3OGFmYzciLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiMSIsImV4cCI6MTcwMzMzNzE1MywiaWF0IjoxNzAzMzMzNTUzLCJzY29wZSI6IlJPT1QifQ.Kp7VjhCz-bjYOVz7VHB3d_GLzEHlLbOC6FGv5fFul1URhsyBk_g6CJLkxJLijG4wfRIfda36gZxoZXcTFXCrBl2J7fLyX1WxGCDlZmK6X7FTQNmxoAgDymB_HVWZ8QwP75Y4wJLD2O3xDoLgF4D3AEGQjQ48WCIGfoFhLjdv8yAqNTcRoQOBHz66N3LqmOQ0Y3zQ2r73bB6k9fb2IifxjtiQU6hG-783BGzGoFqdcSAvKHOW9gIWE3ZHi0DXNFKoaEPTTBRpJotpJSVSswQKOU6R3ujrHyX1LGAE7gMuJH8qdf-05_wlK6kjK1cxLSUYySpuzKeZPNQEHxRgR5Odcw'
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
