import {
    Box,
    IconButton,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TablePagination,
    TableRow,
    useTheme
} from "@mui/material";
import {ColumnDef, flexRender, getCoreRowModel, getPaginationRowModel, Row, useReactTable} from "@tanstack/react-table";
import {Close, Edit} from "@mui/icons-material";
import {StatefulChip} from "@/components/commons/StatefulChip.tsx";
import {EventDialog} from "./EventDialog.tsx";
import {useEffect, useMemo, useState} from "preact/hooks";
import {EventWithPlaceResponse} from "@/openapi";
import {adminApi, defaultApi} from "@/util/api.ts";
import {JSX} from "preact";

type Column = ColumnDef<EventWithPlaceResponse> & {
    cell?: (info: { row: Row<EventWithPlaceResponse> }) => JSX.Element
    accessorFn?: (row: EventWithPlaceResponse) => any
}

/**
 * Таблица с событиями
 * @param updateTableTrigger нужен, чтобы триггерить обновление таблицы
 */
export function EventsTable(updateTableTrigger) {
    const columns: Column[] = useMemo(() => [
        {
            id: 'cancel-or-edit',
            cell: (info: { row: Row<EventWithPlaceResponse> }) => (
                <>
                    <IconButton disabled={info.row.original.canceled || info.row.original.startTime < new Date}
                                onClick={() => adminApi.deleteEventById({id: info.row.original.id})
                                    .then(_ => console.log(`Deleted event with id ${info.row.original.id}`))
                                    .catch(reason => console.log(`Failed to delete event with id ${info.row.original.id}: ${reason}`))
                                }>
                        <Close/>
                    </IconButton>
                    <IconButton disabled={info.row.original.canceled || info.row.original.startTime < new Date}
                                onClick={() => {
                                    info.row.original.imageUrls = (new Set(info.row.original.imageUrls))
                                    setUpdateEventDialog({open: true, event: info.row.original})
                                }}>
                        <Edit/>
                    </IconButton>
                </>
            )
        },
        {
            id: 'startDateTime',
            header: 'Начало',
            accessorFn: (row: EventWithPlaceResponse) => row.startTime.toLocaleString()
        },
        {
            id: 'endDateTime',
            header: 'Окончание',
            accessorFn: (row: EventWithPlaceResponse) => row.endTime.toLocaleString()
        },
        {
            id: 'name',
            header: 'Название',
            accessorFn: (row: EventWithPlaceResponse) => row.name
        },
        {
            id: 'place',
            header: 'Место',
            accessorFn: (row: EventWithPlaceResponse) => row.place.address
        },
        {
            id: 'ageLimit',
            header: 'Возрастное ограничение',
            accessorFn: (row: EventWithPlaceResponse) => row.ageLimit
        },
        {
            id: 'categories',
            header: 'Категории',
            cell: (info: { row: Row<EventWithPlaceResponse> }) => (
                <Box sx={{display: 'flex', gap: 1}}>
                    {Array.from(info.row.original.categories).map(cat => (
                        <StatefulChip key={cat.id} label={cat.name}/>
                    ))}
                </Box>
            )
        }
    ], [])
    const [updateEventDialog, setUpdateEventDialog] = useState<{ open: boolean, event?: EventWithPlaceResponse }>
    ({open: false, event: undefined})
    const theme = useTheme()

    const [events, setEvents] = useState<EventWithPlaceResponse[]>([])

    const table = useReactTable<EventWithPlaceResponse>({
        data: events,
        columns,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel()
    })


    useEffect(() => {
        defaultApi.getAllEvents()
            .then(value => {
                setEvents(value)
            })
            //TODO show popup with error
            .catch(reason => console.log(reason))
    }, [updateEventDialog.open, updateTableTrigger])

    return (
        <>
            <Stack alignItems='flex-start'>
                <TableContainer>
                    <Table size='small'>
                        <TableHead>
                            {table.getHeaderGroups().map(headerGroup => (
                                <TableRow key={headerGroup.id}>
                                    {headerGroup.headers.map(header => (
                                        <TableCell key={header.id}
                                                   sx={{
                                                       color: '#00616D',
                                                       borderRight: `1px solid ${theme.palette.divider}`,
                                                       '&:last-child': {borderRight: 0},
                                                   }}>
                                            {flexRender(header.column.columnDef.header, header.getContext())}
                                        </TableCell>
                                    ))}
                                </TableRow>
                            ))}
                        </TableHead>
                        <TableBody>
                            {table.getRowModel().rows.map(row => (
                                <TableRow key={row.id}>
                                    {row.getVisibleCells().map(cell => (
                                        <TableCell key={cell.id}>
                                            {flexRender(cell.column.columnDef.cell, cell.getContext())}
                                        </TableCell>
                                    ))}
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
                <TablePagination
                    component='div'
                    count={events.length}
                    page={table.getState().pagination.pageIndex}
                    onPageChange={(event, page) => table.setPageIndex(page)}
                    rowsPerPage={table.getState().pagination.pageSize}
                    onRowsPerPageChange={(event) => table.setPageSize(parseInt(event.target.value))}
                    labelDisplayedRows={({from, to, count}) => `${from}–${to} из ${count}`}
                    labelRowsPerPage='Показывать по'
                    showFirstButton
                    showLastButton
                />
            </Stack>
            <EventDialog
                action='update'
                event={updateEventDialog.event}
                open={updateEventDialog.open}
                onClose={() => setUpdateEventDialog({open: false, event: undefined})}/>
        </>
    )
}
