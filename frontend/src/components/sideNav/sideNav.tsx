import type { MouseEvent, ReactNode } from 'react'
import { cx } from '../cx'
import './sideNav.css'

export type NavItem = { href: string; label: string; active?: boolean }
export type NavGroup = { title: string; items: NavItem[] }

export function SideNav({
  groups,
  title,
  label = 'Recursos',
  onNavigate,
  footer,
}: {
  groups: NavGroup[]
  title?: string
  label?: string
  onNavigate?: (item: NavItem) => void
  footer?: ReactNode
}) {
  function click(e: MouseEvent<HTMLAnchorElement>, item: NavItem) {
    if (!onNavigate) return
    if (e.metaKey || e.ctrlKey || e.shiftKey || e.altKey || e.button !== 0) return
    e.preventDefault()
    onNavigate(item)
  }

  return (
    <nav className="cm-nav" aria-label={label}>
      <div className="cm-nav-body">
        {title && <div className="cm-nav-title">{title}</div>}
        {groups.map((group) => (
          <div className="cm-nav-group" key={group.title}>
            <div className="cm-nav-group-title">{group.title}</div>
            <ul className="cm-nav-list">
              {group.items.map((it) => (
                <li key={it.href}>
                  <a
                    href={it.href}
                    className={cx('cm-nav-link', it.active && 'cm-nav-link--active')}
                    aria-current={it.active ? 'page' : undefined}
                    onClick={(e) => click(e, it)}
                  >
                    {it.label}
                  </a>
                </li>
              ))}
            </ul>
          </div>
        ))}
      </div>
      {footer && <div className="cm-nav-footer">{footer}</div>}
    </nav>
  )
}
