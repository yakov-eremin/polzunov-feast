import {useCallback} from 'preact/hooks';
import {FunctionComponent, h} from 'preact';
import {Button, Card, CardActionArea, CardActions, CardMedia, Grid, Stack} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import {CloudUpload} from "@mui/icons-material";

interface ImageUploadProps {
    images: File[]
    setImages: (images: File[]) => void
}

/**
 * Компонент для загрузки изображений
 * @param images
 * @param setImages
 * @constructor
 */
const ImageUpload: FunctionComponent<ImageUploadProps> = ({images, setImages}) => {

    const handleChange = useCallback((event: h.JSX.TargetedEvent<HTMLInputElement, Event>) => {
        if (event.target.files && event.target.files[0]) {
            setImages([...images, event.target.files[0]])
        }
    }, [setImages]);

    const handleDelete = useCallback((image: File) => {
        setImages([...images.filter(prevImage => prevImage !== image)])
    }, [setImages]);

    return (
        <Stack gap={1}>
            <input accept="image/*" style={{display: 'none'}} id="raised-button-file" multiple type="file"
                   onChange={handleChange}/>
            <label htmlFor="raised-button-file">
                <Button startIcon={<CloudUpload/>}
                        variant='outlined'
                        component="span"
                        size='small'
                        color='secondary'>
                    Загрузить
                </Button>
            </label>
            <Grid container spacing={2}>
                {images.map((image, index) => (
                    <Grid item xs={4} key={index}>
                        <Card>
                            <CardActionArea>
                                <CardMedia
                                    component="img"
                                    image={URL.createObjectURL(image)}
                                />
                            </CardActionArea>
                            <CardActions>
                                <Button size="small" color="secondary" onClick={() => handleDelete(image)}>
                                    <DeleteIcon/>
                                </Button>
                            </CardActions>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </Stack>
    );
}

export default ImageUpload;

