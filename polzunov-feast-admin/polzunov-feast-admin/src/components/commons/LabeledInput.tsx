import {FormControl, FormLabel, TextField} from "@mui/material";
import {labeledInputLabelStyle} from "@/util/styles.ts"

/**
 * Поле для ввода с подписью сверху. В качестве компонента ввода используется {@link TextField}
 * @param labelText подпись сверху
 * @param formRegister объект 'register' из react-hook-form, необходимый для регистрации компонента в форме
 * @param props пропсы TextField
 * @constructor
 */
export function LabeledInput({labelText, formRegister, ...props}) {

    const newProps = {
        ...props,
        ...formRegister
    }

    return (
        <FormControl>
            <FormLabel sx={labeledInputLabelStyle}>{labelText}</FormLabel>
            <TextField {...newProps}/>
        </FormControl>
    )
}