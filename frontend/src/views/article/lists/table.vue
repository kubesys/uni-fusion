<template>
  <template class="host-header-list">
    <table-type />
    <div class="host-state">
      <Annular :key="annular"/>
    </div>
  </template>
  <div class="frontendTable_container" style="border-top:1px solid #dbdde0; width: 100%">
    <!-- The Form section of the Table page -->
    <el-form :model="formData" ref="form" label-width="auto" label-position="left" style="margin-top: 20px">
      <el-row>
        <el-col v-for="formItem in formItems" :key="formItem.path" :span="7" style="margin-right: 20px">
          <el-form-item :label="formItem.label">
            <template v-if="formItem.type === 'textbox'">
              <el-input v-model="propslecet[formItem.path]" style="width: 100%;" ></el-input>
            </template>
            <template v-else-if="formItem.type === 'combobox'">
              <el-select v-model="propslecet[formItem.path]" style="width: 100%;" @click="getFormDataValueIndex(formItem.data)">
                <el-option v-for="(option, index) in optionArray" :key="index" :label="option.label" :value="option.value">
                </el-option>
              </el-select>
            </template>
            <!-- 可以根据需要添加其他类型的表单组件 -->
          </el-form-item>
        </el-col>
        <el-col :span="7">
        </el-col>
      </el-row>
    </el-form>
    <!-- Table page action button -->
    <a-space>
      <a-button @click="submitForm" style="border: none; background-color: #f0f2f5; border-radius: 0px"><icon class="icon" name="el-icon-RefreshRight" :size="18" /></a-button>
      <a-button type="primary" @click="creatDialog.showAndInit(ListName, TableName)"  style="border: none; background-color: #005bd4; border-radius: 0px;">
          <icon class="icon" name="el-icon-Plus" />
          <span class="text">创建云主机</span>
      </a-button>
      <a-button type="primary" style="border: none; background-color: #005bd4; border-radius: 0px;">
        <el-icon class="icon"><MoreFilled /></el-icon>
      </a-button>

      <a-button type="primary" style="border: none;border-radius: 0px;background-color: #f7f8fa" disabled>
        <icon class="icon" name="el-icon-video-play" />
        <span class="text">启动</span>
      </a-button>

      <a-button type="primary" style="border: none;border-radius: 0px;background-color: #f7f8fa" disabled>
        <icon class="icon" name="el-icon-video-pause" />
        <span class="text">停止</span>
      </a-button>

      <a-dropdown ><a-button style="border: none;border-radius: 0px;background-color: #f0f2f5">
        <span class="text">批量操作</span>
        <icon class="icon" name="el-icon-arrow-down" />
      </a-button></a-dropdown>

      <a-button @click="submitForm" style="background-color: #d7d9dc;border-radius: 0px">
        <icon class="icon" name="el-icon-search" />
        <span class="text">搜索</span>
      </a-button>
    </a-space>

<!--     Table table component-->
<!--    <el-table-->
<!--        :data="tableData.items"-->
<!--        :header-cell-style="{ color: '#000'}"-->
<!--        :max-height="500"-->
<!--        highlight-current-row-->
<!--        size="large">-->
<!--      >-->
<!--      <el-table-column-->
<!--          type="selection"-->
<!--          width="55">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--          v-for="item in tableColumns"-->
<!--          align="center"-->
<!--          :key="item.key"-->
<!--          sortable-->
<!--          :label="item.label"-->
<!--      >-->
<!--        <template #default="scope">-->
<!--          &lt;!&ndash;          <router-link&ndash;&gt;-->
<!--          &lt;!&ndash;              v-if="item.kind === 'internalLink'"&ndash;&gt;-->
<!--          &lt;!&ndash;              to="baidu">&ndash;&gt;-->
<!--          &lt;!&ndash;            <el-link type="primary">&ndash;&gt;-->
<!--          &lt;!&ndash;              {{ getComplexDataDispose(scope.row, item.row) }}&ndash;&gt;-->
<!--          &lt;!&ndash;            </el-link>&ndash;&gt;-->
<!--          &lt;!&ndash;          </router-link>&ndash;&gt;-->
<!--          <router-link-->
<!--              v-if="item.kind === 'internalLink' && item.internalLink && item.internalLink.kind"-->
<!--              :to="{-->
<!--                path: item.internalLink.kind.indexOf('@') === -1 ? item.internalLink.kind : getComplexDataDispose(scope.row, item.internalLink.kind),-->
<!--                query: {-->

<!--                }-->
<!--              }"-->
<!--          >-->
<!--            <el-link type="primary" v-if="item.internalLink.kind.indexOf('@') !== -1">{{-->
<!--                getComplexDataDispose(scope.row, item.internalLink.item.substring(1))-->
<!--              }}</el-link>-->
<!--            <el-link type="primary" v-else>-->
<!--              {{ getComplexDataDispose(scope.row, item.row) }}-->
<!--            </el-link>-->
<!--          </router-link>-->
<!--          <el-link v-else-if="item.kind === 'terminalLink'" type="primary" :underline="false" :href="getTerminalAddr(scope.row, item.terminalLink)" target="_blank">-->
<!--            <el-icon :size="20">-->
<!--              <component :is="item.terminalLink.icon"></component>-->
<!--            </el-icon>-->
<!--          </el-link>-->
<!--          <el-select-->
<!--              v-else-if="item.kind === 'action'"-->
<!--              placeholder="请选择"-->
<!--              style="width: 100px">-->
<!--            <el-option v-for="action in item.actionLink" :key="action.key" :label="action.label" :value="action.action" @click="handleOptionClick(action.label, action.action, scope.row)" />-->
<!--          </el-select>-->
<!--          <span v-else>-->
<!--            {{ getComplexDataDispose(scope.row, item.row) }}-->
<!--          </span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--    </el-table>-->

    <a-table :row-selection="rowSelection"
             :columns="tableColumns"
             :data-source="tableData.items"
             :scroll="{ x: 2000 }"
             style="margin-top: 5px"
             >
      <template #bodyCell="{ column, text }" >
