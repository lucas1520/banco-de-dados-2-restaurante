import type { Cliente } from './Cliente'
import type { PedidoConta } from './PedidoConta'

export type ContaClienteResponse = {
  cliente: Cliente
  pedidos: PedidoConta[]
  total: number
}
