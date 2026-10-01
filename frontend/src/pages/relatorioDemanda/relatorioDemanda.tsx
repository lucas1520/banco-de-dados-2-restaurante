import { useState } from 'react'
import { demandaDoPeriodo } from '../../api/relatorios'
import { DataTable, type Column } from '../../components/dataTable/dataTable'
import { PeriodoPanel } from '../../components/periodoPanel/periodoPanel'
import { StatusMessage } from '../../components/statusMessage/statusMessage'
import { errorMessage, formatDia, formatQuantidade } from '../../format'
import type { DiaDemanda, IngredienteUso } from '../../models/DiaDemanda'
import type { Periodo } from '../../models/Periodo'
import type { PratoQuantidade } from '../../models/PratoQuantidade'
import './relatorioDemandaStyle.css'

const colunasPratos: Column<PratoQuantidade>[] = [
  { key: 'nome', label: 'Prato' },
  { key: 'quantidade', label: 'Quantidade', mono: true },
]

const colunasIngredientes: Column<IngredienteUso>[] = [
  { key: 'nome', label: 'Ingrediente' },
  { key: 'quantidade', label: 'Quantidade usada', mono: true, render: (i) => formatQuantidade(i.quantidade) },
]

export default function RelatorioDemandaPage() {
  const [dias, setDias] = useState<DiaDemanda[] | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  async function gerar(periodo: Periodo) {
    setLoading(true)
    setError(null)
    setDias(null)
    try {
      setDias(await demandaDoPeriodo(periodo))
    } catch (e) {
      setError(errorMessage(e))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="page">
      <h1 className="page-title">Dias de maior demanda</h1>
      <PeriodoPanel title="Período" submitting={loading} onSubmit={gerar} />
      {error && <StatusMessage kind="error">{error}</StatusMessage>}
      {dias && dias.length === 0 && (
        <StatusMessage kind="empty">Nenhum cliente chegou neste período.</StatusMessage>
      )}
      {dias && dias.length > 0 && (
        <p className="cm-demanda-ajuda">
          Todos os clientes, pagos ou não. Dias com mais clientes primeiro; em empate, o mais antigo.
        </p>
      )}
      {dias?.map((d) => (
        <section key={d.dia} className="cm-demanda-dia">
          <header className="cm-demanda-head">
            <h2 className="cm-demanda-title">{formatDia(d.dia)}</h2>
            <span className="cm-demanda-clientes">
              {d.clientes} {d.clientes === 1 ? 'cliente' : 'clientes'}
            </span>
          </header>
          <div className="cm-demanda-body">
            <div>
              <h3 className="cm-demanda-sub">3 pratos que mais saem</h3>
              <DataTable columns={colunasPratos} rows={d.pratos} rowKey="idPrato" emptyText="Nenhum prato pedido." />
            </div>
            <div>
              <h3 className="cm-demanda-sub">3 ingredientes mais usados</h3>
              <DataTable
                columns={colunasIngredientes}
                rows={d.ingredientes}
                rowKey="idIngrediente"
                emptyText="Nenhum ingrediente usado."
              />
            </div>
          </div>
        </section>
      ))}
    </div>
  )
}
