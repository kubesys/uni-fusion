<template>
  <div class="header-introduce">
    <div class="header-introduce-name">{{ descItem.title }}</div>
    <div class="header-introduce-detail">{{ descItem.desc }}</div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import { getTableDesc } from '@/api/tableconfig'
import {frontendMeta} from "@/api/common";

const route = useRoute()
const tablename = route.meta?.tablename
const descItem = ref([]);

const region = ref(localStorage.getItem('region'));

frontendMeta(tablename, descItem);

// 监听 storage 事件，在数据发生变化时执行回调函数

// onMounted(() => tableDesc());
</script>


<style scoped lang="scss">
.header-introduce{
  float: left;
  text-align: left;
}

.header-introduce .header-introduce-name{
  font-size: 25px;
}

.header-introduce .header-introduce-detail{
  font-size: 10px;
  color: #909399;
}
</style>
