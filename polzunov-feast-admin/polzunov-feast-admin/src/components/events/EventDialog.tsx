import {Button, DialogContent, Divider, Stack} from "@mui/material";
import {LabeledInput} from "@/components/commons/LabeledInput.tsx";
import {TitledDialog} from "@/components/commons/TitledDialog.tsx";
import {useForm} from "react-hook-form";
import {
    defaultAgeLimit,
    validateAgeLimit,
    validateDescription,
    validateDuration,
    validateEventName
} from "@/util/validation/eventValidation.ts";
import {validateAddress, validatePlaceName} from "@/util/validation/placeValidation.ts";
import {LabeledChipListInput} from "@/components/commons/LabeledChipListInput.tsx";
import {useEffect} from "preact/hooks";
import {EventWithPlaceResponse} from "@/openapi/models/EventWithPlaceResponse.ts";

//заглушка, данные о категориях должны быть получены с сервера
const availableCategories = new Set([
    'Концерты',
    'Квесты',
    'Фестивали'
])

/**
 * Диалог для создания и редактирования события
 * @param action {'create' | 'update'}, по умолчанию 'create'
 * @param event {EventWithPlaceResponse | undefined} событие, которое нужно отредактировать. Если action = 'create', то event = undefined
 * @param props пропсы диалога
 */
export function EventDialog({action = 'create', event = undefined, ...props}) {

    const {
        register,
        handleSubmit,
        reset,
        watch,
        setValue,
        formState: {errors}
    } = useForm()

    useEffect(() => {
        reset({
            name: (action === 'create' || !event) ? undefined : event.name,
            startDateTime: (action === 'create' || !event) ? undefined : event.startTime.toISOString().slice(0, -1),
            endDateTime: (action === 'create' || !event) ? undefined : event.endTime.toISOString().slice(0, -1),
            ageLimit: (action === 'create' || !event) ? defaultAgeLimit : event.ageLimit,
            place: (action === 'create' || !event) ? undefined : event.place.name,
            address: (action === 'create' || !event) ? undefined : event.place.address,
            description: (action === 'create' || !event) ? undefined : event.description,
            categories: (action === 'create' || !event) ? new Set() : new Set(Array.from(event.categories).map(c => c.name))
        })
    }, [action, event, reset]);

    useEffect(() => {
        register('categories')
    }, [register])

    return (
        <TitledDialog titleText={action === 'create' ? 'Добавление события' : 'Изменение события'} {...props}>
            <DialogContent>
                <form id='eventCreateUpdateForm'
                      onSubmit={
                          //пока что при сабмите данные выводятся в консоль, заменить на запрос к серверу
                          handleSubmit((data) =>
                              console.log(`data: ${JSON.stringify(data)}, categories: ${[...data.categories]}`))
                      }>
                    <Stack direction={{xs: 'column', sm: 'row'}}
                           divider={<Divider orientation='vertical' flexItem/>}
                           justifyContent='center'
                           gap={2}>
                        <Stack spacing={3}>
                            <LabeledInput labelText='Название*'
                                          formRegister={register('name', {validate: validateEventName})}
                                          error={Boolean(errors.name)}
                                          helperText={errors.name && errors.name.message}/>
                            <LabeledInput labelText={'Начало*'}
                                          type='datetime-local'
                                          formRegister={register('startDateTime', {
                                              validate: (startTime) => {
                                                  const endTime = watch('endDateTime')
                                                  return validateDuration(startTime, endTime)
                                              }
                                          })}
                                          error={Boolean(errors.startDateTime)}
                                          helperText={errors.startDateTime && errors.startDateTime.message}/>
                            <LabeledInput labelText='Окончание*'
                                          type='datetime-local'
                                          formRegister={register('endDateTime')}
                                          error={Boolean(errors.startDateTime)}/>
                            <LabeledInput labelText='Возрастное ограничение'
                                          formRegister={register('ageLimit', {validate: validateAgeLimit})}
                                          error={Boolean(errors.ageLimit)}
                                          helperText={errors.ageLimit && errors.ageLimit.message}/>
                            <LabeledInput labelText='Название места*'
                                          formRegister={register('place', {validate: validatePlaceName})}
                                          error={Boolean(errors.place)}
                                          helperText={errors.place && errors.place.message}/>
                            <LabeledInput labelText='Адрес*'
                                          formRegister={register('address', {validate: validateAddress})}
                                          error={Boolean(errors.address)}
                                          helperText={errors.address && errors.address.message}/>
                            <LabeledInput labelText='Описание'
                                          formRegister={register('description', {validate: validateDescription})}
                                          error={Boolean(errors.description)}
                                          helperText={errors.description && errors.description.message}/>
                        </Stack>
                        <Stack spacing={3}>
                            <LabeledChipListInput labelText='Категории'
                                                  availableItems={availableCategories}
                                                  selectedItems={watch('categories')}
                                                  setSelectedItems={(categories) => {
                                                      setValue('categories', categories)
                                                  }}
                            />
                            <LabeledInput labelText='Фото'
                                          formRegister={register('imageUrls')}/>
                        </Stack>
                    </Stack>
                </form>
            </DialogContent>
            <Divider/>
            <Stack direction='row' sx={{px: 2, py: '12px', gap: '12px', justifyContent: 'flex-end'}}>
                <Button onClick={() => reset()} variant='contained'>Отмена</Button>
                <Button type='submit'
                        form='eventCreateUpdateForm'
                        variant='contained'>
                    Сохранить
                </Button>
            </Stack>
        </TitledDialog>
    )
}
