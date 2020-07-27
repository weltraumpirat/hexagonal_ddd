import React, {ReactElement} from 'react'
import {
  WiredButton,
  WiredCard,
  WiredDivider,
  WiredIconButton
} from 'react-wired-elements'
import {ShoppingCartItemData} from '../../api/shoppingcarts_api'
import './ShoppingCart.css'

type ShoppingCartProps = {
  items: ShoppingCartItemData[]
  total: string
  onCheckOut: () => void
  onItemRemovedFromCart: (item: ShoppingCartItemData) => void
}
type ShoppingCartTableProps = {
  items: ShoppingCartItemData[]
  total: string
  onItemRemovedFromCart: (item: ShoppingCartItemData) => void
}
type ShoppingCartItemProps = {
  index: number
  item: ShoppingCartItemData
  onItemRemovedFromCart: (item: ShoppingCartItemData) => void
}
type ShoppingCartSumProps = { total: string }

function ShoppingCartHeaderRow(): ReactElement {
  return <tr>
    <th>Position</th>
    <th>Item</th>
    <th>Price</th>
    <th>Action</th>
  </tr>
}

function ShoppingCartItemRow(props: ShoppingCartItemProps): ReactElement {
  const {item, index, onItemRemovedFromCart} = props
  const product: ShoppingCartItemData = {...item}
  const handleClick = (): void => onItemRemovedFromCart(product)
  return <tr>
    <td>{index + 1}</td>
    <td>{item.label}</td>
    <td>{item.price}</td>
    <td><WiredIconButton onClick={handleClick} icon="remove_shopping_cart"/></td>
  </tr>
}

function ShoppingCartSumRow({total}: ShoppingCartSumProps): ReactElement {
  return <tr>
    <td className="sum" colSpan={3}>Sum</td>
    <td className="sum">{total}</td>
  </tr>
}


function ShoppingCartTable({items, total, onItemRemovedFromCart}: ShoppingCartTableProps): ReactElement {
  return <table>
    <thead>
      <ShoppingCartHeaderRow/>
    </thead>
    <tbody>
      {
        items.map((item, index) => <ShoppingCartItemRow key={index} index={index} item={item}
                onItemRemovedFromCart={onItemRemovedFromCart}/>)
      }
      <ShoppingCartSumRow total={total}/>
    </tbody>
  </table>
}

export function ShoppingCart(props: ShoppingCartProps): ReactElement {
  return (<div className="shoppingcart">
    <WiredCard elevation={1}>
      <h2>ShoppingCart</h2>
      <ShoppingCartTable {...props}/>
      <WiredDivider/>
      <WiredButton className="checkout" onClick={props.onCheckOut}>Check out</WiredButton>
    </WiredCard>
  </div>)
}


