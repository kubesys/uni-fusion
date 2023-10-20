<!-- 暂时不需要 -->

<!--<template>-->
<!--  <div>-->
<!--    &lt;!&ndash; The Form section of the Table page &ndash;&gt;-->
<!--    <el-form :model="formData" ref="form" label-width="auto" label-position="left" style="margin-top: 30px">-->
<!--      <el-row>-->
<!--        <el-col v-for="formItem in formItems" :key="formItem.path" :span="7" style="margin-right: 20px">-->
<!--          <el-form-item :label="formItem.label">-->
<!--            <template v-if="formItem.type === 'textbox'">-->
<!--              <el-input v-model="props" style="width: 100%;"></el-input>-->
<!--            </template>-->
<!--            <template v-else-if="formItem.type === 'combobox'">-->
<!--              <el-select v-model="formData[formItem.path]" style="width: 100%;" >-->
<!--                <el-option v-for="(option, index) in getFormDataValue(formItem.data, newArray)" :key="index" :label="option.label" :value="option.value">-->

<!--                </el-option>-->
<!--              </el-select>-->
<!--            </template>-->
<!--            &lt;!&ndash; 可以根据需要添加其他类型的表单组件 &ndash;&gt;-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="7">-->
<!--        </el-col>-->
<!--      </el-row>-->
<!--    </el-form>-->
<!--    &lt;!&ndash; Table page action button &ndash;&gt;-->
<!--    <el-button type="primary" @click="handleCreateClick"  style="text-align: left; background-color: #3967FF;"><icon name="el-icon-Plus" :size="16" style="margin-right: 5px"/>创建</el-button>-->
<!--    <el-button @click="submitForm" style="text-align: left; background-color: #d7d9dc;"><icon name="el-icon-RefreshRight" :size="18" /></el-button>-->
<!--    <el-button @click="submitForm" style="text-align: left; background-color: #d7d9dc;">查询</el-button>-->
<!--  </div>-->
<!--</template>-->
<!--<script setup lang="ts">-->

<!--import {getFormDataValue} from "@/api/common";-->
<!--</script>-->
<!--<style scoped lang="scss">-->

<!--</style>-->
