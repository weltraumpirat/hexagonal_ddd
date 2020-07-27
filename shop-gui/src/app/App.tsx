import React, {ReactElement} from 'react'
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
import {UUID} from '../types'

export const ZERO: string = 'EUR 0.00'

const history = createBrowserHistory()
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

const ordersApi: OrdersApi = new OrdersApi()
const shoppingCartApi = new ShoppingCartsApi()

type AppState = {
  products: ProductData[]
  orders: OrderData[]
  cart: UUID
  items: ShoppingCartItemData[]
  itemInfo: ShoppingCartItemsInfo
  count: number
  total: string
  active: string
}

const initialState: AppState = {
  products: [],
  orders: [],
  cart: '',
  items: [],
  itemInfo: {items: [], count: 0, total: ZERO},
  count: 0,
  total: ZERO,
  active: ''
}

export default class App extends React.Component<{}, AppState> {
  constructor(props: {}) {
    super(props)
    this.state = initialState
    this.handleNavigationSelect = this.handleNavigationSelect.bind(this)
    this.handleCheckout = this.handleCheckout.bind(this)
    this.handleItemAddedToCart = this.handleItemAddedToCart.bind(this)
    this.handleItemRemovedFromCart = this.handleItemRemovedFromCart.bind(this)
    this.updateAfterShoppingCartChange = this.updateAfterShoppingCartChange.bind(this)
  }

  componentDidMount(): void {
    const fetch = async (): Promise<void> => {
      await initProducts()
      const products: ProductData[] = await productsApi.getProducts()
      const cart: UUID = await shoppingCartApi.createEmptyShoppingCart()
      const orders: OrderData[] = await ordersApi.getOrders()
      this.setState({products, cart, orders})
    }
    fetch()
  }

  handleNavigationSelect({detail: {selected}}: Partial<CustomEvent>): void {
    if (selected !== this.state.active) {
      this.setState({active: selected}, async (): Promise<void> => {
        const products: ProductData[] = selected === 'products' ? await productsApi.getProducts() : this.state.products
        const orders: OrderData[] = selected === 'orders' ? await ordersApi.getOrders() : this.state.orders
        this.setState({products, orders}, () => history.push(`/${selected}`))
      })
    }
  }

  async updateAfterShoppingCartChange(): Promise<void> {
    const info: ShoppingCartItemsInfo = await shoppingCartApi.getShoppingCartItems(this.state.cart)
    this.setState({...info})
  }

  async handleItemAddedToCart(item: ShoppingCartItemData): Promise<void> {
    await shoppingCartApi.addItemToShoppingCart(this.state.cart, {...item})
    await this.updateAfterShoppingCartChange()
  }

  async handleItemRemovedFromCart(item: ShoppingCartItemData): Promise<void> {
    await shoppingCartApi.removeItemFromShoppingCart(this.state.cart, {...item})
    this.updateAfterShoppingCartChange()
  }

  async handleCheckout(): Promise<void> {
    await shoppingCartApi.checkOut(this.state.cart)
    const cart: UUID = await shoppingCartApi.createEmptyShoppingCart()
    this.setState({
      cart,
      items: [],
      count: 0,
      total: ZERO
    }, () => this.handleNavigationSelect({detail: {selected: 'orders'}}))
  }

  render(): ReactElement {
    const {active, count, products, items, total, orders} = this.state
    return (<div className="App">
      <Router history={history}>
        <Navigation selected={active} onSelect={this.handleNavigationSelect} shoppingCartItems={count}/>
        <Switch>
          <Route path="/shopping">
            <Shopping
                    availableProducts={products}
                    onItemAddedToCart={this.handleItemAddedToCart}/>
          </Route>
          <Route path="/cart">
            <ShoppingCart items={items} total={total} onCheckOut={this.handleCheckout}
                    onItemRemovedFromCart={this.handleItemRemovedFromCart}/>
          </Route>
          <Route path="/orders">
            <Orders orders={orders}/>
          </Route>
        </Switch>
      </Router>
    </div>)
  }
}

