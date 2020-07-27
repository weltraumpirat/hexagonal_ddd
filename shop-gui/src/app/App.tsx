import React, {
  ReactElement,
  useEffect,
  useState,
  useCallback
} from 'react'
import './App.css'
import {
  Route,
  Router,
  Switch
} from 'react-router-dom'
import {createBrowserHistory} from 'history'
import {
  ProductData,
  ProductsApi
} from '../api/products_api'
import {Navigation} from './navigation/Navigation'
import {Products} from './products/Products'
import {Shopping} from './shopping/Shopping'
import {ShoppingCart} from './shoppingcart/ShoppingCart'
import {Orders} from './order/Orders'
import {
  ShoppingCartItemData,
  ShoppingCartItemsInfo,
  ShoppingCartsApi
} from '../api/shoppingcarts_api'
import {
  OrderData,
  OrdersApi
} from '../api/orders_api'
import {PackagingType} from './products/product'

export const ZERO: string = 'EUR 0.00'

const history = createBrowserHistory()
const productsApi = new ProductsApi()
const initProducts = async (): Promise<void> => {
  if( (await productsApi.getProducts()).length === 0 ) {
    await productsApi.createCatalogWithProducts([
      {name: 'Whole Milk', packagingType: PackagingType.CARTON, amount: '1l', price: 'EUR 1.19'},
      {name: 'Whole Milk', packagingType: PackagingType.CARTON, amount: '0.5l', price: 'EUR 0.69'},
      {name: 'White Bread', packagingType: PackagingType.LOAF, amount: '500g', price: 'EUR 1.19'},
      {name: 'Whole Wheat Bread', packagingType: PackagingType.LOAF, amount: '500g', price: 'EUR 1.59'},
      {name: 'Organic Bread', packagingType: PackagingType.LOAF, amount: '500g', price: 'EUR 2.19'},
      {name: 'Butter', packagingType: PackagingType.PACK, amount: '250g', price: 'EUR 1.69'},
      {name: 'Organic  Butter', packagingType: PackagingType.PACK, amount: '250g', price: 'EUR 2.39'}
    ])
  }
}

const ordersApi: OrdersApi = new OrdersApi()
const shoppingCartApi = new ShoppingCartsApi()

function App(): ReactElement {
  const init: ProductData[] = []
  const initCart: ShoppingCartItemData[] = []
  const initOrders: OrderData[] = []
  const [cart, setCart] = useState('')
  const [products, setProducts] = useState(init)
  const [items, setItems] = useState(initCart)
  const [orders, setOrders] = useState(initOrders)
  const [count, setCount] = useState(0)
  const [total, setTotal] = useState(ZERO)
  const [active, setActive] = useState('')

  useEffect(() => {
    const fetch = async (): Promise<void> => {
      await initProducts()
      setProducts(await productsApi.getProducts())
      setCart(await shoppingCartApi.createEmptyShoppingCart())
      setOrders(await ordersApi.getOrders())
    }
    fetch()
  }, [])

  const handleNavigationSelect = useCallback((ev: CustomEvent): void => {
    setActive('')
    history.push(`/${ev.detail.selected}`)
  }, []);

  const updateAfterShoppingCartChange = async (): Promise<void> => {
    const info: ShoppingCartItemsInfo = await shoppingCartApi.getShoppingCartItems(cart)
    setItems(info.items)
    setCount(info.count)
    setTotal(info.total)
  }

  const handleItemAddedToCart: (item: ShoppingCartItemData) => void = async (item) => {
    await shoppingCartApi.addItemToShoppingCart(cart, {...item})
    updateAfterShoppingCartChange()
  }

  const handleItemRemovedFromCart: (item: ShoppingCartItemData) => void = async (item) => {
    await shoppingCartApi.removeItemFromShoppingCart(cart, {...item})
    updateAfterShoppingCartChange()
  }

  const handleCheckout: () => void = async () => {
    await shoppingCartApi.checkOut(cart)
    setOrders(await ordersApi.getOrders())
    setCart(await shoppingCartApi.createEmptyShoppingCart())
    setItems(initCart)
    setCount(0)
    setTotal(ZERO)
    setActive('orders')

    history.push('/orders')
  }

  const handleProductAdded: (p: ProductData) => void = async (p: ProductData) => {
    await productsApi.addProduct(p)
    setProducts(await productsApi.getProducts())
  }

  return (<div className="App">
    <Router history={history}>
      <Navigation selected={active} onSelect={handleNavigationSelect} shoppingCartItems={count}/>
      <Switch>
        <Route path="/products">
          <Products products={products} onProductAdded={handleProductAdded}/>
        </Route>
        <Route path="/shopping">
          <Shopping
                  availableProducts={products}
                  onItemAddedToCart={handleItemAddedToCart}/>
        </Route>
        <Route path="/cart">
          <ShoppingCart items={items} total={total} onCheckOut={handleCheckout}
                  onItemRemovedFromCart={handleItemRemovedFromCart}/>
        </Route>
        <Route path="/orders">
          <Orders orders={orders}/>
        </Route>
      </Switch>
    </Router>
  </div>)
}

export default App
