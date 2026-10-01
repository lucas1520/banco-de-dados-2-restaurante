import type { Mesa } from '../models/Mesa'
import type { PedidoConta } from '../models/PedidoConta'
import type { PratoQuantidade } from '../models/PratoQuantidade'
import { request } from './client'
import { createCrudApi } from './crud'

export const mesasApi = createCrudApi<Mesa>('mesas')

export function pratosDaMesa(id: number): Promise<PratoQuantidade[]> {
  return request<PratoQuantidade[]>(`mesas/${id}/pratos`)
}

export function pedidosDaMesa(id: number): Promise<PedidoConta[]> {
  return request<PedidoConta[]>(`mesas/${id}/pedidos`)
}
