export const API_URL: string = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

export class ApiError extends Error {
  readonly status: number

  constructor(message: string, status: number) {
    super(message)
    this.name = 'ApiError'
    this.status = status
  }
}

async function errorMessage(res: Response): Promise<string> {
  const text = await res.text().catch(() => '')
  const fallback = text || `${res.status} ${res.statusText}`
  try {
    const body = JSON.parse(text) as { message?: string; error?: string }
    const detail = body.message || body.error
    if (detail) return detail
  } catch {
    return fallback
  }
  return fallback
}

export async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(`${API_URL}/api/${path}`, {
    ...init,
    headers: { 'Content-Type': 'application/json', ...init?.headers },
  })

  if (!res.ok) throw new ApiError(await errorMessage(res), res.status)

  const text = await res.text()
  return (text ? JSON.parse(text) : undefined) as T
}
