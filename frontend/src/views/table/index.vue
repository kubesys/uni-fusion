<template>
  <div class="host-header-list">
    <table-type />
    <div class="host-state">
      <annular />
    </div>
  </div>

  <Table />

</template>

<script setup lang="ts">
import {ref} from 'vue'
import Annular from "./annular.vue";
import Select from './select.vue'

import TableType from "./tableType.vue";
import Table from "./table.vue"

</script>

<style scoped>
.host-header-list{
    width: 100%;
    height: 56px;
    display: flex; /* 使用 flex 布局 */
    padding-left: 24px;
    padding-right: 24px;
    margin-top: 20px;
    margin-bottom: 20px;
    justify-content: space-between;
}

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

.host-state{
    display: flex; /* 使用 flex 布局 */
    text-align: right;
    flex: 0;
}

.showAllData{

}

.Tab-card-list{
    padding-top: 20px;
}

::v-deep .el-tabs__item {
    box-shadow: 5px 0 5px -5px rgba(0, 0, 0, 0.5);
}

.custom-table{
    float: left;
    width: 100%;
}

.custom-table .table-pane{
    float: left;
}

.table-style{
    width: auto;
}

.table-box{
    width: 100%;
    overflow: auto;
}
</style>
