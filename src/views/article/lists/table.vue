<template>
  <div>
    <el-form :model="formData" ref="form" label-width="auto" label-position="left" style="margin-top: 80px">
      <el-row>
        <el-col v-for="item in formItems" :key="item.path" :span="7" style="margin-right: 20px">
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
        </el-col>
      </el-row>
    </el-form>
    <el-button type="primary" @click="handleCreateClick"  style="text-align: left; background-color: #3967FF;"><icon name="el-icon-Plus" :size="16" style="margin-right: 5px"/>创建</el-button>
    <el-button @click="submitForm" style="text-align: left; background-color: #d7d9dc;"><icon name="el-icon-RefreshRight" :size="18" /></el-button>
    <el-button @click="submitForm" style="text-align: left; background-color: #d7d9dc;">提交</el-button>

    <el-table :data="tableData.items"
              :header-cell-style="{ color: '#000'}"
              :max-height="500"
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
<!--                            <a :href="generateLink(column, tableData)">{{ getLinkText(column, item) }}</a>-->

            </el-table-column>
          </template>

          <template v-else>
            <!-- 外部链接 -->
            <el-table-column :key="column.row" :label="column.label" :prop="column.row"></el-table-column>
          </template>
        </template>

        <template v-else-if="column.kind === 'action'" >
          <!-- 操作列 -->
          <el-table-column :key="column.row" :label="column.label" :prop="column.row">
            <template #default="scope">
              <el-select placeholder="请选择" style="width: 100px">
                <el-option v-for="(item, index) in actions" :key="index" :label="item.name" :value="item.type" @click="handleOptionClick(item.name, item.type, scope.row)">
                  {{ item.name }}
                </el-option>
              </el-select>
            </template>
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
        @current-change="handleCurrentChange"
    ></el-pagination>
    <el-dialog
        v-model="dialogVisible"
        :title=selectedItemName
        width="50%">
      <div class="dialog-content">
        <el-card v-for="(group, groupName) in scaleItems.data" :key="groupName" style="border:1px solid #d2d2d2; width: 1000px; margin-top:10px;">
          <el-form :model="group" :rules="getRules(group)">
            <el-form-item v-for="(field, fieldName) in group" :key="fieldName" :label="field.value">
              <template v-if="field.type === 'textbox'">
                <!--              <el-input v-model="group[fieldName]" :placeholder="field.value"></el-input>-->
                <el-input  type="textarea"
                           :rows="5"/>
              </template>
              <template v-else-if="field.type === 'text'">
                <!--              <el-input v-model="group[fieldName]" :placeholder="field.value"></el-input>-->
                <el-input v-model="replicaset" />
              </template>
              <template v-else-if="field.type === 'select'">
                <el-select v-model="group[fieldName]" :placeholder="field.value">
                  <el-option v-for="(option, optionIndex) in selectOptions" :key="optionIndex" :label="option.label" :value="option.value"></el-option>
                </el-select>
              </template>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveData">确定</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import router from "@/router";
import {onMounted, ref} from 'vue'
import {frontendFormSearch, frontendData, frontendAction, frontendUpdate} from "@/api/common";
import Step1 from "@/views/guide/Step1.vue";

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
const dialogVisible = ref(false)

console.log(listname, tablename, props)

const tableColumns = ref([
  // 列配置数据
])
const tableData = ref({
  metadata:{
    totalCount:''
  },
  items:[],
  actions:[]
})

const actions = ref([])

const pageSite = ref({limit:5,page:1})
const tableDataLoaded = ref(false)
const formData = ref<Record<string, string>>({}); // 表单数据对象
const formItems: FormItem[] = ref([]); // 用于存储生成的表单项

const scaleItems = ref([])
const selectedItemName = ref(''); // 初始化选中的选项为空
const selectOptions = ref([
  { label: "Option 1", value: "option1" },
  { label: "Option 2", value: "option2" },
  // Add more options as needed
]);


