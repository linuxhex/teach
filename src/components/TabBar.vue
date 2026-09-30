<template>
  <view class="tabbar-wrapper">
    <view class="tabbar">
      <view
        v-for="item in tabs"
        :key="item.key"
        class="tabbar-item"
        :class="{ active: current === item.key }"
        @click="switchTo(item)"
      >
        <text class="tabbar-icon" :class="{ active: current === item.key }">{{ item.icon }}</text>
        <text class="tabbar-text" :class="{ active: current === item.key }">{{ item.label }}</text>
      </view>
    </view>
    <view class="safe-area-bottom"></view>
  </view>
</template>

<script setup>
const props = defineProps({
  current: {
    type: String,
    default: 'index'
  }
})

const tabs = [
  { key: 'index', label: '首页', icon: '⌂', path: '/pages/index/index' },
  { key: 'assessment', label: '测评', icon: '✎', path: '/pages/assessment/index' },
  { key: 'resources', label: '资料库', icon: '▤', path: '/pages/resources/index' },
  { key: 'profile', label: '我的', icon: '○', path: '/pages/profile/index' }
]

function switchTo(item) {
  if (props.current === item.key) return
  uni.switchTab({ url: item.path })
}
</script>

<style scoped>
.tabbar-wrapper {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #ffffff;
  border-top: 1rpx solid #E8E8E8;
  z-index: 999;
}

.tabbar {
  display: flex;
  flex-direction: row;
  height: 100rpx;
}

.tabbar-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.tabbar-icon {
  font-size: 40rpx;
  color: #999999;
  line-height: 1;
  margin-bottom: 4rpx;
}

.tabbar-icon.active {
  color: #4A7BF7;
}

.tabbar-text {
  font-size: 20rpx;
  color: #999999;
  line-height: 1;
}

.tabbar-text.active {
  color: #4A7BF7;
}

.safe-area-bottom {
  padding-bottom: env(safe-area-inset-bottom);
}
</style>
