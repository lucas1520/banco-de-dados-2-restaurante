import { useEffect, useState } from 'react'
import { mesasApi } from '../../api/mesas'
import { errorMessage } from '../../format'
import type { Mesa } from '../../models/Mesa'
import { SelectField } from '../selectField/selectField'
import { StatusMessage } from '../statusMessage/statusMessage'

export function MesaSelect({
  value,
  onChange,
}: {
  value: string
  onChange: (idMesa: string) => void
}) {
  const [mesas, setMesas] = useState<Mesa[]>([])
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    mesasApi
      .list()
      .then(setMesas)
      .catch((e) => setError(errorMessage(e)))
  }, [])

  return (
    <>
      {error && <StatusMessage kind="error">{error}</StatusMessage>}
      <SelectField
        label="Mesa"
        value={value}
        placeholder="Selecione uma mesa…"
        options={mesas.map((m) => ({
          value: m.idMesa,
          label: `Mesa ${m.idMesa} (${m.cadeiras} cadeiras) — ${m.disponivel ? 'Livre' : 'Ocupada'}`,
        }))}
        onChange={onChange}
      />
    </>
  )
}
