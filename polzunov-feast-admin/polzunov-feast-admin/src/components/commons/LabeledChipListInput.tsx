import {Box, FormControl, FormLabel} from "@mui/material";
import {StatefulChip} from "./StatefulChip.tsx";
import {labeledInputLabelStyle} from "@/util/styles.ts"

/**
 * Список из {@link StatefulChip}ов с возможностью выбора нескольких чипов
 * @param availableItems {Set} список возможных значений
 * @param selectedItems {Set} список выбранных значений
 * @param setSelectedItems {(codes: Set) => void} функция для обновления списка выбранных значений
 * @param labelText текст надписи над списком
 * @param chipProps пропсы чипов
 */
export function LabeledChipListInput({availableItems, selectedItems, setSelectedItems, labelText, ...chipProps}) {
    return (
        <FormControl>
            <FormLabel sx={labeledInputLabelStyle}>{labelText}</FormLabel>
            <Box display='flex' flexWrap='wrap' gap='12px'>
                {Array.from(availableItems).map(item => (
                    <StatefulChip
                        state={selectedItems.has(item) ? 'active' : 'disabled'}
                        onClick={() => {
                            if (selectedItems.has(item)) {
                                selectedItems.delete(item)
                            } else {
                                selectedItems.add(item)
                            }
                            setSelectedItems(new Set(selectedItems))
                        }}
                        key={item}
                        label={item}
                        {...chipProps}/>
                ))}
            </Box>
        </FormControl>
    )
}