import { mesasApi } from '../../api/mesas'
import type { CrudApi } from '../../models/CrudApi'
import type { EntityConfig } from '../../models/EntityConfig'
import type { Row } from '../../models/Row'

export const clientesConfig: EntityConfig = {
  path: 'clientes',
  title: 'Clientes',
  singular: 'cliente',
  pk: 'idCliente',
  sortable: true,
  fields: [
    { name: 'nome', label: 'Nome', type: 'text' },
    {
      name: 'mesa',
      label: 'Mesa',
      type: 'select',
      select: {
        api: mesasApi as CrudApi<Row>,
        valueField: 'idMesa',
        labelField: 'idMesa',
        labelPrefix: 'Mesa ',
      },
    },
    { name: 'pago', label: 'Pago', type: 'boolean', readOnly: true },
    { name: 'dataChegada', label: 'Chegada', type: 'text', datetime: true, readOnly: true },
  ],
}
