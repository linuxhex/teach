<template>
  <view class="page">
    <!-- 顶部渐变背景 -->
    <view class="header">
      <view class="status-bar" :style="{ height: statusBarHeight + 'px' }"></view>
      <view class="header-nav">
        <view class="back-btn" @click="goBack">
          <text class="back-icon">&lt;</text>
        </view>
        <text class="header-title">学习报告</text>
        <view class="header-placeholder"></view>
      </view>

      <!-- 时间范围选择 -->
      <view class="time-range">
        <view
          v-for="item in timeRanges"
          :key="item.value"
          class="time-item"
          :class="{ active: selectedRange === item.value }"
          @click="selectedRange = item.value"
        >
          <text class="time-text">{{ item.label }}</text>
        </view>
      </view>
    </view>

    <!-- 可滚动内容 -->
    <scroll-view scroll-y class="scroll-content">
      <!-- 学习概况 -->
      <view class="section">
        <text class="section-title">学习概况</text>
        <view class="stats-grid">
          <view class="stat-card">
            <text class="stat-num blue">{{ userInfo.testCount }}</text>
            <text class="stat-label">测评次数</text>
          </view>
          <view class="stat-card">
            <text class="stat-num green">{{ userInfo.masteredTypes }}</text>
            <text class="stat-label">掌握题型</text>
          </view>
          <view class="stat-card">
            <text class="stat-num orange">{{ userInfo.studyDays }}</text>
            <text class="stat-label">学习天数</text>
          </view>
          <view class="stat-card">
            <text class="stat-num red">{{ accuracyRate }}%</text>
            <text class="stat-label">正确率</text>
          </view>
        </view>
      </view>

      <!-- 知识模块掌握 -->
      <view class="section">
        <text class="section-title">知识模块掌握</text>
        <view class="radar-card">
          <view v-for="m in knowledgeModules" :key="m.id" class="radar-row">
            <text class="radar-name">{{ m.name }}</text>
            <view class="radar-bar-bg">
              <view class="radar-bar-fill" :style="{ width: m.percent + '%', background: m.color }"></view>
            </view>
            <text class="radar-pct" :style="{ color: m.color }">{{ m.percent }}%</text>
          </view>
        </view>
      </view>

      <!-- 测评历史 -->
      <view class="section">
        <text class="section-title">测评历史</text>
        <view class="history-list">
          <view v-for="r in testRecords" :key="r.id" class="history-item">
            <view class="history-top">
              <text class="history-title">{{ r.title }}</text>
              <text class="history-score">{{ r.score }}分</text>
            </view>
            <view class="history-bottom">
              <text class="history-date">{{ r.date }}</text>
              <text class="history-conclusion">{{ r.conclusion }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 学习趋势 -->
      <view class="section">
        <text class="section-title">学习趋势</text>
        <view class="trend-card">
          <view class="trend-chart">
            <view v-for="(r, i) in recentRecords" :key="i" class="trend-bar-wrap">
              <view class="trend-bar" :style="{ height: (r.score / 100 * 200) + 'rpx' }">
                <text class="trend-bar-num">{{ r.score }}</text>
              </view>
              <text class="trend-label">{{ r.date.slice(5) }}</text>
            </view>
          </view>
        </view>
      </view>

      <view class="bottom-space"></view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { userInfo, testRecords, knowledgeModules } from '@/data/mock.js'

const statusBarHeight = ref(20)
const selectedRange = ref('all')

uni.getSystemInfo({
  success: (res) => {
    statusBarHeight.value = res.statusBarHeight || 20
  }
})

const timeRanges = [
  { label: '本周', value: 'week' },
  { label: '本月', value: 'month' },
  { label: '全部', value: 'all' }
]

// 正确率（取所有测评记录的平均分）
const accuracyRate = computed(() => {
  if (testRecords.length === 0) return 0
  const avg = testRecords.reduce((sum, r) => sum + r.score, 0) / testRecords.length
  return Math.round(avg)
})

// 最近3次测评（趋势）
const recentRecords = computed(() => {
  return [...testRecords].reverse().slice(0, 3)
})

function goBack() {
  uni.navigateBack()
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #F5F7FA;
}

/* 顶部 */
.header {
  background: linear-gradient(135deg, #4A7BF7, #6B9BFF);
  border-radius: 0 0 30rpx 30rpx;
}

.header-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 24rpx;
}

.back-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-icon {
  font-size: 36rpx;
  color: #ffffff;
  font-weight: bold;
}

.header-title {
  font-size: 34rpx;
  font-weight: bold;
  color: #ffffff;
}

.header-placeholder {
  width: 60rpx;
}

/* 时间范围 */
.time-range {
  display: flex;
  justify-content: center;
  gap: 24rpx;
  padding: 20rpx 0 30rpx;
}

.time-item {
  padding: 10rpx 32rpx;
  border-radius: 24rpx;
  background: rgba(255, 255, 255, 0.2);
}

.time-item.active {
  background: #ffffff;
}

.time-text {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.9);
}

