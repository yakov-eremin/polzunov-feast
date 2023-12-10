import {useForm} from "react-hook-form";
import {Button, TextField} from "@mui/material";

//заглушка, категории запрашиваются с сервера
const categories = ['Все', 'Праздники', 'Концерты', 'Квесты']

//Это НЕ заглушка. В зависимости от выбранного статуса, должен передаваться параметр 'canceled' в запросе к серверу
const canceledStatuses = ['Все', 'Не отмененные', 'Отмененные']

/**
 * Форма фильтра событий. При сабмите формы отправляется запрос на сервер для получения событий с указанными параметрами
 */
export function EventsFilterForm() {
    const {
        register,
        watch,
        handleSubmit,
        formState: {errors, isValid},
        reset,
        setError
    } = useForm({mode: 'onChange'});

    const validateAgeLimit = (value) => {
        if (value === '') {
            return true
        }
        const ageLimit = parseInt(value)
        if (isNaN(ageLimit) || ageLimit < 0) {
            return 'Возрастное ограничение не может быть отрицательным'
        }
        return true
    }

    const validateDateTimes = (startDateTime) => {
        const endDateTime = watch('endDateTime')
        if (startDateTime && endDateTime && new Date(startDateTime) > new Date(endDateTime)) {
            setError('startDateTime', {
                type: 'validate',
                message: 'Дата начала не может быть позже даты окончания'
            })
            return false
        }
        return true;
    }

    return (
        <form onSubmit={
            //пока что при сабмите выбранные прараметры просто выводятся в консоль
            handleSubmit((data) => console.log(`data: ${JSON.stringify(data)}`))
        }
              style={{
                  display: 'flex',
                  flexDirection: 'row',
                  flexWrap: 'wrap',
                  gap: '16px',
              }}>

            <TextField label='Категории'
                       select
                       {...register('category')}
                       SelectProps={{native: true}}>
                {categories.map((category) => (
                    <option key={category} value={category}>{category}</option>
                ))}
            </TextField>

            <TextField label='Возрастное ограничение'
                       {...register('ageLimit', {validate: validateAgeLimit})}
                       error={Boolean(errors.ageLimit)}
                       helperText={errors.ageLimit && errors.ageLimit.message}
            />

            <TextField label='Статус'
                       select
                       {...register('canceledStatus')}
                       SelectProps={{native: true}}>
                {canceledStatuses.map((status) => (
                    <option key={status} value={status}>{status}</option>
                ))}
            </TextField>

            <TextField label='Начало периода выборки'
                       type='datetime-local'
                       {...register('startDateTime', {validate: validateDateTimes})}
                       error={Boolean(errors.startDateTime)}
                       helperText={errors.startDateTime && errors.startDateTime.message}
                       InputLabelProps={{shrink: true}}/>

            <TextField label='Окончание периода выборки'
                       type='datetime-local'
                       {...register('endDateTime')}
                       error={Boolean(errors.startDateTime)}
                       InputLabelProps={{shrink: true}}/>

            <Button variant='contained' onClick={() => reset()}>Сбросить</Button>

            <Button type='submit' disabled={!isValid} variant='contained'>Применить</Button>
        </form>
    )
}