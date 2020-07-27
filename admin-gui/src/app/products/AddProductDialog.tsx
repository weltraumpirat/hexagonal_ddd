import {ProductData} from '../../api/products_api'
import React, {ReactElement} from 'react'
import {
  InputEvent,
  useInput
} from '../inputbindings'
import {
  WiredButton,
  WiredCombo,
  WiredDialog,
  WiredInput,
  WiredItem
} from 'react-wired-elements'
import {PackagingType} from './product'

type AddProductDialogProps = { open: boolean, onCancel: () => void, onSubmit: (p: ProductData) => void }

export function AddProductDialog(props: AddProductDialogProps): ReactElement {
  const {value: name, bind: bindName} = useInput('')
  const {value: packagingType, setValue: setPackagingType} = useInput({text: 'Select one', value: ''})
  const {value: amount, bind: bindAmount} = useInput('')
  const {value: price, bind: bindPrice} = useInput('')

  const packagingTypes: [string, string][] = Object.entries(PackagingType)
  const packagingTypeItemMapper: ([value, label]: readonly [any, any]) => any = ([value, label]) => (
    <WiredItem key={value} value={value}>{label}</WiredItem>)
  const handlePackagingTypeSelect: (e: InputEvent<string>) => void = (e: InputEvent<string>) => {
    setPackagingType(e.target?.value as unknown as { text: string, value: string })
  }

  const handleSubmit = (): void => {
    const added: ProductData = {name, packagingType: packagingType.text as PackagingType, amount, price}
    props.onSubmit(added)
  }

  return <WiredDialog open={props.open}>
    <WiredInput {...bindName} placeholder="Product name"/> <br/>
    <WiredCombo value={packagingType} onSelect={handlePackagingTypeSelect}>
      {packagingTypes.map(packagingTypeItemMapper)}
    </WiredCombo> <br/>
    <WiredInput {...bindAmount} placeholder="Amount"/> <br/>
    <WiredInput {...bindPrice} placeholder="Price"/> <br/>

    <WiredButton className="cancel" elevation={1} onClick={props.onCancel}>Cancel</WiredButton>
    <WiredButton className="ok" elevation={2} onClick={handleSubmit}>Add</WiredButton>
  </WiredDialog>
}
