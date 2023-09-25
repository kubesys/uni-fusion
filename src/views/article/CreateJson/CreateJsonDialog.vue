<template>
  <div>
    <el-dialog v-model="dialogVisible" width="80%">
      <el-scrollbar height="800px">
        <div class="create-title">
          创建{{ dialogName }}
        </div>
        <div class="main">
          <!-- 原step分步 -->
          <!--        <div class="step" style="flex: 0.2;height: 300px" >-->
          <!--          <el-steps :active="active" direction="vertical">-->
          <!--            <el-step v-for="(step, stepName) in scaleItems.spec" :key="stepName" :title="stepName" :icon="Edit"/>-->
          <!--          </el-steps>-->
          <!--        </div>-->

          <div >
            <div class="table_section">

              <!-- 原card 数据 -->
              <!--            <el-card v-for="(group, groupName) in currentStepGroups" :key="groupName" style="border: 1px solid #d2d2d2; width: 1000px; margin-top: 10px; margin-left: 30px">-->
              <!--              <el-form :model="group" :rules="getRules(group)">-->
              <!--                <el-form-item v-for="(field, fieldName) in group" :key="fieldName" :label="field.value">-->
              <!--                  <template v-if="field.type === 'textbox'">-->
              <!--                    <el-input  :placeholder="field.value" type="textarea" :rows="5" />-->
              <!--                  </template>-->
              <!--                  <template v-else-if="field.type === 'text'">-->
              <!--                    <el-input  :placeholder="field.value" />-->
              <!--                  </template>-->
              <!--                  <template v-else-if="field.type === 'select'">-->
              <!--                    <el-select  :placeholder="field.value">-->
              <!--                      <el-option v-for="(option, optionIndex) in selectOptions" :key="optionIndex" :label="option.label" :value="option.value" />-->
              <!--                    </el-select>-->
              <!--                  </template>-->
              <!--                  <template v-else-if="field.type === 'number'">-->
              <!--                    <el-input-number  :min="1" :max="10"  />-->
              <!--                  </template>-->
              <!--                  <template v-else-if="field.type === 'radio'">-->
              <!--                    <el-radio-group >-->
              <!--                      <el-radio :label="3">Option A</el-radio>-->
              <!--                      <el-radio :label="6">Option B</el-radio>-->
              <!--                      <el-radio :label="9">Option C</el-radio>-->
              <!--                    </el-radio-group>-->
              <!--                  </template>-->
              <!--                  <template v-else-if="field.type === 'switch'">-->
              <!--                    <el-switch v-model="switchvalue" />-->
              <!--                  </template>-->
              <!--                </el-form-item>-->
              <!--              </el-form>-->
              <!--            </el-card>-->

              <!--            <pre> {{ formData }} </pre>-->
              <div style=" margin-top: 10px; margin-left: 30px;flex: 1">
                <span style="font-size: 20px">基本信息:</span>
                <el-form :model="formData" ref="formDataRef" label-width="auto" label-position="right">
                  <el-form-item
                      v-for="(variable, key) in spec.variables"
                      :label="variable.label"
                      :key="key"
                  >
                    <template v-if="variable.type === 'text' && key.startsWith('metadata')">
                      <el-input v-model="formData.metadata.name" style="width: 400px"></el-input>
                    </template>
                    <template v-else-if="variable.type === 'text' && key.startsWith('spec')">
                      <el-input v-model="formData.spec.containers[0].name" ></el-input>
                    </template>
                    <template v-else-if="variable.type === 'combox'">
<!--                      <el-select v-model="formData[key]">-->
<!--                        <el-option-->
<!--                            v-for="option in variable.options"-->
<!--                            :key="option.value"-->
<!--                            :label="option.label"-->
<!--                            :value="option.value"-->
<!--                        ></el-option>-->
<!--                      </el-select>-->
                      <el-button type="primary" style="" @click="comboxDrawer = true">
                        镜像列表
                      </el-button>

                      <el-drawer v-model="comboxDrawer"  :with-header="false">
                        <span>镜像列表列</span>
                      </el-drawer>
                    </template>
                    <!-- 处理其他字段类型 -->
                  </el-form-item>
                </el-form>
              </div>
