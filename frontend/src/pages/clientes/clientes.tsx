import { useState } from 'react'
import { clientesApi, contaCliente, pagarCliente } from '../../api/clientes'
import { Button } from '../../components/button/button'
import { EntityCrud } from '../../components/entityCrud/entityCrud'
import { StatusMessage } from '../../components/statusMessage/statusMessage'
import { brl, errorMessage } from '../../format'
import type { Cliente } from '../../models/Cliente'
import type { CrudApi } from '../../models/CrudApi'
import type { PagarClienteResponse } from '../../models/PagarClienteResponse'
import type { Row } from '../../models/Row'
import { clientesConfig } from './clientes.config'

function pagamentoMessage(cliente: Cliente, res: PagarClienteResponse): string {
  const base = `Pagamento de ${cliente.nome} registrado: ${brl.format(res.total)}.`
  const mesa = cliente.mesa
  if (!mesa) return base
  if (res.mesaLiberada) return `${base} Mesa ${mesa.idMesa} liberada.`
  return `${base} Mesa ${mesa.idMesa} continua ocupada: ${res.clientesPendentesNaMesa} cliente(s) ainda não pagaram.`
}

export default function ClientesPage() {
  const [error, setError] = useState<string | null>(null)
  const [notice, setNotice] = useState<string | null>(null)

  async function pagar(cliente: Cliente, reload: () => Promise<void>) {
    setError(null)
    setNotice(null)
    try {
      const conta = await contaCliente(cliente.idCliente)
      const ok = confirm(
        `Total da conta de ${cliente.nome}: ${brl.format(conta.total)}.\n\nRegistrar pagamento?`,
      )
      if (!ok) return
      setNotice(pagamentoMessage(cliente, await pagarCliente(cliente.idCliente)))
      await reload()
    } catch (e) {
      setError(errorMessage(e))
    }
  }

  return (
    <EntityCrud
      config={clientesConfig}
      api={clientesApi as CrudApi<Row>}
      rowActions={(row, reload) => (
        <Button
          size="sm"
          variant="primary"
          disabled={Boolean(row.pago)}
          onClick={() => pagar(row as unknown as Cliente, reload)}
        >
          Registrar pagamento
        </Button>
      )}
    >
      {error && <StatusMessage kind="error">{error}</StatusMessage>}
      {notice && <StatusMessage kind="success">{notice}</StatusMessage>}
    </EntityCrud>
  )
}
