import type { Ingrediente } from '../models/Ingrediente'
import { createCrudApi } from './crud'

export const ingredientesApi = createCrudApi<Ingrediente>('ingredientes')
