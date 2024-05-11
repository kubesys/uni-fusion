<template>
  <!--  <div style="background: #a62727; padding: 30px">-->

  <!--    <a-row :gutter="16">-->
  <!--      <a-col>-->
  <!--        <a-card  :bordered="false" style="width: 100%">-->
  <!--          <p>Card content</p>-->
  <!--          <div class="wrapper">-->
  <!--            <div class="titleWrapper">-->
  <!--              <div class="icon"}>-->
  <!--                icon-->
  <!--              </div>-->
  <!--              <div class="title">-->
  <!--                <div class="h3">2</div>-->
  <!--                <p class="text-second">-->
  <!--                  1-->
  <!--                </p>-->
  <!--              </div>-->
  <!--            </div>-->
  <!--          </div>-->
  <!--        </a-card>-->
  <!--      </a-col>-->
  <!--    </a-row>-->
  <!--    <a-row>-->
  <!--      <a-col>-->
  <!--        <a-card>-->

  <!--        </a-card>-->
  <!--      </a-col>-->
  <!--    </a-row>-->
  <!--  </div>-->

  <div style="background-color: #eff4f9; padding: 15px; width: 100%; overflow: scroll; height: 100vh">
    <a-row :gutter="24">
      <a-col :span="24">
        <a-card  :bordered="false">
          <div class="wrapper">
            <div class="titleWrapper">
              <div class="icon">
                <image-contain src='/pod.png' />
              </div>
              <div class="title">
                <div className="h3">{{ descItem.title }}</div>
                <p class="text-second">
                  {{ descItem.desc }}
                </p>
              </div>
            </div>
          </div>
        </a-card>
      </a-col>

    </a-row>
    <a-row :gutter="24" style="margin-top: 20px">
      <a-col :span="24">
        <a-card  :bordered="false">
          <!--          <a-button  style="background-color: #d7d9dc;border-radius: 0px">-->
          <!--            <icon class="iconSearch" name="el-icon-search" />-->
          <!--            <span class="text">搜索</span>-->
          <!--          </a-button>-->
          <a-space>
            <a-select
                ref="select"
                value="全部项目"
                style="width: 260px"
            >
            </a-select>
            <a-input placeholder="搜索" style="width: 1180px">
              <template #prefix><SearchOutlined /></template>
            </a-input>

            <a-button type="text" ><SyncOutlined /></a-button>

            <a-button type="text" ><SettingOutlined /></a-button>
          </a-space>

          <div >
            <a-table class="load-table"
                     :row-selection="rowSelection"
                     :columns="tableColumns"
                     :rowKey='record=>record.metadata.name'
                     :data-source="tableData.items"
            >
              <template #bodyCell="{ column, text }" >
                <template v-if="column.kind === 'action'">
                  <a-dropdown >
                    <a-button type="text" :icon="h(EllipsisOutlined)" />
                    <template #overlay>

                      <a-menu >
                        <a-menu-item
                            v-for="action in column.actionLink"
                            :key="action.key"

                            :disabled="getComplexValue(text, action.status)=='🟢' ? action.status && action.action == 'start' : getComplexValue(text, action.status)=='🟡' ? action.status && action.action == 'start' || action.action == 'suspend' : action.status && action.action == 'stop' || action.action == 'reboot' || action.action == 'suspend' || action.action == 'resume' || action.action == 'shutdown' ">
                          <a-tree
                              v-if= "action.action === 'extend'"
                              :tree-data="action.children"
                          />
                          <a target="_blank" >{{action.label}}</a>
                        </a-menu-item>
                      </a-menu>
                    </template>
                  </a-dropdown>
                </template>

                <template v-else-if="column.kind === 'display'">
                  <span >
                    {{  getPlatformValue (text, column.row) }}
                  </span>
                </template>

                <template v-else-if="column.kind === 'link'">
                  <div style="display: flex; align-items: center;">
                    <image-contain src='/pod.png' style="width: 30px; margin-right: 10px;" />
                    <span>{{ getComplexDataDispose(text, column.row) }}</span>
                  </div>
                </template>

                <span v-else>
                  <span class="icon-text-container" v-if="getComplexDataDispose(text, column.row) == '🟢'" >
                    运行中</span>
                  <span class="icon-text-container" v-else-if="getComplexDataDispose(text, column.row) == '🔴'">
                    已停止</span>
                  <span class="icon-text-container" v-else-if="getComplexDataDispose(text, column.row) == '🟡'">
                    暂停中</span>
                  <span v-else>
                    {{ getComplexDataDispose(text, column.row) }}
                  </span>
                </span>
              </template>
            </a-table>
          </div>
        </a-card>
      </a-col>

    </a-row>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted, h} from "vue";
