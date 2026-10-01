import { Button } from '../../../components/button/button'
import { cx } from '../../../components/cx'
import { brl } from '../../../format'
import type { ItemPedido } from '../../../models/ItemPedido'
import type { PedidoConta } from '../../../models/PedidoConta'
import type { Prato } from '../../../models/Prato'
import { AdicionarItemForm } from './adicionarItemForm'
import { ItensPedidoTable } from './itensPedidoTable'

export function PedidoCard({
  conta,
  pratos,
  busy,
  onAdicionarItem,
  onAlterarItem,
  onRemoverItem,
  onEntregar,
  onExcluir,
}: {
  conta: PedidoConta
  pratos: Prato[]
  busy: boolean
  onAdicionarItem: (idPrato: number, quantidade: number) => void
  onAlterarItem: (item: ItemPedido, quantidade: number) => void
  onRemoverItem: (item: ItemPedido) => void
  onEntregar: () => void
  onExcluir: () => void
}) {
  const { pedido, itens, valorTotal, tempoPrepMinutos } = conta
  const aberto = !pedido.entregue

  return (
    <details className="cm-pedido">
      <summary className="cm-pedido-head">
        <span className="cm-pedido-title">Pedido #{pedido.idPedido}</span>
        <span className="cm-pedido-cliente">{pedido.cliente.nome}</span>
        <span className={cx('cm-badge', aberto ? 'cm-badge--open' : 'cm-badge--done')}>
          {aberto ? `Tempo estimado: ${tempoPrepMinutos} min` : 'Entregue'}
        </span>
      </summary>

      <div className="cm-pedido-body">
        <p className="cm-pedido-responsavel">
          Funcionário responsável: {pedido.funcionario.nome} · Total do pedido: {brl.format(valorTotal)}
        </p>

        <ItensPedidoTable
          itens={itens}
          editavel={aberto}
          busy={busy}
          onAlterar={onAlterarItem}
          onRemover={onRemoverItem}
        />

        {aberto && (
          <>
            <AdicionarItemForm pratos={pratos} busy={busy} onAdicionar={onAdicionarItem} />
            <div className="cm-pedido-actions">
              <Button variant="primary" disabled={busy || itens.length === 0} onClick={onEntregar}>
                Marcar como entregue
              </Button>
              <Button variant="danger" disabled={busy} onClick={onExcluir}>
                Excluir pedido
              </Button>
            </div>
          </>
        )}
      </div>
    </details>
  )
}
