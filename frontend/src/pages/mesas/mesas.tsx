import { mesasApi } from '../../api/mesas'
import { mesasConfig } from './mesas.config'
import { EntityCrud } from '../../components/entityCrud/entityCrud'
import type { CrudApi } from '../../models/CrudApi'
import type { Row } from '../../models/Row'

export default function MesasPage() {
  return <EntityCrud config={mesasConfig} api={mesasApi as CrudApi<Row>} />
}
