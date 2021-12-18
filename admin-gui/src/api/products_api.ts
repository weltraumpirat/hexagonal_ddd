import {PackagingType} from './product'
import axios from 'axios'

export interface ProductData {
  id?: string
  name: string
  packagingType: PackagingType
  amount: string
  price: string
}

export interface ProductListRowData {
  id?: string
  label: string
  price: string
}

const ENDPOINT_PRODUCTS = "http://localhost:8081/api/product"
export class ProductsApi {


  public async addProduct(data: ProductData): Promise<void> {
    await axios.post(ENDPOINT_PRODUCTS, data)
  }

  public async createCatalogWithProducts(products: ProductData[]): Promise<void> {
    await Promise.all(products.map(async p => await axios.post(ENDPOINT_PRODUCTS, p)))
  }

  public async getProducts(): Promise<ProductListRowData[]> {
    return (await axios.get(ENDPOINT_PRODUCTS)).data
  }
}
