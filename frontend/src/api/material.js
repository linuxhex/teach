import request from './request'

export function getMaterials(params = {}) {
  const qs = Object.entries(params).filter(([_, v]) => v).map(([k, v]) => `${k}=${v}`).join('&')
  return request(`/api/materials${qs ? '?' + qs : ''}`).then(materials => {
    if (!materials) return []
    return materials.map(m => ({
      id: m.id,
      title: m.title,
      type: m.type,
      typeTag: m.type,
      purpose: m.purpose,
      desc: m.description,
      audience: m.audience,
      tags: m.tags ? m.tags.split(',') : [],
      price: m.price,
      originalPrice: m.originalPrice,
      matchRate: m.matchRate,
      stage: m.stage,
      version: m.version,
      volume: m.volume,
      chapter: m.chapter
    }))
  })
}

export function getMaterialFilters() {
  return request('/api/materials/filters')
}

export function getMaterialDetail(id) {
  return request(`/api/materials/${id}`)
}
