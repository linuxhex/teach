<template>
  <view class="page">
    <!-- 顶部导航栏 -->
    <view class="nav-bar">
      <view class="status-bar"></view>
      <view class="nav-content">
        <text class="nav-title">测评</text>
        <text class="nav-right" @click="goBuy">购买次数</text>
      </view>
    </view>

    <!-- 目标进度区 -->
    <view class="progress-card">
      <view class="progress-row">
        <view class="ring-wrap">
          <view class="ring" :style="{ background: ringBg }">
            <view class="ring-inner">
              <text class="ring-num">62%</text>
            </view>
          </view>
          <text class="progress-label">已掌握 64/103</text>
        </view>
        <view class="stats-grid">
          <view class="stat-item"><text class="stat-num green">64</text><text class="stat-label">已掌握</text></view>
          <view class="stat-item"><text class="stat-num orange">33</text><text class="stat-label">待补齐</text></view>
          <view class="stat-item"><text class="stat-num blue">16</text><text class="stat-label">待确认</text></view>
          <view class="stat-item"><text class="stat-num gray">43</text><text class="stat-label">未检测</text></view>
        </view>
      </view>
      <view class="ai-card">
        <text class="ai-title">AI 智能诊断引擎</text>
        <text class="ai-desc">基于知识图谱精准定位薄弱点，智能推荐最优学习路径</text>
      </view>
    </view>

    <!-- 掌握地图 -->
    <view class="section">
      <text class="section-title">掌握地图</text>
      <view class="module-list">
        <view class="module-row" v-for="(m, i) in knowledgeModules" :key="i" @click="onModuleClick(m)">
          <view class="module-left">
            <view class="module-icon" :style="{ background: m.color }">
              <text class="module-icon-text">{{ m.icon }}</text>
            </view>
            <text class="module-name">{{ m.name }}</text>
          </view>
          <view class="module-right">
            <text class="module-pct">{{ m.percent }}%</text>
            <view class="seg-bar">
              <view class="seg" style="background:#4CAF50" :style="{ width: m.mastered + '%' }"></view>
              <view class="seg" style="background:#FF9800" :style="{ width: m.needFix + '%' }"></view>
              <view class="seg" style="background:#2196F3" :style="{ width: m.pending + '%' }"></view>
              <view class="seg" style="background:#BDBDBD" :style="{ width: m.untested + '%' }"></view>
              <view class="seg" style="background:#E0E0E0" :style="{ width: m.na + '%' }"></view>
            </view>
            <view class="module-tags">
              <text class="tag green">{{ m.mastered }}</text>
              <text class="tag orange">{{ m.needFix }}</text>
              <text class="tag blue">{{ m.pending }}</text>
              <text class="tag gray">{{ m.untested }}</text>
            </view>
            <text class="arrow">></text>
          </view>
        </view>
      </view>
    </view>

    <!-- 状态说明 -->
    <view class="section">
      <text class="section-title">状态说明</text>
      <scroll-view scroll-x class="status-scroll">
        <view class="status-list">
          <view class="status-card" v-for="s in statusList" :key="s.label">
            <view class="status-dot" :style="{ background: s.color }"></view>
            <text class="status-name">{{ s.label }}</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 开始新测评 -->
    <view class="section">
      <text class="section-title">开始新测评</text>
      <view class="test-cards">
        <view class="test-card primary" @click="goTest('foundation')">
          <text class="test-card-title">功能测评推荐</text>
          <text class="test-card-desc">根据你的薄弱点智能组卷</text>
        </view>
        <view class="test-cards-row">
          <view class="test-card white" @click="goTest('chapter')">
            <text class="test-card-title">章节测试</text>
            <text class="test-card-desc">选择章节进行测试</text>
          </view>
          <view class="test-card white" @click="goTest('topic')">
            <text class="test-card-title">专题测试</text>
            <text class="test-card-desc">选择专题进行测试</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 测评记录 -->
    <view class="section">
      <text class="section-title">测评记录</text>
      <view class="tab-bar">
        <text v-for="t in tabs" :key="t" class="tab-item" :class="{ active: activeTab === t }" @click="activeTab = t">{{ t }}</text>
      </view>
      <view class="record-list">
        <view class="record-item" v-for="r in filteredRecords" :key="r.id" @click="goRecordDetail(r)">
          <view class="record-top">
            <text class="record-title">{{ r.title }}</text>
            <text class="record-score">{{ r.score }}分</text>
          </view>
          <view class="record-bottom">
            <text class="record-date">{{ r.date }}</text>
            <text class="record-conclusion">{{ r.conclusion }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- TabBar -->
    <TabBar current="assessment" />
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getKnowledgeMap } from '@/api/user.js'
import { getRecords } from '@/api/test.js'
import TabBar from '@/components/TabBar.vue'

