import { useState } from 'react'
import { BrowserRouter, Navigate, Route, Routes, useLocation, useNavigate } from 'react-router-dom'
import { SettingsMenu } from './components/settingsMenu/settingsMenu'
import { SideNav } from './components/sideNav/sideNav'
import AnotarPedido from './pages/anotarPedido/anotarPedido'
import Clientes from './pages/clientes/clientes'
import { clientesConfig } from './pages/clientes/clientes.config'
import ComposicaoPrato from './pages/composicaoPrato/composicaoPrato'
import { composicaoPratoConfig } from './pages/composicaoPrato/composicaoPrato.config'
import Funcionarios from './pages/funcionarios/funcionarios'
import { funcionariosConfig } from './pages/funcionarios/funcionarios.config'
import Ingredientes from './pages/ingredientes/ingredientes'
import { ingredientesConfig } from './pages/ingredientes/ingredientes.config'
import Mesas from './pages/mesas/mesas'
import { mesasConfig } from './pages/mesas/mesas.config'
import Pedidos from './pages/pedidos/pedidos'
import PratosDaMesa from './pages/pratosDaMesa/pratosDaMesa'
import PratosPossiveis from './pages/pratosPossiveis/pratosPossiveis'
import RelatorioDemanda from './pages/relatorioDemanda/relatorioDemanda'
import RelatorioFuncionario from './pages/relatorioFuncionario/relatorioFuncionario'
import RelatorioLucro from './pages/relatorioLucro/relatorioLucro'
import Pratos from './pages/pratos/pratos'
import { pratosConfig } from './pages/pratos/pratos.config'
import './appStyle.css'

const routes = [
  { config: clientesConfig, element: <Clientes /> },
  { config: mesasConfig, element: <Mesas /> },
  { config: funcionariosConfig, element: <Funcionarios /> },
  { config: pratosConfig, element: <Pratos /> },
  { config: ingredientesConfig, element: <Ingredientes /> },
  { config: composicaoPratoConfig, element: <ComposicaoPrato /> },
]

const operationRoutes = [
  { path: 'anotar-pedido', title: 'Anotar pedido', element: <AnotarPedido /> },
  { path: 'pedidos', title: 'Pedidos', element: <Pedidos /> },
  { path: 'pratos-da-mesa', title: 'Pratos da mesa', element: <PratosDaMesa /> },
  { path: 'pratos-possiveis', title: 'Produção de pratos', element: <PratosPossiveis /> },
]

const reportRoutes = [
  { path: 'relatorio-funcionario', title: 'Pedidos do funcionário', element: <RelatorioFuncionario /> },
  { path: 'relatorio-lucro', title: 'Lucro do restaurante', element: <RelatorioLucro /> },
  { path: 'relatorio-demanda', title: 'Dias de maior demanda', element: <RelatorioDemanda /> },
]

function Layout() {
  const { pathname } = useLocation()
  const navigate = useNavigate()
  const [dataVersion, setDataVersion] = useState(0)

  return (
    <div className="app">
      <SideNav
        title="Comanda"
        groups={[
          {
            title: 'CRUD',
            items: routes.map(({ config }) => ({
              href: `/${config.path}`,
              label: config.title,
              active: pathname === `/${config.path}`,
            })),
          },
          {
            title: 'Processos',
            items: operationRoutes.map((r) => ({
              href: `/${r.path}`,
              label: r.title,
              active: pathname === `/${r.path}`,
            })),
          },
          {
            title: 'Relatórios',
            items: reportRoutes.map((r) => ({
              href: `/${r.path}`,
              label: r.title,
              active: pathname === `/${r.path}`,
            })),
          },
        ]}
        onNavigate={(item) => navigate(item.href)}
        footer={<SettingsMenu onChanged={() => setDataVersion((v) => v + 1)} />}
      />
      <main key={dataVersion} className="app-content">
        <Routes>
          <Route path="/" element={<Navigate to={`/${routes[0].config.path}`} replace />} />
          {routes.map(({ config, element }) => (
            <Route key={config.path} path={`/${config.path}`} element={element} />
          ))}
          {[...operationRoutes, ...reportRoutes].map((r) => (
            <Route key={r.path} path={`/${r.path}`} element={r.element} />
          ))}
        </Routes>
      </main>
    </div>
  )
}

export default function App() {
  return (
    <BrowserRouter>
      <Layout />
    </BrowserRouter>
  )
}
