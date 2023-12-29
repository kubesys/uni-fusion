<template>
  <el-dialog v-model="dialogVisible" width="80%" height="100%">
    <div class="header" style="border-bottom:1px solid #dbdde0">
      <div class="create-title" style="font-size: 16px; margin-left: 50px">
        创建{{ dialogName }}
      </div>
    </div>
    <div class="sync-dialog">
      <div class="item1">
        <a-steps
            style="margin-left: 100px; margin-top: 10px; height: 200px"
            progress-dot
            :current="current"
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
      </div>
      <div class="item2">
        <a-card v-for="(group, groupName) in currentStepGroups" :key="groupName"  :tab-list="group.tabList" :active-tab-key="group.tabKey" style="border-bottom:1px solid #dbdde0;box-shadow: 0px 15px 5px -15px #c4c6c9;width: 100%; margin-top: 10px;">
            <a-form :model="group" :label-col="labelCol" :wrapper-col="wrapperCol" >
              <a-form-item v-for="(variable, key) in group.variables" :key="key" :label="variable.label">
                <template v-if="variable.type === 'text' ">
                  <a-input  ></a-input>
                </template>
                <template v-else-if="variable.type === 'select' " >
                  <a-select  class="m-2" placeholder="Select">
                  </a-select>
                </template>
                <template v-else-if="variable.type === 'radio'" >
                  <a-radio-group >
                    <el-radio :label="variable.data.label">{{ variable.data.label }}</el-radio>
                  </a-radio-group>
                </template>
                <template v-else-if="variable.type === 'slider' " >
                  <el-slider  show-input />
                </template>
                <template v-else-if="variable.type === 'number' " >
                  <el-input-number  :min="1" :max="10" :step="variable.step"/>
                </template>
              </a-form-item>
              <a-form-item v-for="(constants, key) in group.constants" :key="key" :label="constants.label">
                <template v-if="constants.type === 'textbox' ">
                  
                </template>
              </a-form-item>
            </a-form>
          </a-card>
      </div>
    </div>
    <div class="footer" style="border-top:1px solid #dbdde0">footer</div>
  </el-dialog>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
const dialogVisible = ref(false)

const current = ref<number>(0);

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
              "key": "基础信息",
              "tab": "基础信息"
            }
          ],
          "tabKey": "基础信息",
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
  }
})
const labelCol = { style: { width: '70px' } };
const wrapperCol = { span: 6 };

const currentStep = ref('step1');
const currentStepGroups = ref({});
onMounted(() => {
  updateCurrentStepGroups();
});

function updateCurrentStepGroups() {
  currentStepGroups.value = templateDate.value.spec.data[currentStep.value];
}


const dialogName = ref('')
const TableName = ref('')
const showAndInit = (listName:any, tableName:any) => {
  dialogName.value = listName
  TableName.value = tableName
  dialogVisible.value = true
  // frontendCreateTemplate(TableName.value, templateSpec)
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
}

.item1 {
  flex: 1;
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


</style>
