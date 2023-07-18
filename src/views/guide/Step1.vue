<template>
  <div class="main">
    <div class="step">
      <Step :activeStep="1"></Step>
    </div>
    <div class="card_main">
      <el-card class="box-card" style="border:1px solid #d2d2d2; width: 1300px; height:300px ">
        <el-form ref="formRef" size="large" :rules="rules">
          <el-form-item prop="account"
                        label="名称">
            <el-input type="text"></el-input>
          </el-form-item>
          <el-form-item prop="password"
                        label="简介">
            <el-input type="textarea"
                      :rows="5"></el-input>
          </el-form-item>
          <el-form-item prop="password"
                        label="数量">

            <el-button icon="Plus" @click="inc()" style="margin-right: 10px"/>
            {{ count }}
            <el-button icon="Minus" @click="dec()"/>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card class="box-card" style="margin-top:10px; border:1px solid #d2d2d2; width: 1300px; height:200px">
        <el-form class="demo-projectForm">
          <el-form-item
              label="计算规格"
              prop="timeout">
            <el-radio-group size="mini">
              <el-radio border
                        label="选择计算规格"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
              label="镜像"
              prop="timeout">
            <el-radio-group size="mini">
              <el-radio border
                        style="margin-left: 27px"
                        label="选择镜像"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
              label="数据云盘"
              prop="timeout">
            <el-radio-group size="mini">
              <el-radio border
                        label="选择镜像"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </el-card>
      <el-collapse v-model="activeNames" @change="handleChange">
        <el-collapse-item title="高级" name="1" style="margin-top:10px; border:1px solid #d2d2d2; width: 1300px; ">
          <el-form class="demo-projectForm">
            <el-form-item
                label="时间同步"
                prop="timeout">
              <el-radio-group size="mini">
                <el-radio border
                          style="margin-left: 40px"
                          label=""></el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item
                label="选择优先级"
                prop="timeout">
              <el-radio-group size="mini">
                <el-radio border
                          style="margin-left: 27px"
                          label="正常"></el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>

  <div class="controls__wrap" style="float: right; margin-right: 100px;margin-top: 100px">
    <div class="controls__right">
      <router-link :to="`/test1`">
        <button type="primary"
                class="save-btn"
                plain>下一步</button>
      </router-link>
      <div class="run-button">
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Step from './Step.vue'
import {useCounter} from '@vueuse/core'

const {count, inc, dec} = useCounter()

const activeNames = [];

const handleChange = (val:string) => {
  console.log(val);
}
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
