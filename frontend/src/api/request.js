const BASE_URL = 'http://localhost:8080'

function request(url, options = {}) {
  const token = uni.getStorageSync('token')
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.statusCode === 401 || res.statusCode === 403) {
          uni.removeStorageSync('token')
          uni.reLaunch({ url: '/pages/login/index' })
          reject(new Error('未授权'))
          return
        }
        if (res.data && res.data.code === 0) {
          resolve(res.data.data)
        } else {
          reject(new Error(res.data?.message || '请求失败'))
        }
      },
      fail: (err) => reject(err)
    })
  })
}

export default request
