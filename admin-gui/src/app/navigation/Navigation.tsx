import React, {ReactElement} from 'react'
import {
  WiredItem,
  WiredListBox
} from 'react-wired-elements'
import './Navigation.css'

type NavigationProps = {
  onSelect: (ev: CustomEvent) => void
  selected: string
}

export function Navigation(props: NavigationProps): ReactElement {

  return (<div className="Navigation">
    <WiredListBox horizontal selected={props.selected} bgColor="black" onSelect={props.onSelect}>
      <WiredItem value="products">Products</WiredItem>
      <WiredItem value="orders">Orders</WiredItem>
    </WiredListBox>
  </div>)
}
