<template>
  <div>
    <el-dialog v-model="dialogVisible" width="80%">
      <el-scrollbar height="700px">
        <div class="create-title">
          创建{{ dialogName }}
        </div>
        <div class="main">
          <div class="step" style="flex: 0.2;height: 300px" >
            <el-steps :active="active" direction="vertical">
              <el-step v-for="(step, stepName) in templateSpec.data" :key="stepName" :title="stepName" :icon="Edit" />
            </el-steps>
          </div>

          <div>
            <el-card v-for="(group, groupName) in currentStepGroups" :key="groupName" style="border: 1px solid #d2d2d2; width: 1000px; margin-top: 10px;">
              <el-form :model="group">
                <el-form-item v-for="(variable, key) in group.variables" :key="key" :label="variable.label">
                  <template v-if="variable.type === 'text' && key.startsWith('metadata')">
                    <el-input v-model="formData.metadata.name" ></el-input>
                  </template>
                  <template v-else-if="variable.type === 'select' && key.includes('host')" >
                    <el-select v-model="formData.metadata.labels.host" class="m-2" placeholder="Select">
                      <el-option
                          v-for="item in hostOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                      />
                    </el-select>
                  </template>
                  <template v-else-if="variable.type === 'text' && key.includes('nodename')" >
                    <el-input v-model="formData.spec.nodeName" class="m-2"></el-input>
                  </template>
                  <template v-else-if="variable.type === 'radio' && key.includes('virt_type')" >
                    <el-radio-group v-model="formData.spec.lifecycle.createAndStartVMFromISO.virt_type">
                      <el-radio :label="variable.data.label">{{ variable.data.label }}</el-radio>
                    </el-radio-group>
                  </template>
                  <template v-else-if="variable.type === 'slider' && key.includes('memory')" >
                    <el-slider v-model="formData.spec.lifecycle.createAndStartVMFromISO.memory" show-input />
                  </template>
                  <template v-else-if="variable.type === 'number' && key.includes('vcpus')" >
                    <el-input-number v-model="formData.spec.lifecycle.createAndStartVMFromISO.vcpus" :min="1" :max="10" :step="variable.step"/>
                  </template>
                  <template v-else-if="variable.type === 'select' && key.includes('os_variant')" >
                    <el-select v-model="formData.spec.lifecycle.createAndStartVMFromISO.os_variant" class="m-2" placeholder="Select">
                      <el-option
                          v-for="item in osOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                      />
                    </el-select>
                  </template>

                  <template v-else-if="variable.type === 'combox' && key.includes('cdrom')">
                    <el-button type="primary" style="" @click="comboxDrawer = true">
                      {{ variable.label }}
                    </el-button>

                    <el-drawer v-model="comboxDrawer"  :with-header="false" :before-close="handleClose" size="800px">
                      <el-divider>
                        <el-icon><star-filled /></el-icon>
                      </el-divider>
                      <el-select v-model="formData.spec.lifecycle.createAndStartVMFromISO.cdrom" class="m-2" placeholder="Select">
                        <el-option
                            v-for="item in cdromOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                      </el-select>

                      <el-table :data="tableData" stripe style="width: 100%">
                        <el-table-column type="selection" width="55" />
                        <el-table-column prop="name" label="名称" width="200" />
                        <el-table-column prop="platform" label="平台" width="200" />
                        <el-table-column prop="OS" label="操作系统" width="200"/>
                        <el-table-column prop="Image" label="镜像格式" width="200"/>
                      </el-table>

                      <template #footer>
                        <div style="flex: auto">
                          <el-button >关闭</el-button>
                          <el-button type="primary" >确认</el-button>
                        </div>
                      </template>
                    </el-drawer>
                  </template>

                  <template v-else-if="variable.type === 'combox' && key.includes('DVD')">
                    <el-button type="primary" style="" @click="comboxDrawer = true">
                      {{ variable.label }}
                    </el-button>

                    <el-drawer v-model="comboxDrawer"  :with-header="false" :before-close="handleClose" size="800px">
                      <el-divider>
                        <el-icon><star-filled /></el-icon>
                      </el-divider>
                      <el-select v-model="formData.spec.lifecycle.createAndStartVMFromISO.disk" class="m-2" placeholder="Select">
                        <el-option
                            v-for="item in DVDOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                      </el-select>

                      <el-table :data="tableData" stripe style="width: 100%">
                        <el-table-column type="selection" width="55" />
                        <el-table-column prop="name" label="名称" width="200" />
                        <el-table-column prop="platform" label="规格" width="200" />
                      </el-table>

                      <template #footer>
                        <div style="flex: auto">
                          <el-button >关闭</el-button>
                          <el-button type="primary" >确认</el-button>
                        </div>
                      </template>
                    </el-drawer>
                  </template>

                  <template v-else-if="variable.type === 'list' && key.includes('network') ">
                    <el-input
                        v-model="commandValue"
                        :rows="2"
                        type="textarea"
                        placeholder="请输入指令"
                    />
                    <el-button @click="addCommand" style="margin-top: 10px">确认</el-button>
                  </template>

                  <template v-else-if="variable.type === 'select' && key.includes('graphics')" >
                    <el-select v-model="formData.spec.lifecycle.createAndStartVMFromISO.graphics" class="m-2" placeholder="Select">
                      <el-option
                          v-for="item in graphicsOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                      />
                    </el-select>
                  </template>
                </el-form-item>
              </el-form>
            </el-card>
          </div>


