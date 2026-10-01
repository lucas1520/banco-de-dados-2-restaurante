import type { CrudApi } from '../models/CrudApi'
import { request } from './client'

export function createCrudApi<T>(resource: string): CrudApi<T> {
  return {
    list: () => request<T[]>(resource),

    get: (id) => request<T>(`${resource}/${id}`),

    create: (body) =>
      request<T>(resource, { method: 'POST', body: JSON.stringify(body) }),

    update: (id, body) =>
      request<T>(`${resource}/${id}`, { method: 'PUT', body: JSON.stringify(body) }),

    remove: (id) => request<void>(`${resource}/${id}`, { method: 'DELETE' }),
  }
}
