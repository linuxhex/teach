<template>
  <view class="page">
    <!-- 顶部用户信息区 -->
    <view class="header">
      <view class="status-bar"></view>
      <view class="user-area">
        <view class="avatar">
          <text class="avatar-text">{{ userInfo.name.charAt(0) }}</text>
        </view>
        <view class="user-info">
          <text class="user-name">{{ userInfo.name }}</text>
          <text class="user-grade">{{ userInfo.grade }}</text>
        </view>
        <text class="user-target">目标：{{ userInfo.targetScore }}分</text>
      </view>
    </view>

    <!-- 学习统计 -->
    <view class="stats-card">
      <view class="stats-col">
        <text class="stats-num">{{ userInfo.testCount }}</text>
        <text class="stats-label">测评次数</text>
      </view>
      <view class="stats-col">
        <text class="stats-num">{{ userInfo.masteredTypes }}</text>
        <text class="stats-label">掌握题型</text>
      </view>
      <view class="stats-col">
        <text class="stats-num">{{ userInfo.studyDays }}</text>
        <text class="stats-label">学习天数</text>
      </view>
    </view>

    <!-- 功能入口 -->
    <view class="menu-list">
      <view class="menu-item" v-for="item in menuList" :key="item.title" @click="handleMenu(item)">
        <view class="menu-left">
          <text class="menu-icon">{{ item.icon }}</text>
          <text class="menu-title">{{ item.title }}</text>
        </view>
        <text class="menu-arrow">></text>
      </view>
    </view>

    <!-- 版本信息 -->
    <view class="version">
      <text class="version-text">v1.0.0</text>
    </view>

    <!-- TabBar -->
    <TabBar current="profile" />
  </view>
</template>

<script setup>
import { userInfo } from '@/data/mock.js'
import TabBar from '@/components/TabBar.vue'

const menuList = [
  { title: '我的测评', icon: '📋', action: 'navigate', url: '/pages/assessment/index' },
  { title: '我的资料', icon: '📁', action: 'navigate', url: '/pages/resources/index' },
  { title: '学习报告', icon: '📊', action: 'navigate', url: '/pages/profile/report/index' },
  { title: '设置', icon: '⚙️', action: 'navigate', url: '/pages/profile/settings/index' }
]

function handleMenu(item) {
  if (item.action === 'navigate') {
    uni.navigateTo({ url: item.url })
  } else {
    uni.showToast({ title: '功能开发中', icon: 'none' })
  }
}
</script>

<style scoped>
.page { min-height: 100vh; background: #F5F6FA; padding-bottom: 120rpx; }
.status-bar { height: var(--status-bar-height, 44rpx); }

.header { background: linear-gradient(135deg, #4A7BF7, #6B9BFF); padding-bottom: 40rpx; border-radius: 0 0 30rpx 30rpx; }
.user-area { display: flex; align-items: center; padding: 30rpx; }
.avatar { width: 100rpx; height: 100rpx; border-radius: 50%; background: rgba(255,255,255,0.3); display: flex; align-items: center; justify-content: center; margin-right: 20rpx; }
.avatar-text { font-size: 40rpx; color: #fff; font-weight: 600; }
.user-info { flex: 1; }
.user-name { font-size: 34rpx; font-weight: 600; color: #fff; display: block; }
.user-grade { font-size: 24rpx; color: rgba(255,255,255,0.8); margin-top: 6rpx; display: block; }
.user-target { font-size: 26rpx; color: rgba(255,255,255,0.9); background: rgba(255,255,255,0.2); padding: 10rpx 20rpx; border-radius: 20rpx; }

.stats-card { margin: -20rpx 24rpx 0; background: #fff; border-radius: 20rpx; padding: 30rpx 0; display: flex; position: relative; z-index: 2; box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.06); }
.stats-col { flex: 1; display: flex; flex-direction: column; align-items: center; }
.stats-num { font-size: 40rpx; font-weight: 700; color: #333; }
.stats-label { font-size: 22rpx; color: #999; margin-top: 8rpx; }

.menu-list { margin: 30rpx 24rpx 0; background: #fff; border-radius: 20rpx; overflow: hidden; }
.menu-item { display: flex; align-items: center; justify-content: space-between; padding: 30rpx 24rpx; border-bottom: 1rpx solid #F0F0F0; }
.menu-item:last-child { border-bottom: none; }
.menu-left { display: flex; align-items: center; gap: 16rpx; }
.menu-icon { font-size: 36rpx; }
.menu-title { font-size: 28rpx; color: #333; }
.menu-arrow { font-size: 28rpx; color: #CCC; }

.version { text-align: center; margin-top: 60rpx; }
.version-text { font-size: 22rpx; color: #CCC; }

</style>
