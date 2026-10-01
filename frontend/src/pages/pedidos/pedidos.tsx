import { useCallback, useEffect, useState } from 'react'
import { pedidosDaMesa } from '../../api/mesas'
import {
  adicionarItemPedido,
  alterarItemPedido,
  entregarPedido,
  excluirPedido,
  removerItemPedido,
} from '../../api/pedidos'
import { pratosApi } from '../../api/pratos'
import { MesaSelect } from '../../components/mesaSelect/mesaSelect'
import { StatusMessage } from '../../components/statusMessage/statusMessage'
import { errorMessage } from '../../format'
import type { PedidoConta } from '../../models/PedidoConta'
import type { Prato } from '../../models/Prato'
import { PedidoCard } from './components/pedidoCard'
import './pedidosStyle.css'

export default function PedidosPage() {
  const [pratos, setPratos] = useState<Prato[]>([])
  const [idMesa, setIdMesa] = useState('')
  const [pedidos, setPedidos] = useState<PedidoConta[] | null>(null)
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [notice, setNotice] = useState<string | null>(null)

  const loadPratos = useCallback(async () => {
    try {
      setPratos((await pratosApi.list()).filter((p) => p.ativo))
    } catch (e) {
      setError(errorMessage(e))
    }
  }, [])

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    loadPratos()
  }, [loadPratos])

  async function selecionarMesa(id: string) {
    setIdMesa(id)
    setNotice(null)
    setError(null)
    setPedidos(null)
    if (!id) return
    try {
      setPedidos(await pedidosDaMesa(Number(id)))
    } catch (e) {
      setError(errorMessage(e))
    }
  }

  async function run(action: () => Promise<unknown>, success?: string) {
    setBusy(true)
    setError(null)
    setNotice(null)
    try {
      await action()
      if (success) setNotice(success)
    } catch (e) {
      setError(errorMessage(e))
    }
    try {
      setPedidos(await pedidosDaMesa(Number(idMesa)))
    } catch (e) {
      setError(errorMessage(e))
    } finally {
      setBusy(false)
    }
  }

  function excluir(id: number) {
    if (!confirm(`Excluir o pedido #${id}? Os itens serão removidos e o estoque devolvido.`)) return
    return run(() => excluirPedido(id), `Pedido #${id} excluído.`)
  }

  return (
    <div className="page">
      <h1 className="page-title">Pedidos</h1>
      {error && <StatusMessage kind="error">{error}</StatusMessage>}
      {notice && <StatusMessage kind="success">{notice}</StatusMessage>}

      <div className="cm-pedidos-filtro">
        <MesaSelect value={idMesa} onChange={selecionarMesa} />
      </div>

      {pedidos && pedidos.length === 0 && (
        <StatusMessage kind="empty">Nenhum pedido de clientes que ainda não pagaram nesta mesa.</StatusMessage>
      )}
      {pedidos?.map((pc) => {
        const id = pc.pedido.idPedido
        return (
          <PedidoCard
            key={id}
            conta={pc}
            pratos={pratos}
            busy={busy}
            onAdicionarItem={(p, q) => run(() => adicionarItemPedido(id, { idPrato: p, quantidade: q }))}
            onAlterarItem={(item, q) => run(() => alterarItemPedido(id, item.prato.idPrato, { quantidade: q }))}
            onRemoverItem={(item) => run(() => removerItemPedido(id, item.prato.idPrato))}
            onEntregar={() => run(() => entregarPedido(id), `Pedido #${id} entregue.`)}
            onExcluir={() => excluir(id)}
          />
        )
      })}
    </div>
  )
}
