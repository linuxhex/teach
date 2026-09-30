<template>
  <view class="page">
    <!-- 左侧：知识点树 -->
    <view class="left-panel">
      <view class="panel-title">知识点体系</view>
      <view class="tree">
        <view
          v-for="node in knowledgePoints"
          :key="node.id"
          class="tree-node"
        >
          <view
            class="tree-parent"
            :class="{ active: selectedNode?.id === node.id }"
            @click="toggleNode(node)"
          >
            <text class="arrow" :class="{ expanded: expandedIds.includes(node.id) }">▶</text>
            <text class="node-name">{{ node.name }}</text>
          </view>
          <view v-if="expandedIds.includes(node.id)" class="tree-children">
            <view
              v-for="child in node.children"
              :key="child.id"
              class="tree-child"
              :class="{ active: selectedNode?.id === child.id }"
              @click.stop="selectNode(child, node)"
            >
              {{ child.name }}
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 右侧：知识点列表 -->
    <view class="right-panel">
      <view class="list-header">
        <text class="panel-title">知识点列表</text>
        <view class="btn-add" @click="onAdd">+ 新增知识点</view>
      </view>

      <!-- 搜索框 -->
      <view class="search-bar">
        <input
          class="search-input"
          v-model="searchKeyword"
          placeholder="搜索知识点名称..."
          @input="onSearch"
        />
        <text v-if="searchKeyword" class="search-clear" @click="searchKeyword = ''">✕</text>
      </view>

      <!-- 表头 -->
      <view class="table-row table-header">
        <text class="col col-name">名称</text>
        <text class="col col-count">题型数</text>
        <text class="col col-diff">难度范围</text>
        <text class="col col-status">状态</text>
        <text class="col col-action">操作</text>
      </view>

      <!-- 数据行 -->
      <view
        v-for="(item, index) in filteredList"
        :key="item.id"
        class="table-row"
        :class="{ striped: index % 2 === 1 }"
      >
        <text class="col col-name">{{ item.name }}</text>
        <text class="col col-count">{{ item.questionCount }}</text>
        <text class="col col-diff">{{ item.difficulty }}</text>
        <text class="col col-status">
          <text class="tag tag-green" v-if="item.enabled">已启用</text>
          <text class="tag tag-gray" v-else>未启用</text>
        </text>
        <view class="col col-action action-links">
          <text @click="openEdit(item)">编辑</text>
          <text @click="openPreview(item)">预览</text>
          <text @click="toggleStatus(item)">{{ item.enabled ? '停用' : '启用' }}</text>
          <text class="action-delete" @click="onDelete(item, index)">删除</text>
        </view>
      </view>

      <view v-if="filteredList.length === 0 && !searchKeyword" class="empty-tip">
        请在左侧选择一个知识点模块
      </view>
      <view v-if="filteredList.length === 0 && searchKeyword" class="empty-tip">
        未找到匹配的知识点
      </view>

      <!-- 底部状态栏 -->
      <view v-if="selectedParent" class="status-bar">
        <text class="status-text">共 {{ currentList.length }} 个知识点</text>
      </view>
    </view>

    <!-- 编辑/新增弹窗 -->
    <view v-if="showEditDialog" class="mask" @click.self="showEditDialog = false">
      <view class="dialog">
        <view class="dialog-title">{{ editingItem ? '编辑知识点' : '新增知识点' }}</view>

        <view class="form-item">
          <text class="label">知识点名称</text>
          <input class="input" v-model="editForm.name" placeholder="请输入名称" />
        </view>
        <view v-if="formErrors.name" class="form-error">{{ formErrors.name }}</view>

        <view class="form-item">
          <text class="label">所属模块</text>
          <picker :range="moduleNames" @change="onModuleChange">
            <view class="picker-value">{{ editForm.moduleName || '请选择模块' }}</view>
          </picker>
        </view>

        <view class="form-item">
          <text class="label">题型数量</text>
          <input class="input" type="number" v-model="editForm.questionCount" placeholder="请输入数量" />
        </view>
        <view v-if="formErrors.questionCount" class="form-error">{{ formErrors.questionCount }}</view>

        <view class="form-item">
          <text class="label">难度范围</text>
          <picker :range="difficultyOptions" @change="onDifficultyChange">
            <view class="picker-value">{{ editForm.difficulty || '请选择难度' }}</view>
          </picker>
        </view>

        <view class="form-item">
          <text class="label">状态</text>
          <switch :checked="editForm.enabled" @change="editForm.enabled = $event.detail.value" />
        </view>

        <view class="dialog-footer">
          <view class="btn btn-cancel" @click="showEditDialog = false">取消</view>
          <view class="btn btn-save" @click="saveEdit">保存</view>
        </view>
      </view>
    </view>

    <!-- 预览弹窗 -->
    <view v-if="showPreviewDialog" class="mask" @click.self="showPreviewDialog = false">
      <view class="dialog preview-dialog">
        <view class="dialog-title">知识点详情</view>

        <view class="preview-item">
          <text class="preview-label">名称</text>
          <text class="preview-value">{{ previewData.name }}</text>
        </view>
        <view class="preview-item">
          <text class="preview-label">题型数</text>
          <text class="preview-value">{{ previewData.questionCount }} 题</text>
        </view>
        <view class="preview-item">
          <text class="preview-label">难度范围</text>
          <text class="preview-value">{{ previewData.difficulty }}</text>
        </view>
        <view class="preview-item">
          <text class="preview-label">关联题目数</text>
          <text class="preview-value">{{ previewData.questionCount * 3 }} 题</text>
        </view>
        <view class="preview-item">
          <text class="preview-label">状态</text>
          <text class="preview-value">
            <text class="tag tag-green" v-if="previewData.enabled">已启用</text>
            <text class="tag tag-gray" v-else>未启用</text>
          </text>
        </view>

        <view class="dialog-footer">
          <view class="btn btn-save" @click="showPreviewDialog = false">关闭</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { knowledgePoints } from '@/data/mock.js'

