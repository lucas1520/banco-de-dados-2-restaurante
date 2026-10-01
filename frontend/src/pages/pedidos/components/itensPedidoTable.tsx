import { Button } from '../../../components/button/button'
import { DataTable, type Column } from '../../../components/dataTable/dataTable'
import { brl } from '../../../format'
import type { ItemPedido } from '../../../models/ItemPedido'
import { QuantidadeEditor } from './quantidadeEditor'

export function ItensPedidoTable({
  itens,
  editavel,
  busy,
  onAlterar,
  onRemover,
}: {
  itens: ItemPedido[]
  editavel: boolean
  busy: boolean
  onAlterar: (item: ItemPedido, quantidade: number) => void
  onRemover: (item: ItemPedido) => void
}) {
  const columns: Column<ItemPedido>[] = [
    { key: 'prato', label: 'Prato', render: (i) => i.prato.nome },
    {
      key: 'quantidade',
      label: 'Quantidade',
      mono: true,
      render: (i) =>
        editavel ? (
          <QuantidadeEditor
            key={`${i.prato.idPrato}-${i.quantidade}`}
            quantidade={i.quantidade}
            disabled={busy}
            onSave={(q) => onAlterar(i, q)}
          />
        ) : (
          i.quantidade
        ),
    },
    {
      key: 'tempoPrep',
      label: 'Tempo de preparo',
      mono: true,
      render: (i) => (i.prato.tempoPrep == null ? '' : `${i.prato.tempoPrep} min`),
    },
    { key: 'valor', label: 'Valor unit.', mono: true, render: (i) => brl.format(i.precoUnitario) },
    {
      key: 'subtotal',
      label: 'Subtotal',
      mono: true,
      render: (i) => brl.format(i.precoUnitario * i.quantidade),
    },
  ]

  return (
    <DataTable
      columns={columns}
      rows={itens}
      rowKey={(i) => i.prato.idPrato}
      emptyText="Nenhum item neste pedido."
      extraActions={
        editavel
          ? (i) => (
              <Button size="sm" variant="danger" disabled={busy} onClick={() => onRemover(i)}>
                Remover
              </Button>
            )
          : undefined
      }
    />
  )
}
