import type { EntityConfig } from '../../models/EntityConfig'

export const mesasConfig: EntityConfig = {
  path: 'mesas',
  title: 'Mesas',
  singular: 'mesa',
  feminine: true,
  pk: 'idMesa',
  fields: [
    { name: 'disponivel', label: 'Disponível', type: 'boolean', hideOnCreate: true },
    { name: 'cadeiras', label: 'Cadeiras', type: 'number' },
  ],
}
