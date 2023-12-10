import {Dialog, DialogTitle, Divider, IconButton, Typography} from "@mui/material";
import {grey} from "@mui/material/colors";
import {Close} from "@mui/icons-material";

/**
 * Диаолог с заголовком. Использует {@link Dialog}
 * @param titleText текст заголовка
 * @param props пропсы диалога
 */
export function TitledDialog({titleText, ...props}) {

    return (
        <Dialog {...props}>
            <DialogTitle sx={{display: 'flex', justifyContent: 'space-between', p: 2}}>
                <Typography sx={{
                    display: 'flex',
                    alignItems: 'center',
                    fontSize: '16px',
                    fontWeight: 500,
                    color: grey[900]
                }}>
                    {titleText}
                </Typography>
                <IconButton onClick={props.onClose}>
                    <Close sx={{color: grey[900]}}/>
                </IconButton>
            </DialogTitle>
            <Divider/>
            {props.children}
        </Dialog>
    )
}