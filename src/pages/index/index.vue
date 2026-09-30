<template>
  <view class="page">
    <!-- 1. 顶部状态栏占位 -->
    <view class="status-bar" :style="{ height: statusBarHeight + 'px' }"></view>

    <!-- 2. 顶部导航栏 -->
    <view class="nav-bar">
      <view class="nav-left">
        <text class="nav-greeting">Hi, {{ userInfo.name }}</text>
        <view class="nav-grade" @click="onGradePick">
          <text class="nav-grade-text">{{ userInfo.grade }} ▾</text>
        </view>
      </view>
      <view class="nav-right">
        <text class="nav-bell" @click="onBell">🔔</text>
        <view class="nav-admin-btn" @click="goAdmin">
          <text class="nav-admin-text">管理</text>
        </view>
      </view>
    </view>

    <!-- 可滚动内容区 -->
    <scroll-view scroll-y class="scroll-content">
      <!-- 3. AI 诊断 Banner -->
      <view class="ai-banner">
        <view class="ai-banner-content">
          <text class="ai-banner-title">先知道哪里不会，再决定怎么学</text>
          <text class="ai-banner-subtitle">AI 智能诊断，精准定位薄弱环节</text>
          <view class="ai-banner-btn" @click="goAssessment">
            <text class="ai-banner-btn-text">开始测评</text>
          </view>
        </view>
        <view class="ai-banner-tags">
          <text class="ai-tag">精准诊断</text>
          <text class="ai-tag">个性推荐</text>
          <text class="ai-tag">高效提分</text>
          <text class="ai-tag">科学规划</text>
        </view>
      </view>

      <!-- 快捷入口 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">快捷入口</text>
        </view>
        <view class="quick-access">
          <view class="quick-item" @click="goTest('foundation')">
            <text class="quick-icon">📝</text>
            <text class="quick-label">功底测评</text>
          </view>
          <view class="quick-item" @click="goTest('chapter')">
            <text class="quick-icon">📖</text>
            <text class="quick-label">章节练习</text>
          </view>
          <view class="quick-item" @click="goTest('topic')">
            <text class="quick-icon">🎯</text>
            <text class="quick-label">专题突破</text>
          </view>
          <view class="quick-item" @click="goResources">
            <text class="quick-icon">📚</text>
            <text class="quick-label">资料库</text>
          </view>
          <view class="quick-item" @click="goReport">
            <text class="quick-icon"></text>
            <text class="quick-label">学习报告</text>
          </view>
          <view class="quick-item" @click="goAdmin">
            <text class="quick-icon">️</text>
            <text class="quick-label">管理后台</text>
          </view>
        </view>
      </view>

      <!-- 4. 测评入口 -->
      <view class="section">
        <view class="test-entries">
          <view class="test-card test-card-blue" @click="goTest('foundation')">
            <view class="test-card-badge" v-if="true">
              <text class="test-card-badge-text">推荐</text>
            </view>
            <text class="test-card-icon">📐</text>
            <text class="test-card-title">高中数学功底测</text>
            <text class="test-card-desc">全面检测知识掌握</text>
          </view>
          <view class="test-card test-card-light-blue" @click="goTest('chapter')">
            <text class="test-card-icon">📖</text>
            <text class="test-card-title">章节测试</text>
            <text class="test-card-desc">按章节专项练习</text>
          </view>
          <view class="test-card test-card-green" @click="goTest('topic')">
            <text class="test-card-icon">🎯</text>
            <text class="test-card-title">专题测试</text>
            <text class="test-card-desc">重难点专题突破</text>
          </view>
        </view>
      </view>

      <!-- 5. 最近一次测评 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">最近一次测评</text>
        </view>
        <view class="recent-test-card" @click="goReport">
          <view class="recent-test-left">
            <text class="recent-test-name">{{ latestTest.title }}</text>
            <text class="recent-test-date">{{ latestTest.date }}</text>
            <text class="recent-test-conclusion">{{ latestTest.conclusion }}</text>
          </view>
          <view class="recent-test-right">
            <view class="ring-progress" :style="ringStyle(latestTest.mastered)">
              <view class="ring-inner">
                <text class="ring-percent">{{ latestTest.mastered }}%</text>
              </view>
            </view>
          </view>
          <view class="recent-test-footer">
            <text class="recent-test-stat mastered">已掌握 {{ latestTest.mastered }}%</text>
            <text class="recent-test-stat gap">距离目标还差 {{ 100 - latestTest.mastered }}%</text>
          </view>
        </view>
      </view>

      <!-- 6. 学习建议 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">学习建议</text>
        </view>
        <view class="suggestion-list">
          <view v-for="item in studySuggestions" :key="item.id" class="suggestion-card" @click="goSuggestion">
            <view class="suggestion-tag" :style="{ backgroundColor: item.tagColor }">
              <text class="suggestion-tag-text">{{ item.tag }}</text>
            </view>
            <text class="suggestion-title">{{ item.title }}</text>
          </view>
        </view>
      </view>

      <!-- 7. 推荐资料 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">推荐资料</text>
          <text class="section-more" @click="goResources">查看更多</text>
        </view>
        <view class="material-list">
          <view v-for="item in recommendedMaterials" :key="item.id" class="material-card" @click="goMaterialDetail(item)">
            <view class="material-cover" :style="{ backgroundColor: item.id === 1 ? '#4A7BF7' : '#34C759' }">
              <text class="material-cover-icon">📘</text>
            </view>
            <view class="material-info">
              <view class="material-top">
                <text class="material-title">{{ item.title }}</text>
                <view class="material-type-tag">
                  <text class="material-type-text">{{ item.typeTag }}</text>
                </view>
              </view>
              <text class="material-desc">{{ item.desc }}</text>
              <view class="material-tags">
                <text v-for="tag in item.tags" :key="tag" class="material-knowledge-tag">{{ tag }}</text>
              </view>
              <view class="material-bottom">
                <view class="material-match">
                  <text class="material-match-label">匹配度</text>
                  <view class="match-bar-bg">
                    <view class="match-bar-fill" :style="{ width: item.matchRate + '%' }"></view>
                  </view>
                  <text class="material-match-value">{{ item.matchRate }}%</text>
                </view>
                <view class="material-price-row">
                  <text class="material-price">¥{{ item.price }}</text>
                  <text class="material-original-price">¥{{ item.originalPrice }}</text>
                  <view class="material-cart-btn">
                    <text class="material-cart-icon">🛒</text>
                  </view>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 8. AI 诊断引擎说明条 -->
      <view class="engine-bar">
        <text class="engine-text">💡 AI 诊断引擎基于你的答题数据，智能分析知识掌握程度</text>
      </view>

      <!-- 底部占位，给 TabBar 留空间 -->
      <view class="tabbar-placeholder"></view>
    </scroll-view>

    <!-- 9. TabBar -->
    <TabBar current="index" />
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { userInfo, testRecords, studySuggestions, recommendedMaterials } from '@/data/mock.js'
import TabBar from '@/components/TabBar.vue'

