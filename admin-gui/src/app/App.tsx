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
import {Products} from './products/Products'
import {Orders} from './order/Orders'
import {
  OrderData,
  OrdersApi
} from '../api/orders_api'
import {PackagingType} from './products/product'

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

type AppState = {
  products: ProductData[]
  orders: OrderData[]
  active: string
}

const initialState: AppState = {
  products: [],
  orders: [],
  active: ''
}

export default class App extends React.Component<{}, AppState> {
  constructor(props: {}) {
    super(props)
    this.state = initialState
  }

  componentDidMount(): void {
    const fetch = async (): Promise<void> => {
      await initProducts()
      const products: ProductData[] = await productsApi.getProducts()
      const orders: OrderData[] = await ordersApi.getOrders()
      this.setState({products, orders})
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

  async handleProductAdded(p: ProductData) : Promise<void>{
    await productsApi.addProduct(p)
    const products: ProductData[] = await productsApi.getProducts()
    this.setState({products})
  }

  render(): ReactElement {
    const {active, products, orders} = this.state
    return (<div className="App">
      <Router history={history}>
        <Navigation selected={active} onSelect={this.handleNavigationSelect.bind(this)}/>
        <Switch>
          <Route path="/products">
            <Products products={products} onProductAdded={this.handleProductAdded.bind(this)}/>
          </Route>
          <Route path="/orders">
            <Orders orders={orders}/>
          </Route>
        </Switch>
      </Router>
    </div>)
  }
}
