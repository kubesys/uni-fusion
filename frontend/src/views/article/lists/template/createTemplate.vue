<template>
  <el-dialog v-model="dialogVisible" style="width:100%; height:100%" z-index="500">
    <div class="header" style="border-bottom:1px solid #dbdde0">
      <div class="create-title" style="font-size: 16px; margin-left: 50px">
        开始创建{{ nameChange(dialogName) }}
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
              :model="group"
              :label-col="labelCol"
              :wrapper-col="wrapperCol"
          >
            <a-form-item v-for="(variable, key) in group.variables" :key="key" :label="variable.label" labelAlign="left">
              <template v-if="variable.type === 'text' ">
                <a-input  style="width: 300px; border-radius: 0px"/>
              </template>
              <template v-else-if="variable.type === 'select' " >
                <a-select v-model="propadd[variable.path]" style="width: 300px" placeholder="Select"  :options="options">
                </a-select>
              </template>
              <template v-else-if="variable.type === 'radio'" >
                <el-radio-group >
                  <el-radio v-model="propadd[variable.path]" :label="variable.data.label">{{ variable.data.label }}</el-radio>
                </el-radio-group>
              </template>
              <template v-else-if="variable.type === 'slider' " >
                <el-slider v-model="propadd[variable.path]" show-input />
              </template>
              <template v-else-if="variable.type === 'number' " >
                <div style="width: 750px;">
                  <a-space v-for="(number, index) in variable.data" :key="index">
                    <a-button type="primary" style="background-color:#005bd4; border: none ;margin-right: 5px; border-radius: 0px">{{ number }}</a-button>
                  </a-space>
                  -
                  <el-input-number v-model="propadd[variable.path]" :min="1" :max="10" :step="variable.step" controls-position="right"/>
                </div>
              </template>
              <template v-else-if="variable.type === 'combox' " >
                <a-button  v-model="propadd[variable.path]" type="dashed" @click="showDrawer(variable.kind, variable.label)" >{{ variable.label }}</a-button>

              </template>
              <template v-else-if="variable.type === 'list' " >
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
                  <button @click="removeDiv" style="padding: 20px;flex: 0.1"><DeleteOutlined /></button>
                </div>
                <a-button type="link" @click="addDiv"><PlusOutlined />添加配置</a-button>
              </template>
            </a-form-item>
            <a-form-item v-for="(constants, key) in group.constants" :key="key" :label="constants.label" labelAlign="left" >
              <template v-if="constants.type === 'textbox' ">
                <a-textarea style="width: 300px; border-radius: 0px" :rows="3" :maxLength="256" @input="numLimit"/>
                <div class="textarea">{{ numberlimit }}/256</div>
              </template>
            </a-form-item>
          </a-form>
          <template v-if="group == 'confirm'" >
            <v-ace-editor v-model:value="jsonFormdata"
                          lang="json"
                          theme="monokai"
                          style="height: 500px"
                          :options="{
                                            fontSize: 15
                                          }"/>
            <!--            <pre>-->
            <!--              {{ jsonFormdata }}-->
            <!--            </pre>-->
          </template>

        </a-card>
      </div>
    </div>
    <div class="footer" style="border-top:1px solid #dbdde0">
      <div style="float: right; margin-right: 100px">
        <template v-if="active != 2 ">
          <!--      <el-button style="margin-top: 12px" @click="next">下一步</el-button>-->
          <a-button  @click="handlePrevStep" style="margin-top: 10px;float: right">上一步</a-button>
          <a-button  @click="handleNextStep" style="margin-top: 10px;float: right;margin-right: 10px;background-color:cornflowerblue;border: none" type="primary">下一步</a-button>
        </template>
        <div v-else>
          <!--      <el-button style="margin-top: 12px" @click="next">完成</el-button>-->
          <a-button  @click="handlePrevStep" style="margin-top: 10px;float: right">上一步</a-button>
          <a-button  style="margin-top: 10px;float: right;margin-right: 10px;background-color:cornflowerblue;border: none" type="primary" @click="createValue">确认创建</a-button>
        </div>

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
    <el-button style="text-align: left; background-color: #f0f2f5; border-radius: 0px">查询</el-button>

    <a-table :row-selection="rowSelection"
             :columns="tableColumns"
             :data-source="tableData.items"
             :scroll="{ x: 1500 }"
             style="margin-top: 5px"
    >
      <template #bodyCell="{ column, text }" >
        <template v-if="column.kind === 'action'">
          <a>action</a>
        </template>

        <template v-else-if="column.kind === 'display'">
          <span >
             {{  getPlatformValue (text, column.row) }}
          </span>
        </template>

        <template v-else-if="column.kind === 'terminalLink'">
          <el-link type="primary" :underline="false" :href="getTerminalAddr(text, column.terminalLink)" target="_blank">
            <el-icon :size="20">
              <component :is="column.terminalLink.icon"></component>
            </el-icon>
          </el-link>
        </template>

        <span v-else>
            {{ getComplexDataDispose(text, column.row) }}
        </span>


      </template>

    </a-table>

    <template #footer>
      <a-button  @click="onClose" style="margin-top: 10px;float: right">关闭</a-button>
      <a-button  @click="onClose" style="margin-top: 10px;float: right;margin-right: 10px;background-color:cornflowerblue;border: none" type="primary">确认</a-button>
    </template>
  </a-drawer>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { DeleteOutlined, PlusOutlined } from '@ant-design/icons-vue';