const statusBarHeight = ref(20)

// 获取系统状态栏高度
uni.getSystemInfo({
  success: (res) => {
    statusBarHeight.value = res.statusBarHeight || 20
  }
})

// 最近一次测评
const latestTest = computed(() => testRecords[0])

// 环形进度样式
function ringStyle(percent) {
  const deg = percent * 3.6
  return {
    background: `conic-gradient(#4A7BF7 ${deg}deg, #E8E8E8 ${deg}deg)`
  }
}

// 跳转测评
function goAssessment() {
  uni.navigateTo({ url: '/pages/assessment/index' })
}

function goTest(mode) {
  uni.navigateTo({ url: `/pages/test/index?mode=${mode}` })
}

// 查看更多 → 资料库
function goResources() {
  uni.navigateTo({ url: '/pages/resources/index' })
}

// 推荐资料卡片 → 资料详情
function goMaterialDetail(item) {
  uni.navigateTo({ url: `/pages/resources/detail/index?id=${item.id}` })
}

// 学习建议卡片 → 功底测评
function goSuggestion() {
  uni.navigateTo({ url: '/pages/test/index?mode=foundation' })
}

// 最近测评卡片 → 报告页
function goReport() {
  const record = latestTest.value
  uni.setStorageSync('reportData', record)
  uni.navigateTo({ url: '/pages/report/index' })
}

