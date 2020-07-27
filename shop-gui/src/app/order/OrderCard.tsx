import React, {ReactElement} from 'react'
import {
  WiredCard,
  WiredDivider
} from 'react-wired-elements'
import {
  OrderData,
  OrderPosition
} from '../../api/orders_api'

type OrderCardProps = { index: number, order: OrderData }
type OrderTableProps = { positions: OrderPosition[] }
type OrderPositionProps = { index: number, position: OrderPosition }

function OrderHeaderRow(): ReactElement {
  return <tr>
    <th>Position</th>
    <th>Item</th>
    <th>Single Price</th>
    <th>Count</th>
    <th>Combined Price</th>
  </tr>
}

function OrderPositionRow({index, position}: OrderPositionProps): ReactElement {
  return <tr>
    <td>{index + 1}</td>
    <td>{position.itemName}</td>
    <td>{position.singlePrice}</td>
    <td>{position.count}</td>
    <td>{position.combinedPrice}</td>
  </tr>
}

function OrderTable({positions}: OrderTableProps): ReactElement {
  return <table>
    <thead>
      <OrderHeaderRow/>
    </thead>
    <tbody>
      {
        positions.map((position, index) =>
          <OrderPositionRow key={index} index={index} position={position}/>)
      }
    </tbody>
  </table>
}

export function OrderCard({index, order}: OrderCardProps): ReactElement {
  return <div>
    <WiredCard elevation={2}>
      <h3>Order #{index + 1} - Total: {order.total}</h3>
      <OrderTable positions={order.positions}/>
    </WiredCard>
    <WiredDivider elevation={1}/>
  </div>
}
