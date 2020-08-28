import {ProductListRowData} from '../../api/products_api'
import React, {ReactElement} from 'react'

type ProductListEntryProps = { product: ProductListRowData }

function ProductListEntry(props: ProductListEntryProps): ReactElement {
  return (<li>{props.product.label + ': ' + props.product.price}</li>)
}

function ProductListEmptyEntry(): ReactElement {
  return (<li>"No products found."</li>)
}

type ProductListProps = { products?: ProductListRowData[] }

export function ProductList(props: ProductListProps): ReactElement {
  const entryFactory = (product: ProductListRowData, index: number): ReactElement =>
    (<ProductListEntry key={index} product={product}/>)
  return (<ul>
    {
      props.products && props.products.length > 0
        ? props.products.map(entryFactory)
        : <ProductListEmptyEntry/>
    }
  </ul>)
}
