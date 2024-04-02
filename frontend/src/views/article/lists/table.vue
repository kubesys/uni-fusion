<template>
  <template class="host-header-list">
    <table-type />
    <div class="host-state">
      <div id="myChart" :style="{ width: '70px', height: '52px' ,flex: 1}"></div>
      <div style="margin-right: 10px; flex: 1">
        <div style="float:left; margin-top: 10px; font-size: 12px">
          总数
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; font-weight: bolder; font-size: 12px">
          {{tableData.metadata.totalCount || 0}}
        </div>
      </div>
      <div style="margin-right: 20px; flex: 1">
        <span style="float:left; width: 3px; height: 30px; background: #57D344; display: inline-block; transform: translate(-50%, 40%);" />
        <div style="float:left; margin-top: 10px; margin-left: 5px; font-size: 12px">
          运行中
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; margin-left: 5px;font-weight: bolder; font-size: 12px">
          <!--          {{ tableData.resultRun }}-->
          0
        </div>
      </div>

      <div style="margin-right: 20px; flex: 1">
        <span style="float:left; width: 3px; height: 30px; background: red; display: inline-block; transform: translate(-50%, 40%);" />
        <div style="float:left; margin-top: 10px; margin-left: 5px; font-size: 12px">
          已停止
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; margin-left: 5px;font-weight: bolder; font-size: 12px">
          {{ tableData.resultPen }}
        </div>
      </div>

      <div style="margin-right: 20px; flex: 1">
        <span style="float:left; width: 3px; height: 30px; background: #dbdde0; display: inline-block; transform: translate(-50%, 40%);" />
        <div style="float:left; margin-top: 10px; margin-left: 5px; font-size: 12px">
          其他
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; margin-left: 5px;font-weight: bolder; font-size: 12px">
          0
        </div>
      </div>

      <div style="margin-right: 20px; flex: 1">
        <span style="float:left; width: 3px; height: 30px; background: #96989b; display: inline-block; transform: translate(-50%, 40%);" />
        <div style="float:left; margin-top: 10px; margin-left: 5px; font-size: 12px">
          回收站
        </div>
        <div style="float:left;margin-top: 3px;  margin-right:30px; margin-left: 5px;font-weight: bolder; font-size: 12px">
          0
        </div>
      </div>
    </div>
  </template>
  <div class="frontendTable_container" style="border-top:1px solid #dbdde0; width: 100% ">

    <a-row>
      <a-col :span="route.meta?.structure == 'complex' ? 8 : 0">
        <div style="margin-top: 20px; margin-right: 20px">
          <!--          {{route.meta?.title}}-->
          <span>
            云主机
          </span>
          <div>
            <a-space>
              <a-input  placeholder="搜索名称" style="margin-top: 15px;border-radius: 0px;width: 350px;">
                <template #suffix>
                  <a-tooltip title="Extra information">
                    <SearchOutlined />
                  </a-tooltip>
                </template>
              </a-input>

              <a-select
                  ref="select"
                  value="jack"
                  style="width: 150px;margin-top: 15px;border-radius: 0px"
                  @focus="focus"
              >
                <a-select-option value="jack">按快照数量</a-select-option>
              </a-select>
            </a-space>
          </div>

          <a-list v-if="route.meta?.structure == 'complex'"
                  size="large"
                  bordered
                  style="margin-top: 15px;border-radius: 0px; height: 500px"
                  :data-source="tableData.items" >
            <template #renderItem="{ item }">
              <a-list-item>{{ item.spec.volume?.vm }}</a-list-item>
            </template>
          </a-list>
        </div>
      </a-col>
      <a-col :span="route.meta?.structure == 'complex' ? 16 : 24">
        <div >
          <div v-if="route.meta?.structure == 'complex'" style="margin-top: 20px; margin-right: 20px">
            快照
          </div>
          <div style="margin-top: 20px">
            <a-space >

              <a-button @click="reflash" style="border: none; background-color: #f0f2f5; border-radius: 0px"><icon class="icon" name="el-icon-RefreshRight" :size="18" /></a-button>
              <a-button type="primary" @click="creatDialog.showAndInit(ListName, TableName, templateDate, obj)"  style="border: none; background-color: #005bd4; border-radius: 0px;">
                <icon class="icon" name="el-icon-Plus" />
                <span class="text">{{nameChange(ListName)}}</span>
              </a-button>
              <!--      <a-button type="primary" style="border: none; background-color: #005bd4; border-radius: 0px;">-->
              <!--        <el-icon class="icon"><MoreFilled /></el-icon>-->
              <!--      </a-button>-->

              <a-button style="border: none;border-radius: 0px;background-color: #f7f8fa" @click="startFunc" :disabled="disabledButton == 1 && tableRowState == 'Failed'? false : true">
                <icon class="icon" name="el-icon-video-play" />
                <span class="text">启动</span>
              </a-button>

              <a-button style="border: none;border-radius: 0px;background-color: #f7f8fa" @click="stopFunc" :disabled="disabledButton == 1 && tableRowState == 'Ready'? false : true">
                <icon class="icon" name="el-icon-video-pause" />
                <span class="text">停止</span>
              </a-button>

              <a-dropdown ><a-button  style="border: none;border-radius: 0px;background-color: #f0f2f5" @click="deleteFunc" :disabled="disabledButton == 1 ? false : true">
                <span class="text">删除</span>
                <icon class="icon" name="el-icon-arrow-down" />
              </a-button></a-dropdown>

              <!--              <a-range-picker v-model:value="value2" style="border-radius: 0px;"  show-time />-->

              <a-button @click="submitForm" style="background-color: #d7d9dc;border-radius: 0px">
                <icon class="icon" name="el-icon-search" />
                <span class="text">搜索</span>
              </a-button>

            </a-space>
          </div>

          <!-- The Form section of the Table page -->
          <el-form v-if="shouldShowForm" :model="formData" ref="form" label-width="auto" label-position="left" style="margin-top: 20px">
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

          <a-alert v-if="alertVisible" message="当前已选中" type="info"  closable @close="onClose" style="margin-top: 10px" show-icon/>

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


          <div>
            <a-table class="custom-table"
                     :row-selection="rowSelection"
                     :columns="tableColumns"
                     :rowKey='record=>record.metadata.name'
                     :data-source="tableData.items"
                     :scroll="{ x: 100 }"
                     :pagination="{
               pageSize: pageSite.limit,
               current: pageSite.page,
               onChange: handleCurrentChange}"
                     style="margin-top: 5px"
                     size="small"
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
                <template v-if="column.kind === 'action'">
                  <a-dropdown >
                    <a-button :icon="h(EllipsisOutlined)" />
                    <template #overlay>

                      <a-menu >
                        <a-menu-item
                            v-for="action in column.actionLink"
                            :key="action.key"
                            @click="handleOptionClick(action.label, action.action, text)"
                            :disabled="getComplexValue(text, action.status)=='🟢' ? action.status && action.action == 'start' : getComplexValue(text, action.status)=='🟡' ? action.status && action.action == 'start' || action.action == 'suspend' : action.status && action.action == 'stop' || action.action == 'reboot' || action.action == 'suspend' || action.action == 'resume' || action.action == 'shutdown' ">
                          <a-tree
                              v-if= "action.action === 'extend'"
                              :tree-data="action.children"
                          />
                          <a target="_blank" >{{action.label}}</a>
                        </a-menu-item>
                      </a-menu>
                    </template>
                  </a-dropdown>
                </template>

                <template v-else-if="column.kind === 'link'">
                  <a-button type="link" @click="addSubRoute(getComplexDataDispose(text, column.row), text)"> {{ getComplexDataDispose(text, column.row) }}</a-button>
                </template>

                <template v-else-if="column.kind === 'display'">
                  <span >
                    {{  getPlatformValue (text, column.row) }}
                  </span>
                </template>

                <template v-else-if="column.kind === 'icon'">
                  <span v-if="getComplexDataDispose(text, column.row).includes('centos')">
                    <i class="iconfont icon-linux" />Liunx
                  </span>
                  <span v-else-if="getComplexDataDispose(text, column.row).includes('windows')">
                    <i class="iconfont icon-windows-fill" />Windows
                  </span>
                </template>

                <template v-else-if="column.kind === 'terminalLink'">
                  <el-link v-if="getComplexValue(text, 'spec.status.conditions.state.waiting.reason') == '🟢'" type="primary" :underline="false" :href="getTerminalAddr(text, column.terminalLink)"  target="_blank">
                    <!--          <el-link v-if="getComplexValue(text, 'spec.status.conditions.state.waiting.reason') == '🟢'" type="primary" :underline="false" :href="getTerminalAddr(text, column.terminalLink)"  target="_blank">-->
                    <CreditCardOutlined />
                  </el-link>
                  <el-link v-else type="primary" :underline="false"  target="_blank" disabled>
                    <el-icon >
                      <CreditCardOutlined />
                    </el-icon>
                  </el-link>
                </template>

                <span v-else>
          <span class="icon-text-container" v-if="getComplexDataDispose(text, column.row) == '🟢'" >
            <RightCircleTwoTone two-tone-color="#57d344" />运行中

          </span>
          <span class="icon-text-container"v-else-if="getComplexDataDispose(text, column.row) == '🔴'">
            <StopTwoTone two-tone-color="red"/>已停止
          </span>
          <span class="icon-text-container"v-else-if="getComplexDataDispose(text, column.row) == '🟡'">
            <PauseCircleTwoTone two-tone-color="#96989b"/>暂停中
          </span>
          <span v-else>
             {{ getComplexDataDispose(text, column.row) }}
          </span>
        </span>


              </template>

            </a-table>
          </div>
        </div>
      </a-col>
    </a-row>

    <!-- Table page action button -->


    <!-- Table pagination -->
    <!--    <el-pagination-->
    <!--        v-if="tableDataLoaded"-->
    <!--        :page-size=pageSite.limit-->
    <!--        :current-page=pageSite.page-->
    <!--        :total=tableData.metadata.totalCount-->
    <!--        @current-change="handleCurrentChange"-->
    <!--    ></el-pagination>-->
    <el-dialog
        v-model="dialogVisible"
        :title=selectedItemName
        width="auto"
        height="auto"
    >
      <div class="dialog-content">
        <a-form
            :label-col="{ style: { width: '150px' }}"
            :wrapper-col="{ span: 16 }">
          <a-form-item
              label="名称"
              labelAlign="left">
            <a-input  v-model:value="propupdate['metadata.labels.name']" />
          </a-form-item>
          <a-form-item label="简介" labelAlign="left">
            <a-textarea style="width: 400px; border-radius: 0px" :rows="3" :maxLength="256" />
          </a-form-item>
        </a-form>
      </div>

      <!-- 使用 vue-json-pretty 显示 JSON 数据 -->
      <!--      <el-scrollbar height="500px">-->
      <!--        <div  style="display: flex">-->
      <!--          <vue-json-pretty :data="rowItemData" style="flex: 1"></vue-json-pretty>-->
      <!--          <pre>{{ yaml }}</pre>-->
      <!--        </div>-->
      <!--      </el-scrollbar>-->
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
import {onBeforeUnmount, onMounted, ref, watch} from 'vue';
// import Stomp from 'stompjs';
// import {MQTT_SERVICE, MQTT_USERNAME, MQTT_PASSWORD, MQTT_topic} from '@/rabbitmq/mqtt';
import {
  frontendFormSearch,
  frontendData,
  frontendUpdate,
  getComplexDataDispose,
  getFormDataValue,
  getTerminalAddr,
  actionDataValue,
  getPlatformValue, frontendCreateTemplate, nameChange, frontendDelete, getComplexValue, assignValues
} from "@/api/common";
import { h } from 'vue';
import  CreateJsonDialog  from "@/views/article/lists/template/createTemplate.vue";
import VueJsonPretty from 'vue-json-pretty';
import 'vue-json-pretty/lib/styles.css';
import jsYaml from 'js-yaml';
import TableType from "@/views/article/lists/tableType.vue";
import Annular from "@/views/article/lists/annular.vue";
import type { TableProps } from 'ant-design-vue';
import type { Dayjs } from 'dayjs';
import {
  EllipsisOutlined,
  SearchOutlined,
  CreditCardOutlined,
  RightCircleTwoTone,
  StopTwoTone,
  PauseCircleTwoTone, PlusOutlined

} from '@ant-design/icons-vue';
import {HOMEPAGE, LAYOUT} from "@/router/routes";
import useAppStore from "@/stores/modules/app";
import {useRouter} from "vue-router";

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

