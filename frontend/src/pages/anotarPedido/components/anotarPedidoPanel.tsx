import { useCallback, useEffect, useState } from 'react'
import { clientesApi } from '../../../api/clientes'
import { funcionariosApi } from '../../../api/funcionarios'
import { adicionarItemPedido, criarPedido, excluirPedido } from '../../../api/pedidos'
import { pratosApi } from '../../../api/pratos'
import { Button } from '../../../components/button/button'
import { DataTable, type Column } from '../../../components/dataTable/dataTable'
import { SelectField } from '../../../components/selectField/selectField'
import { StatusMessage } from '../../../components/statusMessage/statusMessage'
import { brl, errorMessage } from '../../../format'
import type { Cliente } from '../../../models/Cliente'
import type { Funcionario } from '../../../models/Funcionario'
import type { Prato } from '../../../models/Prato'
import { AdicionarItemForm } from '../../pedidos/components/adicionarItemForm'

type Linha = { prato: Prato; quantidade: number }

export function AnotarPedidoPanel() {
  const [clientes, setClientes] = useState<Cliente[]>([])
  const [funcionarios, setFuncionarios] = useState<Funcionario[]>([])
  const [pratos, setPratos] = useState<Prato[]>([])
  const [idCliente, setIdCliente] = useState('')
  const [idFuncionario, setIdFuncionario] = useState('')
  const [linhas, setLinhas] = useState<Linha[]>([])
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [notice, setNotice] = useState<string | null>(null)

  const load = useCallback(async () => {
    try {
      const [cs, fs, ps] = await Promise.all([
        clientesApi.list(),
        funcionariosApi.list(),
        pratosApi.list(),
      ])
      setClientes(cs.filter((c) => !c.pago))
      setFuncionarios(fs.filter((f) => f.ativo))
      setPratos(ps.filter((p) => p.ativo))
    } catch (e) {
      setError(errorMessage(e))
    }
  }, [])

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    load()
  }, [load])

  const total = linhas.reduce((soma, l) => soma + l.prato.valor * l.quantidade, 0)

  function adicionar(idPrato: number, quantidade: number) {
    const prato = pratos.find((p) => p.idPrato === idPrato)
    if (!prato || !Number.isInteger(quantidade) || quantidade <= 0) return
    setLinhas((atual) =>
      atual.some((l) => l.prato.idPrato === idPrato)
        ? atual.map((l) => (l.prato.idPrato === idPrato ? { ...l, quantidade: l.quantidade + quantidade } : l))
        : [...atual, { prato, quantidade }],
    )
  }

  function remover(idPrato: number) {
    setLinhas((atual) => atual.filter((l) => l.prato.idPrato !== idPrato))
  }

  async function anotar() {
    if (!idCliente || !idFuncionario || linhas.length === 0) return
    setSubmitting(true)
    setError(null)
    setNotice(null)

    let idPedido: number
    try {
      idPedido = (await criarPedido(Number(idCliente), Number(idFuncionario))).idPedido
    } catch (e) {
      setError(errorMessage(e))
      setSubmitting(false)
      return
    }

    try {
      for (const l of linhas) {
        await adicionarItemPedido(idPedido, { idPrato: l.prato.idPrato, quantidade: l.quantidade })
      }
      const cliente = clientes.find((c) => c.idCliente === Number(idCliente))
      setNotice(`Pedido #${idPedido} anotado para ${cliente?.nome ?? 'o cliente'}: ${brl.format(total)}.`)
      setLinhas([])
    } catch (e) {
      try {
        await excluirPedido(idPedido)
        setError(`${errorMessage(e)} O pedido não foi criado; ajuste a lista e tente de novo.`)
      } catch (desfazer) {
        setError(
          `${errorMessage(e)} Não foi possível desfazer o pedido #${idPedido} (${errorMessage(desfazer)}); exclua-o na tela Pedidos.`,
        )
      }
    } finally {
      setSubmitting(false)
    }
  }

  const columns: Column<Linha>[] = [
    { key: 'prato', label: 'Prato', render: (l) => l.prato.nome },
    { key: 'quantidade', label: 'Quantidade', mono: true },
    { key: 'valor', label: 'Valor unit.', mono: true, render: (l) => brl.format(l.prato.valor) },
    {
      key: 'subtotal',
      label: 'Subtotal',
      mono: true,
      render: (l) => brl.format(l.prato.valor * l.quantidade),
    },
  ]

  return (
    <section className="cm-anotar">
      <h2 className="cm-anotar-title">Novo pedido</h2>

      <div className="cm-anotar-selects">
        <SelectField
          label="Cliente"
          value={idCliente}
          placeholder="Selecione um cliente…"
          options={clientes.map((c) => ({
            value: c.idCliente,
            label: c.mesa ? `${c.nome} — Mesa ${c.mesa.idMesa}` : c.nome,
          }))}
          onChange={setIdCliente}
        />
        <SelectField
          label="Funcionário responsável"
          value={idFuncionario}
          placeholder="Selecione um funcionário…"
          options={funcionarios.map((f) => ({ value: f.matricula, label: f.nome }))}
          onChange={setIdFuncionario}
        />
      </div>

      <AdicionarItemForm pratos={pratos} busy={submitting} onAdicionar={adicionar} />

      <DataTable
        columns={columns}
        rows={linhas}
        rowKey={(l) => l.prato.idPrato}
        emptyText="Nenhum prato na lista."
        extraActions={(l) => (
          <Button size="sm" variant="danger" disabled={submitting} onClick={() => remover(l.prato.idPrato)}>
            Remover
          </Button>
        )}
      />

      <p className="cm-anotar-total">
        Total: <strong>{brl.format(total)}</strong>
      </p>

      {error && <StatusMessage kind="error">{error}</StatusMessage>}
      {notice && <StatusMessage kind="success">{notice}</StatusMessage>}

      <div className="cm-anotar-actions">
        <Button
          variant="primary"
          disabled={submitting || !idCliente || !idFuncionario || linhas.length === 0}
          onClick={anotar}
        >
          Anotar pedido
        </Button>
      </div>
    </section>
  )
}
