<template>
  <div class="create-title">
    创建
  </div>
  <div class="main">
    <div class="step" style="flex: 0.2;height: 300px" >
      <el-steps :active="active" direction="vertical">
        <el-step v-for="(step, stepName) in scaleItems.spec" :key="stepName" :title="stepName" :icon="Edit" />
      </el-steps>
    </div>

    <div>
      <el-card v-for="(group, groupName) in currentStepGroups" :key="groupName" style="border: 1px solid #d2d2d2; width: 1000px; margin-top: 10px;">
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
          </el-form-item>
        </el-form>
      </el-card>
      <div class="controls__wrap" style="float: right; margin-right: 100px;margin-top: 100px">
        <div v-if="active != 2 ">
          <!--      <el-button style="margin-top: 12px" @click="next">下一步</el-button>-->
          <el-button class="custom-button" @click="handlePrevStep" style="margin-top: 10px;float: right">上一步</el-button>
          <el-button class="custom-button" @click="handleNextStep" style="margin-top: 10px;float: right;margin-right: 10px">下一步</el-button>
        </div>
        <div v-else>
          <!--      <el-button style="margin-top: 12px" @click="next">完成</el-button>-->
          <el-button class="custom-button" @click="handlePrevStep" style="margin-top: 10px;float: right">上一步</el-button>
          <el-button class="custom-button" style="margin-top: 10px;float: right;margin-right: 10px">完成</el-button>
        </div>

      </div>
    </div>
  </div>
<!--  <el-button @click="handleNextStep" style="margin-top: 10px;float: right">下一步</el-button>-->
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import {Edit} from "@element-plus/icons-vue";

const scaleItems = reactive({
  spec: {
    step1: {
      group1: {
        'spec.replicas': {
          value: '实例数',
          required: 'true',
          type: 'text',
          regexp: '1<x<10'
        },
        'spec.introduce': {
          value: '简介',
          required: 'false',
          type: 'textbox'
        }
      },
      group2: {
        'spec.replicas': {
          value: '计算规格',
          required: 'true',
          type: 'select'
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

</script>

<style>
.create-title{
  margin-left: 300px;
  margin-top: 100px;
  font-size: 40px;
}

.main{
  margin-left: 300px;
  margin-top: 20px;
  overflow: hidden;
  display: flex;
}

.step{
  float: left;
  width: 20%;
}

.el-step__title {
  font-weight: bold;
  font-size: 16px;
}

.el-step__title.is-process{
  color: #0043B1;
}

.custom-button {
  background-color: #fcfcfd; /* 初始背景颜色 */
  color: black; /* 初始文字颜色 */
  transition: background-color 0.3s ease; /* 添加过渡效果 */

  /* 当鼠标悬停时改变背景颜色 */

  &:hover {
    background-color: #409EFF; /* 鼠标悬停时的背景颜色 */
  }
}
</style>