onMounted(()=>{
  frontendFormSearch(tablename, formItems)
  frontendData(listname, tablename, pageSite,tableColumns, tableData, actions)
  frontendAction(tablename, scaleItems)
  tableDataLoaded.value = true
})

// function handleSizeChange(newSize) {
//   this.pageSize = newSize;
//   this.currentPage = 1; // 切换每页显示条数时重置当前页码
//   this.fetchData(); // 重新请求数据
// }
function handleCurrentChange(newPage) {
  pageSite.value.page = newPage
  frontendData(listname, tablename, pageSite,tableColumns, tableData,props.value)
}

const getRules = (group) => {
  const rules = {};
  for (const field in group) {
    if (group[field].required === "true") {
      rules[field] = [{ required: true, message: `${group[field].value}不能为空`, trigger: 'blur' }];
    }
  }
  return rules;
};

// function generateExternalLink(column, rowData) {
//   return 'https://www.baidu.com/' + rowData[column.row];
// }
// function generateLink(column, item) {
//   if (column.link.startsWith('@')) {
//     const tag = column.tag ? column.tag.split('##') : [];
//     const linkParts = column.link.split(';');
//     const apiVersion = getPropertyValue(item, linkParts[0].substring(1));
//     const kind = getPropertyValue(item, linkParts[2]);
//     return `/${apiVersion}/${kind}/${tag.join('/')}`;
//   }
//   return column.link;
// }

// function getLinkText(column, item) {
//   if (column.link.startsWith('@')) {
//     const tag = column.tag ? column.tag.split('##') : [];
//     return this.getPropertyValue(item, tag[tag.length - 1]);
//   }
//   return column.label;
// }
// function getPropertyValue(item, propertyPath) {
//   const properties = propertyPath.split('.');
//   let value = item;
//   for (let i = 0; i < properties.length; i++) {
//     const property = properties[i];
//     if (property.startsWith('@')) {
//       value = value[property.substring(1)];
//     } else if (property.includes('[') && property.includes(']')) {
//       const arrayProperty = property.substring(0, property.indexOf('['));
//       const index = parseInt(property.substring(property.indexOf('[') + 1, property.indexOf(']')));
//       value = value[arrayProperty][index];
//     } else {
//       value = value[property];
//     }
//   }
//   return value;
// }
function handleCreateClick(){
  router.push('/test')
}
function submitForm() {
  frontendData(listname, tablename, pageSite,tableColumns, tableData, props.value)
}

function handleOptionClick(name, type, rowData) {
  if (type === 'Update') {
    // 跳转到更新页面
    // router.push('/test');
    dialogVisible.value = true;
    selectedItemName.value = name
    console.log('点击了操作列，当前行数据：', rowData);
    props.value = rowData
  } else if (type === 'Delete') {
    // 跳转到删除页面
    router.push('/delete-page');
  }
}

const replicaset = ref<number>(0);
function saveData(){
  const rowData = props.value
  console.log(rowData)
  rowData.spec.replicas = Number(replicaset.value)
  console.log(rowData)
  frontendUpdate(rowData)
  dialogVisible.value = false;
}

</script>

<style lang="scss" scoped>
//修改行内线
:deep(.el-table td),
.building-top :deep(.el-table th.is-leaf) {
  border-bottom: 1px solid #cbc7c7;
}

.el-table::v-deep .el-table__header {
  border-bottom: 1px solid #8d9095;
}

.el-table::after {
  content: "";
  display: block;
  border-bottom: 1px solid #cbc7c7;
  height: 4px;
}

/* 在样式中设置对话框内容的布局 */
.dialog-content {
  display: flex; /* 使用flex布局 */
  flex-wrap: wrap; /* 换行 */
  justify-content: space-between; /* 横向分布 */
  align-items: center; /* 垂直居中 */
  margin: 50px;
}

.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>
