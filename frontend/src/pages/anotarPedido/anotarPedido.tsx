import { AnotarPedidoPanel } from './components/anotarPedidoPanel'
import '../pedidos/pedidosStyle.css'
import './anotarPedidoStyle.css'

export default function AnotarPedidoPage() {
  return (
    <div className="page">
      <h1 className="page-title">Anotar pedido</h1>
      <AnotarPedidoPanel />
    </div>
  )
}
