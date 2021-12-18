import {UUID} from '../types'
import axios from 'axios'
import moment, {Moment} from 'moment'


export interface OrderData {
  id: UUID
  positions: OrderPosition[]
  total: string
  timestamp: Moment
}

export interface OrderPosition {
  id: string
  itemName: string
  count: number
  singlePrice: string
  combinedPrice: string
}

const ENDPOINT_ORDER: string = 'http://localhost:8080/api/order'

export class OrdersApi {

  public async create(order: OrderData): Promise<void> {
    await axios.post(ENDPOINT_ORDER, order)
  }

  public async getOrders(): Promise<OrderData[]> {
    const data: OrderData[] = (await axios.get(ENDPOINT_ORDER)).data
    return data.map(d => ({...d, timestamp: moment(d.timestamp)}))
  }
}
