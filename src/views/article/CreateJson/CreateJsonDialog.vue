<template>
  <div>
    <el-dialog v-model="dialogVisible" width="80%">
      <el-scrollbar height="700px">
        <div class="create-title">
          创建{{ dialogName }}
        </div>
        <div class="main">
          <div >
            <div class="table_section">
              <div style=" margin-top: 10px; margin-left: 30px;flex: 1">
                <span style="font-size: 15px;color: #c0c4cc;">基本信息</span>
                <el-form :model="formData" ref="formDataRef" label-width="auto" label-position="right" style="margin-top: 20px">
                  <el-form-item
                      v-for="(variable, key) in templateSpec.variables"
                      :label="variable.label"
                      :key="key"
                  >
                    <template v-if="variable.type === 'text' && key.startsWith('metadata')">
                      <el-input v-model="formData.metadata.name" ></el-input>
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
                        <el-select v-model="formData.spec.containers[0].image" class="m-2" placeholder="Select">
                          <el-option
                              v-for="item in ImageOptions"
                              :key="item.value"
                              :label="item.label"
                              :value="item.value"
                          />
                        </el-select>
                      </el-drawer>
                    </template>
                    <template v-if="variable.required === false">
                      (非必填)
                      <template v-if="variable.type === 'map'">
                        <el-input v-model="keyValue.value1"  placeholder="name"/>
                        <el-input v-model="keyValue.value2"  placeholder="value" />
                        <el-button  @click="addKeyValuePair" style="margin-top: 10px">确认</el-button>
                      </template>
                      <template v-if="key.includes('port')">
                        <el-input v-model="portValue.value2" placeholder="value" />
                        <el-button  @click="addPortPair" style="margin-top: 10px">确认</el-button>
                      </template>
                      <template v-else-if="variable.type === 'list' && variable.label === '启动指令:' ">
                        <el-input
                            v-model="commandValue"
                            :rows="2"
                            type="textarea"
                            placeholder="请输入指令"
                        />
                        <el-button @click="addCommand" style="margin-top: 10px">确认</el-button>
                      </template>
                      <template v-else-if="variable.type === 'list' && variable.label === '启动参数:'">
                        <el-input
                            v-model="argsValue"
                            :rows="2"
                            type="textarea"
                            placeholder="请输入参数"
                        />
                        <el-button @click="addArgs" style="margin-top: 10px">确认</el-button>
                      </template>
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
                              style="height: 500px"
                              readonly="true"
                              :options="{
                                fontSize: 15
                              }"/>
              </div>
            </div>
          </div>
        </div>
      </el-scrollbar>
      <div class="create-bottom">

        <el-button type="primary" style="margin-bottom: 20px" @click="createButton">确认创建</el-button>
        <el-button style="margin-bottom: 20px">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue';
// import {Edit} from "@element-plus/icons-vue";
import {frontendCreateTemplate, frontendCreate} from '@/api/common'
import { VAceEditor } from "vue3-ace-editor";
import './ace.config'

const switchvalue = ref(true)
const dialogVisible = ref(false)
const comboxDrawer = ref(false)

const templateSpec = ref({
  template: {},
  variables: {}
});

const formData = ref({
  // 初始化 formData 数据结构
});

const ImageOptions = [
  {
    value: 'my-image:latest',
    label: 'my-image:latest'
  },
  {
    value: 'nginx',
    label: 'nginx'
  }
]

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
  formData.value.spec.containers[0].command.push(commandValue.value)
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
  frontendCreateTemplate(TableName.value, templateSpec)
}

function generateInitialFormData() {
  const initialData = {
    apiVersion: templateSpec.value.template.apiVersion,
    kind: templateSpec.value.template.kind,
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
              containerPort: 80,
            },
          ],
          env: [],
          command: [],
          args: [],
        },
      ],
    },
  };
  return initialData;
}


function createButton(){
  console.log(jsonFormdata.value)
  frontendCreate(JSON.parse(jsonFormdata.value))
  dialogVisible.value = false
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
