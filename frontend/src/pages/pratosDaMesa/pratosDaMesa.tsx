import { PratosDaMesaPanel } from './components/pratosDaMesaPanel'
import './pratosDaMesaStyle.css'

export default function PratosDaMesaPage() {
  return (
    <div className="page">
      <h1 className="page-title">Pratos da mesa</h1>
      <PratosDaMesaPanel />
    </div>
  )
}
