<template>
  <view class="page">
    <!-- 阶段1：测评介绍页 -->
    <view v-if="phase === 'startScreen'" class="start-screen">
      <view class="nav-bar">
        <view class="back-btn" @click="goBack">
          <text class="back-icon">&#xe60a;</text>
        </view>
        <text class="nav-title">{{ modeTitle }}</text>
        <view class="nav-placeholder"></view>
      </view>

      <view class="start-card">
        <view class="card-icon">
          <text class="icon-text">{{ modeIcon }}</text>
        </view>
        <text class="card-title">{{ modeName }}</text>
        <view class="card-info">
          <view class="info-row">
            <text class="info-label">题目数量</text>
            <text class="info-value">8 题</text>
          </view>
          <view class="info-row">
            <text class="info-label">预计时长</text>
            <text class="info-value">15 分钟</text>
          </view>
        </view>
        <text class="card-desc">{{ modeDesc }}</text>
      </view>

      <view class="start-btn" @click="startTest">
        <text class="start-btn-text">开始测评</text>
      </view>
    </view>

    <!-- 阶段2：答题页 -->
    <view v-if="phase === 'answering'" class="answer-screen">
      <view class="nav-bar">
        <view class="back-btn" @click="confirmExit">
          <text class="back-icon">&#xe60a;</text>
        </view>
        <text class="nav-title">{{ modeTitle }}</text>
        <view class="timer">{{ formatTime(elapsed) }}</view>
      </view>

      <!-- 进度条 -->
      <view class="progress-section">
        <text class="progress-text">{{ currentIndex + 1 }} / {{ questions.length }}</text>
        <view class="progress-bar-bg">
          <view class="progress-bar-fill" :style="{ width: ((currentIndex + 1) / questions.length * 100) + '%' }"></view>
        </view>
      </view>

      <!-- 题目区域 -->
      <scroll-view class="question-area" scroll-y>
        <view class="question-card">
          <view class="tag-row">
            <text class="tag module-tag">{{ currentQuestion.module }}</text>
            <text class="tag knowledge-tag">{{ currentQuestion.knowledgePoint }}</text>
            <text class="tag difficulty-tag">{{ currentQuestion.difficulty }}</text>
          </view>
          <text class="question-text">{{ currentQuestion.question }}</text>
        </view>

        <view class="options-list">
          <view
            v-for="(option, idx) in currentQuestion.options"
            :key="idx"
            class="option-item"
            :class="{ 'option-selected': answers[currentIndex] === idx, 'option-active': activeOption === idx }"
            @click="selectOption(idx)"
            @touchstart="activeOption = idx"
            @touchend="activeOption = -1"
          >
            <view class="option-letter" :class="{ 'letter-selected': answers[currentIndex] === idx }">
              {{ String.fromCharCode(65 + idx) }}
            </view>
            <text class="option-text">{{ option.slice(3) }}</text>
          </view>
        </view>
      </scroll-view>

      <!-- 底部按钮 -->
      <view class="bottom-bar">
        <view v-if="currentIndex > 0" class="prev-btn" @click="prevQuestion">
          <text class="btn-text">上一题</text>
        </view>
        <view v-else class="btn-placeholder"></view>
        <view class="next-btn" @click="nextQuestion">
          <text class="btn-text-white">{{ currentIndex === questions.length - 1 ? '提交测评' : '下一题' }}</text>
        </view>
      </view>
    </view>

    <!-- 阶段3：提交确认弹窗 -->
    <view v-if="showConfirm" class="confirm-mask" @click="showConfirm = false">
      <view class="confirm-dialog" @click.stop>
        <text class="confirm-title">确认提交</text>
        <view class="confirm-stats">
          <view class="stat-item">
            <text class="stat-num answered">{{ answeredCount }}</text>
            <text class="stat-label">已答题</text>
          </view>
          <view class="stat-divider"></view>
          <view class="stat-item">
            <text class="stat-num unanswered">{{ questions.length - answeredCount }}</text>
            <text class="stat-label">未答题</text>
          </view>
        </view>
        <text class="confirm-tip">提交后将无法修改答案</text>
        <view class="confirm-btns">
          <view class="confirm-cancel" @click="showConfirm = false">
            <text class="cancel-text">继续答题</text>
          </view>
          <view class="confirm-ok" @click="submitTest">
            <text class="ok-text">确认提交</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getQuestions, submitTest as submitTestAPI } from '@/api/test.js'

const mode = ref('foundation')
const phase = ref('startScreen')
const currentIndex = ref(0)
const answers = ref([])
const elapsed = ref(0)
const showConfirm = ref(false)
const activeOption = ref(-1)
const loading = ref(false)

let timer = null

const questions = ref([])

const modeTitle = computed(() => {
  const map = { foundation: '功底测评', chapter: '章节测试', topic: '专题测试' }
  return map[mode.value] || '测评'
})

const modeName = computed(() => {
  const map = { foundation: '高中数学功底测评', chapter: '章节达标测试', topic: '专题强化测试' }
  return map[mode.value] || '测评'
})

