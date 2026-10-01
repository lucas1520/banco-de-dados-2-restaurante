import { useState } from 'react'
import { pratosDaMesa } from '../../../api/mesas'
import { DataTable, type Column } from '../../../components/dataTable/dataTable'
import { MesaSelect } from '../../../components/mesaSelect/mesaSelect'
import { StatusMessage } from '../../../components/statusMessage/statusMessage'
import { errorMessage } from '../../../format'
import type { PratoQuantidade } from '../../../models/PratoQuantidade'

const columns: Column<PratoQuantidade>[] = [
  { key: 'nome', label: 'Prato' },
  { key: 'quantidade', label: 'Quantidade', mono: true },
]

export function PratosDaMesaPanel() {
  const [idMesa, setIdMesa] = useState('')
  const [pratos, setPratos] = useState<PratoQuantidade[] | null>(null)
  const [error, setError] = useState<string | null>(null)

  async function selecionar(id: string) {
    setIdMesa(id)
    setPratos(null)
    setError(null)
    if (!id) return
    try {
      setPratos(await pratosDaMesa(Number(id)))
    } catch (e) {
      setError(errorMessage(e))
    }
  }

  return (
    <>
      <div className="cm-mesa-filtro">
        <MesaSelect value={idMesa} onChange={selecionar} />
      </div>
      {error && <StatusMessage kind="error">{error}</StatusMessage>}
      {pratos && (
        <>
          <p className="cm-mesa-ajuda">Pratos pedidos pelos clientes da mesa que ainda não pagaram.</p>
          <DataTable
            columns={columns}
            rows={pratos}
            rowKey="idPrato"
            emptyText="Nenhum prato pedido por clientes não pagos nesta mesa."
          />
        </>
      )}
    </>
  )
}
