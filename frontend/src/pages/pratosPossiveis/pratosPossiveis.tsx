import { useCallback, useEffect, useState } from 'react'
import { pratosPossiveis } from '../../api/pratos'
import { Button } from '../../components/button/button'
import { DataTable, type Column } from '../../components/dataTable/dataTable'
import { StatusMessage } from '../../components/statusMessage/statusMessage'
import { errorMessage } from '../../format'
import type { PratoPossivel } from '../../models/PratoPossivel'

const columns: Column<PratoPossivel>[] = [
  { key: 'nome', label: 'Prato' },
  {
    key: 'quantidadePossivel',
    label: 'Dá para fazer',
    mono: true,
    render: (p) => (p.quantidadePossivel == null ? 'Sem composição' : p.quantidadePossivel),
  },
]

export default function PratosPossiveisPage() {
  const [pratos, setPratos] = useState<PratoPossivel[] | null>(null)
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    setError(null)
    try {
      setPratos(await pratosPossiveis())
    } catch (e) {
      setError(errorMessage(e))
    }
  }, [])

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    load()
  }, [load])

  return (
    <div className="page">
      <h1 className="page-title">Produção de pratos</h1>
      <p>Quantas unidades de cada prato ativo é possível fazer com o estoque atual de ingredientes.</p>
      <div>
        <Button onClick={load}>Atualizar</Button>
      </div>
      {error && <StatusMessage kind="error">{error}</StatusMessage>}
      {!pratos && !error && <StatusMessage kind="loading">Carregando…</StatusMessage>}
      {pratos && (
        <DataTable columns={columns} rows={pratos} rowKey="idPrato" emptyText="Nenhum prato ativo." />
      )}
    </div>
  )
}