const modeIcon = computed(() => {
  const map = { foundation: '基', chapter: '章', topic: '专' }
  return map[mode.value] || '测'
})

const modeDesc = computed(() => {
  const map = {
    foundation: '全面检测高中数学核心知识掌握情况，覆盖函数、导数、三角函数、数列、圆锥曲线等模块，精准定位薄弱环节。',
    chapter: '针对特定章节进行深度检测，评估对该章节各知识点的掌握程度，提供针对性的学习建议。',
    topic: '围绕特定专题进行强化训练，突破难点，提升综合解题能力。'
  }
  return map[mode.value] || ''
})

const currentQuestion = computed(() => questions.value[currentIndex.value] || {})

const answeredCount = computed(() => answers.value.filter(a => a !== -1).length)

function goBack() {
  uni.navigateBack({ fail: () => uni.switchTab({ url: '/pages/index/index' }) })
}

async function startTest() {
  loading.value = true
  try {
    const qs = await getQuestions(mode.value)
    questions.value = qs
    answers.value = Array(qs.length).fill(-1)
    phase.value = 'answering'
    elapsed.value = 0
    timer = setInterval(() => { elapsed.value++ }, 1000)
  } catch (e) {
    uni.showToast({ title: '加载题目失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

function selectOption(idx) {
  answers.value[currentIndex.value] = idx
}

function nextQuestion() {
  if (currentIndex.value === questions.value.length - 1) {
    showConfirm.value = true
    return
  }
  currentIndex.value++
}

function prevQuestion() {
  if (currentIndex.value > 0) currentIndex.value--
}

function confirmExit() {
  uni.showModal({
    title: '提示',
    content: '测评进行中，退出将丢失答题记录，确认退出？',
    success: (res) => { if (res.confirm) { clearInterval(timer); goBack() } }
  })
}

async function submitTest() {
  clearInterval(timer)
  showConfirm.value = false
  loading.value = true

  try {
    // 构建答案数据，包含题目ID和用户答案
    const answerData = questions.value.map((q, i) => ({
      questionId: q.id,
      answer: answers.value[i]
    }))

    const result = await submitTestAPI({
      answers: answerData,
      elapsed: elapsed.value
    })

    uni.setStorageSync('lastTestResult', {
      score: result.score,
      total: result.total,
      mode: mode.value,
      answers: [...answers.value],
      questions: questions.value
    })

    uni.navigateTo({
      url: `/pages/report/index?score=${result.score}&total=${result.total}&mode=${mode.value}`
    })
  } catch (e) {
    console.error('提交失败', e)
    // 如果 API 失败，本地计算分数
    let correct = 0
    questions.value.forEach((q, i) => {
      if (answers.value[i] === q.answer) correct++
    })
    const score = Math.round(correct / questions.value.length * 100)

    uni.setStorageSync('lastTestResult', {
      score,
      total: questions.value.length,
      mode: mode.value,
      answers: [...answers.value],
      questions: questions.value
    })

    uni.navigateTo({
      url: `/pages/report/index?score=${score}&total=${questions.value.length}&mode=${mode.value}`
    })
  } finally {
    loading.value = false
  }
}

function formatTime(s) {
  const m = String(Math.floor(s / 60)).padStart(2, '0')
  const sec = String(s % 60).padStart(2, '0')
  return `${m}:${sec}`
}

// 接收页面参数
onLoad((options) => {
  if (options?.mode) mode.value = options.mode
})
</script>

<style scoped>
.page { min-height: 100vh; background: #f5f7fa; }

/* 导航栏 */
.nav-bar { display: flex; align-items: center; justify-content: space-between; padding: 44rpx 32rpx 20rpx; background: #fff; }
.back-btn { width: 60rpx; height: 60rpx; display: flex; align-items: center; justify-content: center; }
.back-icon { font-size: 36rpx; color: #333; }
.nav-title { font-size: 34rpx; font-weight: 600; color: #1a1a1a; }
.nav-placeholder { width: 60rpx; }
.timer { font-size: 28rpx; color: #4A7BF7; font-weight: 500; }

/* 开始页 */
.start-screen { padding-bottom: 60rpx; }
.start-card { margin: 40rpx 32rpx; background: #fff; border-radius: 24rpx; padding: 60rpx 40rpx; display: flex; flex-direction: column; align-items: center; box-shadow: 0 4rpx 24rpx rgba(74,123,247,0.08); }
.card-icon { width: 120rpx; height: 120rpx; border-radius: 50%; background: linear-gradient(135deg, #4A7BF7, #7B61FF); display: flex; align-items: center; justify-content: center; margin-bottom: 32rpx; }
.icon-text { font-size: 48rpx; color: #fff; font-weight: bold; }
.card-title { font-size: 40rpx; font-weight: 700; color: #1a1a1a; margin-bottom: 32rpx; }
.card-info { width: 100%; background: #f5f7fa; border-radius: 16rpx; padding: 24rpx 32rpx; margin-bottom: 32rpx; }
.info-row { display: flex; justify-content: space-between; padding: 8rpx 0; }
.info-label { font-size: 28rpx; color: #666; }
.info-value { font-size: 28rpx; color: #1a1a1a; font-weight: 500; }
.card-desc { font-size: 26rpx; color: #666; line-height: 1.7; text-align: center; }
.start-btn { margin: 40rpx 32rpx; height: 96rpx; border-radius: 48rpx; background: linear-gradient(135deg, #4A7BF7, #6B8FFF); display: flex; align-items: center; justify-content: center; box-shadow: 0 8rpx 24rpx rgba(74,123,247,0.3); }
.start-btn-text { font-size: 34rpx; color: #fff; font-weight: 600; }

/* 答题页 */
.answer-screen { display: flex; flex-direction: column; height: 100vh; }
.progress-section { padding: 20rpx 32rpx; background: #fff; }
.progress-text { font-size: 26rpx; color: #666; margin-bottom: 12rpx; display: block; }
.progress-bar-bg { height: 12rpx; background: #e8ecf1; border-radius: 6rpx; overflow: hidden; }
.progress-bar-fill { height: 100%; background: linear-gradient(90deg, #4A7BF7, #6B8FFF); border-radius: 6rpx; transition: width 0.3s ease; }

.question-area { flex: 1; padding: 24rpx 32rpx; }
.question-card { background: #fff; border-radius: 20rpx; padding: 32rpx; margin-bottom: 24rpx; }
.tag-row { display: flex; gap: 12rpx; margin-bottom: 24rpx; flex-wrap: wrap; }
.tag { font-size: 22rpx; padding: 6rpx 16rpx; border-radius: 8rpx; }
.module-tag { background: #eef3ff; color: #4A7BF7; }
.knowledge-tag { background: #f0f9f0; color: #34C759; }
.difficulty-tag { background: #fff5eb; color: #FF9500; }
.question-text { font-size: 32rpx; color: #1a1a1a; line-height: 1.7; font-weight: 500; }

.options-list { margin-top: 8rpx; }
.option-item { display: flex; align-items: center; background: #fff; border-radius: 16rpx; padding: 28rpx 24rpx; margin-bottom: 16rpx; border: 2rpx solid #e8ecf1; transition: all 0.2s ease; }
.option-selected { border-color: #4A7BF7; background: #f0f5ff; }
.option-active { transform: scale(0.98); }
.option-letter { width: 52rpx; height: 52rpx; border-radius: 50%; background: #f5f7fa; display: flex; align-items: center; justify-content: center; font-size: 26rpx; color: #666; margin-right: 20rpx; font-weight: 600; flex-shrink: 0; }
.letter-selected { background: #4A7BF7; color: #fff; }
.option-text { font-size: 30rpx; color: #333; flex: 1; }

.bottom-bar { display: flex; align-items: center; padding: 20rpx 32rpx 60rpx; background: #fff; gap: 20rpx; }
.prev-btn { flex: 1; height: 88rpx; border-radius: 44rpx; border: 2rpx solid #4A7BF7; display: flex; align-items: center; justify-content: center; }
.prev-btn .btn-text { font-size: 30rpx; color: #4A7BF7; font-weight: 500; }
.next-btn { flex: 2; height: 88rpx; border-radius: 44rpx; background: linear-gradient(135deg, #4A7BF7, #6B8FFF); display: flex; align-items: center; justify-content: center; }
.btn-text-white { font-size: 30rpx; color: #fff; font-weight: 600; }
.btn-placeholder { flex: 1; }

/* 确认弹窗 */
.confirm-mask { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 999; }
.confirm-dialog { width: 600rpx; background: #fff; border-radius: 24rpx; padding: 48rpx 40rpx 36rpx; }
.confirm-title { font-size: 36rpx; font-weight: 700; color: #1a1a1a; text-align: center; display: block; margin-bottom: 40rpx; }
.confirm-stats { display: flex; align-items: center; justify-content: center; margin-bottom: 24rpx; }
.stat-item { display: flex; flex-direction: column; align-items: center; padding: 0 48rpx; }
.stat-num { font-size: 56rpx; font-weight: 700; }
.stat-num.answered { color: #4A7BF7; }
.stat-num.unanswered { color: #FF9500; }
.stat-label { font-size: 24rpx; color: #999; margin-top: 8rpx; }
.stat-divider { width: 2rpx; height: 60rpx; background: #e8ecf1; }
.confirm-tip { font-size: 24rpx; color: #999; text-align: center; display: block; margin-bottom: 40rpx; }
.confirm-btns { display: flex; gap: 20rpx; }
.confirm-cancel { flex: 1; height: 80rpx; border-radius: 40rpx; border: 2rpx solid #e8ecf1; display: flex; align-items: center; justify-content: center; }
.cancel-text { font-size: 28rpx; color: #666; }
.confirm-ok { flex: 1; height: 80rpx; border-radius: 40rpx; background: linear-gradient(135deg, #4A7BF7, #6B8FFF); display: flex; align-items: center; justify-content: center; }
.ok-text { font-size: 28rpx; color: #fff; font-weight: 600; }
</style>
