import {Button, DialogContent, Divider, FormLabel, Stack} from "@mui/material";
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
import {useEffect, useState} from "preact/hooks";
import {EventWithPlaceResponse} from "@/openapi/models/EventWithPlaceResponse.ts";
import {adminApi, defaultApi} from "@/util/api.ts";
import {Category} from "@/openapi/models/Category.ts";
import {Place} from "@/openapi/models/Place.ts";
import {Event} from "@/openapi/models/Event.ts";
import {
    AddEventImageRequest,
    AddEventRequest,
    AddPlaceRequest,
    UpdateEventByIdRequest,
    UpdatePlaceByIdRequest
} from "@/openapi/apis/AdminApi.ts";
import {labeledInputLabelStyle} from "@/util/styles.ts";
import ImageUpload from "@/components/commons/ImageUpload.tsx";
import {toLocalIsoString} from "@/util/utils.ts";

/**
 * Диалог для создания и редактирования события
 * @param action {'create' | 'update'}, по умолчанию 'create'
 * @param event {EventWithPlaceResponse | undefined} событие, которое нужно отредактировать. Если action = 'create', то event = undefined
 * @param props пропсы диалога
 */
export function EventDialog({action = 'create', event = undefined, ...props}:
                                { action?: 'create' | 'update', event?: EventWithPlaceResponse }) {

    const {
        register,
        handleSubmit,
        reset,
        watch,
        setValue,
        formState: {errors}
    } = useForm()

    const [availableCategoriesArray, setAvailableCategoriesArray] = useState<Category[]>([])

    useEffect(() => {
        defaultApi.getAllCategories()
            .then(value => setAvailableCategoriesArray(value))
            //TODO show popup with error
            .catch(reason => console.log(reason))
    }, [props.open]);

    useEffect(() => {
        register('categories')
        register('images')
    }, [register])

    useEffect(() => {
        reset({
            name: (action === 'create' || !event) ? undefined : event.name,
            startDateTime: (action === 'create' || !event) ? undefined : toLocalIsoString(event.startTime),
            endDateTime: (action === 'create' || !event) ? undefined : toLocalIsoString(event.endTime),
            ageLimit: (action === 'create' || !event) ? defaultAgeLimit : event.ageLimit,
            place: (action === 'create' || !event) ? undefined : event.place.name,
            address: (action === 'create' || !event) ? undefined : event.place.address,
            description: (action === 'create' || !event) ? undefined : event.description,
            categories: (action === 'create' || !event) ? new Set() : new Set(Array.from(event.categories).map(c => c.name)),
            images: []
        })
        if (action === 'update' && event) {
            fetchEventImages(event.imageUrls)
        }
    }, [action, event, reset]);


    return (
        <TitledDialog titleText={action === 'create' ? 'Добавление события' : 'Изменение события'} {...props}>
            <DialogContent>
                <form id='eventCreateUpdateForm'
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
                                                  availableItems={new Set(availableCategoriesArray.map(c => c.name))}
                                                  selectedItems={watch('categories')}
                                                  setSelectedItems={(categories) => setValue('categories', categories)}
                            />
                            <Stack>
                                <FormLabel sx={labeledInputLabelStyle}>Фото</FormLabel>
                                <ImageUpload images={watch('images')}
                                             setImages={(images) => setValue('images', images)}/>
                            </Stack>
                        </Stack>
                    </Stack>
                </form>
            </DialogContent>
            <Divider/>
            <Stack direction='row' sx={{px: 2, py: '12px', gap: '12px', justifyContent: 'flex-end'}}>
                <Button onClick={() => {
                    reset()
                    if (action === 'update' && event) {
                        fetchEventImages(event.imageUrls)
                    }
                }} variant='contained'>Отмена</Button>
                <Button type='submit'
                        form='eventCreateUpdateForm'
                        variant='contained'>
                    Сохранить
                </Button>
            </Stack>
        </TitledDialog>
    )

    function callApi(data) {
        let place: Place = {
            id: event === undefined || action === 'create' ? 0 : event.place.id,
            name: data.place,
            address: data.address,
        }

        const ageLimit = data.ageLimit
        let newEvent: Event = {
            id: event === undefined || action === 'create' ? 0 : event.id,
            name: data.name,
            startTime: new Date(data.startDateTime),
            endTime: new Date(data.endDateTime),
            ageLimit: ageLimit === "" || ageLimit === null || ageLimit === undefined ?
                defaultAgeLimit :
                Number(ageLimit),
            description: data.description,
            canceled: event === undefined || action === 'create' ? false : event.canceled,
            categoryIds: new Set(availableCategoriesArray
                .filter(c => data.categories.has(c.name))
                .map(c => c.id)),
            imageUrls: new Set(),
            placeId: place.id
        }

        if (action === 'create') {
            addEvent(newEvent, place, data.images)
        } else {
            updateEvent(newEvent, place, data.images)
        }
    }

    async function addEvent(eventToAdd: Event, place: Place, images: File[] = []) {
        //Добавляем место
        place.id = 0
        const addPlaceRequest: AddPlaceRequest = {
            place: place
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

        //Добавляем событие
        eventToAdd.id = 0
        eventToAdd.placeId = placeId
        const addEventRequest: AddEventRequest = {
            event: eventToAdd
        }
        let eventId = -1
        await adminApi.addEvent(addEventRequest)
            .then(value => {
                eventId = value.id
                console.log(`Created event ${value}`)
            })
            //TODO show popup with error
            .catch(reason => {
                throw new Error(`Failed to create event: ${reason}`)
            })

        //Добавляем изображения
        eventToAdd.id = eventId
        for (let i = 0; i < images.length; i++) {
            const addEventImageRequest: AddEventImageRequest = {
                id: eventId,
                image: images[i],
                main: i === 0
            }
            await adminApi.addEventImage(addEventImageRequest)
                .then(value => console.log(`Image for event ${eventId} added, url: ${value}`))
                //TODO show popup with message, that not all images were added
                .catch(reason => console.log('Failed to add image: ' + reason))
        }
    }

    async function updateEvent(eventToUpdate: Event, place: Place, images: File[] = []) {
        //Обновляем изображения
        const imageUrls = new Set<string>()
        for (let i = 0; i < images.length; i++) {
            //Если изображение и так было, то добавляем, не сохраняя его на сервере
            if (event.imageUrls.has(images[i].name)) {
                imageUrls.add(images[i].name)
                continue
            }
            const addEventImageRequest: AddEventImageRequest = {
                id: event.id,
                image: images[i],
                main: i === 0
            }
            await adminApi.addEventImage(addEventImageRequest)
                .then(value => {
                        console.log(`Image for event ${event.id} added, url: ${value}`)
                        imageUrls.add(value)
                    }
                )
                //TODO show popup with message, that not all images were added
                .catch(reason => console.log('Failed to add image: ' + reason))
        }

        //Обновляем событие
        eventToUpdate.id = event.id
        eventToUpdate.imageUrls = imageUrls
        const updateEventByIdRequest: UpdateEventByIdRequest = {
            event: eventToUpdate
        }
        await adminApi.updateEventById(updateEventByIdRequest)
            .then(value => {
                console.log(`Updated event ${value}`)
            })
            //TODO show popup with error
            .catch(reason => {
                throw new Error(`Failed to update event: ${reason}`)
            })

        // //Обновляем место
        // place.id = event.place.id
        // const updatePlaceByIdRequest: UpdatePlaceByIdRequest = {
        //     place: place
        // }
        // await adminApi.updatePlaceById(updatePlaceByIdRequest)
        //     .then(value => {
        //         console.log(`Updated place ${value}`)
        //     })
        //     //TODO show popup with error
        //     .catch(reason => {
        //         throw new Error(`Failed to update place: ${reason}`)
        //     })
    }

    /**
     * Отправляет запрос на получение изображения по ссылке
     * @param url
     */
    async function urlToFile(url: string): Promise<File> {
        let response
        await fetch(url)
            .then(value => response = value)
            //TODO show popup with message, that not all images were retrieved
            .catch(reason => console.log(`Failed to fetch image from ${url}, reason: ${reason}`))

        let blob
        await response.blob()
            .then(value => blob = value)
            .catch(reason => console.log(`Failed to convert response to blob: ${reason}`))
        return new File([blob], url)
    }

    /**
     * Загружает все изображения с сервера и добавляет их в форму
     * @param imageUrls
     */
    async function fetchEventImages(imageUrls: Set<string>): Promise<File[]> {
        const files: File[] = [];
        for (let url of imageUrls) {
            await urlToFile(url)
                .then(value => {
                    files.push(value)
                })
                //TODO show popup with message, that not all images were processed
                .catch(reason => console.log(`Failed to convert url to file: ${reason}`))
        }
        setValue('images', files)
        return files
    }
}
