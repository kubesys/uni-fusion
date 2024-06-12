<template>
  <a-dropdown>
    <a-button>
      <EllipsisOutlined />
    </a-button>
    <template #overlay>
      <a-menu>
        <a-menu-item v-for="action in actions" :key="action.key" @click="() => onOptionClick(action.label, action.action, text)">
          <a target="_blank">{{ action.label }}</a>
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
import { EllipsisOutlined } from '@ant-design/icons-vue';

const props = defineProps({
  actions: Array,
  text: String,
  onOptionClick: Function
});
</script>

<style scoped>
/* Add any specific styles here */
</style>