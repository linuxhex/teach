<template>
  <view class="page">
    <!-- 顶部封面大图 -->
    <view class="cover" :style="{ background: coverGradient }">
      <view class="status-bar" :style="{ height: statusBarHeight + 'px' }"></view>
      <view class="cover-content">
        <text class="cover-icon">📘</text>
      </view>
    </view>

    <!-- 返回按钮 -->
    <view class="back-btn" :style="{ top: statusBarHeight + 10 + 'px' }" @click="goBack">
      <text class="back-icon">&lt;</text>
    </view>

    <!-- 资料信息 -->
    <view class="info-section">
      <text class="info-title">{{ material.title }}</text>
      <view class="info-tags">
        <view class="info-tag type-tag">
          <text class="info-tag-text">{{ material.type }}</text>
        </view>
        <view class="info-tag purpose-tag">
          <text class="info-tag-text">{{ material.purpose }}</text>
        </view>
      </view>

      <!-- 价格区域 -->
      <view class="price-row">
        <text class="price-current">¥{{ material.price }}</text>
        <text class="price-original">¥{{ material.originalPrice || (material.price * 1.5).toFixed(1) }}</text>
      </view>

      <!-- 匹配度 -->
      <view class="match-section">
        <view class="match-header">
          <text class="match-label">匹配度</text>
          <text class="match-value">{{ material.matchRate }}%</text>
        </view>
        <view class="match-bar-bg">
          <view class="match-bar-fill" :style="{ width: material.matchRate + '%' }"></view>
        </view>
      </view>
    </view>

    <!-- 分割线 -->
    <view class="divider"></view>

    <!-- 资料简介 -->
    <view class="detail-section">
      <text class="detail-title">资料简介</text>
      <text class="detail-text">{{ material.desc }}</text>
    </view>

    <!-- 适合人群 -->
    <view class="detail-section">
      <text class="detail-title">适合人群</text>
      <text class="detail-text">{{ material.audience }}</text>
    </view>

    <!-- 包含知识点 -->
    <view class="detail-section">
      <text class="detail-title">包含知识点</text>
      <view class="knowledge-tags">
        <text v-for="tag in material.tags" :key="tag" class="knowledge-tag">{{ tag }}</text>
      </view>
    </view>

    <!-- 底部占位 -->
    <view class="bottom-placeholder"></view>

    <!-- 底部固定操作栏 -->
    <view class="bottom-bar">
      <view class="fav-btn" @click="toggleFav">
        <text class="fav-icon">{{ isFav ? '♥' : '♡' }}</text>
      </view>
      <view class="download-btn" @click="onDownload">
        <text class="download-btn-text">立即下载</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { materials } from '@/data/mock.js'

const statusBarHeight = ref(20)
const isFav = ref(false)

uni.getSystemInfo({
  success: (res) => {
    statusBarHeight.value = res.statusBarHeight || 20
  }
})

// 从 mock 中查找资料，找不到则用默认数据
const material = ref({
  id: 0,
  title: '资料详情',
  type: '讲解类',
  purpose: '章节体系',
  desc: '暂无描述',
  audience: '所有学生',
  tags: [],
  price: 0,
  originalPrice: 0,
  matchRate: 0
})

// 渐变色
const coverGradient = ref('linear-gradient(135deg, #4A7BF7, #6B9BFF)')

function initMaterial(id) {
  const found = materials.find(m => m.id === Number(id))
  if (found) {
    material.value = { ...found, originalPrice: found.originalPrice || (found.price * 1.5).toFixed(1) }
    const colorMap = {
      '讲解类': 'linear-gradient(135deg, #4A7BF7, #6B9BFF)',
      '刷题类': 'linear-gradient(135deg, #34C759, #5DD97A)',
      '功能类': 'linear-gradient(135deg, #FF9500, #FFB340)'
    }
    coverGradient.value = colorMap[found.type] || 'linear-gradient(135deg, #4A7BF7, #6B9BFF)'
  }
}

function goBack() {
  uni.navigateBack()
}

function onDownload() {
  uni.showToast({ title: '下载功能开发中', icon: 'none' })
}