// 给每个子节点补充 enabled 字段
knowledgePoints.forEach(kp => {
  kp.children.forEach(child => {
    if (child.enabled === undefined) child.enabled = Math.random() > 0.3
  })
})

const expandedIds = ref([])
const selectedNode = ref(null)
const selectedParent = ref(null)
const showEditDialog = ref(false)
const showPreviewDialog = ref(false)
const editingItem = ref(null)
const searchKeyword = ref('')

const editForm = ref({
  name: '',
  moduleName: '',
  questionCount: 0,
  difficulty: '',
  enabled: true
})

const formErrors = ref({})

const previewData = ref({
  name: '',
  questionCount: 0,
  difficulty: '',
  enabled: true
})

const moduleNames = computed(() => knowledgePoints.map(kp => kp.name))
const difficultyOptions = ['L1', 'L2', 'L3', 'L4']

const currentList = computed(() => {
  if (!selectedParent.value) return []
  return selectedParent.value.children || []
})

const filteredList = computed(() => {
  if (!searchKeyword.value) return currentList.value
  const keyword = searchKeyword.value.trim().toLowerCase()
  return currentList.value.filter(item => item.name.toLowerCase().includes(keyword))
})

// 默认展开第一个节点
onMounted(() => {
  if (knowledgePoints.length > 0) {
    const firstNode = knowledgePoints[0]
    expandedIds.value.push(firstNode.id)
    selectNode(firstNode, firstNode)
  }
})

function toggleNode(node) {
  const idx = expandedIds.value.indexOf(node.id)
  if (idx >= 0) {
    expandedIds.value.splice(idx, 1)
  } else {
    expandedIds.value.push(node.id)
  }
  selectNode(node, node)
}

function selectNode(node, parent) {
  selectedNode.value = node
  selectedParent.value = parent
  searchKeyword.value = ''
}

