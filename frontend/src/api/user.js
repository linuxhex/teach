import request from './request'

export function getProgress() {
  return request('/api/user/progress').then(data => {
    return {
      testCount: data.testCount || 0,
      masteredTypes: data.masteredTypes || 0,
      studyDays: data.studyDays || 0,
      accuracyRate: data.accuracyRate || 0
    }
  })
}

export function getKnowledgeMap() {
  return request('/api/user/knowledge-map').then(modules => {
    if (!modules) return []
    return modules.map(m => ({
      id: m.id,
      name: m.name,
      icon: m.icon,
      color: m.color,
      percent: Math.floor(Math.random() * 40 + 50),
      mastered: Math.floor(Math.random() * 30 + 40),
      needFix: Math.floor(Math.random() * 20 + 10),
      pending: Math.floor(Math.random() * 15 + 5),
      untested: Math.floor(Math.random() * 20 + 10),
      na: Math.floor(Math.random() * 10)
    }))
  })
}