function toggleFav() {
  isFav.value = !isFav.value
  uni.showToast({ title: isFav.value ? '已收藏' : '已取消收藏', icon: 'none' })
}

// 页面加载
onLoad((options) => {
  if (options && options.id) {
    initMaterial(options.id)
  }
})
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #F5F7FA;
  position: relative;
}

/* 封面 */
.cover {
  width: 100%;
  height: 360rpx;
  display: flex;
  flex-direction: column;
}

.cover-content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cover-icon {
  font-size: 100rpx;
}

/* 返回按钮 */
.back-btn {
  position: absolute;
  left: 24rpx;
  width: 60rpx;
  height: 60rpx;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.back-icon {
  font-size: 36rpx;
  color: #ffffff;
  font-weight: bold;
}

/* 资料信息 */
.info-section {
  background: #ffffff;
  margin: -40rpx 24rpx 0;
  border-radius: 20rpx;
  padding: 30rpx;
  position: relative;
  z-index: 2;
}

.info-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333333;
  display: block;
  margin-bottom: 16rpx;
}

.info-tags {
  display: flex;
  gap: 16rpx;
  margin-bottom: 20rpx;
}

.info-tag {
  padding: 6rpx 20rpx;
  border-radius: 8rpx;
}

.type-tag {
  background: rgba(74, 123, 247, 0.12);
}

.type-tag .info-tag-text {
  font-size: 22rpx;
  color: #4A7BF7;
}

.purpose-tag {
  background: #F5F7FA;
}

.purpose-tag .info-tag-text {
  font-size: 22rpx;
  color: #666666;
}

/* 价格 */
.price-row {
  display: flex;
  align-items: baseline;
  gap: 16rpx;
  margin-bottom: 24rpx;
}

.price-current {
  font-size: 44rpx;
  font-weight: bold;
  color: #FF3B30;
}

.price-original {
  font-size: 26rpx;
  color: #CCCCCC;
  text-decoration: line-through;
}

/* 匹配度 */
.match-section {
  margin-top: 8rpx;
}

.match-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12rpx;
}

.match-label {
  font-size: 24rpx;
  color: #999999;
}

.match-value {
  font-size: 24rpx;
  color: #4A7BF7;
  font-weight: 600;
}

.match-bar-bg {
  height: 14rpx;
  background: #F0F0F0;
  border-radius: 7rpx;
  overflow: hidden;
}

.match-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #4A7BF7, #6B9BFF);
  border-radius: 7rpx;
}

/* 分割线 */
.divider {
  height: 1rpx;
  background: #F0F0F0;
  margin: 24rpx 24rpx 0;
}

/* 详情区块 */
.detail-section {
  background: #ffffff;
  margin: 24rpx 24rpx 0;
  border-radius: 16rpx;
  padding: 30rpx;
}

.detail-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333333;
  display: block;
  margin-bottom: 16rpx;
}

.detail-text {
  font-size: 26rpx;
  color: #666666;
  line-height: 1.7;
  display: block;
}

/* 知识点标签 */
.knowledge-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.knowledge-tag {
  font-size: 24rpx;
  color: #4A7BF7;
  background: rgba(74, 123, 247, 0.08);
  padding: 10rpx 24rpx;
  border-radius: 20rpx;
}

/* 底部占位 */
.bottom-placeholder {
  height: 160rpx;
}

/* 底部操作栏 */
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #ffffff;
  display: flex;
  align-items: center;
  padding: 16rpx 30rpx;
  padding-bottom: calc(16rpx + env(safe-area-inset-bottom));
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
  gap: 20rpx;
}

.fav-btn {
  width: 80rpx;
  height: 80rpx;
  border: 2rpx solid #E8E8E8;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.fav-icon {
  font-size: 40rpx;
  color: #FF3B30;
}

.download-btn {
  flex: 1;
  height: 80rpx;
  background: linear-gradient(135deg, #4A7BF7, #6B9BFF);
  border-radius: 40rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.download-btn-text {
  font-size: 30rpx;
  color: #ffffff;
  font-weight: bold;
}
</style>
