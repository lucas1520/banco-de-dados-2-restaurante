import { useCallback, useEffect, useState } from 'react'
import type { CrudApi } from '../../models/CrudApi'
import type { EntityConfig } from '../../models/EntityConfig'
import type { Row } from '../../models/Row'
import { emptyForm, formFromRow, rowId, toPayload, type Options } from './entityCrudHelpers'

function message(e: unknown) {
  return e instanceof Error ? e.message : String(e)
}

export function useEntityCrud(config: EntityConfig, api: CrudApi<Row>) {
  const [rows, setRows] = useState<Row[]>([])
  const [options, setOptions] = useState<Options>({})
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [form, setForm] = useState<Row>(() => emptyForm(config))
  const [editingId, setEditingId] = useState<unknown>(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const selects = config.fields.filter((f) => f.type === 'select')
      const [data, ...lists] = await Promise.all([
        api.list(),
        ...selects.map((f) => f.select!.api.list()),
      ])
      const opts: Options = {}
      selects.forEach((f, i) => {
        const s = f.select!
        opts[f.name] = lists[i].map((r) => ({
          value: r[s.valueField],
          label: `${s.labelPrefix ?? ''}${String(r[s.labelField] ?? '')}`,
          active: s.isActive ? s.isActive(r) : true,
        }))
      })
      setRows(data)
      setOptions(opts)
    } catch (e) {
      setError(message(e))
    } finally {
      setLoading(false)
    }
  }, [config, api])

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    load()
  }, [load])

  function reset() {
    setEditingId(null)
    setForm(emptyForm(config))
  }

  async function submit() {
    setError(null)
    try {
      const body = toPayload(config, form)
      if (editingId == null) await api.create(body)
      else await api.update(editingId, body)
      reset()
      await load()
    } catch (err) {
      setError(message(err))
    }
  }

  async function remove(row: Row) {
    if (!confirm('Excluir este registro?')) return
    setError(null)
    try {
      const id = rowId(config, row)
      await api.remove(id)
      if (editingId === id) reset()
      await load()
    } catch (err) {
      setError(message(err))
    }
  }

  function edit(row: Row) {
    setEditingId(rowId(config, row))
    setForm(formFromRow(config, row))
  }

  function setField(name: string, value: unknown) {
    setForm({ ...form, [name]: value })
  }

  return { rows, options, loading, error, form, editingId, setField, reset, submit, remove, edit, reload: load }
}
