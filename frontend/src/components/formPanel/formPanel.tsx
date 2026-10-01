import { useState, type ReactNode, type SyntheticEvent } from 'react'
import { Button } from '../button/button'
import './formPanel.css'

export function FormPanel({
  title,
  onSubmit,
  onCancel,
  submitLabel = 'Salvar',
  submitting,
  blockedReason,
  children,
}: {
  title: string
  onSubmit: () => void
  onCancel?: () => void
  submitLabel?: string
  submitting?: boolean
  blockedReason?: string
  children: ReactNode
}) {
  const [warn, setWarn] = useState(false)
  return (
    <form
      className="cm-panel"
      onSubmit={(e: SyntheticEvent) => {
        e.preventDefault()
        setWarn(false)
        onSubmit()
      }}
    >
      <h2 className="cm-panel-title">{title}</h2>
      <div className="cm-panel-fields">{children}</div>
      <div className="cm-panel-actions">
        <span
          className={blockedReason ? 'cm-submit-wrap cm-submit-wrap--blocked' : undefined}
          title={blockedReason}
          onClick={blockedReason ? () => setWarn(true) : undefined}
        >
          <Button type="submit" variant="primary" disabled={submitting || Boolean(blockedReason)}>
            {submitLabel}
          </Button>
        </span>
        {onCancel && <Button onClick={onCancel}>Cancelar</Button>}
      </div>
      {blockedReason && warn && (
        <div className="cm-help cm-help--error" role="alert">
          {blockedReason}
        </div>
      )}
    </form>
  )
}
