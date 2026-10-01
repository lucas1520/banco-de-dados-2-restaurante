import { useState } from 'react'
import { Button } from '../../../components/button/button'

export function QuantidadeEditor({
  quantidade,
  disabled,
  onSave,
}: {
  quantidade: number
  disabled?: boolean
  onSave: (quantidade: number) => void
}) {
  const [value, setValue] = useState(String(quantidade))
  const parsed = Number(value)
  const valid = Number.isInteger(parsed) && parsed > 0

  return (
    <span className="cm-qty">
      <input
        className="cm-input cm-qty-input"
        type="number"
        min={1}
        step={1}
        aria-label="Quantidade"
        value={value}
        disabled={disabled}
        onChange={(e) => setValue(e.target.value)}
      />
      <Button size="sm" disabled={disabled || !valid || parsed === quantidade} onClick={() => onSave(parsed)}>
        Salvar
      </Button>
    </span>
  )
}
