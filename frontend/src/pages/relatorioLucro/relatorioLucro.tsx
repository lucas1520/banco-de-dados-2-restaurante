import { useState } from 'react'
import { lucroDoPeriodo } from '../../api/relatorios'
import { PeriodoPanel } from '../../components/periodoPanel/periodoPanel'
import { StatusMessage } from '../../components/statusMessage/statusMessage'
import { brl, errorMessage, formatDia } from '../../format'
import type { Lucro } from '../../models/Lucro'
import type { Periodo } from '../../models/Periodo'
import './relatorioLucroStyle.css'

export default function RelatorioLucroPage() {
  const [lucro, setLucro] = useState<Lucro | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  async function gerar(periodo: Periodo) {
    setLoading(true)
    setError(null)
    setLucro(null)
    try {
      setLucro(await lucroDoPeriodo(periodo))
    } catch (e) {
      setError(errorMessage(e))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="page">
      <h1 className="page-title">Lucro do restaurante</h1>
      <PeriodoPanel title="Período" submitting={loading} onSubmit={gerar} />
      {error && <StatusMessage kind="error">{error}</StatusMessage>}
      {lucro && (
        <>
          <p className="cm-lucro-ajuda">
            Clientes pagos que chegaram entre {formatDia(lucro.inicio)} e {formatDia(lucro.fim)}:{' '}
            <strong>{lucro.clientes}</strong>. O valor bruto usa o preço do prato na época do pedido; o custo
            usa o preço atual dos ingredientes.
          </p>
          <dl className="cm-lucro">
            <div className="cm-lucro-item">
              <dt>Valor bruto</dt>
              <dd>{brl.format(lucro.valorBruto)}</dd>
            </div>
            <div className="cm-lucro-item">
              <dt>Custo dos ingredientes</dt>
              <dd>{brl.format(lucro.custoIngredientes)}</dd>
            </div>
            <div className="cm-lucro-item cm-lucro-item--destaque">
              <dt>Lucro</dt>
              <dd>{brl.format(lucro.lucro)}</dd>
            </div>
          </dl>
        </>
      )}
    </div>
  )
}
