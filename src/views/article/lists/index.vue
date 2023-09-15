<template>
  <div class="table_container">
    <template class="host-header-list">
      <table-type />
      <div class="host-state">
        <annular />
      </div>
    </template>

    <Table style="padding-left: 24px; padding-right: 24px; flex: 1"/>
  </div>
</template>

<script setup lang="ts">
import Annular from "./annular.vue";
import TableType from "./tableType.vue";
import Table from "./table.vue"

</script>

<style scoped>
.table_container{
  display: flex;
  flex-direction: column;
}

.host-header-list{
    flex: 1;
    width: 100%;
    display: flex;
    padding-left: 24px;
    padding-right: 24px;
    margin-top: 20px;
    margin-bottom: 20px;
    justify-content: space-between;
}

.host-state{
    display: flex;
    text-align: right;
    flex: 0;
}

</style>
