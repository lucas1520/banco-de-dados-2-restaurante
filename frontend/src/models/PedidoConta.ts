import type { ItemPedido } from './ItemPedido'
import type { Pedido } from './Pedido'

export type PedidoConta = {
  pedido: Pedido
  itens: ItemPedido[]
  valorTotal: number
  tempoPrepMinutos: number
}