import type { TableProps, DrawerProps } from 'ant-design-vue';
import {frontendCreate, frontendData, getComplexDataDispose, getPlatformValue, getTerminalAddr, frontendCreateTemplate} from "@/api/common";
import {VAceEditor} from "vue3-ace-editor";

const route = useRoute()
const dialogVisible = ref(false)

const listvalue = ref({
  value1:'',
  value2:''
})

const options = ref([
  { value: 'value1', label: 'vm.node31' },
  { value: 'value2', label: 'vm.node32' },
  { value: 'value3', label: 'vm.node33' },
]);

const propadd = ref({})

const templateDate = ref()

// const templateDate = ref({
//   "spec": {
//     "template": {
//       "apiVersion": "v1",
//       "kind": "VirtualMachine"
//     },
//     "stepName": [
//       {
//         "title": "基础信息"
//       }, {
//         "title": "确认信息"
//       }
//     ],
//     "data": {
//       "step1": {
//         "group1": {
//           "constants": {
//             "introduction": {
//               "label": "简介",
//               "type": "textbox"
//             }
//           },
//           "variables": {
//             "metadata.name": {
//               "label": "名称",
//               "regexp": [
//                 "A-Za-z"
//               ],
//               "type": "text"
//             },
//             "spec.lifecycle.createAndStartVMFromISO.DVD": {
//               "kind": "doslab.io.VirtualMachineDisk",
//               "label": "类型",
//               "name": null,
//               "type": "combox"
//             }
//           }
//         },
//         "group2": {
//           "variables": {
//             "spec.lifecycle.createAndStartVMFromISO.DVD": {
//               "kind": "doslab.io.VirtualMachineDisk",
//               "label": "集群",
//               "name": null,
//               "type": "combox"
//             },
//             "spec.lifecycle.createAndStartVMFromISO.virt_type": {
//               "data": {
//                 "label": "IP地址"
//               },
//               "label": "添加方式",
//               "type": "radio"
//             },
//             "metadata.name": {
//               "label": "IP地址",
//               "regexp": [
//                 "A-Za-z"
//               ],
//               "type": "text"
//             },
//             "spec.lifecycle.createAndStartVMFromISO.os_variant": {
//               "label": "操作系统",
//               "type": "select"
//             },
//             "metadata.names": {
//               "label": "SSH端口",
//               "regexp": [
//                 "A-Za-z"
//               ],
//               "type": "text"
//             },
//             "metadata.root": {
//               "label": "用户名",
//               "regexp": [
//                 "A-Za-z"
//               ],
//               "type": "text"
//             },
//             "metadata.password": {
//               "label": "密码",
//               "regexp": [
//                 "A-Za-z"
//               ],
//               "type": "text"
//             }
//           }
//         }
//       },
//       "step2": {
//         "group": "confirm"
//       }
//     }
//   }
// })