const activeTab = ref('全部')
const tabs = ['全部', '功能测评', '章节测试', '专题测试']
const knowledgeModules = ref([])
const testRecords = ref([])

onMounted(async () => {
  try {
    const [modules, records] = await Promise.all([
      getKnowledgeMap().catch(() => []),
      getRecords().catch(() => [])
    ])
    knowledgeModules.value = modules
    testRecords.value = records
  } catch (e) {
    console.error('加载数据失败', e)
  }
})

const ringBg = computed(() => {
  const pct = knowledgeModules.value.length > 0
    ? Math.round(knowledgeModules.value.reduce((sum, m) => sum + m.percent, 0) / knowledgeModules.value.length)
    : 0
  return `conic-gradient(#4A7BF7 ${pct * 3.6}deg, #E8E8E8 ${pct * 3.6}deg)`
})

const statusList = [
  { label: '未检测', color: '#BDBDBD' },
  { label: '待确认', color: '#2196F3' },
  { label: '待补齐', color: '#FF9800' },
  { label: '已掌握', color: '#4CAF50' },
  { label: '暂不适用', color: '#E0E0E0' }
]

const filteredRecords = computed(() => {
  if (activeTab.value === '全部') return testRecords.value
  return testRecords.value.filter(r => r.type === activeTab.value)
})

function goTest(mode) {
  uni.navigateTo({ url: `/pages/test/index?mode=${mode}` })
}

function goBuy() {
  uni.showToast({ title: '购买次数', icon: 'none' })
}

// 测评记录点击 → 跳转报告页
function goRecordDetail(record) {
  uni.setStorageSync('reportData', record)
  uni.navigateTo({ url: '/pages/report/index' })
}

// 掌握地图模块点击 → showToast 显示详情
function onModuleClick(m) {
  uni.showToast({ title: `${m.name}：已掌握${m.mastered}个题型`, icon: 'none' })
}
</script>