// 通知铃铛
function onBell() {
  uni.showToast({ title: '暂无新通知', icon: 'none' })
}

// 跳转管理后台
function goAdmin() {
  uni.navigateTo({ url: '/pages/admin/index' })
}

// 年级选择器
function onGradePick() {
  uni.showActionSheet({
    itemList: ['高一', '高二', '高三'],
    success: (res) => {
      const grades = ['高一', '高二', '高三']
      userInfo.grade = grades[res.tapIndex]
    }
  })
}
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #F5F7FA;
}

/* 1. 状态栏 */
.status-bar {
  width: 100%;
  background-color: #ffffff;
}

/* 2. 导航栏 */
.nav-bar {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 30rpx;
  background-color: #ffffff;
}

.nav-left {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.nav-greeting {
  font-size: 34rpx;
  font-weight: bold;
  color: #333333;
  margin-right: 16rpx;
}

.nav-grade {
  padding: 6rpx 16rpx;
  background-color: #F0F4FF;
  border-radius: 20rpx;
}

.nav-grade-text {
  font-size: 24rpx;
  color: #4A7BF7;
}

.nav-right {
  display: flex;
  align-items: center;
}

.nav-bell {
  font-size: 40rpx;
  margin-right: 16rpx;
}

.nav-admin-btn {
  padding: 6rpx 16rpx;
  background-color: #4A7BF7;
  border-radius: 20rpx;
}

.nav-admin-text {
  font-size: 24rpx;
  color: #ffffff;
}

/* 滚动内容 */
.scroll-content {
  flex: 1;
  height: 0;
}

/* 3. AI Banner */
.ai-banner {
  margin: 20rpx 24rpx 0;
  border-radius: 20rpx;
  background: linear-gradient(135deg, #4A7BF7, #6B9BFF);
  padding: 40rpx 36rpx 30rpx;
  overflow: hidden;
}

.ai-banner-content {
  display: flex;
  flex-direction: column;
}

.ai-banner-title {
  font-size: 34rpx;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 12rpx;
}

.ai-banner-subtitle {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.85);
  margin-bottom: 30rpx;
}

.ai-banner-btn {
  width: 220rpx;
  height: 64rpx;
  background-color: #ffffff;
  border-radius: 32rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ai-banner-btn-text {
  font-size: 28rpx;
  color: #4A7BF7;
  font-weight: bold;
}

.ai-banner-tags {
  display: flex;
  flex-direction: row;
  margin-top: 30rpx;
  gap: 16rpx;
}

.ai-tag {
  font-size: 20rpx;
  color: rgba(255, 255, 255, 0.9);
  background-color: rgba(255, 255, 255, 0.2);
  padding: 6rpx 16rpx;
  border-radius: 16rpx;
}

/* 通用 section */
.section {
  margin-top: 30rpx;
  padding: 0 24rpx;
}

.section-header {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333333;
}

.section-more {
  font-size: 24rpx;
  color: #999999;
}

/* 4. 测评入口 */
.test-entries {
  display: flex;
  flex-direction: row;
  gap: 16rpx;
}

.test-card {
  flex: 1;
  border-radius: 16rpx;
  padding: 24rpx 16rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.test-card-blue {
  background-color: #E8F0FF;
}

.test-card-light-blue {
  background-color: #EDF5FF;
}

.test-card-green {
  background-color: #E8F8EE;
}

.test-card-badge {
  position: absolute;
  top: 0;
  right: 0;
  background-color: #FF9500;
  padding: 4rpx 12rpx;
  border-radius: 0 16rpx 0 12rpx;
}

.test-card-badge-text {
  font-size: 18rpx;
  color: #ffffff;
}

.test-card-icon {
  font-size: 48rpx;
  margin-bottom: 12rpx;
}

.test-card-title {
  font-size: 24rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 6rpx;
  text-align: center;
}

.test-card-desc {
  font-size: 18rpx;
  color: #999999;
  text-align: center;
}

/* 5. 最近测评 */
.recent-test-card {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 30rpx;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-items: center;
}

.recent-test-left {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.recent-test-name {
  font-size: 28rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 8rpx;
}

.recent-test-date {
  font-size: 22rpx;
  color: #999999;
  margin-bottom: 12rpx;
}

.recent-test-conclusion {
  font-size: 22rpx;
  color: #666666;
  line-height: 1.5;
}

.recent-test-right {
  margin-left: 20rpx;
}

.ring-progress {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ring-inner {
  width: 88rpx;
  height: 88rpx;
  border-radius: 50%;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ring-percent {
  font-size: 28rpx;
  font-weight: bold;
  color: #4A7BF7;
}

.recent-test-footer {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-top: 20rpx;
  padding-top: 20rpx;
  border-top: 1rpx solid #F0F0F0;
}

.recent-test-stat {
  font-size: 24rpx;
}

.recent-test-stat.mastered {
  color: #4A7BF7;
}

.recent-test-stat.gap {
  color: #FF9500;
}

/* 6. 学习建议 */
.suggestion-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.suggestion-card {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 24rpx 30rpx;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.suggestion-tag {
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  margin-right: 20rpx;
}

.suggestion-tag-text {
  font-size: 20rpx;
  color: #ffffff;
}

.suggestion-title {
  font-size: 26rpx;
  color: #333333;
  font-weight: 500;
}

/* 7. 推荐资料 */
.material-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.material-card {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 24rpx;
  display: flex;
  flex-direction: row;
}

.material-cover {
  width: 140rpx;
  height: 180rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
  flex-shrink: 0;
}

.material-cover-icon {
  font-size: 56rpx;
}

.material-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.material-top {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 8rpx;
}

.material-title {
  font-size: 26rpx;
  font-weight: bold;
  color: #333333;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.material-type-tag {
  background-color: #F0F4FF;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
  margin-left: 12rpx;
  flex-shrink: 0;
}

.material-type-text {
  font-size: 18rpx;
  color: #4A7BF7;
}

.material-desc {
  font-size: 22rpx;
  color: #999999;
  margin-bottom: 10rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.material-tags {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 8rpx;
  margin-bottom: 12rpx;
}

.material-knowledge-tag {
  font-size: 18rpx;
  color: #666666;
  background-color: #F5F5F5;
  padding: 4rpx 12rpx;
  border-radius: 6rpx;
}

.material-bottom {
  display: flex;
  flex-direction: column;
}

.material-match {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 10rpx;
}

.material-match-label {
  font-size: 20rpx;
  color: #999999;
  margin-right: 10rpx;
  flex-shrink: 0;
}

.match-bar-bg {
  flex: 1;
  height: 10rpx;
  background-color: #F0F0F0;
  border-radius: 5rpx;
  overflow: hidden;
}

.match-bar-fill {
  height: 100%;
  background-color: #4A7BF7;
  border-radius: 5rpx;
}

.material-match-value {
  font-size: 20rpx;
  color: #4A7BF7;
  margin-left: 10rpx;
  flex-shrink: 0;
}

.material-price-row {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.material-price {
  font-size: 30rpx;
  font-weight: bold;
  color: #FF3B30;
}

.material-original-price {
  font-size: 20rpx;
  color: #CCCCCC;
  text-decoration: line-through;
  margin-left: 10rpx;
}

.material-cart-btn {
  margin-left: auto;
  width: 56rpx;
  height: 56rpx;
  background-color: #4A7BF7;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.material-cart-icon {
  font-size: 28rpx;
}

/* 8. AI 引擎说明条 */
.engine-bar {
  margin: 30rpx 24rpx 0;
  background-color: #F0F0F0;
  border-radius: 12rpx;
  padding: 20rpx 24rpx;
}

.engine-text {
  font-size: 22rpx;
  color: #999999;
}

/* 底部占位 */
.tabbar-placeholder {
  height: 160rpx;
}
</style>
