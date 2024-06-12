<template>
  <div class="stat-item">
    <span :class="['indicator', colorClass]" />
    <div class="label">{{ label }}</div>
    <div class="value">{{ value }}</div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  label: String,
  value: Number,
  color: {
    type: String,
    default: '#dbdde0'
  }
});

const colorClass = computed(() => {
  return {
    running: props.color === '#57D344',
    stopped: props.color === 'red',
    other: props.color === '#dbdde0',
    recycleBin: props.color === '#96989b'
  };
});
</script>

<style scoped>
.stat-item {
  display: flex;
  align-items: center;
}

.indicator {
  display: inline-block;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  margin-right: 5px;
}

.running {
  background: #57d344;
}

.stopped {
  background: red;
}

.other {
  background: #dbdde0;
}

.recycleBin {
  background: #96989b;
}

.label {
  margin-right: 10px;
}

.value {
  font-weight: bold;
}
</style>