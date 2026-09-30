<template>
  <view class="page">
    <!-- 顶部结果区 -->
    <view class="result-header">
      <view class="result-bg"></view>
      <view class="result-content">
        <text class="score-text">{{ score }}<text class="score-unit">分</text></text>
        <text class="score-comment">{{ scoreComment }}</text>
        <text class="result-meta">{{ modeTitle }} · {{ today }}</text>
      </view>
    </view>

    <!-- AI 诊断结论 -->
    <view class="section-card">
      <view class="section-title-row">
        <view class="title-dot"></view>
        <text class="section-title">AI 诊断结论</text>
      </view>
      <text class="diagnosis-text">{{ diagnosisText }}</text>
      <view v-if="weakPoints.length" class="weak-list">
        <view v-for="(wp, i) in weakPoints" :key="i" class="weak-item">
          <text class="weak-dot">·</text>
          <text class="weak-text">{{ wp }}</text>
        </view>
      </view>
    </view>

    <!-- 答题详情 -->
    <view class="section-card">
      <view class="section-title-row">
        <view class="title-dot"></view>
        <text class="section-title">答题详情</text>
      </view>
      <view class="detail-list">
        <view
          v-for="(q, i) in questions"
          :key="q.id"
          class="detail-item"
        >
          <view class="detail-row" @click="toggleExpand(i)">
            <view class="detail-left">
              <text class="detail-num">{{ i + 1 }}.</text>
              <view class="detail-tags">
                <text class="detail-module">{{ q.module }}</text>
                <text class="detail-kp">{{ q.knowledgePoint }}</text>
              </view>
            </view>
            <view class="detail-right">
              <text class="result-icon" :class="isCorrect(i) ? 'correct' : 'wrong'">
                {{ isCorrect(i) ? '✓' : '✗' }}
              </text>
              <text class="expand-arrow" :class="{ expanded: expandedIdx === i }">▾</text>
            </view>
          </view>
          <view v-if="expandedIdx === i" class="expand-content">
            <text class="expand-question">{{ q.question }}</text>
            <view class="expand-options">
              <view
                v-for="(opt, oi) in q.options"
                :key="oi"
                class="expand-opt"
                :class="{ 'opt-correct': oi === q.answer, 'opt-wrong': oi === answers[i] && oi !== q.answer }"
              >
                <text class="opt-letter">{{ String.fromCharCode(65 + oi) }}</text>
                <text class="opt-text">{{ opt.slice(3) }}</text>
                <text v-if="oi === q.answer" class="opt-tag correct-tag">正确答案</text>
                <text v-if="oi === answers[i] && oi !== q.answer" class="opt-tag wrong-tag">你的选择</text>
              </view>
            </view>
            <view class="analysis-box">
              <text class="analysis-label">解析</text>
              <text class="analysis-text">{{ q.analysis }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 知识模块影响 -->
    <view class="section-card">
      <view class="section-title-row">
        <view class="title-dot"></view>
        <text class="section-title">本次测评涉及模块</text>
      </view>
      <view class="module-list">
        <view v-for="(m, i) in moduleStats" :key="i" class="module-row">
          <text class="module-name">{{ m.name }}</text>
          <view class="module-bar-bg">
            <view class="module-bar-fill" :style="{ width: m.rate + '%', background: m.rate >= 80 ? '#34C759' : m.rate >= 60 ? '#FF9500' : '#FF3B30' }"></view>
          </view>
          <text class="module-rate" :style="{ color: m.rate >= 80 ? '#34C759' : m.rate >= 60 ? '#FF9500' : '#FF3B30' }">{{ m.rate }}%</text>
        </view>
      </view>
    </view>

    <!-- 底部操作 -->
    <view class="bottom-actions">
      <view class="action-btn secondary" @click="goHome">
        <text class="action-text secondary-text">返回首页</text>
      </view>
      <view class="action-btn primary" @click="retry">
        <text class="action-text primary-text">再来一次</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'

const score = ref(0)
const total = ref(8)
const mode = ref('foundation')
const expandedIdx = ref(-1)

const questions = ref([])

// 从 storage 读取答题数据
const storedResult = uni.getStorageSync('lastTestResult')
const storedAnswers = (storedResult && storedResult.answers) ? storedResult.answers : []
const storedQuestions = (storedResult && storedResult.questions) ? storedResult.questions : []
uni.removeStorageSync('lastTestResult')

const answers = ref(storedAnswers.length ? storedAnswers : Array(8).fill(-1))

if (storedQuestions.length > 0) {
  questions.value = storedQuestions
}

const today = computed(() => {
  const d = new Date()
  return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, '0')}.${String(d.getDate()).padStart(2, '0')}`
})

const modeTitle = computed(() => {
  const map = { foundation: '功底测评', chapter: '章节测试', topic: '专题测试' }
  return map[mode.value] || '测评'
})

const scoreComment = computed(() => {
  if (score.value >= 80) return '表现优秀'
  if (score.value >= 60) return '基础尚可'
  return '需要加强'
})

const weakPoints = computed(() => {
  const points = []
  questions.value.forEach((q, i) => {
    if (answers.value[i] !== q.answer) {
      points.push(`${q.module} · ${q.knowledgePoint}`)
    }
  })
  return points
})

const diagnosisText = computed(() => {
  if (score.value >= 80) {
    return '各知识点掌握良好，基础扎实。建议挑战更高难度题目，进一步提升综合解题能力。'
  } else if (score.value >= 60) {
    return '基础部分掌握尚可，但以下知识点需要加强，建议针对性复习与练习：'
  } else {
    return '基础较为薄弱，建议系统复习以下内容，夯实基础后再进行提升训练：'
  }
})

const moduleStats = computed(() => {
  const map = {}
  questions.value.forEach((q, i) => {
    if (!map[q.module]) map[q.module] = { name: q.module, correct: 0, total: 0 }
    map[q.module].total++
    if (answers.value[i] === q.answer) map[q.module].correct++
  })
  return Object.values(map).map(m => ({ ...m, rate: Math.round(m.correct / m.total * 100) }))
})

function isCorrect(i) {
  return answers.value[i] === questions.value[i].answer
}

function toggleExpand(i) {
  expandedIdx.value = expandedIdx.value === i ? -1 : i
}

function goHome() {
  uni.switchTab({ url: '/pages/index/index' })
}

function retry() {
  uni.redirectTo({ url: `/pages/test/index?mode=${mode.value}` })
}

// 接收页面参数
onLoad((options) => {
  const opts = options || {}
  score.value = Number(opts.score) || 0
  total.value = Number(opts.total) || 8
  mode.value = opts.mode || 'foundation'
  if (!answers.value.length || answers.value.every(a => a === -1)) {
    answers.value = Array(total.value).fill(-1)
  }
})
</script>

<style scoped>
.page { min-height: 100vh; background: #f5f7fa; padding-bottom: 180rpx; }

/* 顶部结果区 */
.result-header { position: relative; height: 380rpx; overflow: hidden; }
.result-bg { position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: linear-gradient(135deg, #4A7BF7, #7B61FF); }
.result-content { position: relative; z-index: 1; display: flex; flex-direction: column; align-items: center; padding-top: 80rpx; }
.score-text { font-size: 80rpx; font-weight: 800; color: #fff; }
.score-unit { font-size: 36rpx; font-weight: 500; margin-left: 4rpx; }
.score-comment { font-size: 32rpx; color: rgba(255,255,255,0.9); margin-top: 8rpx; font-weight: 500; }
.result-meta { font-size: 24rpx; color: rgba(255,255,255,0.7); margin-top: 12rpx; }

/* 通用卡片 */
.section-card { margin: 24rpx 24rpx 0; background: #fff; border-radius: 20rpx; padding: 32rpx; }
.section-title-row { display: flex; align-items: center; margin-bottom: 24rpx; }
.title-dot { width: 8rpx; height: 28rpx; border-radius: 4rpx; background: #4A7BF7; margin-right: 12rpx; }
.section-title { font-size: 32rpx; font-weight: 700; color: #1a1a1a; }

/* AI 诊断 */
.diagnosis-text { font-size: 28rpx; color: #333; line-height: 1.7; display: block; margin-bottom: 16rpx; }
.weak-list { padding-left: 8rpx; }
.weak-item { display: flex; align-items: center; padding: 8rpx 0; }
.weak-dot { font-size: 32rpx; color: #FF9500; margin-right: 12rpx; font-weight: bold; }
.weak-text { font-size: 26rpx; color: #555; }

/* 答题详情 */
.detail-list { }
.detail-item { border-bottom: 1rpx solid #f0f0f0; }
.detail-item:last-child { border-bottom: none; }
.detail-row { display: flex; align-items: center; justify-content: space-between; padding: 20rpx 0; }
.detail-left { display: flex; align-items: center; flex: 1; }
.detail-num { font-size: 28rpx; color: #1a1a1a; font-weight: 600; margin-right: 12rpx; }
.detail-tags { display: flex; gap: 8rpx; }
.detail-module { font-size: 22rpx; color: #4A7BF7; background: #eef3ff; padding: 4rpx 12rpx; border-radius: 6rpx; }
.detail-kp { font-size: 22rpx; color: #34C759; background: #f0f9f0; padding: 4rpx 12rpx; border-radius: 6rpx; }
.detail-right { display: flex; align-items: center; gap: 12rpx; }
.result-icon { font-size: 32rpx; font-weight: 700; }
.result-icon.correct { color: #34C759; }
.result-icon.wrong { color: #FF3B30; }
.expand-arrow { font-size: 24rpx; color: #999; transition: transform 0.3s ease; }
.expand-arrow.expanded { transform: rotate(180deg); }

.expand-content { padding: 0 0 24rpx; animation: fadeIn 0.2s ease; }
.expand-question { font-size: 28rpx; color: #333; line-height: 1.6; display: block; margin-bottom: 16rpx; }
.expand-options { margin-bottom: 16rpx; }
.expand-opt { display: flex; align-items: center; padding: 12rpx 16rpx; border-radius: 12rpx; margin-bottom: 8rpx; background: #f8f9fa; }
.expand-opt.opt-correct { background: #f0f9f0; border: 1rpx solid #34C759; }
.expand-opt.opt-wrong { background: #fff5f5; border: 1rpx solid #FF3B30; }
.opt-letter { width: 40rpx; height: 40rpx; border-radius: 50%; background: #e8ecf1; display: flex; align-items: center; justify-content: center; font-size: 22rpx; color: #666; margin-right: 12rpx; font-weight: 600; }
.opt-correct .opt-letter { background: #34C759; color: #fff; }
.opt-wrong .opt-letter { background: #FF3B30; color: #fff; }
.opt-text { font-size: 26rpx; color: #333; flex: 1; }
.opt-tag { font-size: 20rpx; padding: 4rpx 12rpx; border-radius: 6rpx; }
.correct-tag { color: #34C759; background: #e6f9e6; }
.wrong-tag { color: #FF3B30; background: #ffe6e6; }

.analysis-box { background: #f5f7fa; border-radius: 12rpx; padding: 20rpx; }
.analysis-label { font-size: 22rpx; color: #4A7BF7; font-weight: 600; display: block; margin-bottom: 8rpx; }
.analysis-text { font-size: 26rpx; color: #555; line-height: 1.6; }

/* 模块统计 */
.module-list { }
.module-row { display: flex; align-items: center; padding: 12rpx 0; }
.module-name { font-size: 26rpx; color: #333; width: 140rpx; flex-shrink: 0; }
.module-bar-bg { flex: 1; height: 16rpx; background: #f0f0f0; border-radius: 8rpx; margin: 0 16rpx; overflow: hidden; }
.module-bar-fill { height: 100%; border-radius: 8rpx; transition: width 0.5s ease; }
.module-rate { font-size: 26rpx; font-weight: 600; width: 80rpx; text-align: right; }

/* 底部操作 */
.bottom-actions { position: fixed; left: 0; right: 0; bottom: 0; display: flex; gap: 20rpx; padding: 24rpx 32rpx 60rpx; background: #fff; box-shadow: 0 -4rpx 16rpx rgba(0,0,0,0.05); }
.action-btn { flex: 1; height: 88rpx; border-radius: 44rpx; display: flex; align-items: center; justify-content: center; }
.action-btn.primary { background: linear-gradient(135deg, #4A7BF7, #6B8FFF); }
.action-btn.secondary { background: #f5f7fa; }
.action-text { font-size: 30rpx; font-weight: 500; }
.primary-text { color: #fff; }
.secondary-text { color: #666; }

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-8rpx); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
