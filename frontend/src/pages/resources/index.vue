<template>
  <view class="page">
    <!-- 顶部导航栏 -->
    <view class="navbar">
      <view class="navbar-status" :style="{ height: statusBarHeight + 'px' }"></view>
      <view class="navbar-content">
        <text class="navbar-title">资料库</text>
        <view class="navbar-actions">
          <view class="navbar-icon-btn" @click="onMyDownload">
            <text class="iconfont">&#xe60a;</text>
          </view>
          <view class="navbar-icon-btn" @click="onBrowseHistory">
            <text class="iconfont">&#xe60b;</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 搜索栏 -->
    <view class="search-bar">
      <view class="search-input-wrap">
        <text class="search-icon">&#x1F50D;</text>
        <input class="search-input" placeholder="搜索资料名称、知识点" placeholder-class="search-placeholder" v-model="searchText" />
      </view>
    </view>

    <!-- 学习阶段选择 -->
    <view class="stage-section">
      <view class="stage-grid">
        <view
          v-for="stage in learningStages"
          :key="stage.id"
          class="stage-card"
          :class="{ active: selectedStage === stage.id }"
          @click="selectedStage = stage.id"
        >
          <text class="stage-icon">{{ stage.icon }}</text>
          <text class="stage-name">{{ stage.name }}</text>
          <text class="stage-desc">{{ stage.desc }}</text>
        </view>
      </view>
    </view>

    <!-- 筛选区 -->
    <view class="filter-section">
      <view class="filter-header" @click="filterExpanded = !filterExpanded">
        <text class="filter-title">筛选条件</text>
        <text class="filter-arrow" :class="{ expanded: filterExpanded }">&#x25BC;</text>
      </view>
      <view v-if="filterExpanded" class="filter-body">
        <!-- 教材版本 -->
        <view class="filter-row">
          <text class="filter-label">教材版本</text>
          <scroll-view scroll-x class="filter-options">
            <view
              v-for="item in textbookVersions"
              :key="item"
              class="filter-option"
              :class="{ active: selectedVersion === item }"
              @click="selectedVersion = item"
            >
              <text>{{ item }}</text>
            </view>
          </scroll-view>
        </view>
        <!-- 教材册次 -->
        <view class="filter-row">
          <text class="filter-label">教材册次</text>
          <scroll-view scroll-x class="filter-options">
            <view
              v-for="item in textbookVolumes"
              :key="item"
              class="filter-option"
              :class="{ active: selectedVolume === item }"
              @click="selectedVolume = item"
            >
              <text>{{ item }}</text>
            </view>
          </scroll-view>
        </view>
        <!-- 章节 -->
        <view class="filter-row">
          <text class="filter-label">章节</text>
          <scroll-view scroll-x class="filter-options">
            <view
              v-for="item in chapters"
              :key="item"
              class="filter-option"
              :class="{ active: selectedChapter === item }"
              @click="selectedChapter = item"
            >
              <text>{{ item }}</text>
            </view>
          </scroll-view>
        </view>
        <!-- 资料类型 -->
        <view class="filter-row">
          <text class="filter-label">资料类型</text>
          <scroll-view scroll-x class="filter-options">
            <view
              v-for="item in materialTypes"
              :key="item"
              class="filter-option"
              :class="{ active: selectedType === item }"
              @click="selectedType = item"
            >
              <text>{{ item }}</text>
            </view>
          </scroll-view>
        </view>
        <!-- 资料作用 -->
        <view class="filter-row">
          <text class="filter-label">资料作用</text>
          <scroll-view scroll-x class="filter-options">
            <view
              v-for="item in materialPurposes"
              :key="item"
              class="filter-option"
              :class="{ active: selectedPurpose === item }"
              @click="selectedPurpose = item"
            >
              <text>{{ item }}</text>
            </view>
          </scroll-view>
        </view>
      </view>
    </view>

    <!-- 资料列表 -->
    <view class="material-list">
      <view v-for="item in filteredMaterials" :key="item.id" class="material-card">
        <view class="material-cover" :style="{ background: getCoverColor(item.type) }">
          <text class="cover-text">{{ item.type === '讲解类' ? '讲' : item.type === '刷题类' ? '练' : '用' }}</text>
        </view>
        <view class="material-info">
          <text class="material-title">{{ item.title }}</text>
          <view class="material-tags">
            <text class="tag type-tag" :class="getTypeClass(item.type)">{{ item.type }}</text>
            <text class="tag purpose-tag">{{ item.purpose }}</text>
          </view>
          <text class="material-desc">{{ item.desc }}</text>
          <text class="material-audience">适合：{{ item.audience }}</text>
          <view class="material-knowledge-tags">
            <text v-for="tag in item.tags" :key="tag" class="knowledge-tag">{{ tag }}</text>
          </view>
          <view class="match-bar-wrap">
            <view class="match-bar">
              <view class="match-bar-fill" :style="{ width: item.matchRate + '%' }"></view>
            </view>
            <text class="match-text">匹配度 {{ item.matchRate }}%</text>
          </view>
          <view class="material-bottom">
            <text class="material-price">¥{{ item.price }}</text>
            <view class="detail-btn" @click="onViewDetail(item)">
              <text>查看详情</text>
            </view>
          </view>
        </view>
      </view>
      <view v-if="filteredMaterials.length === 0" class="empty-tip">
        <text>暂无符合条件的资料</text>
      </view>
    </view>

    <!-- TabBar -->
    <tab-bar current="resources" />
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getMaterials, getMaterialFilters } from '@/api/material.js'
import TabBar from '@/components/TabBar.vue'

