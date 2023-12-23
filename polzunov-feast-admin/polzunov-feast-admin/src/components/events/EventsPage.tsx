import {Box, Button, Divider} from "@mui/material";
import {Add} from "@mui/icons-material";
import {EventsFilterForm} from "./EventsFilterForm.tsx";
import {EventsTable} from "./EventsTable.tsx";
import {useState} from "preact/hooks";
import {EventDialog} from "./EventDialog.tsx";

/**
 * Страница с событиями. Содержит фильтр и таблицу с событиями.
 * @constructor
 */
export function EventsPage() {
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
                <EventsFilterForm/>
                <Button
                    onClick={() => setDialogOpen(true)}
                    variant='contained'
                    endIcon={<Add/>}
                >
                    Добавить событие
                </Button>
            </Box>

            <EventDialog open={dialogOpen} onClose={() => setDialogOpen(false)}/>

            <Divider/>

            <EventsTable updateTableTrigger={dialogOpen}/>
        </>
    )
}