import type { ComposicaoPrato } from '../models/ComposicaoPrato'
import { createCrudApi } from './crud'

export const composicoesApi = createCrudApi<ComposicaoPrato>('composicoes')