function onAdd() {
  editingItem.value = null
  editForm.value = {
    name: '',
    moduleName: selectedParent.value?.name || '',
    questionCount: 0,
    difficulty: '',
    enabled: true
  }
  formErrors.value = {}
  showEditDialog.value = true
}

function openEdit(item) {
  editingItem.value = item
  editForm.value = {
    name: item.name,
    moduleName: selectedParent.value?.name || '',
    questionCount: item.questionCount,
    difficulty: item.difficulty,
    enabled: item.enabled
  }
  formErrors.value = {}
  showEditDialog.value = true
}

function openPreview(item) {
  previewData.value = {
    name: item.name,
    questionCount: item.questionCount,
    difficulty: item.difficulty,
    enabled: item.enabled
  }
  showPreviewDialog.value = true
}

function toggleStatus(item) {
  item.enabled = !item.enabled
}

function onDelete(item, index) {
  uni.showModal({
    title: '确认删除',
    content: `确定要删除知识点"${item.name}"吗？`,
    success: (res) => {
      if (res.confirm) {
        selectedParent.value.children.splice(index, 1)
        uni.showToast({ title: '删除成功', icon: 'success' })
      }
    }
  })
}

function onSearch() {
  // searchKeyword is reactive, filteredList updates automatically
}

function onModuleChange(e) {
  editForm.value.moduleName = moduleNames.value[e.detail.value]
}

function onDifficultyChange(e) {
  editForm.value.difficulty = difficultyOptions[e.detail.value]
}

function validateForm() {
  const errors = {}
  if (!editForm.value.name || !editForm.value.name.trim()) {
    errors.name = '知识点名称不能为空'
  }
  if (!editForm.value.questionCount || Number(editForm.value.questionCount) <= 0) {
    errors.questionCount = '题型数量必须大于0'
  }
  formErrors.value = errors
  return Object.keys(errors).length === 0
}

function saveEdit() {
  if (!validateForm()) return

  if (editingItem.value) {
    // 编辑模式
    editingItem.value.name = editForm.value.name.trim()
    editingItem.value.questionCount = Number(editForm.value.questionCount)
    editingItem.value.difficulty = editForm.value.difficulty
    editingItem.value.enabled = editForm.value.enabled
  } else {
    // 新增模式：push 到对应模块的 children
    const targetModule = knowledgePoints.find(kp => kp.name === editForm.value.moduleName)
    if (targetModule) {
      const newId = Date.now()
      targetModule.children.push({
        id: newId,
        name: editForm.value.name.trim(),
        level: 2,
        questionCount: Number(editForm.value.questionCount),
        difficulty: editForm.value.difficulty,
        enabled: editForm.value.enabled
      })
      // 如果新增到当前选中的模块，刷新列表
      if (selectedParent.value?.id === targetModule.id) {
        selectedParent.value = { ...selectedParent.value }
      }
    }
  }
  showEditDialog.value = false
  uni.showToast({ title: '保存成功', icon: 'success' })
}
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: row;
  height: 100vh;
  background: #f5f6fa;
}

/* 左侧 */
.left-panel {
  width: 400rpx;
  background: #fff;
  border-right: 1px solid #e8e8e8;
  overflow-y: auto;
  flex-shrink: 0;
}

.panel-title {
  font-size: 30rpx;
  font-weight: 600;
  padding: 24rpx 24rpx 16rpx;
  color: #333;
}

.tree-node {
  margin-bottom: 4rpx;
}

.tree-parent {
  display: flex;
  align-items: center;
  padding: 16rpx 24rpx;
  font-size: 28rpx;
  color: #333;
  cursor: pointer;
}

.tree-parent.active {
  background: #e6f0ff;
  color: #4A7BF7;
}

.arrow {
  font-size: 20rpx;
  margin-right: 12rpx;
  transition: transform 0.2s;
  color: #999;
}

.arrow.expanded {
  transform: rotate(90deg);
}

.node-name {
  flex: 1;
}

.tree-children {
  padding-left: 48rpx;
}

.tree-child {
  padding: 12rpx 24rpx;
  font-size: 26rpx;
  color: #666;
  border-radius: 8rpx;
  margin: 2rpx 12rpx;
}

