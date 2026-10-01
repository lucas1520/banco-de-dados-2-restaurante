import type { Funcionario } from '../models/Funcionario'
import { createCrudApi } from './crud'

export const funcionariosApi = createCrudApi<Funcionario>('funcionarios')
