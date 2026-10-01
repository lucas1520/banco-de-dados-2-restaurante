import type { Pedido } from './Pedido'
import type { Prato } from './Prato'

export type ItemPedido = {
  pedido: Pedido
  prato: Prato
  quantidade: number
  precoUnitario: number
}
