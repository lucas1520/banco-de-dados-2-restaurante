import type { CrudApi } from './CrudApi'
import type { Row } from './Row'

export type FieldConfig = {
  name: string
  label: string
  type: 'text' | 'number' | 'boolean' | 'select'
  nullable?: boolean
  readOnly?: boolean
  datetime?: boolean
  currency?: boolean
  defaultValue?: unknown
  hideOnCreate?: boolean
  select?: {
    api: CrudApi<Row>
    valueField: string
    labelField: string
    labelPrefix?: string
    isActive?: (row: Row) => boolean
  }
}

export type EntityConfig = {
  path: string
  title: string
  singular: string
  feminine?: boolean
  pk: string
  pkLabel?: string
  compositeKey?: string[]
  sortable?: boolean
  fields: FieldConfig[]
}
