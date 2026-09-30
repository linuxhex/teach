<script>
export default {
  onLaunch: function () {
    console.log('App Launch')
  },
  onShow: function () {
    console.log('App Show')
  },
  onHide: function () {
    console.log('App Hide')
  },
}
</script>

<style>
/* 隐藏原生tabBar，使用自定义TabBar组件 */
.uni-tabbar {
  display: none !important;
}
</style>
