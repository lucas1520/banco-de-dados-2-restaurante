import { ingredientesApi } from '../../api/ingredientes'
import { pratosApi } from '../../api/pratos'
import type { CrudApi } from '../../models/CrudApi'
import type { EntityConfig } from '../../models/EntityConfig'
import type { Row } from '../../models/Row'

export const composicaoPratoConfig: EntityConfig = {
  path: 'composicao-prato',
  title: 'Composição do prato',
  singular: 'composição do prato',
  feminine: true,
  pk: 'prato',
  compositeKey: ['prato', 'ingrediente'],
  fields: [
    {
      name: 'prato',
      label: 'Prato',
      type: 'select',
      select: {
        api: pratosApi as CrudApi<Row>,
        valueField: 'idPrato',
        labelField: 'nome',
        isActive: (row) => row.ativo !== false,
      },
    },
    {
      name: 'ingrediente',
      label: 'Ingrediente',
      type: 'select',
      select: {
        api: ingredientesApi as CrudApi<Row>,
        valueField: 'idIngrediente',
        labelField: 'nome',
      },
    },
    { name: 'quantidade', label: 'Quantidade', type: 'number' },
  ],
}
