import { useMemo, useState, type ReactNode } from 'react'
import type { CrudApi } from '../../models/CrudApi'
import type { EntityConfig } from '../../models/EntityConfig'
import type { Row } from '../../models/Row'
import { StatusMessage } from '../statusMessage/statusMessage'
import { EntityForm } from './entityForm'
import { EntityTable } from './entityTable'
import { SortSelect, type SortBy } from './sortSelect'
import { useEntityCrud } from './useEntityCrud'
import './entityCrud.css'

export function EntityCrud({
  config,
  api,
  rowActions,
  children,
}: {
  config: EntityConfig
  api: CrudApi<Row>
  rowActions?: (row: Row, reload: () => Promise<void>) => ReactNode
  children?: ReactNode
}) {
  const crud = useEntityCrud(config, api)
  const [sortBy, setSortBy] = useState<SortBy>('id')

  const rows = useMemo(() => {
    if (!config.sortable) return crud.rows
    return [...crud.rows].sort((a, b) =>
      sortBy === 'nome'
        ? String(a.nome ?? '').localeCompare(String(b.nome ?? ''), 'pt-BR', { sensitivity: 'base' })
        : Number(a[config.pk]) - Number(b[config.pk]),
    )
  }, [crud.rows, config.sortable, config.pk, sortBy])

  return (
    <div className="page">
      <h1 className="page-title">{config.title}</h1>
      {children}
      {crud.error && <StatusMessage kind="error">{crud.error}</StatusMessage>}

      <EntityForm
        config={config}
        form={crud.form}
        options={crud.options}
        creating={crud.editingId == null}
        onChange={crud.setField}
        onSubmit={crud.submit}
        onCancel={crud.reset}
      />

      {config.sortable && <SortSelect value={sortBy} onChange={setSortBy} />}

      {crud.loading ? (
        <StatusMessage kind="loading">Carregando…</StatusMessage>
      ) : (
        <EntityTable
          config={config}
          rows={rows}
          options={crud.options}
          editingId={crud.editingId}
          onEdit={crud.edit}
          onDelete={crud.remove}
          extraActions={rowActions && ((row) => rowActions(row, crud.reload))}
        />
      )}
    </div>
  )
}
