import request from './request'

export function getProgress() {
  return request('/api/user/progress')
}

export function getKnowledgeMap() {
  return request('/api/user/knowledge-map')
}
