<template>
  <view class="page">
    <!-- 顶部导航 -->
    <view class="nav-bar">
      <view class="status-bar" :style="{ height: statusBarHeight + 'px' }"></view>
      <view class="nav-content">
        <view class="back-btn" @click="goBack">
          <text class="back-icon">&lt;</text>
        </view>
        <text class="nav-title">设置</text>
        <view class="nav-placeholder"></view>
      </view>
    </view>

    <!-- 设置列表 -->
    <view class="setting-list">
      <!-- 个人信息 -->
      <view class="setting-item" @click="onProfile">
        <view class="setting-left">
          <text class="setting-icon">👤</text>
          <text class="setting-label">个人信息</text>
        </view>
        <view class="setting-right">
          <text class="setting-value">{{ userInfo.name }} · {{ userInfo.grade }}</text>
          <text class="setting-arrow">></text>
        </view>
      </view>

      <!-- 学习目标分数 -->
      <view class="setting-item" @click="onTargetScore">
        <view class="setting-left">
          <text class="setting-icon">🎯</text>
          <text class="setting-label">学习目标分数</text>
        </view>
        <view class="setting-right">
          <text class="setting-value">{{ userInfo.targetScore }}分</text>
          <text class="setting-arrow">></text>
        </view>
      </view>

      <!-- 通知设置 -->
      <view class="setting-item" @click="onNotification">
        <view class="setting-left">
          <text class="setting-icon">🔔</text>
          <text class="setting-label">通知设置</text>
        </view>
        <view class="setting-right">
          <text class="setting-arrow">></text>
        </view>
      </view>

      <!-- 清除缓存 -->
      <view class="setting-item" @click="onClearCache">
        <view class="setting-left">
          <text class="setting-icon">🗑️</text>
          <text class="setting-label">清除缓存</text>
        </view>
        <view class="setting-right">
          <text class="setting-arrow">></text>
        </view>
      </view>

      <!-- 关于我们 -->
      <view class="setting-item" @click="onAbout">
        <view class="setting-left">
          <text class="setting-icon">ℹ️</text>
          <text class="setting-label">关于我们</text>
        </view>
        <view class="setting-right">
          <text class="setting-arrow">></text>
        </view>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-section">
      <view class="logout-btn" @click="onLogout">
        <text class="logout-text">退出登录</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { userInfo } from '@/data/mock.js'

const statusBarHeight = ref(20)

uni.getSystemInfo({
  success: (res) => {
    statusBarHeight.value = res.statusBarHeight || 20
  }
})

function goBack() {
  uni.navigateBack()
}

// 个人信息
function onProfile() {
  uni.showToast({ title: '修改功能开发中', icon: 'none' })
}

// 学习目标分数
function onTargetScore() {
  uni.showActionSheet({
    itemList: ['80', '90', '100', '110', '120', '130', '140', '150'],
    success: (res) => {
      const scores = [80, 90, 100, 110, 120, 130, 140, 150]
      userInfo.targetScore = scores[res.tapIndex]
      uni.showToast({ title: `目标已设为${scores[res.tapIndex]}分`, icon: 'none' })
    }
  })
}

// 通知设置
function onNotification() {
  uni.showToast({ title: '通知设置开发中', icon: 'none' })
}

// 清除缓存
function onClearCache() {
  uni.showModal({
    title: '提示',
    content: '确定要清除缓存吗？',
    success: (res) => {
      if (res.confirm) {
        uni.showToast({ title: '缓存已清除', icon: 'none' })
      }
    }
  })
}

// 关于我们
function onAbout() {
  uni.showToast({ title: '高中数学AI诊断测评系统 v1.0', icon: 'none' })
}

// 退出登录
function onLogout() {
  uni.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    success: (res) => {
      if (res.confirm) {
        uni.removeStorageSync('isLoggedIn')
        uni.reLaunch({ url: '/pages/login/index' })
      }
    }
  })
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #F5F7FA;
}

/* 导航栏 */
.nav-bar {
  background: #ffffff;
  position: sticky;
  top: 0;
  z-index: 10;
}

.nav-content {
  height: 88rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
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
  color: #333333;
  font-weight: bold;
}

.nav-title {
  font-size: 34rpx;
  font-weight: bold;
  color: #333333;
}

.nav-placeholder {
  width: 60rpx;
}

/* 设置列表 */
.setting-list {
  margin: 24rpx;
  background: #ffffff;
  border-radius: 20rpx;
  overflow: hidden;
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx 24rpx;
  border-bottom: 1rpx solid #F0F0F0;
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-left {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.setting-icon {
  font-size: 36rpx;
}

.setting-label {
  font-size: 28rpx;
  color: #333333;
}

.setting-right {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.setting-value {
  font-size: 26rpx;
  color: #999999;
}

.setting-arrow {
  font-size: 28rpx;
  color: #CCCCCC;
}

/* 退出登录 */
.logout-section {
  padding: 60rpx 24rpx 0;
}

.logout-btn {
  height: 88rpx;
  background: #ffffff;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logout-text {
  font-size: 30rpx;
  color: #FF3B30;
  font-weight: 500;
}
</style>