<!--          <div >-->
<!--            <div class="table_section">-->
<!--              <div style=" margin-top: 10px; margin-left: 30px;flex: 1">-->
<!--                <span style="font-size: 15px;color: #c0c4cc;">基本信息</span>-->
<!--                <el-form :model="formData" ref="formDataRef" label-width="auto" label-position="right" style="margin-top: 20px">-->
<!--                  <el-form-item-->
<!--                      v-for="(variable, key) in templateSpec.variables"-->
<!--                      :label="variable.label"-->
<!--                      :key="key"-->
<!--                  >-->
<!--                    <template v-if="variable.type === 'text' && key.startsWith('metadata')">-->
<!--                      <el-input v-model="formData.metadata.name" ></el-input>-->
<!--                    </template>-->
<!--                    &lt;!&ndash; 处理其他字段类型 &ndash;&gt;-->
<!--                  </el-form-item>-->
<!--                </el-form>-->
<!--              </div>-->
<!--&lt;!&ndash;              <pre> {{ jsonFormdata }} </pre>&ndash;&gt;-->
<!--              <el-divider direction="vertical" style="margin-left: 100px; height: auto"/>-->
<!--              <div class="ace_editor">-->
<!--                <v-ace-editor v-model:value="jsonFormdata"-->
<!--                              lang="json"-->
<!--                              theme="cobalt"-->
<!--                              style="height: 500px"-->
<!--                              readonly="true"-->
<!--                              :options="{-->
<!--                                fontSize: 15-->
<!--                              }"/>-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->
        </div>
      </el-scrollbar>
      <div class="controls__wrap" style="float: right; margin-right: 100px;margin-top: 100px">
        <template v-if="active != 2 ">
          <!--      <el-button style="margin-top: 12px" @click="next">下一步</el-button>-->
          <el-button class="custom-button" @click="handlePrevStep" style="margin-top: 10px;float: right">上一步</el-button>
          <el-button class="custom-button" @click="handleNextStep" style="margin-top: 10px;float: right;margin-right: 10px">下一步</el-button>
        </template>
        <div v-else>
          <!--      <el-button style="margin-top: 12px" @click="next">完成</el-button>-->
          <el-button class="custom-button" @click="handlePrevStep" style="margin-top: 10px;float: right">上一步</el-button>
          <el-button class="custom-button" style="margin-top: 10px;float: right;margin-right: 10px" @click="createButton">确认创建</el-button>
        </div>

      </div>
<!--      <div class="create-bottom">-->

<!--        <el-button type="primary" style="margin-bottom: 20px" @click="createButton">确认创建</el-button>-->
<!--        <el-button style="margin-bottom: 20px">取消</el-button>-->
<!--      </div>-->
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue';
import {frontendCreateTemplate, frontendCreate} from '@/api/common'
import { VAceEditor } from "vue3-ace-editor";
import { Edit } from "@element-plus/icons-vue";
import { ElMessageBox } from 'element-plus'
import './ace.config'

const switchvalue = ref(true)
const dialogVisible = ref(false)
const comboxDrawer = ref(false)
const tableData = ref([])

