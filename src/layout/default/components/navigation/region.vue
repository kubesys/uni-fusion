<template>
  <el-dropdown class="px-2">
    <div class="flex items-center" >
      <el-button class="button-container">
        <icon :size="16" name=el-icon-location-information />
      </el-button>
    </div>

    <template #dropdown>

      <el-dropdown-menu>
        <el-dropdown-item v-for="s of regionItems" :key="s.index" :command="s.index" @click="handleRegionItemClick(s)">
          {{ s.area }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup lang="ts">
import {ref, onMounted} from "vue";
import {getAllMenus, getRegions} from '@/api/user'
import {frontendData} from "@/api/common";

const regionItems = ref([]);

async function allRegions() {
  let retryCount = 0;
  const maxRetries = 3;

  while (retryCount < maxRetries) {
    try {
      const resp = await getRegions();
      regionItems.value = resp.data.data.spec.data;
      break; // 请求成功，跳出循环
    } catch (error) {
      console.error('请求失败：', error);
      // 进行错误处理

      // 延迟一段时间后再次尝试
      await new Promise(resolve => setTimeout(resolve, 10));
      retryCount++;
    }
  }

  if (retryCount === maxRetries) {
    console.error('多次请求失败');
    // 处理多次请求失败的情况
  }
}

function handleRegionItemClick(region) {
  console.log('点击了区域：', region.value);
  localStorage.setItem('region', region.value)
}

allRegions()

</script>
<style>
.font-style{
  color: #cccccc;
  font-weight: bolder;
}
</style>
