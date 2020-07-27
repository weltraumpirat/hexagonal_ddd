import React, {
  ReactElement,
  useState
} from 'react'
import {
  WiredButton,
  WiredCard
} from 'react-wired-elements'
import {ProductData} from '../../api/products_api'
import './Products.css'
import {AddProductDialog} from './AddProductDialog'
import {ProductList} from './ProductList'


type ProductsProps = {
  products?: ProductData[]
  onProductAdded?: (p: ProductData) => void
}

export function Products(props: ProductsProps): ReactElement {
  const [open, setOpen] = useState(false)

  const handleDialogOpen = (): void => setOpen(true)
  const handleDialogCancel = (): void => setOpen(false)

  const handleProductAdded: (p: ProductData) => void = props.onProductAdded || (p => console.log('product added', p))
  const handleDialogSubmit = (added: ProductData): void => {
    handleProductAdded(added)
    setOpen(false)
  }

  return (<div className="products">
    <WiredCard elevation={1}>
      <h2>Products</h2>
      <AddProductDialog open={open} onCancel={handleDialogCancel} onSubmit={handleDialogSubmit}/>
      <ProductList products={props.products}/>
      <WiredButton onClick={handleDialogOpen}>Add Product</WiredButton>
    </WiredCard>
  </div>)
}
