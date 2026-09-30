<template>
  <view class="register-page">
    <text class="page-title">注册新账号</text>

    <!-- 角色选择 -->
    <view class="role-section">
      <text class="section-label">选择角色</text>
      <view class="role-cards">
        <view
          class="role-card"
          :class="{ active: role === 'student' }"
          @tap="role = 'student'"
        >
          <text class="role-emoji">&#x1F393;</text>
          <text class="role-name">学生</text>
        </view>
        <view
          class="role-card"
          :class="{ active: role === 'admin' }"
          @tap="role = 'admin'"
        >
          <text class="role-emoji">&#x1F468;&#x200D;&#x1F4BC;</text>
          <text class="role-name">管理员</text>
        </view>
      </view>
    </view>

    <!-- 表单 -->
    <view class="form-section">
      <view class="input-group">
        <input class="input-field" v-model="name" placeholder="请输入姓名" />
      </view>
      <view class="input-group">
        <input class="input-field" type="number" v-model="phone" placeholder="请输入手机号" maxlength="11" />
      </view>
      <view class="input-group">
        <input class="input-field" :password="true" v-model="password" placeholder="请输入密码（至少6位）" />
      </view>
      <view class="input-group">
        <input class="input-field" :password="true" v-model="confirmPassword" placeholder="请确认密码" />
      </view>

      <view class="btn-register" @tap="handleRegister">
        <text class="btn-text">注册</text>
      </view>
    </view>

    <view class="footer-section">
      <text class="login-link" @tap="goLogin">已有账号？返回登录</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

const role = ref('student')
const name = ref('')
const phone = ref('')
const password = ref('')
const confirmPassword = ref('')

function handleRegister() {
  if (!name.value.trim()) {
    uni.showToast({ title: '请输入姓名', icon: 'none' })
    return
  }
  if (phone.value.length !== 11) {
    uni.showToast({ title: '手机号须为11位', icon: 'none' })
    return
  }
  if (password.value.length < 6) {
    uni.showToast({ title: '密码至少6位', icon: 'none' })
    return
  }
  if (password.value !== confirmPassword.value) {
    uni.showToast({ title: '两次密码不一致', icon: 'none' })
    return
  }

  uni.showToast({ title: '注册成功', icon: 'success' })
  setTimeout(() => {
    uni.navigateBack()
  }, 1500)
}

function goLogin() {
  uni.navigateBack()
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background: #F5F7FA;
  padding: 0 60rpx;
}

.page-title {
  display: block;
  font-size: 44rpx;
  font-weight: bold;
  color: #1E2A3A;
  margin-top: 120rpx;
  margin-bottom: 50rpx;
}

.role-section {
  margin-bottom: 40rpx;
}

.section-label {
  font-size: 28rpx;
  color: #8E99A4;
  margin-bottom: 20rpx;
  display: block;
}

.role-cards {
  display: flex;
  gap: 30rpx;
}

.role-card {
  flex: 1;
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  border: 3rpx solid transparent;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
}

.role-card.active {
  border-color: #4A7BF7;
}

.role-emoji {
  font-size: 56rpx;
  margin-bottom: 12rpx;
}

.role-name {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
}

.form-section {
  margin-top: 20rpx;
}

.input-group {
  margin-bottom: 24rpx;
}

.input-field {
  background: #FFFFFF;
  border-radius: 16rpx;
  height: 100rpx;
  padding: 0 30rpx;
  font-size: 30rpx;
  color: #333333;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
}

.btn-register {
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
  justify-content: center;
  margin-top: 60rpx;
}

.login-link {
  font-size: 28rpx;
  color: #4A7BF7;
}
</style>
