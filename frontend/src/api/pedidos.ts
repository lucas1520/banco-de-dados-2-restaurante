import type { AdicionarItemRequest } from '../models/AdicionarItemRequest'
import type { AlterarItemRequest } from '../models/AlterarItemRequest'
import type { ItemPedido } from '../models/ItemPedido'
import type { Pedido } from '../models/Pedido'
import { request } from './client'
import { createCrudApi } from './crud'

export const pedidosApi = createCrudApi<Pedido>('pedidos')

export function criarPedido(idCliente: number, matricula: number): Promise<Pedido> {
  return request<Pedido>('pedidos', {
    method: 'POST',
    body: JSON.stringify({ cliente: { idCliente }, funcionario: { matricula } }),
  })
}

export function excluirPedido(id: number): Promise<void> {
  return request<void>(`pedidos/${id}`, { method: 'DELETE' })
}

export function entregarPedido(id: number): Promise<Pedido> {
  return request<Pedido>(`pedidos/${id}/entregar`, { method: 'POST' })
}

export function adicionarItemPedido(id: number, body: AdicionarItemRequest): Promise<ItemPedido> {
  return request<ItemPedido>(`pedidos/${id}/itens`, {
    method: 'POST',
    body: JSON.stringify(body),
  })
}

export function alterarItemPedido(
  id: number,
  idPrato: number,
  body: AlterarItemRequest,
): Promise<ItemPedido> {
  return request<ItemPedido>(`pedidos/${id}/itens/${idPrato}`, {
    method: 'PUT',
    body: JSON.stringify(body),
  })
}

export function removerItemPedido(id: number, idPrato: number): Promise<void> {
  return request<void>(`pedidos/${id}/itens/${idPrato}`, { method: 'DELETE' })
}
