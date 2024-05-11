<template>
  <el-dialog v-model="dialogVisible" style="width:100%; height:100%" z-index="500">
    <div class="header" style="border-bottom:1px solid #dbdde0">
      <div class="create-title" style="font-size: 16px; margin-left: 50px">
        开始{{ nameChange(dialogName) }}
      </div>
    </div>
    <div class="sync-dialog">
      <div class="item1"/>
      <div class="item1">
        <a-steps
            style=" margin-top: 10px; height: 200px"
            progress-dot
            :current="active"
            active-color="#1890ff"
            process-color="#52c41a"
            direction="vertical"
        >
          <a-step v-for="(item, index) in templateDate.stepName" :key="index">
            <template #title>
              <div class="step-title">{{ item.title }}</div>
            </template>
          </a-step>
        </a-steps>
        <!--        <a-button @click="createJson(templateDate.spec)"></a-button>-->
      </div>
      <div class="item2">
        <a-card v-for="(group, groupName) in currentStepGroups"
                :key="groupName"
                :tab-list="group.tabList"
                :active-tab-key="group.tabKey"
                :bordered="false"
                style="border:1px solid #dbdde0;width: 80%; margin-top: 10px;border-radius: 0px">
          <a-form
              ref="formRef"
              :model="propadd"
              :label-col="labelCol"
              :wrapper-col="wrapperCol"
          >
            <div v-for="variable in group.variables" :key="variable.label">
              <a-form-item v-if="variable.type === 'text' " :label="variable.label" :name="variable.path" labelAlign="left" :rules="[{required: variable.required, message: '请填写' + variable.label},{pattern: variable.regexp, message: '请按规则填写！'}]">
                <a-tooltip v-if="variable.message" placement="right">
                  <template #title>
                    <span>{{ variable.message }}</span>
                  </template>
                  <InfoCircleOutlined />
                </a-tooltip>
                <a-input v-model:value.trim="propadd[variable.path]" style="width: 300px; border-radius: 0px"/>
              </a-form-item>
              <a-form-item v-else-if="variable.type === 'select' " :label="variable.label" :name="variable.path" labelAlign="left" :rules="[{required: variable.required, message: '请选择' + variable.label}]">
                <a-select v-model:value="propadd[variable.path]" style="width: 300px" placeholder="Select"  :options="variable.options"></a-select>
              </a-form-item>
              <a-form-item v-else-if="variable.type === 'radio' " :label="variable.label" :name="variable.path" labelAlign="left" :rules="[{required: variable.required, message: '请选择' + variable.label}]">
                <a-radio-group v-model:value="propadd[variable.path]" :options="variable.data" />
              </a-form-item>
              <a-form-item v-else-if="variable.type === 'slider' " :label="variable.label" :name="variable.path" labelAlign="left" >
                <el-slider v-model="propadd[variable.path]" show-input />
              </a-form-item>
              <a-form-item v-else-if="variable.type === 'panduan' " :label="variable.label" :name="variable.path" labelAlign="left" >
                <a-radio-group v-model:value="propadd[variable.path]" :options="variable.data" />
              </a-form-item>
              <a-form-item v-else-if="variable.type === 'number' " :label="variable.label" :name="variable.path" labelAlign="left" :rules="[{required: variable.required, message: '请选择' + variable.label}]">
                <a-radio-group class="custom-radio-group"
                               v-model:value="variable.data[0].label"
                               button-style="solid"
                               option-type="button"
                               :options="variable.data"/>
              </a-form-item>
              <a-form-item v-else-if="variable.type === 'combox' " :label="variable.label" :name="variable.path" :key="variable.id" labelAlign="left" :rules="[{required: variable.required, message: '请' + variable.label}]">

                <!--                  <div >-->
                <!--                    <a-button  v-model="propadd[variable.path]" type="dashed" @click="showDrawer(variable.kind, variable.label, variable.row, variable.path)">{{ tableRow == '' ? variable.label : tableRow}}</a-button>-->
                <!--                  </div>-->
                <div v-if="!showContent[variable.id]">
                  <a-button type="dashed" @click="showDrawer(variable.kind, variable.label, variable.row, variable.path, variable.method, variable.data, variable.id)">{{  variable.label }}</a-button>
                  <a-tooltip v-if="variable.message" placement="right" >
                    <template #title>
                      <span>{{ variable.message }}</span>
                    </template>
                    <InfoCircleOutlined style="margin-left: 10px"/>
                  </a-tooltip>
                </div>
                <div v-else-if="showContent[variable.id]">
                  {{ propadd[variable.path] }}
                </div>
              </a-form-item>
              <a-form-item v-else-if="variable.type === 'list' " :label="variable.label" :name="variable.path" labelAlign="left" :rules="[{required: variable.required, message: '请选择' + variable.label}]">
                <div v-for="(div, index) in divs" :key="index" style="background-color: #f5f7fa;width: 750px;height: 150px; margin-top: 10px; display: flex;">
                  <div style="padding: 20px; flex: 1">
                    {{ div }}
                  </div>
                  <div style="padding: 30px; flex: 6">
                    <div>
                      <a-input v-model:value="listvalue.value1"  style="width: 120px"/>
                      -
                      <a-input v-model:value="listvalue.value2"  style="width: 120px"/>
                    </div>
                    <div style="margin-top: 20px">
                      <el-radio >设为默认</el-radio>
                    </div>
                  </div>
                  <!--                    <button @click="removeDiv" style="padding: 20px;flex: 0.1"><DeleteOutlined /></button>-->
                  <!--                    <a-button  @click="addJsonList(variable.path)">确认</a-button>-->
                </div>
                <!--                  <a-button type="link" @click="addDiv"><PlusOutlined />添加配置</a-button>-->
                <a-button type="link" @click="addJsonList(variable.path)"><PlusOutlined />添加配置</a-button>
              </a-form-item>
            </div>


            <!--              <a-form-item :name="propadd[variable.path]" labelAlign="left">-->
            <!--                <template v-if="variable.type === 'text' ">-->
            <!--                  <a-input v-model:value="propadd[variable.path]" style="width: 300px; border-radius: 0px"/>-->
            <!--                </template>-->
            <!--                <template v-else-if="variable.type === 'select' " >-->
            <!--                  <a-select v-model:value="propadd[variable.path]" style="width: 300px" placeholder="Select"  :options="variable.options">-->
            <!--                  </a-select>-->
            <!--                </template>-->
            <!--                <template v-else-if="variable.type === 'radio'" >-->
            <!--                  <a-radio-group v-model:value="propadd[variable.path]" :options="variable.data" />-->
            <!--                </template>-->
            <!--                <template v-else-if="variable.type === 'slider' " >-->
            <!--                  <el-slider v-model="propadd[variable.path]" show-input />-->
            <!--                </template>-->
            <!--                <template v-else-if="variable.type === 'number' " >-->
            <!--&lt;!&ndash;                  <div style="width: 750px;">&ndash;&gt;-->
            <!--&lt;!&ndash;                    <a-space v-for="(number, index) in variable.data" :key="index">&ndash;&gt;-->
            <!--&lt;!&ndash;                      <a-button type="primary" style="background-color:#005bd4; border: none ;margin-right: 5px; border-radius: 0px" @click="addValuetoJson(variable.path, number)">{{ number }}</a-button>&ndash;&gt;-->
            <!--&lt;!&ndash;                    </a-space>&ndash;&gt;-->
            <!--&lt;!&ndash;&lt;!&ndash;                    - &ndash;&gt;&ndash;&gt;-->
            <!--&lt;!&ndash;&lt;!&ndash;                    <el-input-number v-model="propadd[variable.path]" :min="1"  :step="variable.step" controls-position="right"/>&ndash;&gt;&ndash;&gt;-->
            <!--&lt;!&ndash;                  </div>&ndash;&gt;-->

            <!--                  <a-radio-group class="custom-radio-group"-->
            <!--                                 v-model:value="propadd[variable.path]"-->
            <!--                                 button-style="solid"-->
            <!--                                 option-type="button"-->
            <!--                                 :options="variable.data"/>-->
            <!--                </template>-->
            <!--                <template v-else-if="variable.type === 'combox' " >-->
            <!--                  <div >-->
            <!--                    <a-button  v-model="propadd[variable.path]" type="dashed" @click="showDrawer(variable.kind, variable.label, variable.row, variable.path)">{{ variable.label }}</a-button>-->
            <!--                  </div>-->
            <!--&lt;!&ndash;                  <span v-else>&ndash;&gt;-->
            <!--&lt;!&ndash;                    {{ tableRow }}&ndash;&gt;-->
            <!--&lt;!&ndash;                  </span>&ndash;&gt;-->
            <!--                </template>-->
            <!--                <template v-else-if="variable.type === 'list' " >-->
            <!--                  <div v-for="(div, index) in divs" :key="index" style="background-color: #f5f7fa;width: 750px;height: 150px; margin-top: 10px; display: flex;">-->
            <!--                    <div style="padding: 20px; flex: 1">-->
            <!--                      {{ div }}-->
            <!--                    </div>-->
            <!--                    <div style="padding: 30px; flex: 6">-->
            <!--                      <div>-->
            <!--                        <a-input v-model:value="listvalue.value1"  style="width: 120px"/>-->
            <!--                        - -->
            <!--                        <a-input v-model:value="listvalue.value2"  style="width: 120px"/>-->
            <!--                      </div>-->
            <!--                      <div style="margin-top: 20px">-->
            <!--                        <el-radio >设为默认</el-radio>-->
            <!--                      </div>-->
            <!--                    </div>-->
            <!--&lt;!&ndash;                    <button @click="removeDiv" style="padding: 20px;flex: 0.1"><DeleteOutlined /></button>&ndash;&gt;-->
            <!--&lt;!&ndash;                    <a-button  @click="addJsonList(variable.path)">确认</a-button>&ndash;&gt;-->
            <!--                  </div>-->
            <!--&lt;!&ndash;                  <a-button type="link" @click="addDiv"><PlusOutlined />添加配置</a-button>&ndash;&gt;-->
            <!--                  <a-button type="link" @click="addJsonList(variable.path)"><PlusOutlined />添加配置</a-button>-->
            <!--                </template>-->
            <!--              </a-form-item>-->

            <a-form-item v-for="(constants, key) in group.constants" :key="key" :label="constants.label" labelAlign="left" >
              <template v-if="constants.type === 'textbox' ">
                <a-textarea style="width: 300px; border-radius: 0px" :rows="3" :maxLength="256" @input="numLimit"/>
                <div class="textarea">{{ numberlimit }}/256</div>
              </template>
            </a-form-item>
          </a-form>
          <template v-if="group.kind == 'collapse'" >
            <a-collapse v-model:activeKey="activeKey" :bordered="false" >
              <a-collapse-panel  header="高级设置">
                <a-form
                    ref="formRef"
                    :model="propadd"
                    :label-col="labelCol"
                    :wrapper-col="wrapperCol">
                  <a-form-item label="最大cpu数" labelAlign="left" >
                    <a-input  style="width: 300px; border-radius: 0px"/>
                  </a-form-item>
                  <a-form-item label="插槽数" labelAlign="left" >
                    <a-input  style="width: 300px; border-radius: 0px"/>
                  </a-form-item>
                  <a-form-item label="线程数" labelAlign="left" >
                    <a-input  style="width: 300px; border-radius: 0px"/>
                  </a-form-item>
                </a-form>
              </a-collapse-panel>
            </a-collapse>
            <!--            <pre>-->
            <!--              {{ jsonFormdata }}-->
            <!--            </pre>-->
          </template>

        </a-card>

      </div>
    </div>
    <div class="footer" style="border-top:1px solid #dbdde0">
      <div style="float: right; margin-right: 100px">
        <template v-if="active !== templateDate.stepName.length  ">
          <!--      <el-button style="margin-top: 12px" @click="next">下一步</el-button>-->
          <a-button  @click="handlePrevStep" style="margin-top: 10px;float: right">上一步</a-button>
          <a-button  @click="onSubmit" style="margin-top: 10px;float: right;margin-right: 10px;background-color:cornflowerblue;border: none" type="primary">下一步</a-button>
        </template>
        <template v-else>
          <!--      <el-button style="margin-top: 12px" @click="next">完成</el-button>-->
          <a-button  @click="handlePrevStep" style="margin-top: 10px;float: right">上一步</a-button>
          <a-button  style="margin-top: 10px;float: right;margin-right: 10px;background-color:cornflowerblue;border: none" type="primary" @click="createValue">确认创建</a-button>
        </template>

      </div>
    </div>
  </el-dialog>
  <a-drawer :title="title"
            :closable="false"
            v-if="open"
            size="large"
            :open="open"
            @close="onClose"
            :headerStyle="{'border-bottom':'1px solid #dbdde0'}"
            :footerStyle="{'border-top':'1px solid #dbdde0'}"
            :maskStyle="{'animation':'none','background':'#000','opacity':'0.1'}">
    <el-button style="text-align: left; background-color: #f0f2f5; border-radius: 0px"><icon name="el-icon-RefreshRight" :size="18" /></el-button>
    <!--    <el-button style="text-align: left; background-color: #f0f2f5; border-radius: 0px">查询</el-button>-->

    <a-table class="custom-table"
             :row-selection="rowSelection"
             :columns="tableFilterColumns"
             :rowKey='record=>record.metadata.name'
             :data-source="tableData.items"
             :scroll="{ x: 100 }"
             style="margin-top: 5px"
    >
      <template #bodyCell="{ column, text }" >
        <template v-if="column.kind === 'action'">

        </template>

        <template v-else-if="column.kind === 'display'">
          <span >
             {{  getPlatformValue (text, column.row) }}
          </span>
        </template>

        <span v-else>
            {{ getComplexDataDispose(text, column.row) }}
        </span>
      </template>
    </a-table>

    <!--    <a-table :row-selection="rowSelection"-->
    <!--             :columns="tableColumns"-->
    <!--             :data-source="tableData.items"-->
    <!--             :scroll="{ x: 1500 }"-->
    <!--             style="margin-top: 5px"-->
    <!--    >-->
    <!--      <template #bodyCell="{ column, text }" >-->
    <!--        <template v-if="column.kind === 'action'">-->
    <!--          <a>action</a>-->
    <!--        </template>-->

    <!--        <template v-else-if="column.kind === 'display'">-->
    <!--          <span >-->
    <!--             {{  getPlatformValue (text, column.row) }}-->
    <!--          </span>-->
    <!--        </template>-->

    <!--        <template v-else-if="column.kind === 'terminalLink'">-->
    <!--          <el-link type="primary" :underline="false" :href="getTerminalAddr(text, column.terminalLink)" target="_blank">-->
    <!--            <el-icon :size="20">-->
    <!--              <component :is="column.terminalLink.icon"></component>-->
    <!--            </el-icon>-->
    <!--          </el-link>-->
    <!--        </template>-->

    <!--        <span v-else>-->
    <!--            {{ getComplexDataDispose(text, column.row) }}-->
    <!--        </span>-->


    <!--      </template>-->

    <!--    </a-table>-->

    <template #footer>
      <a-button  @click="onClose" style="margin-top: 10px;float: right">关闭</a-button>
      <a-button  @click="onConfirm" style=" margin-top: 10px;float: right;margin-right: 10px;background-color:cornflowerblue;border: none" type="primary">确认</a-button>
    </template>
  </a-drawer>
