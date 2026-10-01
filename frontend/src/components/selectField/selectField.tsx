import { useId } from 'react'
import { Field } from '../field/field'

export type Option = { value: string | number; label: string }

export function SelectField({
  label,
  value,
  options,
  onChange,
  placeholder = 'Selecione…',
  required,
  error,
  help,
  disabled,
}: {
  label: string
  value: string | number | null
  options: Option[]
  onChange: (value: string) => void
  placeholder?: string
  required?: boolean
  error?: string
  help?: string
  disabled?: boolean
}) {
  const id = useId()
  return (
    <Field id={id} label={label} error={error} help={help}>
      <select
        id={id}
        className="cm-input cm-select"
        value={value ?? ''}
        required={required}
        disabled={disabled}
        onChange={(e) => onChange(e.target.value)}
      >
        <option value="">{placeholder}</option>
        {options.map((o) => (
          <option key={o.value} value={o.value}>
            {o.label}
          </option>
        ))}
      </select>
    </Field>
  )
}
