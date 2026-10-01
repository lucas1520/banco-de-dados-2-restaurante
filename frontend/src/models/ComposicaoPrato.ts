import type { Ingrediente } from './Ingrediente'
import type { Prato } from './Prato'

export type ComposicaoPrato = {
  prato: Prato
  ingrediente: Ingrediente
  quantidade: number
}