<!--              <pre> {{ jsonFormdata }} </pre>-->
              <el-divider direction="vertical" style="margin-left: 100px; height: auto"/>
              <div class="ace_editor">
                <v-ace-editor v-model:value="jsonFormdata"
                              lang="json"
                              theme="cobalt"
                              style="height: 400px"
                              readonly="true"
                              :options="{
                                fontSize: 15
                              }"/>
              </div>
            </div>
            <div class="controls__wrap" style="float: right; margin-right: 100px;margin-top: 100px">
              <div v-if="active != 2 ">
                <!--      <el-button style="margin-top: 12px" @click="next">下一步</el-button>-->
                <el-button class="custom-button" @click="handlePrevStep" style="margin-top: 10px;float: right">上一步</el-button>
                <el-button class="custom-button" @click="handleNextStep" style="margin-top: 10px;float: right;margin-right: 10px">下一步</el-button>
              </div>
              <div v-else>
                <!--      <el-button style="margin-top: 12px" @click="next">完成</el-button>-->
                <el-button class="custom-button" @click="handlePrevStep" style="margin-top: 10px;float: right">上一步</el-button>
                <el-button class="custom-button" @click="finishStep" style="margin-top: 10px;float: right;margin-right: 10px">完成</el-button>
              </div>

            </div>
          </div>
        </div>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue';
// import {Edit} from "@element-plus/icons-vue";
import { VAceEditor } from "vue3-ace-editor";
import './ace.config'

const switchvalue = ref(true)
const dialogVisible = ref(false)
const comboxDrawer = ref(false)

const spec = ref({
  // 你的规格数据
  "template": {
    "apiVersion": "v1",
    "kind": "Pod"
  },
  "variables": {
    "metadata.name": {
      "label": "metadata名称:",
      "type": "text",
      "regexp": [
        "A-Za-z"
      ]
    },
    "spec.containers[0].name": {
      "label": "container名称:",
      "type": "text",
      "regexp": [
        "A-Za-z"
      ]
    },
    "spec.containers[0].image": {
      "label": "镜像:",
      "type": "combox",
      "kind": "ConfigMap",
      "name": "busybox"
    },
    "spec.containers[0].env": {
      "label": "环境变量:",
      "required": false,
      "type": "map",
      "keyRegexp": [
        "A-Za-z"
      ],
      "valueRegexp": [
        "A-Za-z"
      ]
    },
    "spec.containers[0].port": {
      "label": "开放端口:",
      "required": false,
      "type": "list",
      "regexp": [
        "0-9"
      ]
    },
    "spec.containers[0].command": {
      "label": "启动指令:",
      "required": false,
      "type": "list",
      "regexp": [
        "A-Za-z"
      ]
    },
    "spec.containers[0].args": {
      "label": "启动参数:",
      "required": false,
      "type": "list",
      "regexp": [
        "A-Za-z"
      ]
    }
  }
});

const formData = ref({
  // 初始化 formData 数据结构
});

// 处理特殊字段类型，比如 combox 的选项
// onMounted(() => {
//   for (const key in spec.value.variables) {
//     if (spec.value.variables[key].type === 'combox') {
//       spec.value.variables[key].options = fetchOptionsFromConfigMap();
//     }
//   }
// });

// 这里根据规格数据生成 formData 初始结构，你可以根据需求进行调整
function generateInitialFormData() {
  const initialData = {
    apiVersion: spec.value.template.apiVersion,
    kind: spec.value.template.kind,
    metadata: {
      name: '',
    },
    spec: {
      containers: [
        {
          name: '',
          image: '',
          port: [
            {
              containerPort: 0,
            },
          ],
          env: [],
          command: [],
          args: [],
        },
      ],
    },
  };

  // 这里可以根据 spec.variables 中的内容进一步构建 initialData

  return initialData;
}

formData.value = generateInitialFormData();

const jsonFormdata = ref(JSON.stringify(formData.value, null, 2));

watch(formData, () => {
  // 更新 jsonFormdata
  jsonFormdata.value = JSON.stringify(formData.value, null, 2);
}, { deep: true });

// const formData = reactive({
//   apiVersion: 'v1',
//   kind: 'Pod',
//   metadata: {
//     name: '',
//   },
//   spec: {
//     containers: [
//       {
//         name: '',
//         image: '',
//         ports: [
//           {
//             containerPort: 0,
//           },
//         ],
//         env: [],
//         command: [],
//         args: [],
//       },
//     ],
//   },
// });

