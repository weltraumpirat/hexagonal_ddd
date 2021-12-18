import {UUID} from '../types'
import axios from 'axios'

export interface ShoppingCartData {
  id: UUID
  count: number
  total: string
}

export interface ShoppingCartItemData {
  id?: string
  label?: string
  price: string
}

const ENDPOINT_CARTS = 'http://localhost:8082/api/cart'

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

  public async checkOut(id: UUID): Promise<void> {
    await axios.post(`${ENDPOINT_CARTS}/${id}/checkout`)
  }

  async delete(cartId: UUID): Promise<void> {
    await axios.delete(`${ENDPOINT_CARTS}/${cartId}`)
  }

  async getShoppingCarts(): Promise<ShoppingCartData[]> {
    return ((await axios.get(`${ENDPOINT_CARTS}`)).data as ShoppingCartData[])
  }
}

