export const brl = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

export function errorMessage(e: unknown): string {
  return e instanceof Error ? e.message : String(e)
}

export function isoDate(d: Date): string {
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${d.getFullYear()}-${mm}-${dd}`
}

export function formatDia(iso: string): string {
  const [y, m, d] = iso.split('-')
  return `${d}/${m}/${y}`
}

const dataHora = new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: 'short' })

export function formatDataHora(iso: string): string {
  return dataHora.format(new Date(iso))
}

const quantidade = new Intl.NumberFormat('pt-BR', { maximumFractionDigits: 3 })

export function formatQuantidade(n: number): string {
  return quantidade.format(n)
}
