<template>
  <view class="page">
    <!-- 顶部操作栏 -->
    <view class="top-bar">
      <text class="page-title">模板管理</text>
      <view class="btn-add" @click="onAddTemplate">+ 新增模板</view>
    </view>

    <!-- Tab 切换 -->
    <view class="tabs">
      <view
        v-for="(tab, idx) in tabs"
        :key="idx"
        class="tab-item"
        :class="{ active: currentTab === idx }"
        @click="currentTab = idx"
      >
        <text>{{ tab }}</text>
        <view v-if="currentTab === idx" class="tab-line"></view>
      </view>
    </view>

    <!-- Tab 1：模板列表 -->
    <view v-if="currentTab === 0" class="tab-content">
      <view class="table-row table-header">
        <text class="col col-name">模板名称</text>
        <text class="col col-stage">适用阶段</text>
        <text class="col col-score">分数段</text>
        <text class="col col-count">题数</text>
        <text class="col col-duration">时长(分)</text>
        <text class="col col-status">状态</text>
        <text class="col col-time">更新时间</text>
        <text class="col col-action">操作</text>
      </view>

      <view
        v-for="(item, index) in templates"
        :key="item.id"
        class="table-row"
        :class="{ striped: index % 2 === 1 }"
      >
        <text class="col col-name">{{ item.name }}</text>
        <text class="col col-stage">{{ item.stage }}</text>
        <text class="col col-score">{{ item.scoreRange }}</text>
        <text class="col col-count">{{ item.questionCount }}</text>
        <text class="col col-duration">{{ item.duration }}</text>
        <text class="col col-status">
          <text class="tag tag-green" v-if="item.status === 'active'">已发布</text>
          <text class="tag tag-gray" v-else>已停用</text>
        </text>
        <text class="col col-time">{{ item.updatedAt }}</text>
        <view class="col col-action action-links">
          <text @click="onEdit(item)">编辑</text>
          <text @click="onPreviewTemplate(item)">预览</text>
          <text @click="onToggleStatus(item)">{{ item.status === 'active' ? '停用' : '发布' }}</text>
          <text class="action-delete" @click="onDeleteTemplate(item, index)">删除</text>
        </view>
      </view>
    </view>

    <!-- Tab 2：诊断模块 -->
    <view v-if="currentTab === 1" class="tab-content">
      <view
        v-for="mod in diagnosticModules"
        :key="mod.id"
        class="card"
      >
        <view class="card-header">
          <text class="card-title">{{ mod.name }}</text>
          <view class="card-tags">
            <text class="mini-tag">诊断点 {{ mod.diagnosticPoints }}</text>
            <text class="mini-tag">题目位置 {{ mod.questionPositions }}</text>
          </view>
        </view>
        <text class="card-desc">{{ mod.desc }}</text>
        <view class="card-footer">
          <view class="btn-config" @click="onConfigPoints(mod)">配置诊断点</view>
        </view>
      </view>
    </view>

    <!-- Tab 3：判定规则 -->
    <view v-if="currentTab === 2" class="tab-content">
      <!-- 规则预览：输入分数 -->
      <view class="rule-preview-section">
        <text class="rule-preview-title">规则预览</text>
        <view class="rule-preview-input">
          <input
            class="score-input"
            type="number"
            v-model="previewScore"
            placeholder="输入分数查看等级"
          />
          <view v-if="previewScore !== ''" class="score-result" :style="{ color: matchedLevel?.color || '#999' }">
            {{ previewScore !== '' ? (matchedLevel ? `属于「${matchedLevel.name}」等级` : '无匹配等级') : '' }}
          </view>
        </view>
      </view>

      <view
        v-for="(level, idx) in diagnosisRules.levels"
        :key="level.name"
        class="rule-card"
        :class="{ 'rule-highlight': matchedLevel && matchedLevel.name === level.name }"
      >
        <view class="rule-color" :style="{ background: level.color }"></view>
        <view class="rule-info">
          <view class="rule-top">
            <text class="rule-name" :style="{ color: level.color }">{{ level.name }}</text>
            <text class="rule-range" v-if="level.min >= 0">
              {{ getRangeText(level) }}
            </text>
            <text class="rule-range" v-else>不限分数</text>
            <view class="rule-edit-btn" @click="onEditRule(level, idx)">编辑</view>
          </view>
          <text class="rule-desc">{{ level.desc }}</text>
        </view>
      </view>

      <view class="btn-add-level" @click="onAddLevel">+ 添加等级</view>
    </view>

    <!-- 模板编辑弹窗 -->
    <view v-if="showTemplateDialog" class="mask" @click.self="showTemplateDialog = false">
      <view class="dialog">
        <view class="dialog-title">{{ editingTemplate ? '编辑模板' : '新增模板' }}</view>

        <view class="form-item">
          <text class="label">模板名称</text>
          <input class="input" v-model="templateForm.name" placeholder="请输入模板名称" />
        </view>
        <view v-if="templateErrors.name" class="form-error">{{ templateErrors.name }}</view>

        <view class="form-item">
          <text class="label">适用阶段</text>
          <picker :range="stageOptions" @change="onStageChange">
            <view class="picker-value">{{ templateForm.stage || '请选择阶段' }}</view>
          </picker>
        </view>

        <view class="form-item">
          <text class="label">分数段</text>
          <picker :range="scoreRangeOptions" @change="onScoreRangeChange">
            <view class="picker-value">{{ templateForm.scoreRange || '请选择分数段' }}</view>
          </picker>
        </view>

        <view class="form-item">
          <text class="label">题目数量</text>
          <input class="input" type="number" v-model="templateForm.questionCount" placeholder="请输入数量" />
        </view>
        <view v-if="templateErrors.questionCount" class="form-error">{{ templateErrors.questionCount }}</view>

        <view class="form-item">
          <text class="label">考试时长(分钟)</text>
          <input class="input" type="number" v-model="templateForm.duration" placeholder="请输入时长" />
        </view>
        <view v-if="templateErrors.duration" class="form-error">{{ templateErrors.duration }}</view>

        <view class="dialog-footer">
          <view class="btn btn-cancel" @click="showTemplateDialog = false">取消</view>
          <view class="btn btn-save" @click="saveTemplate">保存</view>
        </view>
      </view>
    </view>

    <!-- 模板预览弹窗 -->
    <view v-if="showTemplatePreview" class="mask" @click.self="showTemplatePreview = false">
      <view class="dialog preview-dialog">
        <view class="dialog-title">模板详情</view>

        <view class="preview-item">
          <text class="preview-label">模板名称</text>
          <text class="preview-value">{{ templatePreviewData.name }}</text>
        </view>
        <view class="preview-item">
          <text class="preview-label">适用阶段</text>
          <text class="preview-value">{{ templatePreviewData.stage }}</text>
        </view>
        <view class="preview-item">
          <text class="preview-label">分数段</text>
          <text class="preview-value">{{ templatePreviewData.scoreRange }}</text>
        </view>
        <view class="preview-item">
          <text class="preview-label">题目数量</text>
          <text class="preview-value">{{ templatePreviewData.questionCount }} 题</text>
        </view>
        <view class="preview-item">
          <text class="preview-label">考试时长</text>
          <text class="preview-value">{{ templatePreviewData.duration }} 分钟</text>
        </view>
        <view class="preview-item">
          <text class="preview-label">状态</text>
          <text class="preview-value">
            <text class="tag tag-green" v-if="templatePreviewData.status === 'active'">已发布</text>
            <text class="tag tag-gray" v-else>已停用</text>
          </text>
        </view>
        <view class="preview-item">
          <text class="preview-label">更新时间</text>
          <text class="preview-value">{{ templatePreviewData.updatedAt }}</text>
        </view>

        <view class="dialog-footer">
          <view class="btn btn-save" @click="showTemplatePreview = false">关闭</view>
        </view>
      </view>
    </view>

    <!-- 诊断点配置弹窗 -->
    <view v-if="showDiagConfig" class="mask" @click.self="showDiagConfig = false">
      <view class="dialog diag-dialog">
        <view class="dialog-title">{{ currentDiagModule?.name }} - 诊断点管理</view>

        <!-- 诊断点列表 -->
        <view class="diag-list">
          <view
            v-for="(point, idx) in currentDiagPoints"
            :key="point.id"
            class="diag-item"
          >
            <view class="diag-info">
              <text class="diag-name">{{ point.name }}</text>
              <text class="diag-purpose">{{ point.purpose }}</text>
              <view class="diag-meta">
                <text class="diag-tag">题位 {{ point.positions }}</text>
                <text class="diag-tag" :class="{ 'diag-tag-key': point.intensity === '重点' }">{{ point.intensity }}</text>
                <text class="tag tag-green" v-if="point.status === 'active'">启用</text>
                <text class="tag tag-gray" v-else>停用</text>
              </view>
            </view>
            <view class="diag-actions">
              <text class="action-link" @click="onEditDiagPoint(point, idx)">编辑</text>
              <text class="action-link action-delete" @click="onDeleteDiagPoint(idx)">删除</text>
            </view>
          </view>
          <view v-if="currentDiagPoints.length === 0" class="empty-tip-small">暂无诊断点</view>
        </view>

        <view class="dialog-footer">
          <view class="btn btn-cancel" @click="showDiagConfig = false">关闭</view>
          <view class="btn btn-save" @click="onAddDiagPoint">+ 新增诊断点</view>
        </view>
      </view>
    </view>

    <!-- 诊断点编辑弹窗 -->
    <view v-if="showDiagEditDialog" class="mask" @click.self="showDiagEditDialog = false">
      <view class="dialog">
        <view class="dialog-title">{{ editingDiagPoint !== null ? '编辑诊断点' : '新增诊断点' }}</view>

        <view class="form-item">
          <text class="label">名称</text>
          <input class="input" v-model="diagForm.name" placeholder="请输入诊断点名称" />
        </view>
        <view v-if="diagErrors.name" class="form-error">{{ diagErrors.name }}</view>

        <view class="form-item">
          <text class="label">目的描述</text>
          <input class="input" v-model="diagForm.purpose" placeholder="请输入目的" />
        </view>

        <view class="form-item">
          <text class="label">题位数量</text>
          <input class="input" type="number" v-model="diagForm.positions" placeholder="请输入数量" />
        </view>

        <view class="form-item">
          <text class="label">强度</text>
          <picker :range="intensityOptions" @change="onIntensityChange">
            <view class="picker-value">{{ diagForm.intensity || '请选择强度' }}</view>
          </picker>
        </view>

        <view class="dialog-footer">
          <view class="btn btn-cancel" @click="showDiagEditDialog = false">取消</view>
          <view class="btn btn-save" @click="saveDiagPoint">保存</view>
        </view>
      </view>
    </view>

    <!-- 规则编辑弹窗 -->
    <view v-if="showRuleEditDialog" class="mask" @click.self="showRuleEditDialog = false">
      <view class="dialog">
        <view class="dialog-title">{{ editingRuleIndex !== null ? '编辑等级' : '添加等级' }}</view>

        <view class="form-item">
          <text class="label">等级名称</text>
          <input class="input" v-model="ruleForm.name" placeholder="请输入等级名称" />
        </view>
        <view v-if="ruleErrors.name" class="form-error">{{ ruleErrors.name }}</view>

        <view class="form-item">
          <text class="label">分数阈值</text>
          <input class="input" type="number" v-model="ruleForm.min" placeholder="最低分数" />
        </view>

        <view class="form-item">
          <text class="label">描述</text>
          <input class="input" v-model="ruleForm.desc" placeholder="请输入描述" />
        </view>

        <view class="form-item">
          <text class="label">颜色</text>
          <view class="color-options">
            <view
              v-for="c in colorOptions"
              :key="c"
              class="color-dot"
              :class="{ selected: ruleForm.color === c }"
              :style="{ background: c }"
              @click="ruleForm.color = c"
            ></view>
          </view>
        </view>

        <view class="dialog-footer">
          <view class="btn btn-cancel" @click="showRuleEditDialog = false">取消</view>
          <view class="btn btn-save" @click="saveRule">保存</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { templates, diagnosticModules, diagnosisRules } from '@/data/mock.js'