</template>

<script setup lang="ts">
import {h, onMounted, ref} from 'vue';
import {DeleteOutlined, EllipsisOutlined, PlusOutlined, InfoCircleOutlined} from '@ant-design/icons-vue';
import type { TableProps } from 'ant-design-vue';
import type { Rule } from 'ant-design-vue/es/form';
import {
  frontendCreate,
  frontendData,
  getComplexDataDispose,
  getComplexValue,
  getPlatformValue,
  getTerminalAddr,
  nameChange
} from "@/api/common";
import {VAceEditor} from "vue3-ace-editor";
import useAppStore from "@/stores/modules/app";
import {ElMessage} from "element-plus";

interface DataType {
  key: string;
  name: string;
  age: number;
  address: string;
}

const formRef = ref();
const activeKey = ref(['1']);

const route = useRoute()
const dialogVisible = ref(false)

const listvalue = ref({
  value1:'',
  value2:''
})

const options = ref([
  { value: 'vm.node31', label: 'vm.node31' },
  { value: 'vm.node32', label: 'vm.node32' },
  { value: 'vm.node33', label: 'vm.node33' },
]);

const radioGroupValue = ref('kvm')

const propadd = ref({})
const disabledButton = ref(1)


const templateDate = ref()

const labelCol = { span: 4 };
const wrapperCol = { span: 9 };

