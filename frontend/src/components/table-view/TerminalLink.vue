<template>
  <el-link v-if="isLinkActive" type="primary" :href="terminalAddr" target="_blank">
    <CreditCardOutlined />
  </el-link>
  <el-link v-else type="primary" disabled>
    <el-icon><CreditCardOutlined /></el-icon>
  </el-link>
</template>

<script setup lang="ts">
import { CreditCardOutlined } from '@ant-design/icons-vue';

const props = defineProps({
  text: String,
  terminalLink: String
});

const isLinkActive = computed(() => {
  return props.text !== 'inactive';
});

const terminalAddr = computed(() => {
  return `http://${props.text}:3000/${props.terminalLink}`;
});
</script>

<style scoped>
/* Add any specific styles here */
</style>