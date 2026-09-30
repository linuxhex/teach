<template>
  <view class="admin-page">
    <!-- 左侧菜单 -->
    <view class="sidebar">
      <view class="sidebar-logo">
        <view class="logo-dot"></view>
        <text class="logo-text">AI 诊断管理</text>
      </view>

      <view class="menu-list">
        <view
          class="menu-item"
          :class="{ active: activeMenu === 'overview' }"
          @tap="activeMenu = 'overview'"
        >
          <text class="menu-icon">&#x1F4CA;</text>
          <text class="menu-label">数据概览</text>
        </view>
        <view class="menu-item" @tap="goKnowledge">
          <text class="menu-icon">&#x1F4DA;</text>
          <text class="menu-label">知识点管理</text>
        </view>
        <view class="menu-item" @tap="goTemplate">
          <text class="menu-icon">&#x1F4CB;</text>
          <text class="menu-label">模板管理</text>
        </view>
        <view
          class="menu-item"
          :class="{ active: activeMenu === 'rules' }"
          @tap="activeMenu = 'rules'"
        >
          <text class="menu-icon">&#x1F4C8;</text>
          <text class="menu-label">诊断规则</text>
        </view>
        <view class="menu-item" @tap="showToast('系统设置')">
          <text class="menu-icon">&#x2699;&#xFE0F;</text>
          <text class="menu-label">系统设置</text>
        </view>
      </view>

      <view class="sidebar-bottom">
        <view class="menu-item" @tap="goStudent">
          <text class="menu-icon">&#x1F519;</text>
          <text class="menu-label">返回学生端</text>
        </view>
        <view class="menu-item" @tap="handleLogout">
          <text class="menu-icon">&#x1F6AA;</text>
          <text class="menu-label">退出登录</text>
        </view>
      </view>
    </view>

    <!-- 右侧内容区 -->
    <view class="content">
      <!-- 数据概览面板 -->
      <view v-if="activeMenu === 'overview'" class="panel">
        <text class="panel-title">数据概览</text>

        <!-- 统计卡片 -->
        <view class="stat-cards">
          <view class="stat-card">
            <text class="stat-value">{{ stats.questionCount }}</text>
            <text class="stat-label">题库总数</text>
          </view>
          <view class="stat-card">
            <text class="stat-value">{{ stats.testCount }}</text>
            <text class="stat-label">测评次数</text>
          </view>
          <view class="stat-card">
            <text class="stat-value">{{ stats.activeUsers }}</text>
            <text class="stat-label">活跃用户</text>
          </view>
          <view class="stat-card">
            <text class="stat-value">{{ stats.avgAccuracy }}%</text>
            <text class="stat-label">平均正确率</text>
          </view>
        </view>

        <!-- 最近测评记录 -->
        <view class="table-section">
          <text class="section-title">最近测评记录</text>
          <view class="table-header">
            <text class="th" style="flex:1">学生</text>
            <text class="th" style="flex:1">测评时间</text>
            <text class="th" style="flex:1">得分</text>
            <text class="th" style="flex:1">状态</text>
          </view>
          <view class="table-row" v-for="(item, idx) in recentRecords" :key="idx">
            <text class="td" style="flex:1">{{ item.name }}</text>
            <text class="td" style="flex:1">{{ item.time }}</text>
            <text class="td" style="flex:1">{{ item.score }}</text>
            <text class="td" style="flex:1">
              <text class="status-tag" :class="item.statusClass">{{ item.status }}</text>
            </text>
          </view>
        </view>

        <!-- 知识模块覆盖 -->
        <view class="module-section">
          <text class="section-title">知识模块覆盖情况</text>
          <view class="module-item" v-for="(mod, idx) in modules" :key="idx">
            <text class="module-name">{{ mod.name }}</text>
            <view class="progress-bar">
              <view class="progress-fill" :style="{ width: mod.percent + '%' }"></view>
            </view>
            <text class="module-percent">{{ mod.percent }}%</text>
          </view>
        </view>
      </view>

      <!-- 诊断规则面板 -->
      <view v-if="activeMenu === 'rules'" class="panel">
        <text class="panel-title">诊断规则配置</text>

        <view class="rule-cards">
          <view class="rule-card" v-for="(level, idx) in ruleLevels" :key="idx">
            <view class="rule-header">
              <view class="rule-dot" :style="{ background: level.color }"></view>
              <text class="rule-name">{{ level.name }}</text>
            </view>
            <text class="rule-desc">{{ level.desc }}</text>
            <view class="rule-input-group">
              <text class="rule-input-label">阈值分数</text>
              <input
                class="rule-input"
                type="number"
                v-model="level.threshold"
                :placeholder="'0-100'"
              />
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAdminStats } from '@/api/admin.js'

const activeMenu = ref('overview')

const recentRecords = ref([])
const modules = ref([])

const ruleLevels = reactive([
  { name: '已掌握', desc: '学生已完全掌握该知识点', color: '#27AE60', threshold: '85' },
  { name: '待补齐', desc: '存在薄弱环节需要加强', color: '#F39C12', threshold: '60' },
  { name: '待确认', desc: '需要进一步测评确认', color: '#3498DB', threshold: '40' },
  { name: '未检测', desc: '尚未进行相关测评', color: '#95A5A6', threshold: '0' },
  { name: '暂不适用', desc: '当前教学进度未涉及', color: '#BDC3C7', threshold: '0' },
])

const stats = ref({
  questionCount: 0,
  testCount: 0,
  activeUsers: 0,
  avgAccuracy: 0
})

