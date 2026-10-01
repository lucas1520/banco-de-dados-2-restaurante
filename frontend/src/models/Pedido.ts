import type { Cliente } from './Cliente'
import type { Funcionario } from './Funcionario'

export type Pedido = {
  idPedido: number
  entregue: boolean
  cliente: Cliente
  funcionario: Funcionario
}
