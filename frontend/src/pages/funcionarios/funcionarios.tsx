import { funcionariosApi } from '../../api/funcionarios'
import { funcionariosConfig } from './funcionarios.config'
import { EntityCrud } from '../../components/entityCrud/entityCrud'
import type { CrudApi } from '../../models/CrudApi'
import type { Row } from '../../models/Row'

export default function FuncionariosPage() {
  return <EntityCrud config={funcionariosConfig} api={funcionariosApi as CrudApi<Row>} />
}
