<template>
  <div class="header-introduce">
    <div class="header-introduce-name">{{ descItem.title }}</div>
    <div class="header-introduce-detail">{{ descItem.desc }}</div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import {frontendMeta} from "@/api/common";

const route = useRoute()
const tablename = route.meta?.tablename
const descItem = ref([]);

const region = ref(localStorage.getItem('region'));

frontendMeta(tablename, descItem);

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
  font-size: 12px;
  color: #909399;
}
</style>
