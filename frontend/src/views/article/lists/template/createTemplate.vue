<template>
  <el-dialog v-model="dialogVisible" style="width:80%; height:100%" z-index="500">
    <div class="header" style="border-bottom:1px solid #dbdde0">
      <div class="create-title" style="font-size: 16px; margin-left: 50px">
        创建{{ dialogName }}
      </div>
    </div>
    <div class="sync-dialog">
      <div class="item1">
        <a-steps
            style="margin-left: 150px; margin-top: 10px; height: 200px"
            progress-dot
            :current="active"
            active-color="#1890ff"
            process-color="#52c41a"
            direction="vertical"
        >
          <a-step v-for="(item, index) in templateDate.spec.stepName" :key="index">
            <template #title>
              <div class="step-title">{{ item.title }}</div>
            </template>
          </a-step>
        </a-steps>
        <a-button @click="createJson(templateDate.spec)"></a-button>
      </div>
      <div class="item2">
        <a-card v-for="(group, groupName) in currentStepGroups"
                :key="groupName"
                :tab-list="group.tabList"
                :active-tab-key="group.tabKey"
                :bordered="false"
                style="border:1px solid #dbdde0;width: 80%; margin-top: 10px;border-radius: 0px">
            <a-form :model="group"
                    :label-col="labelCol"
                    :wrapper-col="wrapperCol"
                    >
              <a-form-item v-for="(variable, key) in group.variables" :key="key" :label="variable.label" labelAlign="left">
                <template v-if="variable.type === 'text' ">
                  <a-input  style="border-radius: 0px"/>
                </template>
                <template v-else-if="variable.type === 'select' " >
                  <a-select  placeholder="Select" >

                  </a-select>
                </template>
                <template v-else-if="variable.type === 'radio'" >
                  <el-radio-group >
                    <el-radio :label="variable.data.label">{{ variable.data.label }}</el-radio>
                  </el-radio-group>
                </template>
                <template v-else-if="variable.type === 'slider' " >
                  <el-slider  show-input />
                </template>
                <template v-else-if="variable.type === 'number' " >
                  <div style="width: 750px;">
                    <a-space v-for="(number, index) in variable.data" :key="index">
                      <a-button type="primary" style="background-color:#005bd4; border: none ;margin-right: 5px; border-radius: 0px">{{ number }}</a-button>
                    </a-space>
                    -
                    <el-input-number  :min="1" :max="10" :step="variable.step" controls-position="right"/>
                  </div>
                </template>
                <template v-else-if="variable.type === 'combox' " >
                  <a-button type="dashed" @click="showDrawer(variable.kind, variable.label)" >{{ variable.label }}</a-button>

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

                <template v-else-if="variable.type === 'confirm' " >
                  <v-ace-editor v-model:value="jsonFormdata"
                                lang="json"
                                theme="cobalt"
                                style="height: 500px"
                                readonly="true"
                                :options="{
                                fontSize: 15
                              }"/>
                </template>
              </a-form-item>
              <a-form-item v-for="(constants, key) in group.constants" :key="key" :label="constants.label" labelAlign="left" >
                <template v-if="constants.type === 'textbox' ">
                  <a-textarea style="border-radius: 0px" :rows="3" :maxLength="256" @input="numLimit"/>
                  <div class="textarea">{{ numberlimit }}/256</div>
                </template>
              </a-form-item>
            </a-form>
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
          <a-button  style="margin-top: 10px;float: right;margin-right: 10px;background-color:cornflowerblue;border: none" type="primary">确认创建</a-button>
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

    <a-table :columns="tableColumns" :data-source="tableData.items" :scroll="{ x: 1500, y: 300 }">
      <template #bodyCell="{ column }">
        <template v-if="column.kind === 'action'">
          <a>action</a>
        </template>
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
import type { DrawerProps } from 'ant-design-vue';
import {frontendData} from "@/api/common";
import {VAceEditor} from "vue3-ace-editor";

const route = useRoute()
const dialogVisible = ref(false)

const listvalue = ref({
  value1:'',
  value2:''
})