.time-item.active .time-text {
  color: #4A7BF7;
  font-weight: 600;
}

/* 滚动内容 */
.scroll-content {
  flex: 1;
}

/* 通用 section */
.section {
  margin-top: 24rpx;
  padding: 0 24rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 20rpx;
  display: block;
}

/* 学习概况 */
.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16rpx;
}

.stat-card {
  background: #ffffff;
  border-radius: 16rpx;
  padding: 24rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-num {
  font-size: 44rpx;
  font-weight: bold;
}

.stat-num.blue { color: #4A7BF7; }
.stat-num.green { color: #34C759; }
.stat-num.orange { color: #FF9500; }
.stat-num.red { color: #FF3B30; }

.stat-label {
  font-size: 22rpx;
  color: #999999;
  margin-top: 8rpx;
}

/* 知识模块掌握 */
.radar-card {
  background: #ffffff;
  border-radius: 16rpx;
  padding: 24rpx;
}

.radar-row {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.radar-row:last-child {
  margin-bottom: 0;
}

.radar-name {
  font-size: 24rpx;
  color: #333333;
  width: 120rpx;
  flex-shrink: 0;
}

.radar-bar-bg {
  flex: 1;
  height: 16rpx;
  background: #F0F0F0;
  border-radius: 8rpx;
  overflow: hidden;
  margin: 0 16rpx;
}

.radar-bar-fill {
  height: 100%;
  border-radius: 8rpx;
}

.radar-pct {
  font-size: 24rpx;
  font-weight: 600;
  width: 80rpx;
  text-align: right;
  flex-shrink: 0;
}

/* 测评历史 */
.history-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.history-item {
  background: #ffffff;
  border-radius: 16rpx;
  padding: 24rpx;
}

.history-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10rpx;
}

.history-title {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
}

.history-score {
  font-size: 28rpx;
  font-weight: bold;
  color: #4A7BF7;
}

.history-bottom {
  display: flex;
  justify-content: space-between;
}

.history-date {
  font-size: 22rpx;
  color: #999999;
}

.history-conclusion {
  font-size: 22rpx;
  color: #666666;
}

/* 学习趋势 */
.trend-card {
  background: #ffffff;
  border-radius: 16rpx;
  padding: 30rpx 24rpx;
}

.trend-chart {
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
  height: 260rpx;
}

.trend-bar-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12rpx;
}

.trend-bar {
  width: 80rpx;
  background: linear-gradient(180deg, #4A7BF7, #6B9BFF);
  border-radius: 12rpx 12rpx 0 0;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding-top: 10rpx;
  min-height: 60rpx;
}

.trend-bar-num {
  font-size: 22rpx;
  color: #ffffff;
  font-weight: bold;
}

.trend-label {
  font-size: 20rpx;
  color: #999999;
}

.bottom-space {
  height: 60rpx;
}
</style>
