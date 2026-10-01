export type CrudApi<T> = {
  list(): Promise<T[]>
  get(id: unknown): Promise<T>
  create(body: Partial<T>): Promise<T>
  update(id: unknown, body: Partial<T>): Promise<T>
  remove(id: unknown): Promise<void>
}
