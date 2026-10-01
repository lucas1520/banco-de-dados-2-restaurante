import { useId } from 'react'
import './checkboxField.css'

export function CheckboxField({
  label,
  checked,
  onChange,
  disabled,
}: {
  label: string
  checked: boolean
  onChange: (checked: boolean) => void
  disabled?: boolean
}) {
  const id = useId()
  return (
    <div className="cm-field cm-field--check">
      <input
        id={id}
        className="cm-check"
        type="checkbox"
        checked={checked}
        disabled={disabled}
        onChange={(e) => onChange(e.target.checked)}
      />
      <label className="cm-label cm-label--inline" htmlFor={id}>
        {label}
      </label>
    </div>
  )
}
