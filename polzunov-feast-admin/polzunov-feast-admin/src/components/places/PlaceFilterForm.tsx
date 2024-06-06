import {useForm} from "react-hook-form";
import {TextField} from "@mui/material";

/**
 * Форма фильтра мест. При сабмите формы отправляется запрос на сервер для получения событий с указанными параметрами
 */
export function PlaceFilterForm() {
    const {
        handleSubmit
    } = useForm({mode: 'onChange'});


    return (
        <form onSubmit={
            //TODO отправить запрос на сервер, по аналогии с EventDialog
            handleSubmit((data) => console.log(`data: ${JSON.stringify(data)}`))
        }
            style={{
                display: 'flex',
                flexDirection: 'row',
                flexWrap: 'wrap',
                gap: '16px',
            }}>
            <TextField label='Поиск'/>
        </form>
    )
}