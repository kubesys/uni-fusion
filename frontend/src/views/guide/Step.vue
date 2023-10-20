<script setup lang="ts">
const activeStep =  {
  required: true,
      type: Number
}
</script>

<template>
  <div style="height: 300px; ">
    <el-steps direction="vertical" :active="activeStep">
      <el-step title="基础配置"></el-step>
      <el-step title="资源配置"></el-step>
      <el-step title="确认信息"></el-step>
    </el-steps>
  </div>
</template>

<style scoped lang="scss">
.vertical-steps {
  display: flex;
  flex-direction: column;
  width: 200px;
}

.vertical-step {
  width: 100%;
}
</style>
