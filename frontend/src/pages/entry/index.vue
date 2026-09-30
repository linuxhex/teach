<template>
  <view class="entry-page">
    <!-- 顶部用户信息 -->
    <view class="user-header">
      <view class="avatar-circle">
        <text class="avatar-text">{{ userName.charAt(0) }}</text>
      </view>
      <view class="user-info">
        <text class="welcome-text">欢迎回来，{{ userName }}</text>
        <view class="role-tag">
          <text class="role-tag-text">{{ userRole === 'admin' ? '管理员' : '学生' }}</text>
        </view>
      </view>
    </view>

    <!-- 入口卡片 -->
    <view class="entry-cards">
      <view class="entry-card" @tap="goStudent">
        <text class="card-emoji">&#x1F393;</text>
        <text class="card-title">学生端</text>
        <text class="card-desc">开始测评、查看报告、浏览资料</text>
      </view>

      <view class="entry-card" @tap="goAdmin">
        <text class="card-emoji">&#x1F3E2;</text>
        <text class="card-title">管理后台</text>
        <text class="card-desc">知识点管理、模板配置、数据统计</text>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-section">
      <view class="btn-logout" @tap="handleLogout">
        <text class="logout-text">退出登录</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const userName = ref('用户')
const userRole = ref('student')

onMounted(() => {
  userName.value = uni.getStorageSync('userName') || '用户'
  userRole.value = uni.getStorageSync('userRole') || 'student'
})

function goStudent() {
  uni.reLaunch({ url: '/pages/index/index' })
}

function goAdmin() {
  if (userRole.value === 'admin') {
    uni.reLaunch({ url: '/pages/admin/index' })
  } else {
    uni.showToast({ title: '仅管理员可访问', icon: 'none' })
  }
}

function handleLogout() {
  uni.removeStorageSync('isLoggedIn')
  uni.removeStorageSync('userRole')
  uni.removeStorageSync('userName')
  uni.reLaunch({ url: '/pages/login/index' })
}
</script>

<style scoped>
.entry-page {
  min-height: 100vh;
  background: #F5F7FA;
  padding: 0 40rpx;
}

.user-header {
  display: flex;
  align-items: center;
  padding: 60rpx 20rpx 40rpx;
}

.avatar-circle {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #4A7BF7, #6C5CE7);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.avatar-text {
  color: #FFFFFF;
  font-size: 40rpx;
  font-weight: bold;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.welcome-text {
  font-size: 34rpx;
  font-weight: bold;
  color: #1E2A3A;
  margin-bottom: 8rpx;
}

.role-tag {
  background: rgba(74, 123, 247, 0.1);
  border-radius: 20rpx;
  padding: 4rpx 20rpx;
  display: inline-flex;
}

.role-tag-text {
  font-size: 22rpx;
  color: #4A7BF7;
}

.entry-cards {
  margin-top: 40rpx;
}

.entry-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 50rpx 40rpx;
  margin-bottom: 30rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
}

.card-emoji {
  font-size: 72rpx;
  margin-bottom: 20rpx;
}

.card-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #1E2A3A;
  margin-bottom: 12rpx;
}

.card-desc {
  font-size: 26rpx;
  color: #8E99A4;
}

.logout-section {
  margin-top: 80rpx;
  display: flex;
  justify-content: center;
}

.btn-logout {
  width: 400rpx;
  height: 88rpx;
  border-radius: 44rpx;
  border: 2rpx solid #E0E4E8;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logout-text {
  font-size: 30rpx;
  color: #8E99A4;
}
</style>
