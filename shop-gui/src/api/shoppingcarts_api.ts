import {UUID} from '../types'
import axios from 'axios'
import {ZERO} from '../app/App'

export interface ShoppingCartData {
  id: UUID
  items: ShoppingCartItemData[]
}

export interface ShoppingCartItemData {
  id?: string
  label?: string
  price: string
}

export interface ShoppingCartItemsInfo {
  items: ShoppingCartItemData[]
  count: number
  total: string
}

const ENDPOINT_CARTS = 'http://localhost/api/cart'

export class ShoppingCartsApi {

  public async createEmptyShoppingCart(): Promise<UUID> {
    return (await axios.post(ENDPOINT_CARTS)).data
  }

  public async addItemToShoppingCart(cartId: string, data: ShoppingCartItemData): Promise<void> {
    await axios.post(`${ENDPOINT_CARTS}/${cartId}`, data)
  }

  public async removeItemFromShoppingCart(cartId: UUID, item: ShoppingCartItemData): Promise<void> {
    await axios.delete(`${ENDPOINT_CARTS}/${cartId}/${item.id}`)
  }

  public async checkOut(id: UUID): Promise<UUID> {
    return (await axios.post(`${ENDPOINT_CARTS}/${id}/checkout`)).data
  }


  public async getShoppingCartItems(id: UUID): Promise<ShoppingCartItemsInfo> {
    return (await axios.get(`${ENDPOINT_CARTS}/${id}`)).data
  }
}

type Currency = string
type Amount = string

class Money {
  public readonly currency: Currency
  public readonly amount: Amount

  public constructor(currency: Currency, amount: Amount) {
    this.currency = currency
    this.amount = amount
  }

  public plus(money: Money): Money {
    return new Money(this.currency, (parseFloat(this.amount) + parseFloat(money.amount)).toFixed(2))
  }

  public toString(): string {
    return this.currency + Money.separator + this.amount
  }

  static readonly separator: string = ' '

  static fromString(price: string): Money {
    const [currency, amount]: string[] = price.split(Money.separator)
    return new Money(currency as Currency, amount as Amount)
  }
}