const templateDate = ref({
  "spec": {
    "template": {
      "apiVersion": "v1",
      "kind": "VirtualMachine"
    },
    "stepName": [
      {
        "title": "基础配置"
      }, {
        "title": "资源配置"
      }, {
        "title": "确认信息"
      }
    ],
    "data": {
      "step1": {
        "group1": {
          "tabList": [
            {
              "key": "基本信息",
              "tab": "基本信息"
            }
          ],
          "tabKey": "基本信息",
          "constants": {
            "introduction": {
              "label": "简介",
              "type": "textbox"
            }
          },
          "variables": {
            "metadata.labels.host": {
              "label": "host",
              "type": "select"
            },
            "metadata.name": {
              "label": "名称",
              "regexp": [
                "A-Za-z"
              ],
              "type": "text"
            }
          }
        },
        "group2": {
          "tabList": [
            {
              "key": "基础规格",
              "tab": "基础规格"
            }
          ],
          "tabKey": "基础规格",
          "variables": {
            "spec.nodename": {
              "label": "节点名称",
              "type": "text"
            },
            "spec.lifecycle.createAndStartVMFromISO.memory": {
              "data": [
                "1GB","2GB","4GB","8GB","16GB"
              ],
              "label": "内存大小(GB)",
              "step": 1,
              "type": "number"
            },
            "spec.lifecycle.createAndStartVMFromISO.os_variant": {
              "label": "操作系统",
              "type": "select"
            },
            "spec.lifecycle.createAndStartVMFromISO.vcpus": {
              "data": [
                  "1核","2核","4核","8核","16核","32核"
              ],
              "label": "CPU数量",
              "regexp": [
                "0-9"
              ],
              "step": 1,
              "type": "number"
            },
            "spec.lifecycle.createAndStartVMFromISO.virt_type": {
              "data": {
                "label": "kvm"
              },
              "label": "虚拟化类型",
              "type": "radio"
            }
          }
        }
      },
      "step2": {
        "group1": {
          "variables": {
            "spec.lifecycle.createAndStartVMFromISO.DVD": {
              "kind": "doslab.io.VirtualMachineDisk",
              "label": "选择云盘",
              "name": null,
              "type": "combox"
            },
            "spec.lifecycle.createAndStartVMFromISO.cdrom": {
              "kind": "doslab.io.VirtualMachineImages",
              "label": "选择光盘镜像",
              "name": null,
              "type": "combox"
            }
          }
        },
        "group2": {
          "variables": {
            "spec.lifecycle.createAndStartVMFromISO.graphics": {
              "label": "VNC协议",
              "type": "select"
            },
            "spec.lifecycle.createAndStartVMFromISO.network": {
              "label": "网络配置",
              "regexp": [
                "A-Za-z"
              ],
              "type": "list"
            },
            "spec.lifecycle.createAndStartVMFromISO.noautoconsole": {
              "data": {
                "label": "true",
                "label2": "false"
              },
              "label": "自动打开控制台",
              "type": "radio"
            }
          }
        }
      },
      "step3": {
      }
    }
  }
})

function createJson(json) {
  const jsontemplate = {}
  let obj = { ...json.template, ...jsontemplate }
  console.log(obj)
  const arr = Object.values(json.data);
  console.log(arr)

  for (const stepKey in json.data) {
    if (Object.hasOwnProperty.call(json.data, stepKey)) {
      const step = json.data[stepKey];

      // 遍历每个分组
      for (const groupKey in step) {
        if (Object.hasOwnProperty.call(step, groupKey)) {
          const group = step[groupKey];

          // 检查是否存在 variables 属性
          if (group.variables) {
            // 遍历每个变量
            for (const variableKey in group.variables) {
              if (Object.hasOwnProperty.call(group.variables, variableKey)) {
                const variable = group.variables[variableKey];

                // 在这里进行相应的处理
                console.log(stepKey, groupKey, variableKey, variable);

                let oj = {}
                const arr1 = variableKey.split(".");
                for (let index = arr1.length-1; index >=0; index--) {
                  if (index==arr1.length-1) {
                    oj={
                      [arr1[index]]:''
                    }
                  }else{
                    oj={
                      [arr1[index]]:oj
                    }
                  }

                  obj = assiginObj(oj, obj)
                }
              }
            }
          }
        }
      }
    }
  }

  // for (const i  in arr) {
  //   console.log(i)
  //   for (const j in i) {
  //     console.log(j)
  //     const arr = Object.keys(j.variables)
  //     for (const k in arr) {
  //       let oj = {}
  //       const arr1 = k.split(".");
  //       for (let index = arr1.length-1; index >=0; index--) {
  //         if (index==arr1.length-1) {
  //           oj={
  //             [arr1[index]]:'133.133.135.134'
  //           }
  //         }else{
  //           oj={
  //             [arr1[index]]:oj
  //           }
  //         }
  //
  //         obj = assiginObj(oj, obj)
  //       }
  //     }
  //   }
  // }
  //
  console.log(obj)
}

const jsonFormdata = createJson(templateDate.value.spec)

function assiginObj(target = {},sources= {}){
  const obj = target;
  if(typeof target != 'object' || typeof sources != 'object'){
    return sources; // 如果其中一个不是对象 就返回sources
  }
  for(const key in sources){
    // 如果target也存在 那就再次合并
    if(target.hasOwnProperty(key)){
      obj[key] = assiginObj(target[key],sources[key]);
    } else {
      // 不存在就直接添加
      obj[key] = sources[key];
    }
  }
  return obj;
}

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
  currentStepGroups.value = templateDate.value.spec.data[currentStep.value];
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


const showAndInit = (listName:any, tableName:any) => {
  dialogName.value = listName
  TableName.value = tableName
  dialogVisible.value = true
  // frontendCreateTemplate(TableName.value, templateSpec)
}

function handleNextStep() {
  const stepNames = Object.keys(templateDate.value.spec.data);
  const currentIndex = stepNames.indexOf(currentStep.value);
  if (currentIndex < stepNames.length - 1) {
    currentStep.value = stepNames[currentIndex + 1];
    active.value = currentIndex + 1;
    updateCurrentStepGroups();
  }
  hasNextStep.value = currentIndex < stepNames.length - 2;
}

function handlePrevStep() {
  const stepNames = Object.keys(templateDate.value.spec.data);
  const currentIndex = stepNames.indexOf(currentStep.value);
  if (currentIndex > 0) {
    currentStep.value = stepNames[currentIndex - 1];
    active.value = currentIndex - 1;
    updateCurrentStepGroups();
  }
  hasNextStep.value = currentIndex < stepNames.length - 2;
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
  flex: 1.5;
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
  flex: 6;
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
  width: 370px;
  font-size: 10px;
  text-align: right;
  margin-top: -15px;
  margin-right: 10px;
}

</style>