interface DataType {
  key: string;
  name: string;
  age: number;
  address: string;
}

const route = useRoute()
const ListName = route.meta?.listname
const TableName = route.meta?.tablename
const filter = route.meta?.filter || {}
const props = ref('')
const propslecet = ref({})
const propupdate = ref({})
const allLabels = ref(filter)
const dialogVisible = ref(false)
const shouldShowForm = ref(false)

// 列配置数据
const tableColumns:tableColumns = ref([])
const tableData = ref({
  metadata:{
    totalCount:''
  },
  items:[],
  actions:[],
  resultRun: 0,
  resultPen: 0
})
const actions = ref([])
const pageSite = ref({limit:5,page:1})
const tableDataLoaded = ref(false)
const formData = ref<Record<string, string>>({}); // 表单数据对象
const formItems: FormItem[] = ref([]); // 用于存储生成的表单项
const buttonItem = ref([]); // 用于存储生成的表单按钮

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

const templateDate = ref()
const obj = ref({})

onMounted(()=>{
  frontendFormSearch(TableName, formItems, buttonItem)
  frontendData(ListName, TableName, pageSite,tableColumns, tableData,allLabels.value, actions)
  frontendCreateTemplate(TableName, templateDate, obj)
  // frontendAction(TableName, scaleItems)
  tableDataLoaded.value = true
})

