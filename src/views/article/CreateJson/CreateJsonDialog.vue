<template>
  <div>
    <el-dialog v-model="CreateJsonVisible"
               width="80%">
      <div class="create-title">
        创建{{listname}}
      </div>
      <div class="main">
        <div class="step" style="flex: 0.2;height: 300px" >
          <el-steps :active="active" direction="vertical">
            <el-step v-for="(step, stepName) in scaleItems.spec" :key="stepName" :title="stepName" :icon="Edit"/>
          </el-steps>
        </div>

        <div >
          <div class="card">
            <el-card v-for="(group, groupName) in currentStepGroups" :key="groupName" style="border: 1px solid #d2d2d2; width: 1000px; margin-top: 10px; margin-left: 30px">
              <el-form :model="group" :rules="getRules(group)">
                <el-form-item v-for="(field, fieldName) in group" :key="fieldName" :label="field.value">
                  <template v-if="field.type === 'textbox'">
                    <el-input  :placeholder="field.value" type="textarea" :rows="5" />
                  </template>
                  <template v-else-if="field.type === 'text'">
                    <el-input  :placeholder="field.value" />
                  </template>
                  <template v-else-if="field.type === 'select'">
                    <el-select  :placeholder="field.value">
                      <el-option v-for="(option, optionIndex) in selectOptions" :key="optionIndex" :label="option.label" :value="option.value" />
                    </el-select>
                  </template>
                  <template v-else-if="field.type === 'number'">
                    <el-input-number  :min="1" :max="10"  />
                  </template>
                  <template v-else-if="field.type === 'radio'">
                    <el-radio-group >
                      <el-radio :label="3">Option A</el-radio>
                      <el-radio :label="6">Option B</el-radio>
                      <el-radio :label="9">Option C</el-radio>
                    </el-radio-group>
                  </template>
                  <template v-else-if="field.type === 'switch'">
                    <el-switch v-model="value" />
                  </template>
                </el-form-item>
              </el-form>
            </el-card>
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
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import {Edit} from "@element-plus/icons-vue";

defineProps<{CreateJsonVisible:boolean,listname:string}>()

const value = ref(true)

const scaleItems = reactive({
  spec: {
    step1: {
      group1: {
        'spec.replicas': {
          value: '命名',
          required: 'true',
          type: 'text',
        },
        'spec.introduce': {
          value: '简介',
          required: 'false',
          type: 'textbox'
        },
        'spec.number': {
          value: '实例数',
          required: 'true',
          type: 'number',
          regexp: '1<x<10'
        },
      },
      group2: {
        'spec.replicas': {
          value: '计算规格',
          required: 'true',
          type: 'select'
        },
        'spec.radio': {
          value: '需要',
          required: 'true',
          type: 'radio'
        },
        'spec.switch': {
          value: '是否开启',
          required: 'true',
          type: 'switch'
        }
      }
    },
    step2: {
      group1: {
        'spec.namespace': {
          value: '命名空间',
          required: 'true',
          type: 'text',
          regexp: '1<x<10'
        }
      },
      group2: {
        'spec.namespace': {
          value: 'xxx',
          required: 'true',
          type: 'textbox',
          regexp: '1<x<10'
        }
      },
      group3: {
        'spec.namespace': {
          value: 'xxx',
          required: 'true',
          type: 'textbox',
          regexp: '1<x<10'
        }
      }
    },
    step3: {
      group1: {
        'spec.namespace': {
          value: '其他',
          required: 'false',
          type: 'textbox'
        }
      }
    }
  }
});

const selectOptions = ref([]);
const currentStep = ref('step1');

// 计算当前步骤对应的 group
const currentStepGroups = ref({});
onMounted(() => {
  updateCurrentStepGroups();
});

function updateCurrentStepGroups() {
  currentStepGroups.value = scaleItems.spec[currentStep.value];
}

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

function finishStep() {
  CreateJsonVisible.value = true
}

</script>
<style>
.create-title{
  margin-left: 200px;
  margin-top: 50px;
  font-size: 40px;
}

.main{
  margin-left: 200px;
  margin-top: 20px;
  margin-bottom: 30px;
  overflow: hidden;
  display: flex;
}

.step{
  float: left;
  width: 20%;
}

.card{
  height: 400px;
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
