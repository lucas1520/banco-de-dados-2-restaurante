import { useId } from 'react'
import { Field } from '../field/field'

export function TextField({
  label,
  value,
  onChange,
  type = 'text',
  step,
  required,
  error,
  help,
  disabled,
}: {
  label: string
  value: string | number | null
  onChange: (value: string) => void
  type?: 'text' | 'number' | 'date'
  step?: string
  required?: boolean
  error?: string
  help?: string
  disabled?: boolean
}) {
  const id = useId()
  return (
    <Field id={id} label={label} error={error} help={help}>
      <input
        id={id}
        className="cm-input"
        type={type}
        value={value ?? ''}
        step={step}
        required={required}
        disabled={disabled}
        aria-invalid={error ? true : undefined}
        aria-describedby={error || help ? `${id}-msg` : undefined}
        onChange={(e) => onChange(e.target.value)}
      />
    </Field>
  )
}
