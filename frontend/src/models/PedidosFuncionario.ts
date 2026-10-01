import type { Cliente } from './Cliente'
import type { Funcionario } from './Funcionario'
import type { PedidoConta } from './PedidoConta'

export type ClientePedidos = {
  cliente: Cliente
  pedidos: PedidoConta[]
}

export type PedidosFuncionario = {
  funcionario: Funcionario
  inicio: string
  fim: string
  clientes: ClientePedidos[]
}