onMounted(async () => {
  try {
    const data = await getAdminStats()
    stats.value = {
      questionCount: data.questionCount || 0,
      testCount: data.testCount || 0,
      activeUsers: data.activeUsers || 0,
      avgAccuracy: data.avgAccuracy || 0
    }

    if (data.recentRecords) {
      recentRecords.value = data.recentRecords.map(r => ({
        name: r.user?.name || '未知',
        time: r.createdAt ? new Date(r.createdAt).toLocaleString('zh-CN', { month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }) : '',
        score: r.score + '分',
        status: '已完成',
        statusClass: 'done'
      }))
    }

    // 模拟模块数据
    modules.value = [
      { name: '集合与逻辑', percent: 85 },
      { name: '函数与导数', percent: 72 },
      { name: '三角函数', percent: 68 },
      { name: '数列', percent: 55 },
      { name: '立体几何', percent: 48 },
      { name: '概率统计', percent: 90 },
    ]
  } catch (e) {
    console.error('加载统计数据失败', e)
  }
})

function goKnowledge() {
  uni.navigateTo({ url: '/pages/admin/knowledge/index' })
}

function goTemplate() {
  uni.navigateTo({ url: '/pages/admin/template/index' })
}

function goStudent() {
  uni.reLaunch({ url: '/pages/index/index' })
}

function handleLogout() {
  uni.removeStorageSync('token')
  uni.removeStorageSync('isLoggedIn')
  uni.removeStorageSync('userRole')
  uni.removeStorageSync('userName')
  uni.reLaunch({ url: '/pages/login/index' })
}

function showToast(msg) {
  uni.showToast({ title: msg, icon: 'none' })
}
</script>

<style scoped>
.admin-page {
  display: flex;
  min-height: 100vh;
  background: #F5F7FA;
}

/* 左侧菜单 */
.sidebar {
  width: 360rpx;
  background: #1E2A3A;
  display: flex;
  flex-direction: column;
  padding: 40rpx 0;
}

.sidebar-logo {
  display: flex;
  align-items: center;
  padding: 0 30rpx;
  margin-bottom: 50rpx;
}

.logo-dot {
  width: 24rpx;
  height: 24rpx;
  border-radius: 50%;
  background: #4A7BF7;
  margin-right: 16rpx;
}

.logo-text {
  color: #FFFFFF;
  font-size: 28rpx;
  font-weight: bold;
}

.menu-list {
  flex: 1;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 28rpx 30rpx;
  margin: 4rpx 16rpx;
  border-radius: 12rpx;
}

.menu-item.active {
  background: rgba(74, 123, 247, 0.2);
}

.menu-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}

.menu-label {
  font-size: 26rpx;
  color: #B0B8C1;
}

.menu-item.active .menu-label {
  color: #FFFFFF;
  font-weight: 500;
}

.sidebar-bottom {
  border-top: 1rpx solid rgba(255, 255, 255, 0.1);
  padding-top: 20rpx;
}

/* 右侧内容 */
.content {
  flex: 1;
  padding: 40rpx;
  overflow-y: auto;
}

.panel {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 40rpx;
}

.panel-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #1E2A3A;
  margin-bottom: 30rpx;
  display: block;
}

/* 统计卡片 */
.stat-cards {
  display: flex;
  gap: 20rpx;
  margin-bottom: 40rpx;
}

.stat-card {
  flex: 1;
  background: #F5F7FA;
  border-radius: 16rpx;
  padding: 24rpx 16rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 40rpx;
  font-weight: bold;
  color: #4A7BF7;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 22rpx;
  color: #8E99A4;
}

/* 表格 */
.table-section {
  margin-bottom: 40rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #1E2A3A;
  margin-bottom: 20rpx;
  display: block;
}

.table-header {
  display: flex;
  padding: 16rpx 0;
  border-bottom: 2rpx solid #E0E4E8;
}

.th {
  font-size: 24rpx;
  color: #8E99A4;
  font-weight: 500;
}

.table-row {
  display: flex;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #F0F2F5;
}

.td {
  font-size: 26rpx;
  color: #333333;
}

.status-tag {
  font-size: 22rpx;
  padding: 4rpx 16rpx;
  border-radius: 16rpx;
}

.status-tag.done {
  background: rgba(39, 174, 96, 0.1);
  color: #27AE60;
}

.status-tag.ongoing {
  background: rgba(243, 156, 18, 0.1);
  color: #F39C12;
}

/* 知识模块 */
.module-section {
  margin-top: 10rpx;
}

.module-item {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.module-name {
  width: 160rpx;
  font-size: 26rpx;
  color: #333333;
}

.progress-bar {
  flex: 1;
  height: 16rpx;
  background: #F0F2F5;
  border-radius: 8rpx;
  margin: 0 20rpx;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #4A7BF7, #6C5CE7);
  border-radius: 8rpx;
}

.module-percent {
  width: 80rpx;
  font-size: 24rpx;
  color: #8E99A4;
  text-align: right;
}

/* 诊断规则 */
.rule-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.rule-card {
  width: calc(50% - 10rpx);
  background: #F5F7FA;
  border-radius: 16rpx;
  padding: 24rpx;
}

.rule-header {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
}

.rule-dot {
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  margin-right: 12rpx;
}

.rule-name {
  font-size: 28rpx;
  font-weight: bold;
  color: #1E2A3A;
}

.rule-desc {
  font-size: 22rpx;
  color: #8E99A4;
  margin-bottom: 16rpx;
  display: block;
}

.rule-input-group {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.rule-input-label {
  font-size: 22rpx;
  color: #8E99A4;
}

.rule-input {
  width: 120rpx;
  height: 56rpx;
  background: #FFFFFF;
  border-radius: 8rpx;
  text-align: center;
  font-size: 26rpx;
  color: #333333;
}
</style>
