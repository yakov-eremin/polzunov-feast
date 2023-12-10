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
import {flexRender, getCoreRowModel, getPaginationRowModel, useReactTable} from "@tanstack/react-table";
import {Close, Edit} from "@mui/icons-material";
import {StatefulChip} from "@/components/commons/StatefulChip.tsx";
import {EventDialog} from "./EventDialog.tsx";
import {useEffect, useMemo, useState} from "preact/hooks";
import {Category, EventWithPlaceResponse, Place} from "@/openapi";

//Заглушки:
const initPlace : Place = {
    id: 0,
    name: "место",
    address: "адрес места"
}

const initCategory1 : Category = {
    id: 0,
    name: "Концерты"
}

const initCategory2 : Category = {
    id: 1,
    name: "Квесты"
}

const initEvent : EventWithPlaceResponse = {
    id: 0,
    name: "событие",
    description: 'описание события',
    startTime: new Date("2023-12-31T17:00:00Z"),
    endTime: new Date("2023-12-31T18:00:00Z"),
    place: initPlace,
    canceled: false,
    categories: new Set<Category>([initCategory1, initCategory2]),
    ageLimit: 0,
    imageUrls: new Set<string>()
}

function makeEvents(n) {
    const events = []
    for (let i = 0; i < n; i++) {
        events.push({
            ...initEvent,
            id: i,
            name: `событие ${i}`
        })
    }
    return events
}

const events = makeEvents(30)

/**
 * Таблица с событиями
 */
export function EventsTable() {
    const columns = useMemo(() => [
        {
            id: 'cancel-or-edit',
            cell: info => (
                <>
                    <IconButton disabled={info.row.original.canceled || info.row.original.startTime < new Date}
                                onClick={
                                    //при нажатии на крестик нужно отправить запрос с отменой события, пока просто выводим в консоль id события
                                    () => console.log(`delete ${info.row.original.id}`)
                                }>
                        <Close/>
                    </IconButton>
                    <IconButton disabled={info.row.original.canceled || info.row.original.startTime < new Date}
                                onClick={() => setUpdateEventDialog({open: true, event: info.row.original})}>
                        <Edit/>
                    </IconButton>
                </>
            )
        },
        {
            id: 'startDateTime',
            header: 'Начало',
            accessorFn: row => row.startTime.toLocaleString()
        },
        {
            id: 'endDateTime',
            header: 'Окончание',
            accessorFn: row => row.endTime.toLocaleString()
        },
        {
            id: 'name',
            header: 'Название',
            accessorFn: row => row.name
        },
        {
            id: 'place',
            header: 'Место',
            accessorFn: row => row.place.address
        },
        {
            id: 'ageLimit',
            header: 'Возрастное ограничение',
            accessorFn: row => row.ageLimit
        },
        {
            id: 'categories',
            header: 'Категории',
            cell: info => (
                <Box sx={{display: 'flex', gap: 1}}>
                    {Array.from(info.row.original.categories).map(cat => (
                        <StatefulChip key={cat.id} label={cat.name}/>
                    ))}
                </Box>
            )
        }
    ], [])

    const [updateEventDialog, setUpdateEventDialog] = useState({open: false, event: undefined})

    const theme = useTheme()

    const table = useReactTable({
        data: events,
        columns,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel()
    })

    useEffect(() => {

    }, [])

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
