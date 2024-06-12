<template>
  <span>
    <RightCircleTwoTone v-if="isRunning" twoToneColor="#52c41a" />
    <PauseCircleTwoTone v-else-if="isPaused" twoToneColor="#eb2f96" />
    <StopTwoTone v-else twoToneColor="red" />
  </span>
</template>

<script setup lang="ts">
import { RightCircleTwoTone, PauseCircleTwoTone, StopTwoTone } from '@ant-design/icons-vue';

const props = defineProps({
  status: String
});

const isRunning = computed(() => {
  return props.status === 'Running';
});

const isPaused = computed(() => {
  return props.status === 'Paused';
});
</script>

<style scoped>
/* Add any specific styles here */
</style>