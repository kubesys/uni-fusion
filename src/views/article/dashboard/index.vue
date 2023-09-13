<template>
  <div>
    <iframe :src=source style="width: 100%;height: 1000px"/>
  </div>
</template>
<script setup lang="ts">
const route = useRoute()
const source = route.meta?.source
console.log(source)
</script>

