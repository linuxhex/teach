<template>
  <view class="login-page">
    <!-- Logo 区域 -->
    <view class="logo-section">
      <view class="logo-circle">
        <text class="logo-icon">AI</text>
      </view>
      <text class="app-title">高中数学 AI 诊断</text>
      <text class="app-subtitle">智能测评，精准提分</text>
    </view>

    <!-- 表单区域 -->
    <view class="form-section">
      <view class="input-group">
        <view class="input-wrapper">
          <text class="input-icon">&#x1F4F1;</text>
          <input
            class="input-field"
            type="number"
            v-model="phone"
            placeholder="请输入手机号"
            maxlength="11"
          />
        </view>
      </view>

      <view class="input-group">
        <view class="input-wrapper">
          <text class="input-icon">&#x1F512;</text>
          <input
            class="input-field"
            :password="!showPassword"
            v-model="password"
            placeholder="请输入密码"
          />
          <text class="toggle-pwd" @tap="showPassword = !showPassword">
            {{ showPassword ? '隐藏' : '显示' }}
          </text>
        </view>
      </view>

      <view class="btn-login" @tap="handleLogin">
        <text class="btn-text">登录</text>
      </view>
    </view>

    <!-- 底部 -->
    <view class="footer-section">
      <text class="register-link" @tap="goRegister">还没有账号？立即注册</text>
      <text class="demo-tip">演示账号：学生 13800138000/123456 管理员 admin/admin</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { login } from '@/api/auth.js'

const phone = ref('')
const password = ref('')
const showPassword = ref(false)
const loading = ref(false)

async function handleLogin() {
  if (!phone.value || !password.value) {
    uni.showToast({ title: '请输入手机号和密码', icon: 'none' })
    return
  }

  loading.value = true
  try {
    const res = await login(phone.value, password.value)
    uni.setStorageSync('token', res.token)
    uni.setStorageSync('userId', res.userId)
    uni.setStorageSync('userRole', res.role)
    uni.setStorageSync('isLoggedIn', true)
    uni.setStorageSync('userName', res.name)
    uni.setStorageSync('userGrade', res.grade || '')
    uni.showToast({ title: '登录成功', icon: 'success' })
    setTimeout(() => {
      uni.reLaunch({ url: '/pages/entry/index' })
    }, 500)
  } catch (e) {
    uni.showToast({ title: e.message || '登录失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

function goRegister() {
  uni.navigateTo({ url: '/pages/register/index' })
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: #F5F7FA;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 60rpx;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 160rpx;
  margin-bottom: 80rpx;
}

.logo-circle {
  width: 140rpx;
  height: 140rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #4A7BF7, #6C5CE7);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30rpx;
}

.logo-icon {
  color: #FFFFFF;
  font-size: 48rpx;
  font-weight: bold;
}

.app-title {
  font-size: 40rpx;
  font-weight: bold;
  color: #1E2A3A;
  margin-bottom: 12rpx;
}

.app-subtitle {
  font-size: 28rpx;
  color: #8E99A4;
}

.form-section {
  width: 100%;
}

.input-group {
  margin-bottom: 30rpx;
}

.input-wrapper {
  display: flex;
  align-items: center;
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 0 30rpx;
  height: 100rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
}

.input-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
}

.input-field {
  flex: 1;
  height: 100rpx;
  font-size: 30rpx;
  color: #333333;
}

.toggle-pwd {
  font-size: 26rpx;
  color: #4A7BF7;
  padding-left: 20rpx;
}

.btn-login {
  width: 100%;
  height: 100rpx;
  border-radius: 16rpx;
  background: linear-gradient(135deg, #4A7BF7, #6C5CE7);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 50rpx;
}

.btn-text {
  color: #FFFFFF;
  font-size: 34rpx;
  font-weight: bold;
}

.footer-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 80rpx;
}

.register-link {
  font-size: 28rpx;
  color: #4A7BF7;
  margin-bottom: 20rpx;
}

.demo-tip {
  font-size: 22rpx;
  color: #B0B8C1;
}
</style>
