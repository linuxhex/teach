import request from './request'

export function login(phone, password) {
  return request('/api/auth/login', { method: 'POST', data: { phone, password } })
}

export function register(data) {
  return request('/api/auth/register', { method: 'POST', data })
}

export function getProfile() {
  return request('/api/auth/profile')
}
