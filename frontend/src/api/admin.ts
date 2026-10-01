import type { ContagemTabelas } from '../models/ContagemTabelas'
import { request } from './client'

export function limparDados(): Promise<ContagemTabelas> {
  return request<ContagemTabelas>('admin/limpar-dados', { method: 'POST' })
}

export function adicionarDadosTeste(): Promise<ContagemTabelas> {
  return request<ContagemTabelas>('admin/dados-teste', { method: 'POST' })
}
