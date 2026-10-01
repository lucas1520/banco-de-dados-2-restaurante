import { composicoesApi } from '../../api/composicoes'
import { composicaoPratoConfig } from './composicaoPrato.config'
import { EntityCrud } from '../../components/entityCrud/entityCrud'
import type { CrudApi } from '../../models/CrudApi'
import type { Row } from '../../models/Row'

export default function ComposicaoPratoPage() {
  return <EntityCrud config={composicaoPratoConfig} api={composicoesApi as CrudApi<Row>} />
}
