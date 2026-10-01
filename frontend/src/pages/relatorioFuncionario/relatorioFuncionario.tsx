import { useEffect, useState } from 'react'
import { funcionariosApi } from '../../api/funcionarios'
import { pedidosDoFuncionario } from '../../api/relatorios'
import { DataTable, type Column } from '../../components/dataTable/dataTable'
import { PeriodoPanel } from '../../components/periodoPanel/periodoPanel'
import { SelectField } from '../../components/selectField/selectField'
import { StatusMessage } from '../../components/statusMessage/statusMessage'
import { brl, errorMessage, formatDataHora } from '../../format'
import type { Funcionario } from '../../models/Funcionario'
import type { ItemPedido } from '../../models/ItemPedido'
import type { PedidosFuncionario } from '../../models/PedidosFuncionario'
import type { Periodo } from '../../models/Periodo'
import './relatorioFuncionarioStyle.css'

const colunas: Column<ItemPedido>[] = [
  { key: 'prato', label: 'Prato', render: (i) => i.prato.nome },
  { key: 'quantidade', label: 'Quantidade', mono: true },
  { key: 'precoUnitario', label: 'Valor unit.', mono: true, render: (i) => brl.format(i.precoUnitario) },
  {
    key: 'subtotal',
    label: 'Subtotal',
    mono: true,
    render: (i) => brl.format(i.precoUnitario * i.quantidade),
  },
]

export default function RelatorioFuncionarioPage() {
  const [funcionarios, setFuncionarios] = useState<Funcionario[]>([])
  const [matricula, setMatricula] = useState('')
  const [relatorio, setRelatorio] = useState<PedidosFuncionario | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    funcionariosApi
      .list()
      .then(setFuncionarios)
      .catch((e) => setError(errorMessage(e)))
  }, [])

  async function gerar(periodo: Periodo) {
    if (!matricula) {
      setError('Selecione um funcionário.')
      return
    }
    setLoading(true)
    setError(null)
    setRelatorio(null)
    try {
      setRelatorio(await pedidosDoFuncionario(Number(matricula), periodo))
    } catch (e) {
      setError(errorMessage(e))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="page">
      <h1 className="page-title">Pedidos do funcionário</h1>
      <PeriodoPanel title="Funcionário e período" submitting={loading} onSubmit={gerar}>
        <SelectField
          label="Funcionário"
          value={matricula}
          placeholder="Selecione um funcionário…"
          options={funcionarios.map((f) => ({
            value: f.matricula,
            label: f.ativo ? f.nome : `${f.nome} (inativo)`,
          }))}
          onChange={setMatricula}
        />
      </PeriodoPanel>
      {error && <StatusMessage kind="error">{error}</StatusMessage>}
      {relatorio && relatorio.clientes.length === 0 && (
        <StatusMessage kind="empty">Nenhum pedido deste funcionário neste período.</StatusMessage>
      )}
      {relatorio && relatorio.clientes.length > 0 && (
        <p className="cm-func-ajuda">
          Pedidos de {relatorio.funcionario.nome}, agrupados por cliente. O período filtra pela data de chegada do
          cliente.
        </p>
      )}
      {relatorio?.clientes.map(({ cliente, pedidos }) => (
        <section key={cliente.idCliente} className="cm-func-cliente">
          <header className="cm-func-head">
            <h2 className="cm-func-title">{cliente.nome}</h2>
            <span className="cm-func-info">
              Chegada: {formatDataHora(cliente.dataChegada)} · {cliente.mesa ? `Mesa ${cliente.mesa.idMesa}` : 'Sem mesa'}
            </span>
          </header>
          <div className="cm-func-body">
            {pedidos.map((pc) => (
              <div key={pc.pedido.idPedido}>
                <h3 className="cm-func-pedido">
                  Pedido #{pc.pedido.idPedido} · {pc.pedido.entregue ? 'Entregue' : 'Aberto'} · Total{' '}
                  {brl.format(pc.valorTotal)}
                </h3>
                <DataTable columns={colunas} rows={pc.itens} rowKey={(i) => i.prato.idPrato} emptyText="Sem itens." />
              </div>
            ))}
          </div>
        </section>
      ))}
    </div>
  )
}
