import type { EntityConfig } from '../../models/EntityConfig'

export const ingredientesConfig: EntityConfig = {
  path: 'ingredientes',
  title: 'Ingredientes',
  singular: 'ingrediente',
  pk: 'idIngrediente',
  sortable: true,
  fields: [
    { name: 'nome', label: 'Nome', type: 'text' },
    { name: 'preco', label: 'Preço', type: 'number', currency: true },
    { name: 'estoque', label: 'Estoque', type: 'number' },
  ],
}
