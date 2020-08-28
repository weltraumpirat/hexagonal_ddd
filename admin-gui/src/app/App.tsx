import React, {
  ReactElement,
  useCallback,
  useEffect,
  useState
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
  ProductListRowData,
  ProductsApi
} from '../api/products_api'
import {Navigation} from './navigation/Navigation'
import {Products} from './products/Products'
import {Orders} from './order/Orders'
import {
  OrderData,
  OrdersApi
} from '../api/orders_api'
import {PackagingType} from '../api/product'
import ShoppingCarts from './shoppingcarts/ShoppingCarts'
import {UUID} from '../types'
import {
  ShoppingCartData,
  ShoppingCartsApi
} from '../api/shoppingcarts_api'

export const ZERO: string = 'EUR 0.00'

const history = createBrowserHistory({basename: '/admin'})
const productsApi = new ProductsApi()
const initProducts = async (): Promise<void> => {
  if ((await productsApi.getProducts()).length === 0) {
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

const ordersApi = new OrdersApi()
const shoppingCartsApi = new ShoppingCartsApi()

function App(): ReactElement {
  const init: ProductListRowData[] = []
  const initOrders: OrderData[] = []
  const initCarts: ShoppingCartData[] = []
  const [products, setProducts] = useState(init)
  const [orders, setOrders] = useState(initOrders)
  const [active, setActive] = useState('')
  const [carts, setCarts] = useState(initCarts)

  useEffect(() => {
    const fetch = async (): Promise<void> => {
      await initProducts()
      setProducts(await productsApi.getProducts())
      setOrders(await ordersApi.getOrders())
      setCarts( await shoppingCartsApi.getShoppingCarts())
    }
    fetch()
  }, [])

  const handleNavigationSelect = useCallback(async ({detail:{selected}}: CustomEvent): Promise<void> => {
    setActive(selected)
    if(selected === 'orders')
      setOrders(await ordersApi.getOrders())
    if(selected === 'shoppingcarts')
      setCarts(await shoppingCartsApi.getShoppingCarts())
    history.push(`/${selected}`)
  }, [])


  const handleProductAdded = useCallback(async (p: ProductData): Promise<void> => {
    await productsApi.addProduct(p)
    setProducts(await productsApi.getProducts())
  }, [])

  const handleCartDeleted = useCallback( async (cartId: UUID): Promise<void> => {
    await shoppingCartsApi.delete(cartId)
    setCarts( await shoppingCartsApi.getShoppingCarts())
  }, [])

  return (<div className="App">
    <Router history={history}>
      <Navigation selected={active} onSelect={handleNavigationSelect}/>
      <Switch>
        <Route path="/products">
          <Products products={products} onProductAdded={handleProductAdded}/>
        </Route>
        <Route path="/shoppingcarts">
          <ShoppingCarts carts={carts} onCartDeleted={handleCartDeleted}/>
        </Route>
        <Route path="/orders">
          <Orders orders={orders}/>
        </Route>
      </Switch>
    </Router>
  </div>)
}

export default App