import {
  frontendCreateTemplate,
  frontendData,
  frontendFormSearch,
  frontendMeta,
  getComplexDataDispose,
  getComplexValue, getPlatformValue, getTerminalAddr
} from "@/api/common";
import {
  CreditCardOutlined,
  EllipsisOutlined,
  SearchOutlined,
  SyncOutlined,
  SettingOutlined,
  PauseCircleTwoTone,
  RightCircleTwoTone,
  StopTwoTone
} from "@ant-design/icons-vue";

import type { TableProps } from 'ant-design-vue';


interface tableColumns {
  label: string;
  row: string;
  internalLink: object;
  actionLink: object;
  terminalLink: object
}

interface DataType {
  key: string;
  name: string;
  age: number;
  address: string;
}

const route = useRoute()
const TableName = route.meta?.tablename
const descItem = ref([]);
const ListName = route.meta?.listname

const filter = route.meta?.filter || {}
const props = ref('')
const propslecet = ref({})
const allLabels = ref(filter)

const actions = ref([])
const pageSite = ref({limit:10,page:1})
const formData = ref<Record<string, string>>({}); // 表单数据对象
const formItems = ref([]); // 用于存储生成的表单项
const buttonItem = ref([]); // 用于存储生成的表单按钮

const tableColumns:tableColumns = ref([])
const tableData = ref({
  metadata:{
    totalCount:''
  },
  items:[],
  actions:[],
  resultRun: 0,
  resultPen: 0
})

function handleCurrentChange(newPage) {
  pageSite.value.page = newPage
  frontendData(ListName, TableName, pageSite,tableColumns, tableData,propslecet.value, actions)
}

const jsonCreateTable = ref([])
const rowSelection: TableProps['rowSelection'] = {
  onChange: (selectedRowKeys: string[], selectedRows: DataType[]) => {
    console.log(selectedRows)
    selectedRows.forEach(row => {
      // tableRowState.value  = row.spec.status.conditions.state.waiting.reason
      const isRowExists = jsonCreateTable.value.some(existingRow => existingRow.metadata.name === row.metadata.name);
      if (!isRowExists) {
        jsonCreateTable.value.push(row);
      } else {
        jsonCreateTable.value = []
      }
    });
  }
};

onMounted(()=>{
  frontendMeta(TableName, descItem);
  frontendFormSearch(TableName, formItems, buttonItem)
  frontendData(ListName, TableName, pageSite,tableColumns, tableData,allLabels.value, actions)
  // frontendCreateTemplate(TableName, templateDate, obj)
})



</script>

<style lang="less" scoped>
.wrapper {
  background-color: #ffffff;

  & > div {
    &:last-of-type {
      border-radius: 0 0 4px 4px;
    }
  }
}

.titleWrapper {
  position: relative;
  overflow: hidden;

  .icon {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    margin-left: 8px;
    padding: 6px;
    border-radius: 100px 0 100px 100px;
    background-color: #eff4f9;
  }

  .title {
    padding-left: 50px;
    font-weight: 600;


    .h3 {
      display: block;
      font-weight: 600;
      font-style: normal;
      line-height: 32px;
      font-size: 25px;
      color: #242e42;
    }

  }
}

.text-second {
  color: #79879c;
}


.tabsWrapper {
  padding: 6px 12px;
  background-color: #eff4f9;
}

.iconSearch {
  float: left;
  width: 15px;
  height: 20px;
}
.text {
  float: left;
  padding: 0 5px 0 5px;
  font-size: 14px;
  line-height: 20px;
}

// table
table {
  width: 100%;
  border-collapse: collapse;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  margin-bottom: 20px;
}

th, td {
  padding: 12px 15px;
  text-align: left;
  font-size: 14px;
}
th {
  background-color: #f7f8fa;
  color: #79879c;
  font-weight: 600;
}

::v-deep(.ant-table-cell) {
  background: #ffffff !important;
}

.ant-input::placeholder {
  color: red !important;
}

</style>
