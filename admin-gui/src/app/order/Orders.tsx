import React, {ReactElement} from 'react'
import {WiredCard} from 'react-wired-elements'
import './Orders.css'
import {OrderCard} from './OrderCard'
import {OrderData} from '../../api/orders_api'

type OrdersProps = {
  orders: OrderData[]
}

export function Orders(props: OrdersProps): ReactElement {
  return (<div className="orders">
    <WiredCard elevation={1}>
      <h2>Orders</h2>
      {
        props.orders.map((order, index) =>
          <OrderCard key={index} index={props.orders.length-1-index} order={order}/>)
      }
    </WiredCard>
  </div>)
}
