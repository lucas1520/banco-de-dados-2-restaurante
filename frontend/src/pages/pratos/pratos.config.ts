import type { EntityConfig } from '../../models/EntityConfig'

export const pratosConfig: EntityConfig = {
  path: 'pratos',
  title: 'Pratos',
  singular: 'prato',
  pk: 'idPrato',
  sortable: true,
  fields: [
    { name: 'nome', label: 'Nome', type: 'text' },
    { name: 'valor', label: 'Valor', type: 'number', currency: true },
    { name: 'tempoPrep', label: 'Tempo de preparo', type: 'number' },
    { name: 'ativo', label: 'Ativo', type: 'boolean', defaultValue: true, hideOnCreate: true },
  ],
}