// 引入诊断点 mock 数据
import { diagnosticPoints as allDiagPoints } from '@/data/mock.js'

const tabs = ['模板列表', '诊断模块', '判定规则']
const currentTab = ref(0)

// ===== Tab 1: 模板 =====
const stageOptions = ['高一', '高二', '高三']
const scoreRangeOptions = ['120+', '100-120', '70-100', '70以下']

const showTemplateDialog = ref(false)
const showTemplatePreview = ref(false)
const editingTemplate = ref(null)
const editingTemplateIndex = ref(-1)

const templateForm = ref({
  name: '',
  stage: '',
  scoreRange: '',
  questionCount: 0,
  duration: 0
})

const templateErrors = ref({})

const templatePreviewData = ref({
  name: '',
  stage: '',
  scoreRange: '',
  questionCount: 0,
  duration: 0,
  status: '',
  updatedAt: ''
})

function onAddTemplate() {
  editingTemplate.value = null
  editingTemplateIndex.value = -1
  templateForm.value = { name: '', stage: '', scoreRange: '', questionCount: 0, duration: 0 }
  templateErrors.value = {}
  showTemplateDialog.value = true
}

function onEdit(item) {
  editingTemplate.value = item
  editingTemplateIndex.value = templates.indexOf(item)
  templateForm.value = {
    name: item.name,
    stage: item.stage,
    scoreRange: item.scoreRange,
    questionCount: item.questionCount,
    duration: item.duration
  }
  templateErrors.value = {}
  showTemplateDialog.value = true
}

