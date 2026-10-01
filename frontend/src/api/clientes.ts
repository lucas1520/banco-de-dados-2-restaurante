import type { Cliente } from '../models/Cliente'
import type { ContaClienteResponse } from '../models/ContaClienteResponse'
import type { PagarClienteResponse } from '../models/PagarClienteResponse'
import { request } from './client'
import { createCrudApi } from './crud'

export const clientesApi = createCrudApi<Cliente>('clientes')

export function contaCliente(id: number): Promise<ContaClienteResponse> {
  return request<ContaClienteResponse>(`clientes/${id}/conta`)
}

export function pagarCliente(id: number): Promise<PagarClienteResponse> {
  return request<PagarClienteResponse>(`clientes/${id}/pagar`, { method: 'POST' })
}
