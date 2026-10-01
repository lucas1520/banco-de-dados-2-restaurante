import { useState, type ReactNode } from 'react'
import { isoDate } from '../../format'
import type { Periodo } from '../../models/Periodo'
import { FormPanel } from '../formPanel/formPanel'
import { TextField } from '../textField/textField'

export function PeriodoPanel({
  title,
  submitting,
  onSubmit,
  children,
}: {
  title: string
  submitting?: boolean
  onSubmit: (periodo: Periodo) => void
  children?: ReactNode
}) {
  const [inicio, setInicio] = useState(() => isoDate(new Date(Date.now() - 30 * 24 * 60 * 60 * 1000)))
  const [fim, setFim] = useState(() => isoDate(new Date()))

  return (
    <FormPanel
      title={title}
      submitLabel="Gerar relatório"
      submitting={submitting}
      onSubmit={() => onSubmit({ inicio, fim })}
    >
      {children}
      <TextField label="De" type="date" required value={inicio} onChange={setInicio} />
      <TextField label="Até" type="date" required value={fim} onChange={setFim} />
    </FormPanel>
  )
}
