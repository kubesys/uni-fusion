<template>
  <a-button @click="onClick" :disabled="disabled" class="action-button">
    <icon class="icon" :name="icon" />
    <span class="text">{{ text }}</span>
  </a-button>
</template>

<script setup lang="ts">
const props = defineProps({
  onClick: Function,
  disabled: Boolean,
  icon: String,
  text: String
});
</script>

<style scoped>
.action-button {
  margin-right: 10px;
}

.icon {
  margin-right: 5px;
}
</style>