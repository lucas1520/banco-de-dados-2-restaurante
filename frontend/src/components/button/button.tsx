import type { ReactNode } from 'react'
import { cx } from '../cx'
import './button.css'

export function Button({
  variant = 'secondary',
  size = 'md',
  type = 'button',
  disabled,
  onClick,
  children,
}: {
  variant?: 'primary' | 'secondary' | 'danger'
  size?: 'md' | 'sm'
  type?: 'button' | 'submit'
  disabled?: boolean
  onClick?: () => void
  children: ReactNode
}) {
  return (
    <button
      type={type}
      className={cx('cm-btn', `cm-btn--${variant}`, size === 'sm' && 'cm-btn--sm')}
      disabled={disabled}
      onClick={onClick}
    >
      {children}
    </button>
  )
}