const currentStep = ref('step1');
const currentStepGroups = ref({});

const pageSite = ref({limit:5,page:1})
const tableData = ref({
  metadata:{
    totalCount:''
  },
  items:[],
  actions:[]
})
const filter = route.meta?.filter || {}
const allLabels = ref(filter)
const actions = ref([])
onMounted(() => {
  updateCurrentStepGroups();
});

const jsonCreateTable = ref([])
const rowSelection: TableProps['rowSelection'] = {
  onChange: (selectedRowKeys: string[], selectedRows: DataType[]) => {
    console.log(selectedRows)
    selectedRows.forEach(row => {
      // tableRowState.value  = row.spec.status.conditions.state.waiting.reason
      const isRowExists = jsonCreateTable.value.some(existingRow => existingRow.metadata.name === row.metadata.name);
      if (!isRowExists) {
        jsonCreateTable.value.push(row);
      } else {
        jsonCreateTable.value = []
      }
    });
  }
};

function updateCurrentStepGroups() {

  // currentStepGroups.value = templateDate.value.data[currentStep.value];
}


const dialogName = ref('')
const TableName = ref('')

// step
const active = ref(1);
const hasNextStep = ref(false);

// drawer
const open = ref<boolean>(false);
const title = ref('')

// table
const tableColumns = ref([])
const tableFilterColumns = computed(() => {
  return tableColumns.value.filter(column => column.kind !== 'action');
});


