import './statusMessage.css'

export function StatusMessage({
  kind = 'loading',
  children,
}: {
  kind?: 'loading' | 'error' | 'empty' | 'success'
  children: string
}) {
  const prefix = kind === 'error' ? 'Erro: ' : ''
  return (
    <div className={`cm-status cm-status--${kind}`} role={kind === 'error' ? 'alert' : 'status'}>
      {prefix + children}
    </div>
  )
}
