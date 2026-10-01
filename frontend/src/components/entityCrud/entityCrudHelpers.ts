import type { EntityConfig, FieldConfig } from '../../models/EntityConfig'
import type { Row } from '../../models/Row'

const brl = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

const dateTime = new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: 'short' })

export type Options = Record<string, { value: unknown; label: string; active?: boolean }[]>

export function emptyValue(f: FieldConfig): unknown {
  if (f.defaultValue !== undefined) return f.defaultValue
  if (f.type === 'boolean') return false
  return ''
}

export function fieldValue(f: FieldConfig, row: Row): unknown {
  const v = row[f.name]
  if (f.type !== 'select') return v
  return v == null ? null : (v as Row)[f.select!.valueField]
}

export function rowId(config: EntityConfig, row: Row): unknown {
  if (!config.compositeKey) return row[config.pk]
  return config.compositeKey
    .map((name) => fieldValue(config.fields.find((f) => f.name === name)!, row))
    .join('/')
}

export function emptyForm(config: EntityConfig): Row {
  return Object.fromEntries(config.fields.map((f) => [f.name, emptyValue(f)]))
}

export function formFromRow(config: EntityConfig, row: Row): Row {
  return Object.fromEntries(
    config.fields.map((f) => [f.name, fieldValue(f, row) ?? emptyValue(f)]),
  )
}

function coerce(v: unknown): unknown {
  const n = Number(v)
  return Number.isNaN(n) ? v : n
}

export function toPayload(config: EntityConfig, form: Row): Row {
  const out: Row = {}
  for (const f of config.fields) {
    if (f.readOnly) continue
    const v = form[f.name]
    if (f.type === 'number') out[f.name] = v === '' ? null : Number(v)
    else if (f.type === 'select') {
      out[f.name] =
        v === '' || v == null ? null : { [f.select!.valueField]: coerce(v) }
    } else out[f.name] = v
  }
  return out
}

export function displayValue(f: FieldConfig, row: Row, options: Options): string {
  const value = fieldValue(f, row)
  if (value == null) return ''
  if (f.type === 'boolean') return value ? 'Sim' : 'Não'
  if (f.datetime) return dateTime.format(new Date(String(value)))
  if (f.currency && !Number.isNaN(Number(value))) return brl.format(Number(value))
  if (f.type === 'select') {
    const opt = options[f.name]?.find((o) => String(o.value) === String(value))
    return opt?.label ?? String(value)
  }
  return String(value)
}
