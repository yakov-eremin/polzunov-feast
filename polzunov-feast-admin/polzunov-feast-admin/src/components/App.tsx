import {ThemeProvider} from "@emotion/react";
import {colors, createTheme, CssBaseline} from "@mui/material";
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import {MainPage} from "./MainPage.tsx";
import {SignInPage} from "@/components/auth/SignInPage.tsx";

export function App() {

    const theme = createTheme({
        palette: {
            primary: {
                main: '#1E78F0'
            },
            secondary: {
                main: '#424242'
            },
            background: {
                default: colors.grey[200]
            }
        },
        components: {
            MuiTextField: {
                defaultProps: {
                    size: 'small'
                }
            },
            MuiButton: {
                defaultProps: {
                    disableElevation: true,
                    size: 'medium',
                    sx: {
                        textTransform: 'none',
                    }
                }
            }
        }
    })

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline/>
            <BrowserRouter>
                <Routes>
                    <Route path="/events" element={<MainPage/>}/>
                    <Route path="/signin" element={<SignInPage/>}/>
                    <Route path='/*' element={<Navigate to='/signin' replace/>}/>
                </Routes>
            </BrowserRouter>
        </ThemeProvider>
    )
}
