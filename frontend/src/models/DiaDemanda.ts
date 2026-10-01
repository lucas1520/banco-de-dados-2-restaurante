import type { PratoQuantidade } from './PratoQuantidade'

export type IngredienteUso = {
  idIngrediente: number
  nome: string
  quantidade: number
}

export type DiaDemanda = {
  dia: string
  clientes: number
  pratos: PratoQuantidade[]
  ingredientes: IngredienteUso[]
}
