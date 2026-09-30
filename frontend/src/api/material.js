import request from './request'

export function getMaterials(params = {}) {
  const qs = Object.entries(params).filter(([_, v]) => v).map(([k, v]) => `${k}=${v}`).join('&')
  return request(`/api/materials${qs ? '?' + qs : ''}`)
}

export function getMaterialFilters() {
  return request('/api/materials/filters')
}

export function getMaterialDetail(id) {
  return request(`/api/materials/${id}`)
}