// list
const divs = ref(['配置']);

// textarea
const numberlimit = ref(0)

const methodRow = ref("")
const tableRow = ref("")
const dataRow = ref()
const idRow = ref()

function showDrawer(kind:any, label: any, row: string, path: string, method: any, data: any, id: any) {
  frontendData(kind, kind.toLowerCase(), pageSite, tableColumns, tableData,allLabels.value, actions);
  open.value = true;
  title.value = label;
  methodRow.value = method;
  dataRow.value = data;
  idRow.value = id
};

const appStore = useAppStore()

// UUID生成
function generateUUID(): string {
  let dt = new Date().getTime();
  const uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
    const r = (dt + Math.random() * 16) % 16 | 0;
    dt = Math.floor(dt / 16);
    return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
  });
  return uuid;
}

// 创建
function createValue(){
  const metadataname = 'metadata.name'
  propadd.value[metadataname] = generateUUID();
  if(propadd.value['spec.lifecycle.createAndStartVMFromISO.noautoconsole'] == 'true') {
    propadd.value['spec.lifecycle.createAndStartVMFromISO.noautoconsole'] = JSON.parse ('true')
  }
  const metadatalabelhost = 'metadata.labels.host'
  propadd.value[metadatalabelhost] = propadd.value['spec.nodeName']
  console.log(propadd.value)
  assignValues(propadd.value,jsonFormdata.value)
  console.log(jsonFormdata.value)
  console.log(JSON.stringify(jsonFormdata.value, null,2))
  frontendCreate(jsonFormdata.value)
  dialogVisible.value = false
  appStore.refreshView()
}

