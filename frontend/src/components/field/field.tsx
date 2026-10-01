import type { ReactNode } from 'react'
import './field.css'

export function Field({
  id,
  label,
  error,
  help,
  children,
}: {
  id: string
  label: string
  error?: string
  help?: string
  children: ReactNode
}) {
  return (
    <div className="cm-field">
      <label className="cm-label" htmlFor={id}>
        {label}
      </label>
      {children}
      {error ? (
        <div className="cm-help cm-help--error" id={`${id}-msg`}>
          Erro: {error}
        </div>
      ) : help ? (
        <div className="cm-help" id={`${id}-msg`}>
          {help}
        </div>
      ) : null}
    </div>
  )
}
