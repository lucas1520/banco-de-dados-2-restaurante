import { pratosApi } from '../../api/pratos'
import { pratosConfig } from './pratos.config'
import { EntityCrud } from '../../components/entityCrud/entityCrud'
import type { CrudApi } from '../../models/CrudApi'
import type { Row } from '../../models/Row'

export default function PratosPage() {
  return <EntityCrud config={pratosConfig} api={pratosApi as CrudApi<Row>} />
}
