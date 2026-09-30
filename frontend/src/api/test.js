import request from './request'

export function getQuestions(mode) {
  return request(`/api/test/questions?mode=${mode}`).then(questions => {
    return questions.map(q => ({
      id: q.id,
      questionType: q.questionType || '单选题',
      question: q.content,
      options: JSON.parse(q.options || '[]').map((opt, i) => `${String.fromCharCode(65 + i)}. ${opt}`),
      answer: q.answer,
      referenceAnswer: q.referenceAnswer,
      analysis: q.analysis,
      difficulty: q.difficulty,
      module: q.module?.name || '',
      knowledgePoint: q.knowledgePoint?.name || ''
    }))
  })
}

export function submitTest(data) {
  return request('/api/test/submit', { method: 'POST', data })
}

export function getRecords(mode) {
  return request(`/api/test/records${mode ? '?mode=' + mode : ''}`).then(records => {
    if (!records) return []
    return records.map(r => ({
      id: r.id,
      title: r.mode === 'foundation' ? '功底测评' : r.mode === 'chapter' ? '章节测试' : '专题测试',
      type: r.mode === 'foundation' ? '功能测评' : r.mode === 'chapter' ? '章节测试' : '专题测试',
      score: r.score,
      date: r.createdAt ? new Date(r.createdAt).toLocaleDateString('zh-CN') : '',
      conclusion: r.score >= 80 ? '表现优秀' : r.score >= 60 ? '基础尚可' : '需要加强',
      mastered: r.score,
      total: r.total,
      correctCount: r.correctCount
    }))
  })
}
