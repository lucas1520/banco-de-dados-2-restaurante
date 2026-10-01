import { useState, type SyntheticEvent } from 'react'
import { Button } from '../../../components/button/button'
import { SelectField } from '../../../components/selectField/selectField'
import { TextField } from '../../../components/textField/textField'
import type { Prato } from '../../../models/Prato'

export function AdicionarItemForm({
  pratos,
  busy,
  onAdicionar,
}: {
  pratos: Prato[]
  busy: boolean
  onAdicionar: (idPrato: number, quantidade: number) => void
}) {
  const [idPrato, setIdPrato] = useState('')
  const [quantidade, setQuantidade] = useState('1')

  function submit(e: SyntheticEvent) {
    e.preventDefault()
    if (!idPrato) return
    onAdicionar(Number(idPrato), Number(quantidade))
    setIdPrato('')
    setQuantidade('1')
  }

  return (
    <form className="cm-item-form" onSubmit={submit}>
      <SelectField
        label="Prato"
        value={idPrato}
        placeholder="Selecione um prato…"
        required
        options={pratos.map((p) => ({ value: p.idPrato, label: p.nome }))}
        onChange={setIdPrato}
      />
      <TextField label="Quantidade" type="number" step="1" required value={quantidade} onChange={setQuantidade} />
      <Button type="submit" variant="primary" disabled={busy}>
        Adicionar item
      </Button>
    </form>
  )
}
