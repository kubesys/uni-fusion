<template>
  <div class="main">
    <div class="step" style="flex: 0.2" >
        <el-steps :active="active" finish-status="success" direction="vertical">
            <el-step title="Step 1" :icon="Edit"/>
            <el-step title="Step 2" :icon="Edit"/>
            <el-step title="Step 3" :icon="Edit"/>
        </el-steps>
    </div>
    <div>
      <el-card v-for="(group, groupName) in steps.step1" :key="groupName" style="border:1px solid #d2d2d2; width: 1000px;">
        <el-form :model="group" :rules="getRules(group)">
          <el-form-item v-for="(field, fieldName) in group" :key="fieldName" :label="field.value">
            <template v-if="field.type === 'textbox'">
<!--              <el-input v-model="group[fieldName]" :placeholder="field.value"></el-input>-->
              <el-input  type="textarea"
                         :rows="5"/>
            </template>
            <template v-else-if="field.type === 'text'">
<!--              <el-input v-model="group[fieldName]" :placeholder="field.value"></el-input>-->
              <el-input />
            </template>
            <template v-else-if="field.type === 'select'">
              <el-select v-model="group[fieldName]" :placeholder="field.value">
                <el-option v-for="(option, optionIndex) in selectOptions" :key="optionIndex" :label="option.label" :value="option.value"></el-option>
              </el-select>
            </template>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
<!--    <div class="card_main">-->
<!--      <el-card class="box-card" style="border:1px solid #d2d2d2; width: 1300px; height:300px ">-->
<!--        <el-form ref="formRef" size="large" :rules="rules">-->
<!--          <el-form-item prop="account"-->
<!--                        label="名称">-->
<!--            <el-input type="text"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="password"-->
<!--                        label="简介">-->
<!--            <el-input type="textarea"-->
<!--                      :rows="5"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item prop="password"-->
<!--                        label="数量">-->

<!--            <el-button icon="Plus" @click="inc()" style="margin-right: 10px"/>-->
<!--            {{ count }}-->
<!--            <el-button icon="Minus" @click="dec()"/>-->
<!--          </el-form-item>-->
<!--        </el-form>-->
<!--      </el-card>-->
<!--      <el-card class="box-card" style="margin-top:10px; border:1px solid #d2d2d2; width: 1300px; height:200px">-->
<!--        <el-form class="demo-projectForm">-->
<!--          <el-form-item-->
<!--              label="计算规格"-->
<!--              prop="timeout">-->
<!--            <el-radio-group size="mini">-->
<!--              <el-radio border-->
<!--                        label="选择计算规格"></el-radio>-->
<!--            </el-radio-group>-->
<!--          </el-form-item>-->
<!--          <el-form-item-->
<!--              label="镜像"-->
<!--              prop="timeout">-->
<!--            <el-radio-group size="mini">-->
<!--              <el-radio border-->
<!--                        style="margin-left: 27px"-->
<!--                        label="选择镜像"></el-radio>-->
<!--            </el-radio-group>-->
<!--          </el-form-item>-->
<!--          <el-form-item-->
<!--              label="数据云盘"-->
<!--              prop="timeout">-->
<!--            <el-radio-group size="mini">-->
<!--              <el-radio border-->
<!--                        label="选择镜像"></el-radio>-->
<!--            </el-radio-group>-->
<!--          </el-form-item>-->
<!--        </el-form>-->
<!--      </el-card>-->
<!--      <el-collapse v-model="activeNames" @change="handleChange">-->
<!--        <el-collapse-item title="高级" name="1" style="margin-top:10px; border:1px solid #d2d2d2; width: 1300px; ">-->
<!--          <el-form class="demo-projectForm">-->
<!--            <el-form-item-->
<!--                label="时间同步"-->
<!--                prop="timeout">-->
<!--              <el-radio-group size="mini">-->
<!--                <el-radio border-->
<!--                          style="margin-left: 40px"-->
<!--                          label=""></el-radio>-->
<!--              </el-radio-group>-->
<!--            </el-form-item>-->
<!--            <el-form-item-->
<!--                label="选择优先级"-->
<!--                prop="timeout">-->
<!--              <el-radio-group size="mini">-->
<!--                <el-radio border-->
<!--                          style="margin-left: 27px"-->
<!--                          label="正常"></el-radio>-->
<!--              </el-radio-group>-->
<!--            </el-form-item>-->
<!--          </el-form>-->
<!--        </el-collapse-item>-->
<!--      </el-collapse>-->
<!--    </div>-->
  </div>

  <div class="controls__wrap" style="float: right; margin-right: 100px;margin-top: 100px">
<!--    <div class="controls__right">-->
<!--      <router-link :to="`/test1`">-->
<!--        <button type="primary"-->
<!--                class="save-btn"-->
<!--                plain>下一步</button>-->
<!--      </router-link>-->
<!--      <div class="run-button">-->
<!--      </div>-->
<!--    </div>-->
      <template v-if="active != 3 ">
          <el-button style="margin-top: 12px" @click="next">下一步</el-button>
      </template>
      <template v-else>
          <el-button style="margin-top: 12px" @click="next">完成</el-button>
      </template>

  </div>

</template>

<script setup lang="ts">
import {ref} from 'vue'
import {useCounter} from '@vueuse/core'
import {frontendAction} from "@/api/common";
import { Edit } from '@element-plus/icons-vue'

const {count, inc, dec} = useCounter()

const activeNames = [];
const steps = ref([])
const active = ref(0)

const next = () => {
    if (active.value++ > 2) active.value = 0
}

frontendAction('apps.replicaset', steps)
console.log(steps.value)
const handleChange = (val:string) => {
  console.log(val);
}

const selectOptions = ref([
  { label: "Option 1", value: "option1" },
  { label: "Option 2", value: "option2" },
  // Add more options as needed
]);

const getRules = (group) => {
  const rules = {};
  for (const field in group) {
    if (group[field].required === "true") {
      rules[field] = [{ required: true, message: `${group[field].value}不能为空`, trigger: 'blur' }];
    }
  }
  return rules;
};

</script>

<style>
::v-deep .el-radio__label{
  color: #ff0000;
  margin-right:10px
}

.el-input {

  width: 400px;

}
.el-textarea {
  width: 400px;

}
.main{
  margin-left: 300px;
  margin-top: 200px;
  overflow: hidden;
  display: flex;
}
.step{
  float: left;
  width: 10%;
}

.text {
  font-size: 14px;
}

.item {
  padding: 18px 0;
}

.box-card {
  width: 480px;
}

.card_main{
  width: 90%;
  float: right;
}
</style>