function onPreviewTemplate(item) {
  templatePreviewData.value = { ...item }
  showTemplatePreview.value = true
}

function onToggleStatus(item) {
  item.status = item.status === 'active' ? 'inactive' : 'active'
}

function onDeleteTemplate(item, index) {
  uni.showModal({
    title: '确认删除',
    content: `确定要删除模板"${item.name}"吗？`,
    success: (res) => {
      if (res.confirm) {
        templates.splice(index, 1)
        uni.showToast({ title: '删除成功', icon: 'success' })
      }
    }
  })
}

function onStageChange(e) {
  templateForm.value.stage = stageOptions[e.detail.value]
}

function onScoreRangeChange(e) {
  templateForm.value.scoreRange = scoreRangeOptions[e.detail.value]
}

function validateTemplate() {
  const errors = {}
  if (!templateForm.value.name || !templateForm.value.name.trim()) {
    errors.name = '模板名称不能为空'
  }
  if (!templateForm.value.questionCount || Number(templateForm.value.questionCount) <= 0) {
    errors.questionCount = '题目数量必须大于0'
  }
  if (!templateForm.value.duration || Number(templateForm.value.duration) <= 0) {
    errors.duration = '考试时长必须大于0'
  }
  templateErrors.value = errors
  return Object.keys(errors).length === 0
}