<!--        <template v-if="column.kind === 'internalLink' && column.internalLink && column.internalLink.kind">-->

<!--          <router-link-->

<!--              :to="{-->
<!--                path: column.internalLink.kind.indexOf('@') === -1 ? column.internalLink.kind : getComplexDataDispose(scope.row, column.internalLink.kind),-->
<!--                query: {-->

<!--                }-->
<!--              }"-->
<!--          >-->
<!--            <el-link type="primary" v-if="column.internalLink.kind.indexOf('@') !== -1">{{-->
<!--                getComplexDataDispose(scope.row, column.internalLink.item.substring(1))-->
<!--              }}</el-link>-->
<!--            <el-link type="primary" v-else>-->
<!--              {{ getComplexDataDispose(column, column.row) }}-->
<!--            </el-link>-->
<!--          </router-link>-->
<!--        </template>-->

<!--        <template v-else-if="column.kind === 'terminalLink'">-->
<!--          <el-link type="primary" :underline="false" :href="getTerminalAddr(scope.row, column.terminalLink)" target="_blank">-->
<!--            <el-icon :size="20">-->
<!--              <component :is="column.terminalLink.icon"></component>-->
<!--            </el-icon>-->
<!--          </el-link>-->
<!--        </template>-->

<!--        <template v-eles-if="column.kind === 'action'">-->
<!--          <el-select-->
<!--              placeholder="请选择"-->
<!--              style="width: 100px">-->
<!--            <el-option v-for="action in column.actionLink" :key="action.key" :label="action.label" :value="action.action" @click="handleOptionClick(action.label, action.action, scope.row)" />-->
<!--          </el-select>-->
<!--        </template>-->

        <template >
<!--            {{ getComplexDataDispose(scope.row, column.row) }}-->
        </template>
      </template>

    </a-table>

    <!-- Table pagination -->
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
        width="60%">
      <!--      <div class="dialog-content">-->
      <!--        <el-card  style="border:1px solid #d2d2d2; width: 1000px; margin-top:10px;">-->
      <!--          <el-form v-for="group in scaleItems.data" :key="group.key" :model="group" :rules="getRules(group)" label-width="90px" label-position="left" >-->
      <!--            <el-form-item  :label="group.label">-->
      <!--              <template v-if="group.type === 'combobox'">-->
      <!--                &lt;!&ndash;              <el-input v-model="group[fieldName]" :placeholder="field.value"></el-input>&ndash;&gt;-->
      <!--                <el-select style="width: 200px">-->
      <!--                  <el-option></el-option>-->
      <!--                </el-select>-->
      <!--              </template>-->
      <!--              <template v-else-if="group.type === 'text'">-->
      <!--                &lt;!&ndash;              <el-input v-model="group[fieldName]" :placeholder="field.value"></el-input>&ndash;&gt;-->
      <!--                <el-input style="width: 200px"/>-->
      <!--              </template>-->
      <!--              <template v-if="group.type === 'range'">-->
      <!--                <el-input-number v-model="group.lessThan" :min="1" :max="10"  />-->
      <!--              </template>-->
      <!--&lt;!&ndash;              <template v-else-if="field.type === 'select'">&ndash;&gt;-->
      <!--&lt;!&ndash;                <el-select v-model="group[fieldName]" :placeholder="field.value">&ndash;&gt;-->
      <!--&lt;!&ndash;                  <el-option v-for="(option, optionIndex) in selectOptions" :key="optionIndex" :label="option.label" :value="option.value"></el-option>&ndash;&gt;-->
      <!--&lt;!&ndash;                </el-select>&ndash;&gt;-->
      <!--&lt;!&ndash;              </template>&ndash;&gt;-->
      <!--              <template v-else-if="group.type === 'select'">-->
      <!--                <el-select v-model="group[group]" :placeholder="group.value">-->
      <!--                  <el-option v-for="(option, optionIndex) in selectOptions" :key="optionIndex" :label="option.label" :value="option.value"></el-option>-->
      <!--                </el-select>-->
      <!--              </template>-->
      <!--            </el-form-item>-->
      <!--          </el-form>-->
      <!--        </el-card>-->
      <!--      </div>-->

      <!-- 使用 vue-json-pretty 显示 JSON 数据 -->
      <el-scrollbar height="500px">
        <div  style="display: flex">
          <vue-json-pretty :data="rowItemData" style="flex: 1"></vue-json-pretty>
          <pre>{{ yaml }}</pre>
        </div>
      </el-scrollbar>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveData">确定</el-button>
      </span>
      </template>
    </el-dialog>

    <CreateJsonDialog ref="creatDialog"/>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
