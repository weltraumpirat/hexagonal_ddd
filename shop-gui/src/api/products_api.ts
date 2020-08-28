import {PackagingType} from '../app/products/product'
import axios from 'axios'

export interface ProductData {
  id?: string
  name: string
  packagingType: PackagingType
  amount: string
  price: string
}
const ENDPOINT_PRODUCTS = "http://localhost/api/product"
const ENDPOINT_SHOPPING = "http://localhost/api/product/shopping"
export class ProductsApi {


  public async addProduct(data: ProductData): Promise<void> {
    await axios.post(ENDPOINT_PRODUCTS, data)
  }

  public async createCatalogWithProducts(products: ProductData[]): Promise<void> {
    await Promise.all(products.map(this.addProduct))
  }

  public async getProducts(): Promise<ProductData[]> {
    return (await axios.get(ENDPOINT_SHOPPING)).data
  }
}
