import request from './request'

/**
 * 完整 AI 分析（评估 + 诊断 + 规划 + 推荐）
 */
export function analyzeTest(recordId) {
  return request({
    url: `/api/ai/analyze/${recordId}`,
    method: 'POST'
  })
}

/**
 * 快速评估
 */
export function evaluateTest(recordId) {
  return request({
    url: `/api/ai/evaluate/${recordId}`,
    method: 'POST'
  })
}

/**
 * 快速诊断
 */
export function diagnoseTest(recordId) {
  return request({
    url: `/api/ai/diagnose/${recordId}`,
    method: 'POST'
  })
}
