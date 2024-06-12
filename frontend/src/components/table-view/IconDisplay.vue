<template>
  <span v-if="isLinux">
    <i class="iconfont icon-linux" />Linux
  </span>
  <span v-else-if="isWindows">
    <i class="iconfont icon-windows-fill" />Windows
  </span>
</template>

<script setup lang="ts">
const props = defineProps({
  text: String,
  row: Object
});

const isLinux = computed(() => {
  return props.text.includes('linux');
});

const isWindows = computed(() => {
  return props.text.includes('windows');
});
</script>

<style scoped>
/* Add any specific styles here */
</style>