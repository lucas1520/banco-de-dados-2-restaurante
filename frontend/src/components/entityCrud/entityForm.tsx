import type { EntityConfig, FieldConfig } from '../../models/EntityConfig'
import type { Row } from '../../models/Row'
import { CheckboxField } from '../checkboxField/checkboxField'
import { FormPanel } from '../formPanel/formPanel'
import { SelectField } from '../selectField/selectField'
import { TextField } from '../textField/textField'
import type { Options } from './entityCrudHelpers'

function FieldInput({
  field: f,
  value,
  options,
  disabled,
  onChange,
}: {
  field: FieldConfig
  value: unknown
  options: Options
  disabled?: boolean
  onChange: (v: unknown) => void
}) {
  switch (f.type) {
    case 'boolean':
      return (
        <CheckboxField label={f.label} checked={Boolean(value)} disabled={disabled} onChange={onChange} />
      )
    case 'select':
      return (
        <SelectField
          label={f.label}
          value={String(value ?? '')}
          required={!f.nullable}
          disabled={disabled}
          placeholder={f.nullable ? '(nenhum)' : 'Selecione…'}
          options={(options[f.name] ?? [])
            .filter((o) => o.active !== false || String(o.value) === String(value ?? ''))
            .map((o) => ({ value: String(o.value), label: o.label }))}
          onChange={onChange}
        />
      )
    case 'number':
      return (
        <TextField
          label={f.label}
          type="number"
          step={f.currency ? '0.01' : 'any'}
          required
          disabled={disabled}
          value={String(value ?? '')}
          onChange={onChange}
        />
      )
    default:
      return (
        <TextField
          label={f.label}
          required
          disabled={disabled}
          value={String(value ?? '')}
          onChange={onChange}
        />
      )
  }
}

export function EntityForm({
  config,
  form,
  options,
  creating,
  onChange,
  onSubmit,
  onCancel,
}: {
  config: EntityConfig
  form: Row
  options: Options
  creating: boolean
  onChange: (name: string, value: unknown) => void
  onSubmit: () => void
  onCancel: () => void
}) {
  const missing = config.fields.find(
    (f) =>
      f.type !== 'boolean' &&
      !(f.type === 'select' && f.nullable) &&
      !f.readOnly &&
      !(creating && f.hideOnCreate) &&
      String(form[f.name] ?? '').trim() === '',
  )
  const blockedReason =
    missing && `${missing.type === 'select' ? 'Selecione' : 'Preencha'} ${missing.label.toLowerCase()} para continuar`

  return (
    <FormPanel
      blockedReason={blockedReason}
      title={
        creating
          ? `${config.feminine ? 'Nova' : 'Novo'} ${config.singular}`
          : `Editar ${config.singular}`
      }
      submitLabel={creating ? `Criar ${config.singular}` : 'Salvar'}
      onSubmit={onSubmit}
      onCancel={creating ? undefined : onCancel}
    >
      {config.fields.filter((f) => !f.readOnly && !(creating && f.hideOnCreate)).map((f) => (
        <FieldInput
          key={f.name}
          field={f}
          value={form[f.name]}
          options={options}
          disabled={!creating && config.compositeKey?.includes(f.name)}
          onChange={(v) => onChange(f.name, v)}
        />
      ))}
    </FormPanel>
  )
}