const statusBarHeight = ref(44)

const searchText = ref('')
const selectedStage = ref('sync')
const filterExpanded = ref(true)
const selectedVersion = ref('全部')
const selectedVolume = ref('全部')
const selectedChapter = ref('全部')
const selectedType = ref('全部')
const selectedPurpose = ref('全部')

const learningStages = ref([
  { id: 'sync', icon: '📘', name: '同步学习', desc: '跟随学校进度' },
  { id: 'review', icon: '🎯', name: '高三复习', desc: '系统复习备考' },
  { id: 'summer', icon: '☀️', name: '暑假', desc: '预习提升' },
  { id: 'winter', icon: '❄️', name: '寒假', desc: '查漏补缺' }
])
const textbookVersions = ref(['全部', '人教A版', '人教B版', '北师大版'])
const textbookVolumes = ref(['全部', '必修一', '必修二', '选择性必修一', '选择性必修二', '全一册'])
const chapters = ref(['全部', '第一章', '第二章', '第三章', '第四章'])
const materialTypes = ref(['全部', '讲解类', '刷题类', '功能类'])
const materialPurposes = ref(['全部', '章节体系', '专题突破', '工具资料'])

const materials = ref([])

onMounted(async () => {
  try {
    const [mats, filters] = await Promise.all([
      getMaterials().catch(() => []),
      getMaterialFilters().catch(() => null)
    ])
    materials.value = mats
    if (filters) {
      if (filters.versions) textbookVersions.value = filters.versions
      if (filters.types) materialTypes.value = ['全部', ...filters.types]
      if (filters.purposes) materialPurposes.value = ['全部', ...filters.purposes]
    }
  } catch (e) {
    console.error('加载资料失败', e)
  }
})

const filteredMaterials = computed(() => {
  return materials.value.filter(item => {
    // 资料类型筛选
    if (selectedType.value !== '全部' && item.type !== selectedType.value) return false
    // 资料作用筛选
    if (selectedPurpose.value !== '全部' && item.purpose !== selectedPurpose.value) return false
    // 教材版本筛选
    if (selectedVersion.value !== '全部') {
      const versionMatch = item.version === selectedVersion.value || item.tags.some(t => t.includes(selectedVersion.value)) || item.title.includes(selectedVersion.value)
      if (!versionMatch) return false
    }
    // 册次筛选
    if (selectedVolume.value !== '全部') {
      const volumeMatch = item.volume === selectedVolume.value || item.title.includes(selectedVolume.value) || item.tags.some(t => t.includes(selectedVolume.value))
      if (!volumeMatch) return false
    }
    // 章节筛选
    if (selectedChapter.value !== '全部') {
      const chapterMatch = item.chapter === selectedChapter.value || item.tags.some(t => t.includes(selectedChapter.value)) || item.title.includes(selectedChapter.value)
      if (!chapterMatch) return false
    }
    // 搜索关键词
    if (searchText.value) {
      const keyword = searchText.value.toLowerCase()
      const inTitle = item.title.toLowerCase().includes(keyword)
      const inTags = item.tags.some(t => t.toLowerCase().includes(keyword))
      if (!inTitle && !inTags) return false
    }
    return true
  })
})

function getCoverColor(type) {
  const map = { '讲解类': '#4A7BF7', '刷题类': '#34C759', '功能类': '#FF9500' }
  return map[type] || '#4A7BF7'
}

function getTypeClass(type) {
  const map = { '讲解类': 'blue', '刷题类': 'green', '功能类': 'orange' }
  return map[type] || 'blue'
}

function onViewDetail(item) {
  uni.navigateTo({ url: `/pages/resources/detail/index?id=${item.id}` })
}

function onMyDownload() {
  uni.showToast({ title: '暂无下载记录', icon: 'none' })
}

function onBrowseHistory() {
  uni.showToast({ title: '暂无浏览记录', icon: 'none' })
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #F5F7FA;
  padding-bottom: 120rpx;
}