getComplexDataDispose

/************************
 *
 *  页面操作
 *
 ***********************/
// 操作按钮

// 日期操作按钮
type RangeValue = [Dayjs, Dayjs];

const value2 = ref<RangeValue>();

// table
const optionArray = ref()
function getFormDataValueIndex(data:any){
  getFormDataValue(data, optionArray)
}

const addSubRoute = (name: any,obj: any) => {
  // 获取当前路由路径
  const currentPath = route.path;

  // 生成子路由路径
  const childRoutePath = `${currentPath}/${name}`;

  router.addRoute(currentPath, {
    path: '/user',
    component: LAYOUT,
    children: [
      {
        path: childRoutePath,
        component: () => import('@/views/article/lists/info/info.vue'),
        props: {
          myObject: {
            obj: obj,
            tablename: TableName
          }
        }
      }]
  });

  router.push({
    path: childRoutePath,
    query: {
      name: name
    }
  })
};

// a-table选择框
const state: TableProps['rowSelection'] = reactive({
  selectedRowKeys: []
});

const alertVisible = ref(false);
const disabledButton = ref(0)
const tableRowState = ref("")
const jsonStop = ref([])
const appStore = useAppStore()

function stopFunc() {
  console.log(jsonStop.value)
  jsonStop.value.forEach(json => {
    json.spec.status.state = "Failed";
    console.log(json.spec)
    frontendUpdate(json)
    appStore.refreshView()
  })
}

