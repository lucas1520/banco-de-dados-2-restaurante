import { ingredientesApi } from '../../api/ingredientes'
import { ingredientesConfig } from './ingredientes.config'
import { EntityCrud } from '../../components/entityCrud/entityCrud'
import type { CrudApi } from '../../models/CrudApi'
import type { Row } from '../../models/Row'

export default function IngredientesPage() {
  return <EntityCrud config={ingredientesConfig} api={ingredientesApi as CrudApi<Row>} />
}
