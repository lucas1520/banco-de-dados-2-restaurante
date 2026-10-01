import type { Mesa } from './Mesa'

export type Cliente = {
  idCliente: number
  nome: string
  pago: boolean
  dataChegada: string
  mesa: Mesa | null
}
