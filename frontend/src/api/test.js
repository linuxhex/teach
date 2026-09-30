import request from './request'

export function getQuestions(mode) {
  return request(`/api/test/questions?mode=${mode}`)
}

export function submitTest(data) {
  return request('/api/test/submit', { method: 'POST', data })
}

export function getRecords(mode) {
  return request(`/api/test/records${mode ? '?mode=' + mode : ''}`)
}
