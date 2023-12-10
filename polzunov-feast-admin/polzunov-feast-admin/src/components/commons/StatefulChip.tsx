import Chip from '@mui/material/Chip';
import {grey} from "@mui/material/colors";
import {alpha, useTheme} from "@mui/material";

/**
 * @param state {undefined | 'active' | 'disabled'} состояние чипа
 * @param props пропсы чипа
 */
export function StatefulChip({state, ...props}) {
    const theme = useTheme()

    const newProps = {
        ...props,
        sx: {
            ...getSxConfig(state, theme),
            ...props.sx
        }
    }
    return <Chip {...newProps} />
}

function getSxConfig(config, theme) {
    const newSx = {color: grey[900]}
    switch (config) {
        case 'disabled':
            newSx.bgcolor = grey[100]
            break
        case 'active':
            newSx.bgcolor = alpha(theme.palette.primary.main, 0.12)
            newSx.border = `1px solid ${theme.palette.primary.main}`
            break
        default:
            newSx.bgcolor = alpha(theme.palette.primary.main, 0.12)
    }
    return newSx
}