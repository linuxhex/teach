import request from './request'

export function getKnowledgeTree() {
  return request('/api/admin/knowledge/tree')
}

export function addKnowledgeModule(data) {
  return request('/api/admin/knowledge/module', { method: 'POST', data })
}

export function addKnowledgePoint(data) {
  return request('/api/admin/knowledge/point', { method: 'POST', data })
}

export function deleteKnowledgePoint(id) {
  return request(`/api/admin/knowledge/point/${id}`, { method: 'DELETE' })
}

export function getTemplates() {
  return request('/api/admin/templates')
}

export function addTemplate(data) {
  return request('/api/admin/templates', { method: 'POST', data })
}

export function deleteTemplate(id) {
  return request(`/api/admin/templates/${id}`, { method: 'DELETE' })
}

export function getDiagModules() {
  return request('/api/admin/diagnostic-modules')
}

export function getDiagPoints(moduleId) {
  return request(`/api/admin/diagnostic-points${moduleId ? '?moduleId=' + moduleId : ''}`)
}

export function getAdminStats() {
  return request('/api/admin/stats')
}
