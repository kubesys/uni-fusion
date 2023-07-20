<template>

  <el-form :model="formData" ref="form" label-width="120px" style="margin-top: 80px">
    <el-row>
      <el-col v-for="item in formItems" :key="item.path" :span="7">
        <el-form-item :label="item.label">
          <template v-if="item.type === 'textbox'">
            <el-input v-model="props" style="width: 100%;"></el-input>
          </template>
          <template v-else-if="item.type === 'combobox'">
            <el-select v-model="formData[item.path]" style="width: 100%;">
              <el-option v-for="(option, index) in item.data" :key="index" :label="option.label" :value="option.value"></el-option>
            </el-select>
          </template>
          <!-- 可以根据需要添加其他类型的表单组件 -->
        </el-form-item>
      </el-col>
      <el-col :span="7">
        <el-form-item>
          <el-button type="primary" @click="submitForm" style="text-align: left;"><icon name="el-icon-RefreshRight" :size="18" /></el-button>
          <el-button type="primary" @click="submitForm" style="text-align: left;">提交</el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>

  <el-table clsss="customer-table"
            :data="tableData.items"
            :header-cell-style="{background: '#eaeff3', color: '#000'}"
            :max-height="500"
            class="table-style"
            size="large">
    <el-table-column
        type="selection"
        width="55">
    </el-table-column>
    <!-- 列配置 -->
    <template v-for="(column, index) in tableColumns" :key="index" :label="column.label">
      <!-- 自定义列内容 -->
      <template v-if="column.kind === 'internalLink'">
        <template v-if="column.link.startsWith('@')">
          <!-- 内部链接 -->
            <el-table-column :key="column.row" :label="column.label" :prop="column.row">
<!--              <a :href="generateLink(column, tableData)">{{ getLinkText(column, item) }}</a>-->
            </el-table-column>
        </template>

        <template v-else>:
          <!-- 外部链接 -->
          <el-table-column :key="column.row" :label="column.label" :prop="column.row"></el-table-column>
        </template>
      </template>

      <template v-else-if="column.kind === 'action'">
        <!-- 操作列 -->
        <el-table-column :key="column.row" :label="column.label" :prop="column.row">
          <el-select placeholder="请选择">
            <el-option/>
          </el-select>
        </el-table-column>
      </template>

      <template v-else>
        <!-- 其他类型列 -->
        <el-table-column :key="column.row" :label="column.label" :prop="column.row"></el-table-column>
      </template>
    </template>
  </el-table>
  <el-pagination
      v-if="tableDataLoaded"
      :page-size=pageSite.limit
      :current-page=pageSite.page
      :total=tableData.metadata.totalCount
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
  ></el-pagination>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {frontendFormSearch, frontendData} from "@/api/common";

interface Option {
  label: string;
  value: string;
}
interface FormItem {
  label: string;
  path: string;
  type: 'textbox' | 'combobox';
  data: Option[];
}

const route = useRoute()
const listname = route.meta?.listname
const tablename = route.meta?.tablename
const props = ref()

console.log(listname, tablename, props)

const tableColumns = ref([
  // 列配置数据
])
const tableData = ref({
  metadata:{
    totalCount:''
  },
  items:[]
})
const pageSite = ref({limit:10,page:1})
const tableDataLoaded = ref(false)
const formData = ref<Record<string, string>>({}); // 表单数据对象
const formItems: FormItem[] = ref([]); // 用于存储生成的表单项


onMounted(()=>{
  frontendFormSearch(tablename, formItems)
  frontendData(listname, tablename, pageSite,tableColumns, tableData)
  tableDataLoaded.value = true
})

function handleSizeChange(newSize) {
  this.pageSize = newSize;
  this.currentPage = 1; // 切换每页显示条数时重置当前页码
  this.fetchData(); // 重新请求数据
}
function handleCurrentChange(newPage) {
  pageSite.value.page = newPage
  frontendData(listname, tablename, pageSite,tableColumns, tableData,props.value)
}

function generateLink(column, item) {
  if (column.link.startsWith('@')) {
    const tag = column.tag ? column.tag.split('##') : [];
    const linkParts = column.link.split(';');
    const apiVersion = getPropertyValue(item, linkParts[0].substring(1));
    const kind = getPropertyValue(item, linkParts[2]);
    return `/${apiVersion}/${kind}/${tag.join('/')}`;
  }
  return column.link;
}

function getLinkText(column, item) {
  if (column.link.startsWith('@')) {
    const tag = column.tag ? column.tag.split('##') : [];
    return this.getPropertyValue(item, tag[tag.length - 1]);
  }
  return column.label;
}
function getPropertyValue(item, propertyPath) {
  const properties = propertyPath.split('.');
  let value = item;
  for (let i = 0; i < properties.length; i++) {
    const property = properties[i];
    if (property.startsWith('@')) {
      value = value[property.substring(1)];
    } else if (property.includes('[') && property.includes(']')) {
      const arrayProperty = property.substring(0, property.indexOf('['));
      const index = parseInt(property.substring(property.indexOf('[') + 1, property.indexOf(']')));
      value = value[arrayProperty][index];
    } else {
      value = value[property];
    }
  }
  return value;
}

function submitForm() {
  frontendData(listname, tablename, pageSite,tableColumns, tableData, props.value)
}
</script>

<style>
.el-table .el-table__row {
  border-bottom: 2px solid #000;
}
</style>
