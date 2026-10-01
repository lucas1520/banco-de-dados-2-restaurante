import type { ReactNode } from 'react'
import { Button } from '../button/button'
import './dataTable.css'

export type Column<T> = {
  key: string
  label: string
  mono?: boolean
  render?: (row: T) => ReactNode
}

export function DataTable<T extends Record<string, unknown>>({
  columns,
  rows,
  rowKey,
  onEdit,
  onDelete,
  extraActions,
  editingKey,
  emptyText = 'Nenhum registro.',
}: {
  columns: Column<T>[]
  rows: T[]
  rowKey: string | ((row: T) => unknown)
  onEdit?: (row: T) => void
  onDelete?: (row: T) => void
  extraActions?: (row: T) => ReactNode
  editingKey?: unknown
  emptyText?: string
}) {
  const hasActions = Boolean(onEdit || onDelete || extraActions)
  const keyOf = (r: T) => (typeof rowKey === 'function' ? rowKey(r) : r[rowKey])
  return (
    <div className="cm-table-wrap">
      <table className="cm-table">
        <thead>
          <tr>
            {columns.map((c) => (
              <th key={c.key} scope="col" className={c.mono ? 'cm-num' : undefined}>
                {c.label}
              </th>
            ))}
            {hasActions && (
              <th scope="col" className="cm-actions-col">
                Ações
              </th>
            )}
          </tr>
        </thead>
        <tbody>
          {rows.length === 0 ? (
            <tr>
              <td colSpan={columns.length + (hasActions ? 1 : 0)} className="cm-empty">
                {emptyText}
              </td>
            </tr>
          ) : (
            rows.map((r) => {
              const editing = editingKey != null && editingKey === keyOf(r)
              return (
                <tr key={String(keyOf(r))} className={editing ? 'cm-row--editing' : undefined}>
                  {columns.map((c) => {
                    const v = c.render ? c.render(r) : (r[c.key] as ReactNode)
                    return (
                      <td key={c.key} className={c.mono ? 'cm-num' : undefined}>
                        {v == null || v === '' ? '—' : v}
                      </td>
                    )
                  })}
                  {hasActions && (
                    <td className="cm-actions-col">
                      {extraActions?.(r)}
                      {onEdit && (
                        <Button size="sm" onClick={() => onEdit(r)}>
                          Editar
                        </Button>
                      )}
                      {onDelete && (
                        <Button size="sm" variant="danger" onClick={() => onDelete(r)}>
                          Excluir
                        </Button>
                      )}
                    </td>
                  )}
                </tr>
              )
            })
          )}
        </tbody>
      </table>
    </div>
  )
}