// const scaleItems = reactive({
//   spec: {
//     step1: {
//       group1: {
//         'spec.replicas': {
//           value: '命名',
//           required: 'true',
//           type: 'text',
//         },
//         'spec.introduce': {
//           value: '简介',
//           required: 'false',
//           type: 'textbox'
//         },
//         'spec.number': {
//           value: '实例数',
//           required: 'true',
//           type: 'number',
//           regexp: '1<x<10'
//         },
//       },
//       group2: {
//         'spec.replicas': {
//           value: '计算规格',
//           required: 'true',
//           type: 'select'
//         },
//         'spec.radio': {
//           value: '需要',
//           required: 'true',
//           type: 'radio'
//         },
//         'spec.switch': {
//           value: '是否开启',
//           required: 'true',
//           type: 'switch'
//         }
//       }
//     },
//     step2: {
//       group1: {
//         'spec.namespace': {
//           value: '命名空间',
//           required: 'true',
//           type: 'text',
//           regexp: '1<x<10'
//         }
//       },
//       group2: {
//         'spec.namespace': {
//           value: 'xxx',
//           required: 'true',
//           type: 'textbox',
//           regexp: '1<x<10'
//         }
//       },
//       group3: {
//         'spec.namespace': {
//           value: 'xxx',
//           required: 'true',
//           type: 'textbox',
//           regexp: '1<x<10'
//         }
//       }
//     },
//     step3: {
//       group1: {
//         'spec.namespace': {
//           value: '其他',
//           required: 'false',
//           type: 'textbox'
//         }
//       }
//     }
//   }
// });
// const scaleItems = reactive({
//   spec: {
//     template: {
//       apiVersion: 'v1',
//       kind: 'Pod',
//     },
//     variables: {
//       'metadata.name': {
//         label: 'metadata名称:',
//         type: 'text',
//         regexp: ['A-Za-z'],
//       },
//       'spec.containers[0].name': {
//         label: 'containers名称:',
//         type: 'text',
//         regexp: ['A-Za-z'],
//       },
//       'spec.containers[0].image': {
//         label: '镜像:',
//         type: 'combox',
//         options: {
//           'my-image:latest': 'my-image:latest',
//           // 添加更多选项
//         },
//       },
//       'spec.containers[0].env': {
//         label: '环境变量:',
//         required: false,
//         type: 'map',
//         keyRegexp: ['A-Za-z'],
//         valueRegexp: ['A-Za-z'],
//       },
//       'spec.containers[0].port': {
//         label: '开放端口:',
//         required: false,
//         type: 'list',
//         regexp: ['0-9'],
//       },
//       'spec.containers[0].command': {
//         label: '启动指令:',
//         required: false,
//         type: 'list',
//         regexp: ['A-Za-z'],
//       },
//       'spec.containers[0].args': {
//         label: '启动参数:',
//         required: false,
//         type: 'list',
//         regexp: ['A-Za-z'],
//       },
//     },
//   }
// });

// const submitForm = () => {
//   // 构建最终的 JSON 对象
//   const finalJson = {
//     apiVersion: formData.apiVersion,
//     kind: formData.kind,
//     metadata: {
//       name: formData['metadata.name'],
//     },
//     spec: {
//       containers: [
//         {
//           name: formData['spec.containers[0].name'],
//           image: formData['spec.containers[0].image'],
//           ports: [
//             {
//               containerPort: formData['spec.containers[0].port'],
//             },
//           ],
//           env: formData['spec.containers[0].env'],
//           command: formData['spec.containers[0].command'],
//           args: formData['spec.containers[0].args'],
//         },
//       ],
//     },
//   };
//
//   // 打印最终 JSON 对象
//   console.log(JSON.stringify(finalJson, null, 2));
// };

const selectOptions = ref([]);
const currentStep = ref('step1');

// 计算当前步骤对应的 group
// const currentStepGroups = ref({});
// onMounted(() => {
//   updateCurrentStepGroups();
// });

// function updateCurrentStepGroups() {
//   currentStepGroups.value = scaleItems.spec[currentStep.value];
// }

function getRules(group) {
  // 在这里编写验证规则
  // 返回验证规则对象
  return {};
}

const hasNextStep = ref(false);
const active = ref(0);

function handleNextStep() {
  const stepNames = Object.keys(scaleItems.spec);
  const currentIndex = stepNames.indexOf(currentStep.value);
  if (currentIndex < stepNames.length - 1) {
    currentStep.value = stepNames[currentIndex + 1];
    active.value = currentIndex + 1;
    updateCurrentStepGroups();
  }
  hasNextStep.value = currentIndex < stepNames.length - 2;
}

function handlePrevStep() {
  const stepNames = Object.keys(scaleItems.spec);
  const currentIndex = stepNames.indexOf(currentStep.value);
  if (currentIndex > 0) {
    currentStep.value = stepNames[currentIndex - 1];
    active.value = currentIndex - 1;
    updateCurrentStepGroups();
  }
  hasNextStep.value = currentIndex < stepNames.length - 2;
}

const dialogName = ref('')
const showAndInit = (listName:any) => {
  dialogName.value = listName
  dialogVisible.value = true
}

defineExpose({
  showAndInit,
})

</script>
<style>
.create-title{
  margin-left: 50px;
  margin-top: 50px;
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