function startFunc() {
  jsonStop.value.forEach(json => {
    json.spec.status.state = "Ready";
    console.log(json.spec)
    frontendUpdate(json)
    appStore.refreshView()
  })
}

function deleteFunc() {
  jsonStop.value.forEach(json => {
    console.log(json.spec)
    frontendDelete(ListName, json.metadata.name)
    frontendData(ListName, TableName, pageSite,tableColumns, tableData,allLabels.value, actions)
    alertVisible.value = false
  })
}

const rowSelection: TableProps['rowSelection'] = {
  onChange: (selectedRowKeys: string[], selectedRows: DataType[]) => {
    console.log(selectedRows)
    selectedRows.forEach(row => {
      tableRowState.value  = row.spec.status.conditions.state.waiting.reason
      const isRowExists = jsonStop.value.some(existingRow => existingRow.metadata.name === row.metadata.name);
      if (!isRowExists) {
        jsonStop.value.push(row);
      }
      // 在这里访问每一行的数据
    });
    if (selectedRows.length > 0) {
      alertVisible.value = true; // 有选中的行，弹出提示框
      disabledButton.value = 1;
      // tableRowState.value  = selectedRows.spec.status.state

    } else {
      alertVisible.value = false; // 没有选中的行，关闭提示框
      disabledButton.value = 0
    }
  }
};

