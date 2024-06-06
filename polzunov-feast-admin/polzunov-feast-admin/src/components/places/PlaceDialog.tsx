import {Button, DialogContent, Divider, Stack} from "@mui/material";
import {LabeledInput} from "@/components/commons/LabeledInput.tsx";
import {TitledDialog} from "@/components/commons/TitledDialog.tsx";
import {useForm} from "react-hook-form";
import {
    validateEventName
} from "@/util/validation/eventValidation.ts";
import {validateAddress} from "@/util/validation/placeValidation.ts";
import {useEffect} from "preact/hooks";
import {adminApi} from "@/util/api.ts";
import {Place} from "@/openapi/models/Place.ts";
import {
    AddPlaceRequest,
    updatePlaceById
} from "@/openapi/apis/AdminApi.ts";

/**
 * Диалог для создания и редактирования события
 * @param action {'create' | 'update'}, по умолчанию 'create'
 * @param event {EventWithPlaceResponse | undefined} событие, которое нужно отредактировать. Если action = 'create', то event = undefined
 * @param props пропсы диалога
 */
export function PlaceDialog({action = 'create', place = undefined, ...props}:
                                { action?: 'create' | 'update', place?: Place }) {

    const {
        register,
        handleSubmit,
        reset,
        formState: {errors}
    } = useForm()

    // useEffect(() => {
    //     register('categories')
    //     register('images')
    // }, [register])

    useEffect(() => {
        reset({
            name: (action === 'create' || !place) ? undefined : place.name,
            address: (action === 'create' || !place) ? undefined : place.address
        })
    }, [action, place, reset]);


    return (
        <TitledDialog titleText={action === 'create' ? 'Добавление места' : 'Изменение места'} {...props}>
            <DialogContent>
                <form id='placeCreateUpdateForm'
                      onSubmit={handleSubmit(callApi)}>
                    <Stack direction={{xs: 'column', sm: 'row'}}
                           divider={<Divider orientation='vertical' flexItem/>}
                           justifyContent='center'
                           gap={2}>
                        <Stack spacing={3}>
                            <LabeledInput labelText='Название*'
                                          formRegister={register('name', {validate: validateEventName})}
                                          error={Boolean(errors.name)}
                                          helperText={errors.name && errors.name.message}/>
                            <LabeledInput labelText='Адрес*'
                                          formRegister={register('address', {validate: validateAddress})}
                                          error={Boolean(errors.address)}
                                          helperText={errors.address && errors.address.message}/>
                        </Stack>
                    </Stack>
                </form>
            </DialogContent>
            <Divider/>
            <Stack direction='row' sx={{px: 2, py: '12px', gap: '12px', justifyContent: 'flex-end'}}>
                <Button onClick={() => {
                    reset()
                }} variant='contained'>Отмена</Button>
                <Button type='submit'
                        form='placeCreateUpdateForm'
                        variant='contained'>
                    Сохранить
                </Button>
            </Stack>
        </TitledDialog>
    )

    function callApi(data) {
        let newPlace: Place = {
            id: place === undefined || action === 'create' ? 0 : place.id,
            name: data.name,
            address: data.address,
        }

        if (action === 'create') {
            addPlace(newPlace)
        } else {
            updatePlace(newPlace)
        }
    }

    async function addPlace(placeToAdd: Place) {
        //Добавляем место
        placeToAdd.id = 0
        const addPlaceRequest: AddPlaceRequest = {
            place: placeToAdd
        }
        let placeId = -1
        await adminApi.addPlace(addPlaceRequest)
            .then(value => {
                placeId = value.id
                console.log(`Created place ${value}`)
            })
            //TODO show popup with error
            .catch(reason => {
                throw new Error(`Failed to create place: ${reason}`)
            })
    }

    async function updatePlace(placeToUpdate: Place) {
        //Обновляем событие
        placeToUpdate.id = place.id
        const updatePlaceByIdRequest: updatePlaceById = {
            place: placeToUpdate
        }

        console.log(updatePlaceByIdRequest)

        await adminApi.updatePlaceById(updatePlaceByIdRequest)
            .then(value => {
                console.log(`Updated place ${value}`)
            })
            //TODO show popup with error
            .catch(reason => {
                throw new Error(`Failed to update place: ${reason}`)
            })
    }
}
