import { useId } from 'react'

export type SortBy = 'id' | 'nome'

export function SortSelect({ value, onChange }: { value: SortBy; onChange: (v: SortBy) => void }) {
  const id = useId()
  return (
    <div className="sort-select">
      <label className="cm-label" htmlFor={id}>
        Ordenar por
      </label>
      <select
        id={id}
        className="cm-input cm-select"
        value={value}
        onChange={(e) => onChange(e.target.value as SortBy)}
      >
        <option value="id">ID</option>
        <option value="nome">Nome</option>
      </select>
    </div>
  )
}