// const templateDate = ref({
//   "spec": {
//     "template": {
//       "apiVersion": "v1",
//       "kind": "VirtualMachine"
//     },
//     "stepName": [
//       {
//         "title": "基础信息"
//       }, {
//         "title": "确认信息"
//       }
//     ],
//     "data": {
//       "step1": {
//         "group1": {
//           "constants": {
//             "introduction": {
//               "label": "简介",
//               "type": "textbox"
//             }
//           },
//           "variables": {
//             "metadata.labels.host": {
//               "label": "host",
//               "type": "select"
//             },
//             "metadata.name": {
//               "label": "名称",
//               "regexp": [
//                 "A-Za-z"
//               ],
//               "type": "text"
//             }
//           }
//         },
//         "group2": {
//           "variables": {
//             "spec.nodeName": {
//               "label": "节点名称",
//               "type": "text"
//             },
//             "spec.lifecycle.createDisk.type": {
//               "data": {
//                 "label": "localfs"
//               },
//               "label": "文件系统类型",
//               "type": "radio"
//             },
//             "spec.lifecycle.createDisk.pool": {
//               "kind": "doslab.io.VirtualMachinePool",
//               "label": "选择资源池",
//               "name": null,
//               "type": "combox"
//             },
//             "spec.lifecycle.createDisk.capacity": {
//               "data": [
//                 "20GB","40GB","100GB","200GB","500GB"
//               ],
//               "label": "容量(GB)",
//               "step": 10,
//               "type": "number"
//             },
//             "spec.lifecycle.createDisk.format": {
//               "data": {
//                 "label": "qcow2"
//               },
//               "label": "格式",
//               "type": "radio"
//             },
//           }
//         }
//       },
//       "step2": {
//         "group" : "confirm"
//       }
//     }
//   }
// })



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

// list
const divs = ref(['配置']);

// textarea
const numberlimit = ref(0)


function showDrawer(kind:any, label: any) {
  frontendData(kind, kind.toLowerCase(), pageSite, tableColumns, tableData,allLabels.value, actions)
  console.log(tableData.value.items)
  open.value = true;
  title.value = label
};

function createValue(){
  frontendCreate(jsonFormdata)
}
const onClose = () => {
  open.value = false;
};

const addDiv = () => {
  divs.value.push(`配置`);
};

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
const showAndInit = (listName:any, tableName:any, Date:any, obj: any ) => {
  templateDate.value = Date
  currentStepGroups.value = templateDate.value.data[currentStep.value]
  jsonFormdata.value = JSON.stringify(obj, 0.5, 2)
  dialogName.value = listName
  TableName.value = tableName
  dialogVisible.value = true
}

const rowSelection: TableProps['rowSelection'] = {
  onChange: (selectedRowKeys: string[], selectedRows) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
  },
};

function handleNextStep() {
  const stepNames = Object.keys(templateDate.value.data);
  const currentIndex = stepNames.indexOf(currentStep.value);
  if (currentIndex < stepNames.length - 1) {
    currentStep.value = stepNames[currentIndex + 1];
    active.value = currentIndex + 1;
    currentStepGroups.value = templateDate.value.data[currentStep.value]
  }
  hasNextStep.value = currentIndex < stepNames.length - 2;
}

function handlePrevStep() {
  const stepNames = Object.keys(templateDate.value.data);
  const currentIndex = stepNames.indexOf(currentStep.value);
  if (currentIndex > 0) {
    currentStep.value = stepNames[currentIndex - 1];
    active.value = currentIndex - 1;
    currentStepGroups.value = templateDate.value.data[currentStep.value]
  }
  hasNextStep.value = currentIndex < stepNames.length - 2;
}

function nameChange(name) {
  if (name == 'doslab.io.VirtualMachine') {
    return '云主机'
  } else if (name == 'doslab.io.VirtualMachineDisk') {
    return '云盘'
  } else if (name == 'doslab.io.VirtualMachineDiskImage') {
    return '云盘镜像'
  } else if (name == 'doslab.io.VirtualMachineSpec') {
    return '计算规格'
  } else if (name == 'doslab.io.VirtualMachineDiskSpec') {
    return '云盘规格'
  }
}




defineExpose({
  showAndInit,
})

</script>

<style scoped lang="scss">
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

</style>
