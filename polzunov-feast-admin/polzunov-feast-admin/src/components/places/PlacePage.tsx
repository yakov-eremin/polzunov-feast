import {Box, Button, Divider} from "@mui/material";
import {Add} from "@mui/icons-material";
import {useState} from "preact/hooks";
import {PlaceFilterForm} from "./PlaceFilterForm.tsx";
import {PlaceTable} from "./PlaceTable.tsx";
import {PlaceDialog} from "./PlaceDialog.tsx";

/**
 * Страница с событиями. Содержит фильтр и таблицу с событиями.
 * @constructor
 */
export function PlacePage() {
    const [dialogOpen, setDialogOpen] = useState(false)

    return (
        <>
            <Box sx={{
                display: 'flex',
                flexWrap: 'wrap',
                justifyContent: 'space-between',
                gap: 3,
                p: 2,
            }}>
                <PlaceFilterForm/>
                <Button
                    onClick={() => setDialogOpen(true)}
                    variant='contained'
                    endIcon={<Add/>}
                >
                    Добавить место
                </Button>
            </Box>

            <PlaceDialog open={dialogOpen} onClose={() => setDialogOpen(false)}/>

            <Divider/>

            <PlaceTable updateTableTrigger={dialogOpen}/>
        </>
    )
}