<style scoped>
.page { min-height: 100vh; background: #F5F6FA; padding-bottom: 120rpx; }
.status-bar { height: var(--status-bar-height, 44rpx); }
.nav-bar { background: #fff; position: sticky; top: 0; z-index: 10; }
.nav-content { height: 88rpx; display: flex; align-items: center; justify-content: space-between; padding: 0 30rpx; }
.nav-title { font-size: 34rpx; font-weight: 600; color: #333; }
.nav-right { font-size: 26rpx; color: #4A7BF7; }

.progress-card { margin: 20rpx 24rpx; background: #fff; border-radius: 20rpx; padding: 30rpx; }
.progress-row { display: flex; align-items: center; }
.ring-wrap { display: flex; flex-direction: column; align-items: center; margin-right: 30rpx; }
.ring { width: 140rpx; height: 140rpx; border-radius: 50%; display: flex; align-items: center; justify-content: center; }
.ring-inner { width: 100rpx; height: 100rpx; border-radius: 50%; background: #fff; display: flex; align-items: center; justify-content: center; }
.ring-num { font-size: 32rpx; font-weight: 700; color: #4A7BF7; }
.progress-label { font-size: 22rpx; color: #999; margin-top: 10rpx; }
.stats-grid { flex: 1; display: grid; grid-template-columns: 1fr 1fr; gap: 16rpx; }
.stat-item { display: flex; flex-direction: column; align-items: center; }
.stat-num { font-size: 36rpx; font-weight: 700; }
.stat-label { font-size: 22rpx; color: #999; }
.green { color: #4CAF50; }
.orange { color: #FF9800; }
.blue { color: #2196F3; }
.gray { color: #BDBDBD; }

.ai-card { margin-top: 24rpx; background: linear-gradient(135deg, #EEF2FF, #F0F7FF); border-radius: 14rpx; padding: 20rpx 24rpx; }
.ai-title { font-size: 26rpx; font-weight: 600; color: #4A7BF7; display: block; }
.ai-desc { font-size: 22rpx; color: #666; margin-top: 6rpx; display: block; }

.section { margin: 24rpx 24rpx 0; }
.section-title { font-size: 30rpx; font-weight: 600; color: #333; margin-bottom: 20rpx; display: block; }

.module-list { background: #fff; border-radius: 20rpx; overflow: hidden; }
.module-row { display: flex; align-items: center; padding: 24rpx; border-bottom: 1rpx solid #F0F0F0; }
.module-row:last-child { border-bottom: none; }
.module-left { display: flex; align-items: center; min-width: 200rpx; }
.module-icon { width: 56rpx; height: 56rpx; border-radius: 14rpx; display: flex; align-items: center; justify-content: center; margin-right: 16rpx; }
.module-icon-text { color: #fff; font-size: 22rpx; font-weight: 600; }
.module-name { font-size: 26rpx; color: #333; }
.module-right { flex: 1; display: flex; align-items: center; gap: 12rpx; }
.module-pct { font-size: 26rpx; font-weight: 600; color: #333; min-width: 70rpx; text-align: right; }
.seg-bar { flex: 1; height: 12rpx; background: #F0F0F0; border-radius: 6rpx; display: flex; overflow: hidden; min-width: 80rpx; }
.seg { height: 100%; }
.module-tags { display: flex; gap: 6rpx; }
.tag { font-size: 18rpx; padding: 2rpx 8rpx; border-radius: 6rpx; min-width: 32rpx; text-align: center; }
.tag.green { background: #E8F5E9; color: #4CAF50; }
.tag.orange { background: #FFF3E0; color: #FF9800; }
.tag.blue { background: #E3F2FD; color: #2196F3; }
.tag.gray { background: #F5F5F5; color: #BDBDBD; }
.arrow { font-size: 28rpx; color: #CCC; margin-left: 4rpx; }

.status-scroll { white-space: nowrap; }
.status-list { display: flex; gap: 16rpx; }
.status-card { display: inline-flex; align-items: center; background: #fff; border-radius: 14rpx; padding: 16rpx 24rpx; gap: 10rpx; }
.status-dot { width: 16rpx; height: 16rpx; border-radius: 50%; }
.status-name { font-size: 24rpx; color: #666; }

.test-cards { display: flex; flex-direction: column; gap: 16rpx; }
.test-cards-row { display: flex; gap: 16rpx; }
.test-card { border-radius: 16rpx; padding: 28rpx 24rpx; flex: 1; }
.test-card.primary { background: linear-gradient(135deg, #4A7BF7, #6B9BFF); }
.test-card.primary .test-card-title, .test-card.primary .test-card-desc { color: #fff; }
.test-card.white { background: #fff; }
.test-card-title { font-size: 28rpx; font-weight: 600; color: #333; display: block; }
.test-card-desc { font-size: 22rpx; color: #999; margin-top: 8rpx; display: block; }

.tab-bar { display: flex; gap: 24rpx; margin-bottom: 20rpx; }
.tab-item { font-size: 26rpx; color: #999; padding-bottom: 8rpx; }
.tab-item.active { color: #4A7BF7; font-weight: 600; border-bottom: 4rpx solid #4A7BF7; }

.record-list { display: flex; flex-direction: column; gap: 16rpx; }
.record-item { background: #fff; border-radius: 16rpx; padding: 24rpx; }
.record-top { display: flex; justify-content: space-between; align-items: center; }
.record-title { font-size: 28rpx; color: #333; font-weight: 500; }
.record-score { font-size: 28rpx; font-weight: 700; color: #4A7BF7; }
.record-bottom { display: flex; justify-content: space-between; margin-top: 10rpx; }
.record-date { font-size: 22rpx; color: #999; }
.record-conclusion { font-size: 22rpx; color: #666; }

</style>
