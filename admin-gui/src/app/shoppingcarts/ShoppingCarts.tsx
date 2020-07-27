import React, {ReactElement} from 'react'
import {
  ShoppingCartData,
  ShoppingCartItemData
} from '../../api/shoppingcarts_api'
import {UUID} from '../../types'
import {
  WiredCard,
  WiredIconButton
} from 'react-wired-elements'

type ShoppingCartProps = {
  carts: ShoppingCartData[]
  onCartDeleted: (cartId: UUID) => void
}

type ShoppingCartTableRowProps = {
  id: UUID
  items: ShoppingCartItemData[]
  count: number
  total: string
  onCartDeleted: (cartId: UUID) => void
}

function ShoppingCartTableRow(props: ShoppingCartTableRowProps): ReactElement {
  return (<tr>
    <td>{props.id}</td>
    <td>{props.count}</td>
    <td>{props.total}</td>
    <td><WiredIconButton onClick={() => props.onCartDeleted(props.id)} icon="clear_shopping_cart"/></td>
  </tr>)
}

type ShoppingCartListProps = {
  carts: ShoppingCartData[]
  onCartDeleted: (cartId: UUID) => void
}

function ShoppingCartList(props: ShoppingCartListProps): ReactElement {
  return (<table>
    <thead>
      <tr>
        <th>Cart ID</th>
        <th>Items</th>
        <th>Total Price</th>
        <th>Action</th>
      </tr>
    </thead>
    <tbody>
      {
        props.carts.map(c => (<ShoppingCartTableRow {...c} onCartDeleted={props.onCartDeleted} key={c.id}/>))
      }
    </tbody>
  </table>
  )
}

function ShoppingCarts(props: ShoppingCartProps): ReactElement {
  return ((<div className="shoppingcarts">
    <WiredCard elevation={1}>
      <h2>ShoppingCarts</h2>
      <ShoppingCartList carts={props.carts} onCartDeleted={props.onCartDeleted}/>
    </WiredCard>
  </div>))
}

export default ShoppingCarts
