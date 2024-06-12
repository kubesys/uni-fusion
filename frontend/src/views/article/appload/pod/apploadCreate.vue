<template>
  <el-dialog v-model="dialogVisible" style="width:100%; height:100%" z-index="500">
    <div class="header" style="border-bottom:1px solid #dbdde0">
      <div class="create-title" style="font-size: 16px; margin-left: 50px">
        开始{{ nameChange(dialogName) }}
      </div>
    </div>
    <div >




        <div>
          <v-ace-editor v-model:value="jsonForm"
                        lang="json"
                        theme="cobalt"
                        style="height: 700px"
                        :options="{
                                        fontSize: 12
                                      }"/>

        </div>

    </div>
    <div class="footer" style="border-top:1px solid #dbdde0">
      <div style="float: right; margin-right: 100px">

          <!--      <el-button style="margin-top: 12px" @click="next">完成</el-button>-->
          <a-button  style="margin-top: 10px;float: right;margin-right: 10px;background-color:cornflowerblue;border: none" type="primary" @click="createValue">确认创建</a-button>


      </div>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import {h, onMounted, ref} from 'vue';
import {DeleteOutlined, EllipsisOutlined, PlusOutlined, InfoCircleOutlined} from '@ant-design/icons-vue';
import type { TableProps } from 'ant-design-vue';

import type { Rule } from 'ant-design-vue/es/form';
import {
  frontendCreate,
  frontendData,
  getComplexDataDispose,
  getComplexValue,
  getPlatformValue,
  getTerminalAddr,
  nameChange
} from "@/api/common";
import { VAceEditor } from "vue3-ace-editor";
import useAppStore from "@/stores/modules/app";
import {ElMessage} from "element-plus";
import '@/views/article/createJson/ace.config'


const formRef = ref();

const route = useRoute()
const dialogVisible = ref(false)

const options = ref([
  { value: 'vm.node31', label: 'vm.node31' },
  { value: 'vm.node32', label: 'vm.node32' },
  { value: 'vm.node33', label: 'vm.node33' },
]);

const propadd = ref({})

const templateDate = ref()

const currentStep = ref('step1');
const currentStepGroups = ref({});

const pageSite = ref({limit:5,page:1})
const tableData = ref({
  metadata:{
    totalCount:''
  },
  items:[],
  actions:[]
})
const filter = route.meta?.filter || {}
const allLabels = ref(filter)
const actions = ref([])
onMounted(() => {
  updateCurrentStepGroups();
});

const jsonCreateTable = ref([])

function updateCurrentStepGroups() {

  // currentStepGroups.value = templateDate.value.data[currentStep.value];
}


const dialogName = ref('')
const TableName = ref('')

// step
const active = ref(1);
const hasNextStep = ref(false);

// drawer
const open = ref<boolean>(false);
const title = ref('')

// table
const tableColumns = ref([])


// list
const divs = ref(['配置']);


const methodRow = ref("")

const dataRow = ref()
const idRow = ref()

function showDrawer(kind:any, label: any, row: string, path: string, method: any, data: any, id: any) {
  frontendData(kind, kind.toLowerCase(), pageSite, tableColumns, tableData,allLabels.value, actions);
  open.value = true;
  title.value = label;
  methodRow.value = method;
  dataRow.value = data;
  idRow.value = id
};

const appStore = useAppStore()

// 创建
function createValue(){
  // assignValues(propadd.value,jsonFormdata.value)
  console.log(jsonForm.value)
  frontendCreate(jsonForm.value)
  dialogVisible.value = false
  appStore.refreshView()
}

const createformdata = ref({
      "apiVersion": "doslab.io/v1",
      "kind": "VirtualMachine",
      "metadata": {
        "name": "",
        "labels": {
          "name": "",
          "host": ""
        }
      },
      "spec": {
        "nodeName": "",
        "lifecycle": {
          "createAndStartVMFromISO": {
            "virt_type": "",
            "memory": "",
            "vcpus": "",
            "os_variant": "",
            "cdrom": "",
            "disk": "",
            "network": "",
            "graphics": "",
            "noautoconsole": true
          }
        }
      }
    }
)

const jsonForm = ref();

// function assignValues(obj1:any, obj2:any) {
//   for (const key in obj1) {
//     if (Object.prototype.hasOwnProperty.call(obj1, key)) {
//       const value = obj1[key];
//       assignValue(obj2, key, value);
//     }
//   }
// }

// function assignValue(obj:any, key:any, value:any) {
//   const keys = key.split('.');
//   let currentObj = obj;
//
//   for (let i = 0; i < keys.length; i++) {
//     const currentKey = keys[i];
//     if (i === keys.length - 1) {
//       // Last key, assign the value
//       currentObj[currentKey] = value;
//     } else {
//       // Not the last key, create nested objects if necessary
//       if (!currentObj[currentKey]) {
//         currentObj[currentKey] = {};
//       }
//       currentObj = currentObj[currentKey];
//     }
//   }
//
//   return currentObj
// }


const showAndInit = (listName:any, Date:any ) => {
  // templateDate.value = Date
  // currentStepGroups.value = templateDate.value.data[currentStep.value]
  // jsonFormdata.value = obj
  // aceJson.value = JSON.stringify(jsonFormdata.value,null,2)
  // console.log(jsonFormdata.value)
  dialogName.value = listName
  console.log(Date)
  jsonForm.value = JSON.stringify(Date, null, 2)
  // TableName.value = tableName
  // console.log(currentStepGroups.value)
  // traverseObject(currentStepGroups.value)
  dialogVisible.value = true
}

// function traverseObject(obj:any) {
// for (const key in obj) {
//   if (typeof obj[key] === 'object' && Object.keys(obj[key]).length > 0) {
//     traverseObject(obj[key]);
//   } else {
//     innermostObjects.value.push({ [key]: obj[key] });
//   }
// }
// console.log(innermostObjects.value)
// }


// function checkForm(){
//   if( Object.keys(propadd.value).length == 0 ){
//     return false
//   }
//   return true
// }


defineExpose({
  showAndInit,
})

</script>

<style  lang="less" scoped>
.sync-dialog {
  height: 700px;
  overflow: auto;
  background: #f5f7fa

}

.item1 {
  flex: 1;
  position: relative;
  text-align: right;
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
  flex: 8;
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

.textarea{
  width: 330px;
  font-size: 10px;
  text-align: right;
  margin-top: -15px;
  margin-right: 10px;
}

.custom-radio-group :deep(.ant-radio-button-wrapper) {
  margin-right: 8px; /* 调整按钮之间的间距，根据需要修改值 */
  border-radius: 0px
}
</style>
