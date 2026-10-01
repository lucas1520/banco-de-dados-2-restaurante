import type { Cliente } from './Cliente'

export type PagarClienteResponse = {
  cliente: Cliente
  total: number
  mesaLiberada: boolean
  clientesPendentesNaMesa: number
}