function onClose(){
  alertVisible.value = false
}

// 分页
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
  shouldShowForm.value = true
  frontendData(ListName, TableName, pageSite,tableColumns, tableData, propslecet.value, actions)
}

const rowJsonData = ref()
function handleOptionClick(dialogname, action, rowData) {
  rowJsonData.value = rowData
  // console.log(rowData)
  actionDataValue(TableName, ListName, dialogVisible, selectedItemName, rowItemData, dialogname, action, rowData)
  setTimeout( reflash,3000)
}

function reflash() {
  frontendData(ListName, TableName, pageSite,tableColumns, tableData,allLabels.value, actions)
}

const replicaset = ref<number>(0);
function saveData(){
  console.log(rowJsonData.value)
  // console.log(propupdate.value)
  // console.log(assignValues(propupdate.value, rowJsonData.value))

  // frontendData(ListName, TableName, pageSite,tableColumns, tableData,allLabels.value, actions)
  // dialogVisible.value = false

  // rowData.spec.replicas = Number(replicaset.value)
  // console.log(rowData)
  // frontendUpdate(rowData)
  // dialogVisible.value = false;
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

/************************
 *
 *  EChart
 *
 ***********************/
const router = useRouter()
const { proxy } = getCurrentInstance() as any
const echartsInstance = ref(null)
watch(
    [() => tableData.value.resultRun, () => tableData.value.resultPen],
    ([newResultRun, newResultPen]) => {
      const option = {
        color: ['#57D344', '#F93940'],
        series: [
          {
            type: 'pie',
            radius: ['40%', '80%'],
            data: [{ value: newResultRun }, { value: newResultPen }],
            label: {
              show: false, // 设置为 false 隐藏指示线
            },
          },
        ],
      }

      const echarts = proxy.$ECharts

      // 初始化挂载
      echartsInstance.value = echarts.init(document.getElementById('myChart')!)
      // 添加配置
      echartsInstance.value.setOption(option)
      // 自适应
      window.onresize = function () {
        echartsInstance.value.resize()
      }

      onBeforeUnmount(() => {
        // 在组件销毁前销毁 ECharts 图表
        if (echartsInstance.value) {
          echartsInstance.value.dispose()
        }
      })

      // 监听路由变化，当路由切换时重新初始化 ECharts 图表
      router.beforeEach(() => {
        if (echartsInstance.value) {
          // 销毁当前的 ECharts 图表
          echartsInstance.value.dispose()
        }
      })
    }
)
</script>

<style lang="less" scoped>
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
//:deep(.el-table td),
//.building-top :deep(.el-table th.is-leaf) {
//  border-bottom: 1px solid #cbc7c7;
//}
//
//.el-table::v-deep .el-table__header {
//  border-bottom: 1px solid #8d9095;
//}
//
//.el-table::after {
//  content: "";
//  display: block;
//  border-bottom: 1px solid #cbc7c7;
//  height: 4px;
//}


.custom-table .ant-table-content {
  overflow-x: auto;
}

.custom-table ::-webkit-scrollbar {
  width: 10px;
  height: 8px;
}

.custom-table ::-webkit-scrollbar-thumb {
  background-color: #c8cacd;
  border-radius: 5px;
}

.custom-table ::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}

::v-deep(.ant-table-cell) {
  background: #ffffff !important;
}

//.custom-table .table-striped td {
//  border-bottom: red;
//}

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

.icon-text-container {
  display: flex;
  align-items: center;
}


</style>
