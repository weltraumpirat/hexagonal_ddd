import React, {ReactElement} from 'react'
import './Shopping.css'
import {
  WiredCard,
  WiredIconButton
} from 'react-wired-elements'
import {ProductData} from '../../api/products_api'
import {ShoppingCartItemData} from '../../api/shoppingcarts_api'

type ShoppingProps = {
  availableProducts: ProductData[]
  onItemAddedToCart: (item: ShoppingCartItemData) => void
}
type ShoppingTableProps = { products: ProductData[], onItemAddedToCart: (item: ShoppingCartItemData) => void }

type ShoppingTableRowProps = {
  product: ProductData
  onItemAddedToCart: (item: ShoppingCartItemData) => void
}

function ShoppingTableRow(props: ShoppingTableRowProps): ReactElement {
  const onItemAddedToCart = props.onItemAddedToCart || ((item: ShoppingCartItemData) => console.log('added to cart', item))
  return <tr>
    <td>{props.product.name}</td>
    <td>{props.product.amount}</td>
    <td>{props.product.packagingType}</td>
    <td>{props.product.price}</td>
    <td><WiredIconButton onClick={() => onItemAddedToCart({
      label: props.product.name + ', ' + props.product.packagingType + ' ' + props.product.amount,
      price: props.product.price
    })} icon="add_shopping_cart"/></td>
  </tr>
}


function ShoppingTableHeaderRow(): ReactElement {
  return <tr>
    <th>Name</th>
    <th>Amount</th>
    <th>Packaging</th>
    <th>Price per unit</th>
    <th>Action</th>
  </tr>
}

function ShoppingTable({products, onItemAddedToCart}: ShoppingTableProps): ReactElement {
  return <table>
    <thead>
      <ShoppingTableHeaderRow/>
    </thead>
    <tbody>
      {
        products.map((product, index) => <ShoppingTableRow key={index} product={product}
                onItemAddedToCart={onItemAddedToCart}/>)
      }
    </tbody>
  </table>
}

export function Shopping({availableProducts, onItemAddedToCart}: ShoppingProps): ReactElement {
  return (<div className="shopping">
    <WiredCard elevation={1}>
      <h2>Shopping</h2>
      <ShoppingTable products={availableProducts} onItemAddedToCart={onItemAddedToCart}/>
    </WiredCard>
  </div>)
}
