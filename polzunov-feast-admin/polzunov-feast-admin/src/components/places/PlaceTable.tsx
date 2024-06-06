import {
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
import {PlaceDialog} from "./PlaceDialog.tsx";
import {useEffect, useMemo, useState} from "preact/hooks";
import {Place} from "@/openapi";
import {adminApi, defaultApi} from "@/util/api.ts";
import {JSX} from "preact";

type Column = ColumnDef<Place> & {
    cell?: (info: { row: Row<Place> }) => JSX.Element
    accessorFn?: (row: Place) => any
}

/**
 * Таблица с событиями
 * @param updateTableTrigger нужен, чтобы триггерить обновление таблицы
 */
export function PlaceTable(updateTableTrigger) {
    const columns: Column[] = useMemo(() => [
        {
            id: 'cancel-or-edit',
            cell: (info: { row: Row<Place> }) => (
                <>
                    <IconButton onClick={() => adminApi.deletePlaceById({id: info.row.original.id})
                                    .then(_ => console.log(`Deleted place with id ${info.row.original.id}`))
                                    .catch(reason => console.log(`Failed to delete place with id ${info.row.original.id}: ${reason}`))
                                }>
                        <Close/>
                    </IconButton>
                    <IconButton onClick={() => {
                                    console.log(info.row.original)
                                    setUpdatePlaceDialog({open: true, place: info.row.original})
                                }}>
                        <Edit/>
                    </IconButton>
                </>
            )
        },
        {
            id: 'name',
            header: 'Название',
            accessorFn: (row: Place) => row.name
        },
        {
            id: 'place',
            header: 'Место',
            accessorFn: (row: Place) => row.address
        }
    ], [])
    const [updatePlaceDialog, setUpdatePlaceDialog] = useState<{ open: boolean, place?: Place }>
    ({open: false, place: undefined})

    const theme = useTheme()

    const [places, setPlaces] = useState<Place[]>([])

    const table = useReactTable<Place>({
        data: places,
        columns,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel()
    })


    useEffect(() => {
        defaultApi.getAllPlaces()
            .then(value => {
                setPlaces(value)
            })
            //TODO show popup with error
            .catch(reason => console.log(reason))
    }, [updatePlaceDialog.open, updateTableTrigger])

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
                    count={places.length}
                    page={table.getState().pagination.pageIndex}
                    onPageChange={(places, page) => table.setPageIndex(page)}
                    rowsPerPage={table.getState().pagination.pageSize}
                    onRowsPerPageChange={(place) => table.setPageSize(parseInt(place.target.value))}
                    labelDisplayedRows={({from, to, count}) => `${from}–${to} из ${count}`}
                    labelRowsPerPage='Показывать по'
                    showFirstButton
                    showLastButton
                />
            </Stack>
            <PlaceDialog
                action='update'
                place={updatePlaceDialog.place}
                open={updatePlaceDialog.open}
                onClose={() => setUpdatePlaceDialog({open: false, place: undefined})}/>
        </>
    )
}