function saveTemplate() {
  if (!validateTemplate()) return

  const now = new Date()
  const timeStr = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`

  if (editingTemplate.value) {
    Object.assign(editingTemplate.value, {
      name: templateForm.value.name.trim(),
      stage: templateForm.value.stage,
      scoreRange: templateForm.value.scoreRange,
      questionCount: Number(templateForm.value.questionCount),
      duration: Number(templateForm.value.duration),
      updatedAt: timeStr
    })
  } else {
    templates.push({
      id: Date.now(),
      name: templateForm.value.name.trim(),
      stage: templateForm.value.stage,
      scoreRange: templateForm.value.scoreRange,
      questionCount: Number(templateForm.value.questionCount),
      duration: Number(templateForm.value.duration),
      status: 'active',
      updatedAt: timeStr
    })
  }
  showTemplateDialog.value = false
  uni.showToast({ title: '保存成功', icon: 'success' })
}

// ===== Tab 2: 诊断模块 =====
const showDiagConfig = ref(false)
const showDiagEditDialog = ref(false)
const currentDiagModule = ref(null)
const currentDiagPoints = ref([])
const editingDiagPoint = ref(null)
const editingDiagPointIndex = ref(-1)
const intensityOptions = ['普通', '重点']

const diagForm = ref({
  name: '',
  purpose: '',
  positions: 0,
  intensity: ''
})

const diagErrors = ref({})

function onConfigPoints(mod) {
  currentDiagModule.value = mod
  // 过滤出该模块对应的诊断点（按模块名匹配或默认全部）
  currentDiagPoints.value = allDiagPoints.filter(p => p.status !== undefined)
  showDiagConfig.value = true
}

function onAddDiagPoint() {
  editingDiagPoint.value = null
  editingDiagPointIndex.value = -1
  diagForm.value = { name: '', purpose: '', positions: 0, intensity: '' }
  diagErrors.value = {}
  showDiagEditDialog.value = true
}

function onEditDiagPoint(point, idx) {
  editingDiagPoint.value = point
  editingDiagPointIndex.value = idx
  diagForm.value = {
    name: point.name,
    purpose: point.purpose,
    positions: point.positions,
    intensity: point.intensity
  }
  diagErrors.value = {}
  showDiagEditDialog.value = true
}

function onDeleteDiagPoint(idx) {
  uni.showModal({
    title: '确认删除',
    content: `确定要删除该诊断点吗？`,
    success: (res) => {
      if (res.confirm) {
        currentDiagPoints.value.splice(idx, 1)
        uni.showToast({ title: '删除成功', icon: 'success' })
      }
    }
  })
}

function onIntensityChange(e) {
  diagForm.value.intensity = intensityOptions[e.detail.value]
}

function saveDiagPoint() {
  const errors = {}
  if (!diagForm.value.name || !diagForm.value.name.trim()) {
    errors.name = '名称不能为空'
  }
  diagErrors.value = errors
  if (Object.keys(errors).length > 0) return

  if (editingDiagPoint.value) {
    Object.assign(editingDiagPoint.value, {
      name: diagForm.value.name.trim(),
      purpose: diagForm.value.purpose,
      positions: Number(diagForm.value.positions),
      intensity: diagForm.value.intensity
    })
  } else {
    currentDiagPoints.value.push({
      id: Date.now(),
      name: diagForm.value.name.trim(),
      purpose: diagForm.value.purpose,
      positions: Number(diagForm.value.positions),
      intensity: diagForm.value.intensity,
      status: 'active'
    })
    // 更新模块诊断点计数
    if (currentDiagModule.value) {
      currentDiagModule.value.diagnosticPoints = currentDiagPoints.value.length
    }
  }
  showDiagEditDialog.value = false
  uni.showToast({ title: '保存成功', icon: 'success' })
}

// ===== Tab 3: 判定规则 =====
const previewScore = ref('')
const showRuleEditDialog = ref(false)
const editingRuleIndex = ref(-1)

const ruleForm = ref({
  name: '',
  min: 0,
  desc: '',
  color: '#4A7BF7'
})

const ruleErrors = ref({})
const colorOptions = ['#34C759', '#FF9500', '#4A7BF7', '#999999', '#FF3B30', '#7B61FF', '#CCCCCC']

const matchedLevel = computed(() => {
  const score = Number(previewScore.value)
  if (isNaN(score) || previewScore.value === '') return null
  const levels = diagnosisRules.levels
  for (let i = 0; i < levels.length; i++) {
    if (score >= levels[i].min) return levels[i]
  }
  return null
})

function getRangeText(level) {
  const levels = diagnosisRules.levels
  const idx = levels.indexOf(level)
  if (idx < 0) return ''
  if (level.min < 0) return '不限分数'
  // 找到上一级的 min 作为上限
  if (idx === 0) return `${level.min}分以上`
  return `${level.min}-${levels[idx - 1].min - 1}分`
}

function onEditRule(level, idx) {
  editingRuleIndex.value = idx
  ruleForm.value = {
    name: level.name,
    min: level.min,
    desc: level.desc,
    color: level.color
  }
  ruleErrors.value = {}
  showRuleEditDialog.value = true
}

function onAddLevel() {
  editingRuleIndex.value = -1
  ruleForm.value = { name: '', min: 0, desc: '', color: '#4A7BF7' }
  ruleErrors.value = {}
  showRuleEditDialog.value = true
}

function saveRule() {
  const errors = {}
  if (!ruleForm.value.name || !ruleForm.value.name.trim()) {
    errors.name = '等级名称不能为空'
  }
  ruleErrors.value = errors
  if (Object.keys(errors).length > 0) return

  if (editingRuleIndex.value >= 0) {
    // 编辑已有等级
    const level = diagnosisRules.levels[editingRuleIndex.value]
    level.name = ruleForm.value.name.trim()
    level.min = Number(ruleForm.value.min)
    level.desc = ruleForm.value.desc
    level.color = ruleForm.value.color
  } else {
    // 添加新等级
    diagnosisRules.levels.push({
      name: ruleForm.value.name.trim(),
      color: ruleForm.value.color,
      min: Number(ruleForm.value.min),
      desc: ruleForm.value.desc
    })
    // 按 min 降序排列
    diagnosisRules.levels.sort((a, b) => b.min - a.min)
  }
  showRuleEditDialog.value = false
  uni.showToast({ title: '保存成功', icon: 'success' })
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f5f6fa;
  padding: 24rpx;
}

/* 顶部 */
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.page-title {
  font-size: 36rpx;
  font-weight: 700;
  color: #333;
}

.btn-add {
  background: #4A7BF7;
  color: #fff;
  font-size: 26rpx;
  padding: 12rpx 28rpx;
  border-radius: 8rpx;
}

/* Tabs */
.tabs {
  display: flex;
  background: #fff;
  border-radius: 12rpx 12rpx 0 0;
  padding: 0 24rpx;
}

.tab-item {
  position: relative;
  padding: 24rpx 32rpx;
  font-size: 28rpx;
  color: #666;
  margin-right: 16rpx;
}

.tab-item.active {
  color: #4A7BF7;
  font-weight: 600;
}

.tab-line {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60%;
  height: 4rpx;
  background: #4A7BF7;
  border-radius: 2rpx;
}

.tab-content {
  background: #fff;
  padding: 24rpx;
  border-radius: 0 0 12rpx 12rpx;
}

/* 表格 */
.table-row {
  display: flex;
  align-items: center;
  padding: 20rpx 12rpx;
  border-bottom: 1px solid #f0f0f0;
  font-size: 26rpx;
  color: #333;
}

.table-header {
  background: #fafafa;
  font-weight: 600;
  color: #666;
  border-radius: 8rpx;
}

.striped {
  background: #fafbff;
}

.col {
  padding: 0 8rpx;
}

.col-name { flex: 2; }
.col-stage { flex: 1; text-align: center; }
.col-score { flex: 1; text-align: center; }
.col-count { flex: 0.8; text-align: center; }
.col-duration { flex: 1; text-align: center; }
.col-status { flex: 1; text-align: center; }
.col-time { flex: 1.5; text-align: center; }
.col-action { flex: 2; text-align: center; }

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
  margin: 0 6rpx;
  font-size: 24rpx;
}

.action-delete {
  color: #FF3B30 !important;
}

/* 卡片 - 诊断模块 */
.card {
  background: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 12rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.card-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
}

.card-tags {
  display: flex;
  gap: 12rpx;
}

.mini-tag {
  font-size: 22rpx;
  background: #eef2ff;
  color: #4A7BF7;
  padding: 4rpx 16rpx;
  border-radius: 6rpx;
}

.card-desc {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 20rpx;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
}

.btn-config {
  background: #4A7BF7;
  color: #fff;
  font-size: 24rpx;
  padding: 10rpx 28rpx;
  border-radius: 8rpx;
}

/* 规则卡片 */
.rule-card {
  display: flex;
  align-items: stretch;
  background: #fff;
  border: 1px solid #f0f0f0;
  border-radius: 12rpx;
  margin-bottom: 16rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
  transition: all 0.3s;
}

.rule-card.rule-highlight {
  border-color: #4A7BF7;
  box-shadow: 0 4rpx 20rpx rgba(74, 123, 247, 0.25);
  transform: scale(1.02);
}

.rule-color {
  width: 12rpx;
  flex-shrink: 0;
}

.rule-info {
  flex: 1;
  padding: 24rpx 28rpx;
}

.rule-top {
  display: flex;
  align-items: center;
  gap: 20rpx;
  margin-bottom: 8rpx;
}

.rule-name {
  font-size: 30rpx;
  font-weight: 600;
}

.rule-range {
  font-size: 24rpx;
  color: #999;
  background: #f5f5f5;
  padding: 4rpx 16rpx;
  border-radius: 6rpx;
}

.rule-desc {
  font-size: 26rpx;
  color: #666;
}

.rule-edit-btn {
  font-size: 22rpx;
  color: #4A7BF7;
  padding: 4rpx 16rpx;
  border: 1px solid #4A7BF7;
  border-radius: 6rpx;
  margin-left: auto;
}

.btn-add-level {
  text-align: center;
  padding: 24rpx;
  color: #4A7BF7;
  font-size: 28rpx;
  border: 1px dashed #4A7BF7;
  border-radius: 12rpx;
  margin-top: 16rpx;
}

/* 规则预览 */
.rule-preview-section {
  margin-bottom: 24rpx;
  padding: 24rpx;
  background: #f8f9ff;
  border-radius: 12rpx;
}

.rule-preview-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 16rpx;
  display: block;
}

.rule-preview-input {
  display: flex;
  align-items: center;
  gap: 24rpx;
}

.score-input {
  width: 300rpx;
  height: 64rpx;
  border: 1px solid #ddd;
  border-radius: 8rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
}

.score-result {
  font-size: 28rpx;
  font-weight: 600;
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
  max-height: 80vh;
  overflow-y: auto;
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

/* 诊断点弹窗 */
.diag-dialog {
  width: 680rpx;
}

.diag-list {
  max-height: 500rpx;
  overflow-y: auto;
  margin-bottom: 20rpx;
}

.diag-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 16rpx;
  border-bottom: 1px solid #f5f5f5;
}

.diag-info {
  flex: 1;
}

.diag-name {
  font-size: 28rpx;
  font-weight: 500;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
}

.diag-purpose {
  font-size: 24rpx;
  color: #999;
  display: block;
  margin-bottom: 8rpx;
}

.diag-meta {
  display: flex;
  gap: 12rpx;
  align-items: center;
}

.diag-tag {
  font-size: 20rpx;
  background: #f0f0f0;
  color: #666;
  padding: 2rpx 12rpx;
  border-radius: 4rpx;
}

.diag-tag-key {
  background: #fff3e0;
  color: #FF9500;
}

.diag-actions {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}

.action-link {
  font-size: 24rpx;
  color: #4A7BF7;
}

.empty-tip-small {
  text-align: center;
  color: #999;
  padding: 40rpx 0;
  font-size: 26rpx;
}

/* 颜色选择 */
.color-options {
  display: flex;
  gap: 16rpx;
  flex: 1;
}

.color-dot {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  border: 3px solid transparent;
}

.color-dot.selected {
  border-color: #333;
  box-shadow: 0 0 8rpx rgba(0, 0, 0, 0.3);
}
</style>
