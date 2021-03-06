import React, {ReactElement} from 'react'
import {
  WiredFab,
  WiredItem,
  WiredListBox
} from 'react-wired-elements'
import './Navigation.css'

type NavigationProps = {
  shoppingCartItems: number
  onSelect: (ev: CustomEvent) => void
  selected: string
}


export function Navigation(props: NavigationProps): ReactElement {
  return (<div className="Navigation">
    <WiredListBox horizontal selected={props.selected} bgColor="black" onSelect={props.onSelect}>
      <WiredItem value="shopping">Shopping</WiredItem>
      <WiredItem value="cart">
        Shopping Cart<WiredFab bgColor="red" className="badge" icon={props.shoppingCartItems}/>
      </WiredItem>
      <WiredItem value="orders">My Orders</WiredItem>
    </WiredListBox>
  </div>)
}