function assignValues(obj1:any, obj2:any) {
  for (const key in obj1) {
    if (Object.prototype.hasOwnProperty.call(obj1, key)) {
      const value = obj1[key];
      assignValue(obj2, key, value);
    }
  }
}

function assignValue(obj:any, key:any, value:any) {
  const keys = key.split('.');
  let currentObj = obj;

  for (let i = 0; i < keys.length; i++) {
    const currentKey = keys[i];
    if (i === keys.length - 1) {
      // Last key, assign the value
      currentObj[currentKey] = value;
    } else {
      // Not the last key, create nested objects if necessary
      if (!currentObj[currentKey]) {
        currentObj[currentKey] = {};
      }
      currentObj = currentObj[currentKey];
    }
  }

  return currentObj
}

function addValuetoJson(key:object, str:string) {
  propadd.value[key] = str
  // propadd.value["spec.status.conditions.state.waiting.reason"] = "Ready"
  console.log(propadd.value)
}

const onClose = () => {
  open.value = false;
};

const showContent = ref({});

function onConfirm (){
  for (const i of jsonCreateTable.value) {
    console.log(i)
    if(methodRow.value == "direct"){
      tableRow.value = getComplexValue(i, dataRow.value.row)
      propadd.value[dataRow.value.path] = tableRow.value
    } else if(methodRow.value == "multiple"){
      for (const j of dataRow.value) {
        tableRow.value = getComplexValue(i, j.row)
        propadd.value[j.path] = tableRow.value
      }
    } else if(methodRow.value == "montage"){
      tableRow.value = getComplexValue(i, dataRow.value.row) + ',format=' + getComplexValue(i, dataRow.value.pin)
      propadd.value[dataRow.value.path] = tableRow.value
    }
  }
  console.log(tableRow.value)
  console.log(propadd.value)
  showContent.value[idRow.value] = !showContent.value[idRow.value];
  disabledButton.value = 0
  open.value = false
}



const addDiv = () => {
  divs.value.push(`配置`);
};

