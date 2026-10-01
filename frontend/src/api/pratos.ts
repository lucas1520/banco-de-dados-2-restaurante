import type { Prato } from '../models/Prato'
import type { PratoPossivel } from '../models/PratoPossivel'
import { request } from './client'
import { createCrudApi } from './crud'

export const pratosApi = createCrudApi<Prato>('pratos')

export function pratosPossiveis(): Promise<PratoPossivel[]> {
  return request<PratoPossivel[]>('pratos/possiveis')
}
