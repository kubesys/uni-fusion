<template>
  <div class="main">
    <div class="step">
      <Step :activeStep="2"></Step>
    </div>
    <div class="card_main">
      <el-card class="box-card" style="border:1px solid #d2d2d2; width: 1300px; height:300px ">
        <el-form ref="formRef" size="large" :rules="rules">
          <el-form-item prop="account"
                        label="网络配置">
            <el-input type="textarea"
                      :rows="5"></el-input>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card class="box-card" style="margin-top:10px; border:1px solid #d2d2d2; width: 1300px; height:200px">
        <el-form class="demo-projectForm">
          <el-form-item
              label="集群"
              prop="timeout">
            <el-radio-group size="mini">
              <el-radio border
                        label="选择集群"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
              label="存储分配策略"
              prop="timeout">
            <el-radio-group size="mini">
              <el-radio border
                        style="margin-left: 27px"
                        label="系统分配"></el-radio>
            </el-radio-group>
            <el-radio-group size="mini">
              <el-radio border
                        style="margin-left: 27px"
                        label="手动指定"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
              label="物理机"
              prop="timeout">
            <el-radio-group size="mini">
              <el-radio border
                        label="选择物理机"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </el-card>
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
