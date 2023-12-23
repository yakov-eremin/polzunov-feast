import {Controller, useForm} from "react-hook-form";
import {Box, Button, Checkbox, FormControlLabel, Paper, Stack, Typography} from "@mui/material";
import {LabeledInput} from "@/components/commons/LabeledInput.tsx";
import {defaultApi, setAccessToken} from "@/util/api.ts";
import {useEffect, useState} from "preact/hooks";
import {useNavigate} from "react-router-dom";
import {addCookie, getCookie} from "@/util/utils.ts";

type AuthFormData = {
    email: string
    password: string,
    rememberMe: boolean
}

export function SignInPage() {
    const navigate = useNavigate()
    const {register, control, handleSubmit} = useForm<AuthFormData>()
    const [authorizationError, setAuthorizationError] = useState<string | null>(null)

    useEffect(() => {
        if (getCookie('accessToken')) {
            navigate('/events')
        }
    }, [])

    return (
        <Box display="flex" alignItems="center" justifyContent="center" height="100vh">
            <Paper sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: "center",
                justifyContent: "center",
                gap: 3,
                maxWidth: 'md',
                p: 6
            }}
            >
                <Typography variant="h5">Авторизация</Typography>
                <Stack component='form'
                       gap={5}
                       onSubmit={handleSubmit(data => {
                           console.log(data)
                           signin(data.email, data.password, data.rememberMe)
                       })}>
                    <LabeledInput labelText='Почта'
                                  formRegister={register('email', {required: true})}
                                  type='email'/>
                    <LabeledInput labelText='Пароль'
                                  formRegister={register('password', {required: true})}
                                  type='password'/>
                    <Stack gap={2}>
                        <Controller
                            render={({field}) => (
                                <FormControlLabel control={<Checkbox {...field}/>} label={'Запомнить меня'}/>)}
                            name={'rememberMe'}
                            control={control}
                            defaultValue={false}
                        />
                        <Button variant='contained' type='submit'>Войти</Button>
                    </Stack>
                </Stack>
                {Boolean(authorizationError) && <Typography color="error">{authorizationError}</Typography>}
            </Paper>
        </Box>
    )

    function signin(email: string, password: string, rememberMe = false) {
        defaultApi.signInUser({credentials: {email: email, password: password}})
            .then(value => {
                console.log(`Obtained token: ${JSON.stringify(value)}`)
                if (rememberMe) {
                    addCookie('accessToken', value.accessToken)
                }
                setAccessToken(value.accessToken)
                navigate('/events')
            })
            .catch(reason => {
                console.log(`Failed to login: ${reason}`)
                setAuthorizationError(`Ошибка при авторизации, повторите попытку`)
            })
    }
}