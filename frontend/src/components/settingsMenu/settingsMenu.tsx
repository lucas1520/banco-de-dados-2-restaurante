import { useEffect, useId, useRef, useState } from 'react'
import { adicionarDadosTeste, limparDados } from '../../api/admin'
import { ApiError } from '../../api/client'
import { errorMessage, formatQuantidade } from '../../format'
import type { ContagemTabelas } from '../../models/ContagemTabelas'
import { Button } from '../button/button'
import { StatusMessage } from '../statusMessage/statusMessage'
import './settingsMenu.css'

type Operacao = 'limpar' | 'dados'
type Resultado = { kind: 'success' | 'error'; text: string }

const CONFIRMA_LIMPAR =
  'Apagar TODOS os dados do banco?\n\n' +
  'Mesas, clientes, funcionários, pratos, ingredientes, composições, pedidos e itens serão removidos ' +
  'e os IDs voltam a começar em 1. Esta ação é irreversível.'

const FERRAMENTAS_DESABILITADAS =
  'As ferramentas de desenvolvimento estão desabilitadas no servidor (app.dev-tools.enabled=false).'

function resumo(c: ContagemTabelas): string {
  const n = formatQuantidade
  return (
    `${n(c.mesas)} mesas, ${n(c.clientes)} clientes, ${n(c.funcionarios)} funcionários, ` +
    `${n(c.pratos)} pratos, ${n(c.ingredientes)} ingredientes, ${n(c.composicoes)} composições, ` +
    `${n(c.pedidos)} pedidos e ${n(c.itens)} itens`
  )
}

export function SettingsMenu({ onChanged }: { onChanged: () => void }) {
  const [open, setOpen] = useState(false)
  const [busy, setBusy] = useState<Operacao | null>(null)
  const [resultado, setResultado] = useState<Resultado | null>(null)
  const rootRef = useRef<HTMLDivElement>(null)
  const toggleRef = useRef<HTMLButtonElement>(null)
  const menuRef = useRef<HTMLDivElement>(null)
  const focusFirst = useRef(false)
  const menuId = useId()

  useEffect(() => {
    if (!open) return
    if (focusFirst.current) {
      focusFirst.current = false
      menuRef.current?.querySelector('button')?.focus()
    }

    function onPointerDown(e: PointerEvent) {
      if (!rootRef.current?.contains(e.target as Node)) setOpen(false)
    }
    function onKeyDown(e: KeyboardEvent) {
      if (e.key !== 'Escape') return
      setOpen(false)
      toggleRef.current?.focus()
    }
    document.addEventListener('pointerdown', onPointerDown)
    document.addEventListener('keydown', onKeyDown)
    return () => {
      document.removeEventListener('pointerdown', onPointerDown)
      document.removeEventListener('keydown', onKeyDown)
    }
  }, [open])

  function toggle() {
    focusFirst.current = !open
    setOpen(!open)
  }

  async function run(operacao: Operacao) {
    if (busy) return
    if (operacao === 'limpar' && !window.confirm(CONFIRMA_LIMPAR)) return

    setBusy(operacao)
    setResultado(null)
    toggleRef.current?.focus()
    try {
      const c = await (operacao === 'limpar' ? limparDados() : adicionarDadosTeste())
      setResultado({
        kind: 'success',
        text: operacao === 'limpar' ? `Banco limpo. Removidos: ${resumo(c)}.` : `Dados de teste adicionados: ${resumo(c)}.`,
      })
      onChanged()
    } catch (e) {
      const desabilitado = e instanceof ApiError && e.status === 404
      setResultado({ kind: 'error', text: desabilitado ? FERRAMENTAS_DESABILITADAS : errorMessage(e) })
    } finally {
      setBusy(null)
      setOpen(true)
    }
  }

  return (
    <div
      className="cm-settings"
      ref={rootRef}
      onBlur={(e) => {
        if (e.relatedTarget && !rootRef.current?.contains(e.relatedTarget as Node)) setOpen(false)
      }}
    >
      {open && (
        <div className="cm-settings-menu" id={menuId} ref={menuRef} role="group" aria-label="Ferramentas de desenvolvimento">
          <div className="cm-settings-title">Ferramentas de desenvolvimento</div>
          <div className="cm-settings-items">
            <Button variant="danger" disabled={busy !== null} onClick={() => run('limpar')}>
              Apagar todos os dados do banco
            </Button>
            <Button disabled={busy !== null} onClick={() => run('dados')}>
              Adicionar dados de teste (acrescenta aos dados atuais)
            </Button>
          </div>
          {busy && (
            <StatusMessage kind="loading">
              {busy === 'limpar' ? 'Apagando dados…' : 'Gerando dados de teste…'}
            </StatusMessage>
          )}
          {!busy && resultado && <StatusMessage kind={resultado.kind}>{resultado.text}</StatusMessage>}
        </div>
      )}
      <button
        ref={toggleRef}
        type="button"
        className="cm-settings-toggle"
        aria-label="Configurações"
        aria-haspopup="true"
        aria-expanded={open}
        aria-controls={open ? menuId : undefined}
        aria-busy={busy !== null}
        onClick={toggle}
      >
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
          <circle cx="12" cy="12" r="3" />
          <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 1 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 1 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 1 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 1 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z" />
        </svg>
      </button>
    </div>
  )
}
