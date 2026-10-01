import type { EntityConfig } from '../../models/EntityConfig'

export const funcionariosConfig: EntityConfig = {
  path: 'funcionarios',
  title: 'Funcionários',
  singular: 'funcionário',
  pk: 'matricula',
  pkLabel: 'Matrícula',
  sortable: true,
  fields: [
    { name: 'nome', label: 'Nome', type: 'text' },
    { name: 'cargo', label: 'Cargo', type: 'text' },
    { name: 'cpf', label: 'CPF', type: 'text' },
    { name: 'ativo', label: 'Ativo', type: 'boolean', defaultValue: true, hideOnCreate: true },
  ],
}
