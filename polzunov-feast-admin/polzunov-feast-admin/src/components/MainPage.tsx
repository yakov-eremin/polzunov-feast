import {AdminPanelSettings, ListAlt, People, Place, Settings} from "@mui/icons-material";
import MenuIcon from '@mui/icons-material/Menu';
import {AppBar, Box, IconButton, ListItemIcon, Menu, MenuItem, MenuList, Paper, Typography} from "@mui/material";
import {useState} from "preact/hooks";
import {EventsPage} from "@/components/events/EventsPage.tsx";

const pagesToIcons = new Map([
    ['Программа', <ListAlt/>],
    ['Места', <Place/>],
    ['Пользователи', <People/>],
    ['Администраторы', <AdminPanelSettings/>],
    ['Настройки', <Settings/>]
])

/**
 * Основная страница с апп баром, по которому происходит навигация.
 */
export function MainPage() {

    const [activePage, setActivePage] = useState(pagesToIcons.keys().next().value)
    const [anchorElNav, setAnchorElNav] = useState(null)

    const onMenuPageClicked = (page) => {
        setActivePage(page)
    }

    const openNavMenu = (event) => {
        setAnchorElNav(event.currentTarget)
    }

    const closeNavMenu = () => {
        setAnchorElNav(null)
    }

    return (
        <>
            <AppBar position='sticky' sx={{bgcolor: 'secondary.main', boxShadow: 0, px: 2}}>
                {/*этот вариант навигации появляется на маленьком экране (мобильные устройства)*/}
                <Box sx={{
                    display: {xs: 'flex', md: 'none'},
                    alignItems: 'center',
                    width: '100%'
                }}>
                    <Box>
                        <IconButton onClick={openNavMenu} color="inherit">
                            <MenuIcon/>
                        </IconButton>
                        <Menu
                            keepMounted
                            anchorEl={anchorElNav}
                            open={Boolean(anchorElNav)}
                            onClose={closeNavMenu}
                        >
                            {[...pagesToIcons.keys()].map((page) => (
                                <MenuItem key={page}
                                          onClick={() => {
                                              closeNavMenu()
                                              setActivePage(page)
                                          }}>
                                    <Typography textAlign="center">{page}</Typography>
                                </MenuItem>
                            ))}
                        </Menu>
                    </Box>

                    <Typography variant="h6" component='h1' width='100%' textAlign='center'>
                        {activePage}
                    </Typography>
                </Box>

                {/*этот вариант навигации появляется на большом экране (десктоп)*/}
                <MenuList sx={{
                    display: {xs: 'none', md: 'flex'},
                    gap: '16px',
                    m: 0, p: 0
                }}>
                    {Array.from(pagesToIcons.entries()).map(([page, icon]) => (
                        <MenuItem
                            key={page}
                            tabIndex={0}
                            sx={{
                                bgcolor: activePage === page ? 'primary.main' : 'default',
                                '&:hover, &:focus': {
                                    bgcolor: activePage === page ? 'primary.light' : 'secondary.light',
                                },
                                p: 2
                            }}
                            onClick={() => onMenuPageClicked(page)}
                        >
                            <ListItemIcon sx={{color: 'inherit'}}>{icon}</ListItemIcon>
                            {page}
                        </MenuItem>
                    ))}
                </MenuList>
            </AppBar>

            {/*TODO Сейчас отображается только страница с событиями, фактически навигации нет*/}
            <Paper sx={{m: {md: 2}}}>
                <EventsPage/>
            </Paper>
        </>
    )
}
