import type { DiaDemanda } from '../models/DiaDemanda'
import type { Lucro } from '../models/Lucro'
import type { PedidosFuncionario } from '../models/PedidosFuncionario'
import type { Periodo } from '../models/Periodo'
import { request } from './client'

const query = (p: Periodo) => `inicio=${p.inicio}&fim=${p.fim}`

export function pedidosDoFuncionario(matricula: number, p: Periodo): Promise<PedidosFuncionario> {
  return request<PedidosFuncionario>(`relatorios/funcionarios/${matricula}/pedidos?${query(p)}`)
}

export function lucroDoPeriodo(p: Periodo): Promise<Lucro> {
  return request<Lucro>(`relatorios/lucro?${query(p)}`)
}

export function demandaDoPeriodo(p: Periodo): Promise<DiaDemanda[]> {
  return request<DiaDemanda[]>(`relatorios/demanda?${query(p)}`)
}