/* 导航栏 */
.navbar {
  background: #fff;
  position: sticky;
  top: 0;
  z-index: 100;
}
.navbar-content {
  height: 88rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  padding: 0 30rpx;
}
.navbar-title {
  font-size: 34rpx;
  font-weight: 600;
  color: #333;
}
.navbar-actions {
  position: absolute;
  right: 30rpx;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  gap: 20rpx;
}
.navbar-icon-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36rpx;
  color: #666;
}

/* 搜索栏 */
.search-bar {
  padding: 16rpx 30rpx;
  background: #fff;
}
.search-input-wrap {
  display: flex;
  align-items: center;
  background: #F5F7FA;
  border-radius: 36rpx;
  padding: 0 24rpx;
  height: 72rpx;
}
.search-icon {
  font-size: 28rpx;
  margin-right: 12rpx;
}
.search-input {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}
.search-placeholder {
  color: #bbb;
}

/* 学习阶段 */
.stage-section {
  padding: 20rpx 30rpx;
  background: #fff;
}
.stage-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16rpx;
}
.stage-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 24rpx 12rpx;
  border-radius: 16rpx;
  background: #F5F7FA;
  border: 2rpx solid transparent;
}
.stage-card.active {
  border-color: #4A7BF7;
  background: rgba(74, 123, 247, 0.08);
}
.stage-icon {
  font-size: 40rpx;
  margin-bottom: 8rpx;
}
.stage-name {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 4rpx;
}
.stage-desc {
  font-size: 22rpx;
  color: #999;
}

/* 筛选区 */
.filter-section {
  margin-top: 16rpx;
  background: #fff;
  padding: 0 30rpx;
}
.filter-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
}
.filter-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
}
.filter-arrow {
  font-size: 22rpx;
  color: #999;
  transition: transform 0.3s;
}
.filter-arrow.expanded {
  transform: rotate(180deg);
}
.filter-body {
  padding-bottom: 20rpx;
}
.filter-row {
  margin-bottom: 20rpx;
}
.filter-label {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 12rpx;
  display: block;
}
.filter-options {
  white-space: nowrap;
}
.filter-options :deep(.uni-scroll-view) {
  display: flex;
  gap: 16rpx;
}
.filter-option {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 12rpx 28rpx;
  border-radius: 30rpx;
  background: #F5F7FA;
  font-size: 24rpx;
  color: #666;
  flex-shrink: 0;
}
.filter-option.active {
  background: rgba(74, 123, 247, 0.12);
  color: #4A7BF7;
  font-weight: 500;
}

/* 资料列表 */
.material-list {
  padding: 20rpx 30rpx;
}
.material-card {
  display: flex;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  margin-bottom: 20rpx;
}
.material-cover {
  width: 160rpx;
  min-height: 320rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.cover-text {
  color: #fff;
  font-size: 48rpx;
  font-weight: 700;
}
.material-info {
  flex: 1;
  padding: 20rpx 24rpx;
  display: flex;
  flex-direction: column;
}
.material-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 10rpx;
}
.material-tags {
  display: flex;
  gap: 12rpx;
  margin-bottom: 10rpx;
  flex-wrap: wrap;
}
.tag {
  font-size: 20rpx;
  padding: 4rpx 14rpx;
  border-radius: 6rpx;
}
.type-tag.blue {
  background: rgba(74, 123, 247, 0.12);
  color: #4A7BF7;
}
.type-tag.green {
  background: rgba(52, 199, 89, 0.12);
  color: #34C759;
}
.type-tag.orange {
  background: rgba(255, 149, 0, 0.12);
  color: #FF9500;
}
.purpose-tag {
  background: #F5F7FA;
  color: #666;
}
.material-desc {
  font-size: 24rpx;
  color: #666;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  margin-bottom: 8rpx;
}
.material-audience {
  font-size: 22rpx;
  color: #999;
  margin-bottom: 10rpx;
}
.material-knowledge-tags {
  display: flex;
  gap: 10rpx;
  flex-wrap: wrap;
  margin-bottom: 12rpx;
}
.knowledge-tag {
  font-size: 20rpx;
  padding: 4rpx 12rpx;
  border-radius: 20rpx;
  background: #F5F7FA;
  color: #666;
}
.match-bar-wrap {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 12rpx;
}
.match-bar {
  flex: 1;
  height: 12rpx;
  background: #F0F0F0;
  border-radius: 6rpx;
  overflow: hidden;
}
.match-bar-fill {
  height: 100%;
  background: #4A7BF7;
  border-radius: 6rpx;
}
.match-text {
  font-size: 22rpx;
  color: #4A7BF7;
  white-space: nowrap;
}
.material-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 4rpx;
}
.material-price {
  font-size: 32rpx;
  font-weight: 700;
  color: #FF3B30;
}
.detail-btn {
  padding: 10rpx 28rpx;
  background: #4A7BF7;
  border-radius: 30rpx;
}
.detail-btn text {
  font-size: 24rpx;
  color: #fff;
}

/* 空状态 */
.empty-tip {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
