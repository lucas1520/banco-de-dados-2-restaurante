import type { ReactNode } from 'react'
import type { EntityConfig } from '../../models/EntityConfig'
import type { Row } from '../../models/Row'
import { DataTable, type Column } from '../dataTable/dataTable'
import { displayValue, rowId, type Options } from './entityCrudHelpers'

export function EntityTable({
  config,
  rows,
  options,
  editingId,
  onEdit,
  onDelete,
  extraActions,
}: {
  config: EntityConfig
  rows: Row[]
  options: Options
  editingId: unknown
  onEdit: (row: Row) => void
  onDelete: (row: Row) => void
  extraActions?: (row: Row) => ReactNode
}) {
  const columns: Column<Row>[] = [
    ...(config.compositeKey ? [] : [{ key: config.pk, label: config.pkLabel ?? 'ID', mono: true }]),
    ...config.fields.map((f) => ({
      key: f.name,
      label: f.label,
      mono: f.type === 'number',
      render: (row: Row) => displayValue(f, row, options),
    })),
  ]

  return (
    <DataTable
      columns={columns}
      rows={rows}
      rowKey={config.compositeKey ? (row) => rowId(config, row) : config.pk}
      editingKey={editingId}
      onEdit={onEdit}
      onDelete={onDelete}
      extraActions={extraActions}
    />
  )
}