// import Stomp from 'stompjs';
// import {MQTT_SERVICE, MQTT_USERNAME, MQTT_PASSWORD, MQTT_topic} from '@/rabbitmq/mqtt';
import { frontendFormSearch, frontendData, frontendUpdate, getComplexDataDispose, getFormDataValue, getTerminalAddr, actionDataValue } from "@/api/common";
import  CreateJsonDialog  from "@/views/article/lists/template/createTemplate.vue";
import VueJsonPretty from 'vue-json-pretty';
import 'vue-json-pretty/lib/styles.css';
import jsYaml from 'js-yaml';
import TableType from "@/views/article/lists/tableType.vue";
import Annular from "@/views/article/lists/annular.vue";

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

interface tableColumns {
  label: string;
  row: string;
  internalLink: object;
  actionLink: object;
  terminalLink: object
}

const route = useRoute()
const ListName = route.meta?.listname
const TableName = route.meta?.tablename
const filter = route.meta?.filter || {}
const props = ref('')
const propslecet = ref({})
const allLabels = ref(filter)
const dialogVisible = ref(false)

// 列配置数据
const tableColumns:tableColumns = ref([])
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
const rowItemData = ref()
const selectOptions = ref([]);
const yaml = ref<string>('');

watch(rowItemData, () => {
  // 当 JSON 数据发生变化时，将其转换为 YAML
  yaml.value = jsYaml.dump(rowItemData.value);
}, { deep: true });

watch(yaml, () => {
  // 当 YAML 数据发生变化时，将其转换回 JSON
  try {
    rowItemData.value = jsYaml.load(yaml.value);
  } catch (error) {
    console.error('YAML 解析错误：', error);
  }
});

onMounted(()=>{
  frontendFormSearch(TableName, formItems)
  frontendData(ListName, TableName, pageSite,tableColumns, tableData,allLabels.value, actions)
  // frontendAction(TableName, scaleItems)
  tableDataLoaded.value = true
})

getComplexDataDispose

/************************
 *
 *  页面操作
 *
 ***********************/
const optionArray = ref(

)
function getFormDataValueIndex(data:any){
  getFormDataValue(data, optionArray)
}

// a-table选择框
const rowSelection = ref({
  checkStrictly: false,
  onChange: (selectedRowKeys: (string | number)[], selectedRows: tableData.value.items[]) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
  },
  onSelect: (record: tableData.value.items, selected: boolean, selectedRows: tableData.value.items[]) => {
    console.log(record, selected, selectedRows);
  },
  onSelectAll: (selected: boolean, selectedRows: tableData.value.items[], changeRows: tableData.value.items[]) => {
    console.log(selected, selectedRows, changeRows);
  },
});

function handleCurrentChange(newPage) {
  pageSite.value.page = newPage
  frontendData(ListName, TableName, pageSite,tableColumns, tableData,propslecet.value, actions)
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

// 创建对话框的引用
const creatDialog = ref()
const annular = ref(0)

function submitForm() {
  frontendData(ListName, TableName, pageSite,tableColumns, tableData, propslecet.value, actions)
}

function handleOptionClick(dialogname, action, rowData) {
  actionDataValue(TableName, ListName, dialogVisible, selectedItemName, rowItemData, dialogname, action, rowData)
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

/************************
 *
 *  连接Rabbitmq部分
 *
 ***********************/
// const client = Stomp.client(MQTT_SERVICE);
//
// function onConnected() {
//   const topic = MQTT_topic;
//   const subscribeHeaders = {
//     durable: false, // 设置队列为非持久化
//     'auto-delete': false,
//     exclusive: false
//   };
//   client.subscribe(topic, responseCallback, subscribeHeaders, onFailed);
// }
//
// function onFailed(msg: any) {
//   console.log("MQ Failed: " + msg);
// }
//
// function responseCallback(msg: any) {
//   console.log("MQ msg => " + msg.body);
//   submitForm()
//   annular.value += 1
//   // location.reload();
// }
//
// function connect() {
//   const headers = {
//     login: MQTT_USERNAME,
//     password: MQTT_PASSWORD,
//   };
//   client.connect(headers, onConnected, onFailed);
// }
//
// onMounted(()=>{connect()})


</script>

<style lang="scss" scoped>
.icon {
  float: left;
  width: 15px;
  height: 20px;
}
.text {
  float: left;
  padding: 0 5px 0 5px;
  font-size: 14px;
  line-height: 20px;
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

.frontendTable_container{
  padding-left: 24px;
  padding-right: 24px;
}

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

::v-deep(.ant-table-cell) {
  background: #ffffff !important;
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