const JsonList = ref([])
function addJsonList(path: any) {
  JsonList.value.push(listvalue.value.value1 + '=' + listvalue.value.value2)
  let obj = ""
  for (let i = 0; i < JsonList.value.length; i++) {
    obj += JsonList.value[i];

    // 如果不是最后一项，则添加逗号
    if (i < JsonList.value.length - 1) {
      obj += ",";
    }
  }
  console.log(obj)
  propadd.value[path] = obj
  listvalue.value.value1 = '';
  listvalue.value.value2 = '';
  ElMessage.success('已添加。')
}

const removeDiv = () => {
  if (divs.value.length > 0) {
    divs.value.pop(); // 移除最后一个 div
  }
};


function numLimit(){
  const num = 0
  numberlimit.value = 256 - num
}

const jsonFormdata = ref()
const aceJson = ref()
const innermostObjects = ref([]);
const showAndInit = (listName:any, tableName:any, Date:any, obj: any ) => {
  templateDate.value = Date
  currentStepGroups.value = templateDate.value.data[currentStep.value]
  jsonFormdata.value = obj
  aceJson.value = JSON.stringify(jsonFormdata.value,null,2)
  console.log(jsonFormdata.value)
  dialogName.value = listName
  TableName.value = tableName
  console.log(currentStepGroups.value)
  // traverseObject(currentStepGroups.value)
  dialogVisible.value = true
}

// function traverseObject(obj:any) {
// for (const key in obj) {
//   if (typeof obj[key] === 'object' && Object.keys(obj[key]).length > 0) {
//     traverseObject(obj[key]);
//   } else {
//     innermostObjects.value.push({ [key]: obj[key] });
//   }
// }
// console.log(innermostObjects.value)
// }

function handleNextStep() {
  const stepNames = Object.keys(templateDate.value.data);
  const currentIndex = stepNames.indexOf(currentStep.value);
  if (active.value < stepNames.length ) {
    active.value = active.value + 1;
    currentStep.value = stepNames[currentIndex + 1]; // 更新 currentStep
    currentStepGroups.value = templateDate.value.data[currentStep.value];
  }
  hasNextStep.value = currentIndex < stepNames.length - 2;
}

function handlePrevStep() {
  const stepNames = Object.keys(templateDate.value.data);
  const currentIndex = stepNames.indexOf(currentStep.value);
  if (active.value > 1) {
    active.value = active.value - 1;
    currentStep.value = stepNames[currentIndex - 1]; // 更新 currentStep
    currentStepGroups.value = templateDate.value.data[currentStep.value];
  }
  hasNextStep.value = currentIndex < stepNames.length - 2;
}


// rules
const validateRule = async (_rule: Rule, value: string) => {
  console.log(value)
  if (value === '') {
    return Promise.reject('Please input the password again');
  }
};


const onSubmit = () => {
  if(Object.keys(propadd.value).length == 0 ){
    ElMessage.error('请填写相关信息！');
    return;
  } else {
    for (const key in propadd.value) {
      if (propadd.value[key] == '') {
        ElMessage.error('请填写相关信息！');
        return;
      }
    }
  }

  handleNextStep()

};

// function checkForm(){
//   if( Object.keys(propadd.value).length == 0 ){
//     return false
//   }
//   return true
// }

const resetForm = () => {
  formRef.value.resetFields();
};


defineExpose({
  showAndInit,
})

</script>

<style  lang="less" scoped>
.sync-dialog {
  height: 700px;
  overflow: auto;
  display: flex;
  background: #f5f7fa

}

.item1 {
  flex: 1;
  position: relative;
  text-align: right;
}

.step-title {
  text-align: left;
  font-size: 14px;
}

:deep(.ant-steps-item::after) {
  border-color: blue !important;
  border-width: 2px !important;
}

.item2 {
  flex: 8;
}

.wrap {
  display: -webkit-box;
  display: -webkit-flex;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-flex-direction: column;
  -ms-flex-direction: column;
  /*布局方向是垂直的*/
  flex-direction: column;
  width: 100%;
  height: 100%;
}

.header {
  height: 40px;
  line-height: 40px;
  text-align: left;
}

.footer {
  height: 40px;
  text-align: right;
  line-height: 40px;

}

.main {
  -webkit-box-flex: 1;
  -webkit-flex: 1;
  -ms-flex: 1;
  flex: 1;
  width: 100%;
  overflow: auto;
}

.textarea{
  width: 330px;
  font-size: 10px;
  text-align: right;
  margin-top: -15px;
  margin-right: 10px;
}

.custom-radio-group :deep(.ant-radio-button-wrapper) {
  margin-right: 8px; /* 调整按钮之间的间距，根据需要修改值 */
  border-radius: 0px
}
</style>