.tree-child.active {
  background: #4A7BF7;
  color: #fff;
}

/* 右侧 */
.right-panel {
  flex: 1;
  padding: 24rpx;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.btn-add {
  background: #4A7BF7;
  color: #fff;
  font-size: 26rpx;
  padding: 12rpx 28rpx;
  border-radius: 8rpx;
}

/* 搜索框 */
.search-bar {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
  position: relative;
}

.search-input {
  flex: 1;
  height: 64rpx;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 12rpx;
  padding: 0 60rpx 0 24rpx;
  font-size: 26rpx;
  color: #333;
}

.search-clear {
  position: absolute;
  right: 20rpx;
  font-size: 28rpx;
  color: #999;
  padding: 8rpx;
}

/* 表格 */
.table-row {
  display: flex;
  align-items: center;
  padding: 20rpx 16rpx;
  border-bottom: 1px solid #f0f0f0;
  font-size: 26rpx;
  color: #333;
}

.table-header {
  background: #fafafa;
  font-weight: 600;
  color: #666;
  border-radius: 8rpx 8rpx 0 0;
}

.striped {
  background: #fafbff;
}

.col {
  padding: 0 12rpx;
}

.col-name {
  flex: 2;
}

.col-count {
  flex: 1;
  text-align: center;
}

.col-diff {
  flex: 1;
  text-align: center;
}

.col-status {
  flex: 1;
  text-align: center;
}

.col-action {
  flex: 2;
  text-align: center;
}

.tag {
  font-size: 22rpx;
  padding: 4rpx 16rpx;
  border-radius: 6rpx;
}

.tag-green {
  background: #e8f8ee;
  color: #34C759;
}

.tag-gray {
  background: #f0f0f0;
  color: #999;
}

.action-links text {
  color: #4A7BF7;
  margin: 0 8rpx;
  font-size: 24rpx;
}

.action-delete {
  color: #FF3B30 !important;
}

.empty-tip {
  text-align: center;
  color: #999;
  padding: 80rpx 0;
  font-size: 28rpx;
}

/* 底部状态栏 */
.status-bar {
  margin-top: auto;
  padding: 20rpx 16rpx;
  background: #fff;
  border-top: 1px solid #f0f0f0;
  border-radius: 0 0 12rpx 12rpx;
}

.status-text {
  font-size: 24rpx;
  color: #999;
}

/* 弹窗 */
.mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.dialog {
  width: 600rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 40rpx;
}

.dialog-title {
  font-size: 32rpx;
  font-weight: 600;
  text-align: center;
  margin-bottom: 32rpx;
  color: #333;
}

.form-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 28rpx;
}

.form-error {
  font-size: 22rpx;
  color: #FF3B30;
  margin-top: -20rpx;
  margin-bottom: 20rpx;
  padding-left: 180rpx;
}

.label {
  font-size: 28rpx;
  color: #333;
  width: 180rpx;
  flex-shrink: 0;
}

.input {
  flex: 1;
  height: 64rpx;
  border: 1px solid #ddd;
  border-radius: 8rpx;
  padding: 0 16rpx;
  font-size: 26rpx;
}

.picker-value {
  flex: 1;
  height: 64rpx;
  line-height: 64rpx;
  border: 1px solid #ddd;
  border-radius: 8rpx;
  padding: 0 16rpx;
  font-size: 26rpx;
  color: #666;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 32rpx;
  gap: 20rpx;
}

.btn {
  padding: 16rpx 40rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.btn-cancel {
  background: #f0f0f0;
  color: #666;
}

.btn-save {
  background: #4A7BF7;
  color: #fff;
}

/* 预览弹窗 */
.preview-dialog {
  width: 560rpx;
}

.preview-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1px solid #f5f5f5;
}

.preview-label {
  font-size: 28rpx;
  color: #666;
  width: 180rpx;
}

.preview-value {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  flex: 1;
  text-align: right;
}
</style>