const templateSpec = reactive({
  template: {},
  "data": {
    "step1": {
      "group1": {
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
        "variables": {
          "spec.lifecycle.createAndStartVMFromISO.memory": {
            "label": "内存大小",
            "show": "show-input",
            "type": "slider"
          },
          "spec.lifecycle.createAndStartVMFromISO.os_variant": {
            "label": "操作系统",
            "type": "select"
          },
          "spec.lifecycle.createAndStartVMFromISO.vcpus": {
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
          },
          "spec.nodename": {
            "label": "节点名称",
            "type": "text"
          }
        }
      }
    },
    "step2": {
      "group1": {
        "variables": {
          "spec.lifecycle.createAndStartVMFromISO.DVD": {
            "kind": null,
            "label": "选择云盘",
            "name": null,
            "type": "combox"
          },
          "spec.lifecycle.createAndStartVMFromISO.cdrom": {
            "kind": null,
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
              "label1": "true",
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
});

const formData = ref({});


// 测试例子
const hostOptions = [
  {
    value: 'vm.node31',
    label: 'vm.node31'
  }
]

const radio = ref('')

const osOptions = [
  {
    value: 'centos7.0',
    label: 'centos7.0'
  },
  {
    value: 'Ubuntu22.10',
    label: 'Ubuntu22.10'
  }
]

const cdromOptions = [
  {
    value: '/var/lib/libvirt/iso/centos7-minimal-1511.iso',
    label: '/var/lib/libvirt/iso/centos7-minimal-1511.iso'
  }
]

const DVDOptions = [
  {
    value: '/var/lib/libvirt/pooltest/disktest/disktest.qcow2,format=qcow2',
    label: '/var/lib/libvirt/pooltest/disktest/disktest.qcow2,format=qcow2'
  }
]

const graphicsOptions = [
  {
    value: '/var/lib/libvirt/pooltest/disktest/disktest.qcow2,format=qcow2',
    label: '/var/lib/libvirt/pooltest/disktest/disktest.qcow2,format=qcow2'
  }
]


const handleClose = (done: () => void) => {
  ElMessageBox.confirm('确认关闭吗?')
      .then(() => {
        done()
      })
      .catch(() => {
        // catch error
      })
}

const keyValue = ref({ value1: "", value2: "" });

const addKeyValuePair = () => {
  if (keyValue.value.value1 && keyValue.value.value2) {
    formData.value.spec.containers[0].env.push({ "name": keyValue.value.value1, "value": keyValue.value.value2 });
    // 清空输入
    keyValue.value = { value1: "", value2: "" };
  }
};

const portValue = ref({value1: "DATABASE_PORT", value2: ""})
const addPortPair = () => {
  if (portValue.value.value1 && portValue.value.value2) {
    formData.value.spec.containers[0].env.push({ "name": portValue.value.value1, "value": portValue.value.value2 });
    keyValue.value = { value1: "", value2: "" };
  }
}

const commandValue = ref('')
const argsValue = ref('')
const addCommand = () => {
  formData.spec.lifecycle.createAndStartVMFromISO.network.push(commandValue.value)
  commandValue.value = ''
}
const addArgs = () => {
  formData.value.spec.containers[0].args.push(argsValue.value)
  argsValue.value = ''
}



formData.value = generateInitialFormData();

const jsonFormdata = ref(JSON.stringify(formData.value, null, 2));

watch(formData, () => {
  jsonFormdata.value = JSON.stringify(formData.value, null, 2);
}, { deep: true });

function getRules() {

  return {};
}


const dialogName = ref('')
const TableName = ref('')
const showAndInit = (listName:any, tableName:any) => {
  dialogName.value = listName
  TableName.value = tableName
  dialogVisible.value = true
  // frontendCreateTemplate(TableName.value, templateSpec)
}

function generateInitialFormData() {
  // 测试例子
  const initialData = {
    apiVersion: 'doslab.io/v1',
    kind: 'VirtualMachine',
    metadata: {
      name: '',
      labels: {
        host: ''
      }
    },
    spec: {
      nodeName: '',
      lifecycle: {
        createAndStartVMFromISO: {
          virt_type: '',
          memory: '',
          vcpus: '',
          os_variant: '',
          cdrom: '',
          disk: '',
          network: [],
          graphics: '',
          noautoconsole: 'true'
        }
      }
    }
  };
  return initialData;
}

function createButton(){
  console.log(jsonFormdata.value)
  frontendCreate(JSON.parse(jsonFormdata.value))
  dialogVisible.value = false
}

const active = ref(1);
const currentStep = ref('step1');
const hasNextStep = ref(false);

const currentStepGroups = ref({});
onMounted(() => {
  updateCurrentStepGroups();
});

function updateCurrentStepGroups() {
  currentStepGroups.value = templateSpec.data[currentStep.value];
}

function handleNextStep() {
  const stepNames = Object.keys(templateSpec.data);
  const currentIndex = stepNames.indexOf(currentStep.value);
  if (currentIndex < stepNames.length - 1) {
    currentStep.value = stepNames[currentIndex + 1];
    active.value = currentIndex + 1;
    updateCurrentStepGroups();
  }
  hasNextStep.value = currentIndex < stepNames.length - 2;
}

function handlePrevStep() {
  const stepNames = Object.keys(templateSpec.data);
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
<style>
.create-title{
  margin-left: 40px;

  font-size: 40px;
}

.main{
  margin-left: 25px;
  margin-top: 10px;
  margin-bottom: 30px;
  overflow: hidden;
  display: flex;
}

.step{
  float: left;
  width: 20%;
}

.table_section{
  display: flex;
  height: 100%;
}

.ace_editor{
  width: 600px;
  margin-top: 10px;
  margin-left: 40px;
}

.create-bottom{
  float: right;

}

.el-step__title {
  font-weight: bold;
  font-size: 13px;
}

.el-step__title.is-process{
  color: #0043B1;
}

.custom-button {
  background-color: #fcfcfd; /* 初始背景颜色 */
  color: black; /* 初始文字颜色 */
  transition: background-color 0.3s ease; /* 添加过渡效果 */

}
</style>
