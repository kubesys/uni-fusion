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
</template>

<script setup lang="ts">
import { ref } from 'vue';
import {frontendFormSearch,frontendData} from "@/api/common";

const props = ref()

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

const formData = ref<Record<string, string>>({}); // 表单数据对象
const formItems: FormItem[] = ref([]); // 用于存储生成的表单项
const buttonItem = ref([]); // 用于存储生成的表单项

const route = useRoute()
const tablename = route.meta?.tablename
const listname = route.meta?.listname
// 假设后台传递的数据为 response
const pageSite = ref({limit:10,page:1})
const tableColumns = ref([
  // 列配置数据
])
const tableData = ref({
  metadata:{
    totalCount:''
  },
  items:[]
})

frontendFormSearch(tablename, formItems, buttonItem)
// frontendData(listname, tablename, pageSite,tableColumns, tableData, props)
function submitForm() {
  frontendData(listname, tablename, pageSite,tableColumns, tableData, props.value)
}


</script>
